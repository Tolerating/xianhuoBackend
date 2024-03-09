package com.xianhuo.xianhuobackend.controller;

import cn.hutool.json.JSONObject;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xianhuo.xianhuobackend.common.ResponseProcess;
import com.xianhuo.xianhuobackend.common.ResponseResult;
import com.xianhuo.xianhuobackend.config.AliPayConfig;
import com.xianhuo.xianhuobackend.entity.OrderInfo;
import com.xianhuo.xianhuobackend.entity.Product;
import com.xianhuo.xianhuobackend.service.OrderInfoService;
import com.xianhuo.xianhuobackend.service.ProductService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class AliPayController {
//    支付宝沙箱网关地址
    private static final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
        private static final String FORMAT = "JSON";
    private static final String CHARSET = "UTF-8";
//    签名方式
    private static final String SIGN_TYPE = "RSA2";
    @Resource
    private AliPayConfig aliPayConfig;
    @Resource
    private OrderInfoService orderInfoService;
    @Resource
    private ProductService productService;
    @GetMapping("/pay")
    public ResponseResult pay(String orderNo, HttpServletResponse httpServletResponse) throws Exception{
//        查询订单信息
        OrderInfo order = orderInfoService.getOne(new LambdaQueryWrapper<OrderInfo>()
                .eq(OrderInfo::getOrderId, orderNo));
//        1.创建Client，通过sdk提供的Client，负责调用支付宝的API
        DefaultAlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppid(), aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);
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
        boolean flag = AlipaySignature.rsaCheckV1 (params,aliPayConfig.getAlipayPublicKey(), CHARSET,SIGN_TYPE);
        if(flag){
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
        }
    }
}
