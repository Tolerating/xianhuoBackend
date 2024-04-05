package com.xianhuo.xianhuobackend.controller;


import cn.hutool.db.sql.Order;
import cn.hutool.json.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xianhuo.xianhuobackend.common.ResponseProcess;
import com.xianhuo.xianhuobackend.common.ResponseResult;
import com.xianhuo.xianhuobackend.common.SocketSessions;
import com.xianhuo.xianhuobackend.config.AliPayConfig;
import com.xianhuo.xianhuobackend.config.Common;
import com.xianhuo.xianhuobackend.entity.*;
import com.xianhuo.xianhuobackend.service.*;
import com.xianhuo.xianhuobackend.utils.JWTUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 售后表(AfterService)表控制层
 *
 * @author makejava
 * @since 2024-03-14 20:58:43
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
public class AfterServiceController {
    /**
     * 服务对象
     */
    @Resource
    private AfterServiceService afterServiceService;
    @Resource
    private ProductService productService;
    @Resource
    private OrderInfoService orderInfoService;
    @Resource
    private AliPayConfig aliPayConfig;
    @Resource
    private NoticeService noticeService;
    @Resource
    private UserService userService;

    @Resource
    private HttpServletRequest httpServletRequest;

    //    申请售后或者
//    申请平台介入，新建一个售后，对应需要买家补充信息
    @PostMapping("/afterService")
    public ResponseResult addAfter(@RequestBody AfterService afterService) {
        long time = new Date().getTime();
        afterService.setId(time);
        boolean saved = afterServiceService.save(afterService);
        if (afterService.getPlatformStatus() == -1) {
            pushNotice(afterService.getSellerId(), afterService.getBuyerId(), "售后申请", afterService.getProductId(), "您有一个售后申请待处理，请尽快处理！", true, false);
        }
//       订单表绑定售后id，设置平台计入为0
        orderInfoService.update(new LambdaUpdateWrapper<OrderInfo>()
                .set(OrderInfo::getAfterServiceId, time)
                .set(afterService.getPlatformStatus() == -1, OrderInfo::getPlatformFlag, 0)
                .set(afterService.getPlatformStatus() == 0, OrderInfo::getPlatformFlag, 1)
                .eq(OrderInfo::getOrderId, afterService.getOrderId()));
        return ResponseProcess.returnString(saved, "申请成功", "申请失败");
    }

    //    根据购买用户id和订单号获取售后记录
    @GetMapping("/afterService")
    public ResponseResult getAfter(Long buyerId, Long orderId) {
        AfterService one = afterServiceService.getOne(new LambdaQueryWrapper<AfterService>()
                .eq(AfterService::getBuyerId, buyerId)
                .eq(AfterService::getOrderId, orderId));
        return ResponseProcess.returnObject(one);
    }

    //    获取购买者的所有售后
    @GetMapping("/afterServices")
    public ResponseResult allAfter(Long buyerId) {
        List<AfterService> list = afterServiceService.list(new LambdaQueryWrapper<AfterService>()
                .eq(AfterService::getBuyerId, buyerId)
                .eq(AfterService::getStatus, 0));
        return ResponseProcess.returnList(list);
    }

    //    申请平台介入，在当前售后的基础上进行
    @GetMapping("/askPlatform")
    public ResponseResult askPlatform(Long afterId, Long orderId) {
        boolean update = afterServiceService.update(new LambdaUpdateWrapper<AfterService>()
                .set(AfterService::getPlatformStatus, 0)
                .eq(AfterService::getId, afterId));
        orderInfoService.update(new LambdaUpdateWrapper<OrderInfo>()
                .set(OrderInfo::getStatus, 2)
                .eq(OrderInfo::getOrderId, orderId));
        return ResponseProcess.returnString(update, "申请成功", "申请失败");
    }

    //    申请平台介入，新建一个售后，对应需要买家补充信息
//    @GetMapping("/askPlatform/add")
//    public ResponseResult askPlatformAdd(@RequestBody AfterService afterService) {
//
//    }

    //    获取售后记录
    @GetMapping("/afterHistory")
    public ResponseResult afterHistory() {
        String authorization = httpServletRequest.getHeader("authorization");
        String id = JWTUtil.parseJWT(authorization).getId();
        List<AfterService> list = afterServiceService.list(new LambdaQueryWrapper<AfterService>()
                .ne(AfterService::getStatus, 0)
                .eq(AfterService::getBuyerId, id)
                .or()
                .eq(AfterService::getSellerId, id));
        return ResponseProcess.returnList(list);
    }

