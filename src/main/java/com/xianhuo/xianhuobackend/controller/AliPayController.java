package com.xianhuo.xianhuobackend.controller;

import cn.hutool.json.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xianhuo.xianhuobackend.common.ResponseProcess;
import com.xianhuo.xianhuobackend.common.ResponseResult;
import com.xianhuo.xianhuobackend.common.SocketSessions;
import com.xianhuo.xianhuobackend.config.AliPayConfig;
import com.xianhuo.xianhuobackend.entity.*;
import com.xianhuo.xianhuobackend.service.*;
import org.springframework.boot.autoconfigure.web.ConditionalOnEnabledResourceChain;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

import com.xianhuo.xianhuobackend.config.Common;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class AliPayController {

    @Resource
    private AliPayConfig aliPayConfig;
    @Resource
    private OrderInfoService orderInfoService;
    @Resource
    private ProductService productService;
    @Resource
    private NoticeService noticeService;
    @Resource
    private ChatMessageService chatMessageService;
    @Resource
    private ChatListService chatListService;
    @Resource
    private ChatUserLinkService chatUserLinkService;
//    支付接口
    @GetMapping("/pay")
    public ResponseResult pay(String orderNo, HttpServletResponse httpServletResponse) throws Exception{
//        查询订单信息
        OrderInfo order = orderInfoService.getOne(new LambdaQueryWrapper<OrderInfo>()
                .eq(OrderInfo::getOrderId, orderNo));
//        1.创建Client，通过sdk提供的Client，负责调用支付宝的API
        DefaultAlipayClient alipayClient = new DefaultAlipayClient(Common.GATEWAY_URL, aliPayConfig.getAppid(), aliPayConfig.getAppPrivateKey(), Common.FORMAT, Common.CHARSET, aliPayConfig.getAlipayPublicKey(), Common.SIGN_TYPE);
//        2.创建Request并设置Request参数
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        Product product = productService.getById(order.getProductId());
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        JSONObject bizContent = new JSONObject();
        bizContent.set("out_trade_no",order.getOrderId());
        bizContent.set("total_amount",product.getCurrentPrice());
        bizContent.set("subject", product.getDetail());
        bizContent.set("product_code","QUICK_MSECURITY_PAY");
        request.setBizContent(bizContent.toString());
        AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
        String orderStr = response.getBody();
        if(response.isSuccess()){
            System.out.println("调用成功");
            return ResponseResult.ok(orderStr,"支付成功");
        } else {
            System.out.println("调用失败");
//            删除未支付订单
            orderInfoService.removeById(order.getId());
            return  ResponseResult.fail(orderStr,"支付失败");
        }
    }
//    支付成功通知接口
    @PostMapping("/alipay/notify")
    public void payNotify(HttpServletRequest request) throws Exception{
        Map< String , String > params = new HashMap < String , String > ();
        Map requestParams = request.getParameterMap();
        for(Iterator iter = requestParams.keySet().iterator(); iter.hasNext();){
            String name = (String)iter.next();
            String[] values = (String [])requestParams.get(name);
            String valueStr = "";
            for(int i = 0;i < values.length;i ++ ){
                valueStr =  (i==values.length-1)?valueStr + values [i]:valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put (name,valueStr);
        }
        //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        boolean flag = AlipaySignature.rsaCheckV1 (params,aliPayConfig.getAlipayPublicKey(), Common.CHARSET,Common.SIGN_TYPE);
        if(flag){
            if(Objects.equals(params.get("trade_status"), "TRADE_SUCCESS")){
                String tradeNo = params.get("out_trade_no");
                String gmtPayment = params.get("gmt_payment");
                String totalAmount = params.get("total_amount");
                String alipayTradeNo = params.get("trade_no");
                String buyerId = params.get("buyer_id");
                String buyerLogonId = params.get("buyer_logon_id");
                OrderInfo order = orderInfoService.getOne(new LambdaQueryWrapper<OrderInfo>().eq(OrderInfo::getOrderId, tradeNo));
                order.setStatus(1);
                order.setBuyerSysId(Long.valueOf(buyerId));
                order.setBuyerSysAccount(buyerLogonId);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                order.setPayTime(simpleDateFormat.parse(gmtPayment));
                order.setAlipayId(alipayTradeNo);
                order.setTotal(Double.valueOf(totalAmount));
                orderInfoService.update(order, new LambdaQueryWrapper<OrderInfo>().eq(OrderInfo::getOrderId, tradeNo));
                OrderInfo orderInfo = orderInfoService.getOne(new LambdaQueryWrapper<OrderInfo>().eq(OrderInfo::getOrderId, tradeNo));
                productService.update(new LambdaUpdateWrapper<Product>()
                        .set(Product::getStatus,0)
                        .eq(Product::getId,orderInfo.getProductId()));
//                发送通知
                Map<String, Object> map = SocketSessions.createMessageTemplate("商品动态", "您发布的商品已被购买，请尽快处理！", new Date(), orderInfo.getProductId(), SocketSessions.messageType.PRODUCT);
                SocketSessions.sendMsg(orderInfo.getSellId(), map);
                Notice notice = new Notice();
                notice.setAttachType(1);

                notice.setTitle("商品动态");
                notice.setAttach(orderInfo.getProductId());
                notice.setContent("您发布的商品已被购买，请尽快处理！");
                notice.setPublisher(orderInfo.getSellId());
                notice.setReceiverId(orderInfo.getSellId());
                noticeService.save(notice);
//                ChatUserLink one = chatUserLinkService.getOne(new LambdaQueryWrapper<ChatUserLink>()
//                        .eq(ChatUserLink::getToUser, orderInfo.getBuyId())
//                        .eq(ChatUserLink::getFromUser, orderInfo.getSellId())
//                        .or()
//                        .eq(ChatUserLink::getToUser, orderInfo.getSellId())
//                        .eq(ChatUserLink::getFromUser, orderInfo.getBuyId()));
//                 //取消上一条消息为最新
//                chatMessageService.update(new LambdaUpdateWrapper<ChatMessage>().set(ChatMessage::getIsLatest, 0).eq(ChatMessage::getLinkId, chatMsg.getLinkId()));
            }
        }
    }
}