    //    获取待处理售后
    @GetMapping("/afterService/toDeal")
    public ResponseResult getAfterServiceToDeal() {
        String authorization = httpServletRequest.getHeader("authorization");
        String id = JWTUtil.parseJWT(authorization).getId();
        List<AfterService> serviceList = afterServiceService.list(new LambdaQueryWrapper<AfterService>()
                .eq(AfterService::getStatus, 0)
                .eq(AfterService::getPlatformStatus, -1)
                .eq(AfterService::getSellerId, id));
        return ResponseProcess.returnList(serviceList);
    }


    //    商家处理售后
    @GetMapping("/afterService/sellerDeal")
    public ResponseResult sellerDeal(Long afterId, Long productId, Long orderId, Long buyerId, String refuse, Integer type) {
        String authorization = httpServletRequest.getHeader("authorization");
        Long sellerId = Long.valueOf(JWTUtil.parseJWT(authorization).getId());
        Product product = productService.getById(productId);
        AfterService afterService = new AfterService();
        afterService.setId(afterId);
        if (type == 0) {
//            拒绝售后，
//            将订单状态恢复为已支付【1】，对应买家退货原因描述不详细，需要在此提交申请。
//            平台介入字段置为1【标识平台能够介入】，对应卖家拒不处理，给买家开通平台介入
            orderInfoService.update(new LambdaUpdateWrapper<OrderInfo>()
                    .set(OrderInfo::getStatus, 1)
                    .set(OrderInfo::getPlatformFlag, 1)
                    .eq(OrderInfo::getOrderId, orderId));
//            标记售后状态为失败【-1】
            afterService.setStatus(-1);
            afterService.setSellerDealTime(new Date());
            String msg = "亲爱的用户您好，您的售后申请已被商家拒绝，原因如下：" + refuse;
            afterService.setSellerRefuseCause(refuse);
//            将通知存入数据库
            noticeService.save(new Notice("售后结果通知", msg, buyerId, 1, productId, sellerId));
//            通知消费者
            Map<String, Object> map = SocketSessions.createMessageTemplate("售后结果通知", msg, afterService.getPlatformDealTime(), productId, SocketSessions.messageType.PRODUCT);
            SocketSessions.sendMsg(buyerId, map);

        } else {
//            同意售后
            afterService.setSellerDealTime(new Date());
            afterService.setSellerStatus(1);
//            将售后状态变为待发货【11】
            afterService.setStatus(11);
//            扣除商家我的收益中的余额【减去商品的价格】，由于暂时未做余额提现操作，所以不做余额判断
            userService.update(new LambdaUpdateWrapper<Users>()
                    .setSql("profit = profit - " + product.getCurrentPrice().toString())
                    .eq(Users::getId, sellerId));
//            通知消费者
            String buyerMsg = "亲爱的买家您好，我已经同意了您的售后申请，请尽快与我联系。";
            noticeService.save(new Notice("售后结果通知", buyerMsg, buyerId, 1, productId, sellerId));
            Map<String, Object> buyerMap = SocketSessions.createMessageTemplate("售后结果通知", buyerMsg, afterService.getPlatformDealTime(), productId, SocketSessions.messageType.PRODUCT);
            SocketSessions.sendMsg(buyerId, buyerMap);
        }
        boolean update = afterServiceService.updateById(afterService);
        return ResponseProcess.returnString(update, "处理成功", "处理失败");

    }

    //    买家确定发货
    @GetMapping("/afterService/dispatch")
    public ResponseResult afterDispatch(Long id) {
        AfterService afterService = new AfterService();
        afterService.setId(id);
        afterService.setBuyerSend(1);
        afterService.setBuyerSendTime(new Date());
        afterService.setStatus(12);
        boolean updated = afterServiceService.updateById(afterService);
        return ResponseProcess.returnString(updated, "成功", "失败");
    }

    //    卖家确定收货
    @GetMapping("/afterService/receive")
    public ResponseResult afterReceive(Long id, Long orderId) throws AlipayApiException {
        AfterService service = afterServiceService.getById(id);
        OrderInfo orderInfo = orderInfoService.getOne(new LambdaQueryWrapper<OrderInfo>().eq(OrderInfo::getOrderId, orderId));
        AlipayClient alipayClient = new DefaultAlipayClient(Common.GATEWAY_URL, aliPayConfig.getAppid(), aliPayConfig.getAppPrivateKey(), Common.FORMAT, Common.CHARSET, aliPayConfig.getAlipayPublicKey(), Common.SIGN_TYPE);
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("trade_no", orderInfo.getAlipayId());
        bizContent.put("refund_amount", orderInfo.getTotal());

        request.setBizContent(bizContent.toString());
        AlipayTradeRefundResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            System.out.println("调用成功");
//            通知买家
            pushNotice(service.getSellerId(), service.getBuyerId(), "退款通知", service.getProductId(), "退款已到账，请到支付宝中查看", false, true);
            //  将订单状态恢复为已退款【0】，，平台介入置为0
            boolean update = orderInfoService.update(new LambdaUpdateWrapper<OrderInfo>()
                    .set(OrderInfo::getStatus, 0)
                    .set(OrderInfo::getPlatformFlag, 0)
                    .eq(OrderInfo::getOrderId, orderId));
            AfterService afterService = new AfterService();
            afterService.setId(id);
            afterService.setSellerReceive(1);
            afterService.setSellerReceiveTime(new Date());
            afterService.setStatus(1);
            boolean updated = afterServiceService.updateById(afterService);
            return ResponseProcess.returnString(updated, "成功", "失败");
        } else {
            System.out.println("调用失败");
            return ResponseResult.fail(-1, "退款失败，请稍后重试");
        }

    }

    //    商家迟迟未发货，用户申请退款【或者用户买完又不想要了】
    @GetMapping("/refund")
    public ResponseResult refund(Long id) throws AlipayApiException {
        OrderInfo orderInfo = orderInfoService.getById(id);
        if (orderInfo.getSellerStatus() == 1) {
            // 商家已经发货，不能退款
            return ResponseResult.fail(0,"卖家已发货，不能退款！");
        }else{
            // 商家未发货，退款
            AlipayClient alipayClient = new DefaultAlipayClient(Common.GATEWAY_URL, aliPayConfig.getAppid(), aliPayConfig.getAppPrivateKey(), Common.FORMAT, Common.CHARSET, aliPayConfig.getAlipayPublicKey(), Common.SIGN_TYPE);
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("trade_no", orderInfo.getAlipayId());
            bizContent.put("refund_amount", orderInfo.getTotal());

            request.setBizContent(bizContent.toString());
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                System.out.println("调用成功");
//            通知卖家
                pushNotice(orderInfo.getSellId(), orderInfo.getBuyId(), "退款通知",orderInfo.getProductId(), "买家申请退款成功，订单已取消，商品恢复到上架状态", true, false);
//                通知买家退款成功
                pushNotice(orderInfo.getSellId(), orderInfo.getBuyId(), "退款通知",orderInfo.getProductId(), "退款成功，请到支付宝查收", false, true);
                //  将订单状态恢复为已退款【0】，，平台介入置为0
                orderInfoService.update(new LambdaUpdateWrapper<OrderInfo>()
                        .set(OrderInfo::getStatus, 0)
                        .set(OrderInfo::getPlatformFlag, 0)
                        .eq(OrderInfo::getOrderId,orderInfo.getOrderId()));
                // 将商品恢复到上架状态
                boolean update = productService.update(new LambdaUpdateWrapper<Product>()
                        .set(Product::getStatus, 1)
                        .eq(Product::getId, orderInfo.getProductId()));
                return ResponseProcess.returnString(update, "成功", "失败");
            } else {
                System.out.println("调用失败");
                return ResponseResult.fail(-1, "退款失败，请稍后重试");
            }
        }
    }

    private void pushNotice(Long sellerId, Long buyerId, String title, Long productId, String content, Boolean toSeller, Boolean toBuyer) {
        Map<String, Object> messageTemplate = SocketSessions.createMessageTemplate(title, content, new Date(), productId, SocketSessions.messageType.PRODUCT);
        Notice notice = new Notice();
        notice.setAttachType(1);

        notice.setTitle(title);
        notice.setAttach(productId);
        notice.setContent(content);
        if (toSeller) {
            SocketSessions.sendMsg(sellerId, messageTemplate);
            notice.setPublisher(sellerId);
            notice.setReceiverId(sellerId);

        }
        if (toBuyer) {
            SocketSessions.sendMsg(buyerId, messageTemplate);
            notice.setPublisher(sellerId);
            notice.setReceiverId(buyerId);
        }

        noticeService.save(notice);
    }


}

