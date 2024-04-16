package com.xianhuo.xianhuobackend.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xianhuo.xianhuobackend.common.ResponseProcess;
import com.xianhuo.xianhuobackend.common.ResponseResult;
import com.xianhuo.xianhuobackend.common.SocketSessions;
import com.xianhuo.xianhuobackend.entity.AfterService;
import com.xianhuo.xianhuobackend.entity.Notice;
import com.xianhuo.xianhuobackend.entity.OrderInfo;
import com.xianhuo.xianhuobackend.entity.Users;
import com.xianhuo.xianhuobackend.service.AfterServiceService;
import com.xianhuo.xianhuobackend.service.NoticeService;
import com.xianhuo.xianhuobackend.service.OrderInfoService;
import com.xianhuo.xianhuobackend.service.UserService;
import com.xianhuo.xianhuobackend.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageReadParam;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * (OrderInfo)表控制层
 *
 * @author makejava
 * @since 2024-03-01 22:20:06
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
public class OrderInfoController {
    /**
     * 服务对象
     */
    @Resource
    private OrderInfoService orderInfoService;
    @Resource
    private UserService userService;
    @Resource
    private NoticeService noticeService;
    @Resource
    private AfterServiceService afterServiceService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    //    新增订单
    @PostMapping("/order")
    public ResponseResult addOrder(@RequestBody OrderInfo orderInfo) {
        OrderInfo isExit = orderInfoService.getOne(new LambdaQueryWrapper<OrderInfo>()
                .eq(OrderInfo::getProductId, orderInfo.getProductId())
                .eq(OrderInfo::getStatus,1));
        System.out.println(isExit);
        if (isExit == null) {
            boolean saved = orderInfoService.save(orderInfo);
            pushNotice(orderInfo.getSellId(), orderInfo.getBuyId(), "商品动态", orderInfo.getProductId(), "您发布的商品已被购买，请尽快处理！", true, false);
            return ResponseProcess.returnString(saved, "success", "fail");
        }
        return ResponseResult.fail(null, "商品已被他人购买");
    }

    @DeleteMapping("/order")
    public ResponseResult deleteOrder(Long orderId){
        boolean removed = orderInfoService.remove(new LambdaQueryWrapper<OrderInfo>().eq(OrderInfo::getOrderId, orderId));
        return  ResponseProcess.returnString(removed,"success","未支付订单删除失败");
    }

    //    获取未发货订单,卖家显示
    @GetMapping("/orders/dispatch")
    public ResponseResult dispatchProduct() {
        String authorization = httpServletRequest.getHeader("authorization");
        String id = JWTUtil.parseJWT(authorization).getId();
        List<OrderInfo> infoList = orderInfoService.list(new LambdaQueryWrapper<OrderInfo>()
                .eq(OrderInfo::getSellId, id)
                        .eq(OrderInfo::getStatus,1)
                .eq(OrderInfo::getSellerStatus, 0));
        return ResponseProcess.returnList(infoList);
    }

    //    获取待收货订单，买家显示
    @GetMapping("/orders/receive")
    public ResponseResult receiveProduct() {
        String authorization = httpServletRequest.getHeader("authorization");
        String id = JWTUtil.parseJWT(authorization).getId();
        List<OrderInfo> infoList = orderInfoService.list(new LambdaQueryWrapper<OrderInfo>()
                .eq(OrderInfo::getBuyId, id)
                .eq(OrderInfo::getStatus, 1)
                .eq(OrderInfo::getBuyerStatus, 0));
        return ResponseProcess.returnList(infoList);
    }

    //    确认发货
    @GetMapping("/order/dispatched")
    public ResponseResult disptchedProduct(Long id) {
        boolean updated = orderInfoService.update(new LambdaUpdateWrapper<OrderInfo>()
                .set(OrderInfo::getSellerStatus, 1)
                .set(OrderInfo::getSellerSendTime, new Date())
                .eq(OrderInfo::getId, id));
        return ResponseProcess.returnString(updated, "success", "fail");
    }

    //    确认收货
    @GetMapping("/order/received")
    public ResponseResult receivedProduct(Long id) {
        OrderInfo orderInfo = orderInfoService.getById(id);
        if (orderInfo.getSellerStatus() == 1) {
            boolean updated = orderInfoService.update(new LambdaUpdateWrapper<OrderInfo>()
                    .set(OrderInfo::getBuyerStatus, 1)
                    .set(OrderInfo::getBuyerReceiveTime, new Date())
                    .eq(OrderInfo::getId, id));
//            更新卖家余额
            if (updated) {
                Users seller = userService.getById(orderInfo.getSellId());
                userService.update(new LambdaUpdateWrapper<Users>()
                        .set(Users::getProfit, seller.getProfit() + orderInfo.getTotal())
                        .eq(Users::getId, orderInfo.getSellId()));
            }
            pushNotice(orderInfo.getSellId(), orderInfo.getBuyId(), "收益到账", orderInfo.getProductId(), "收益到账" + orderInfo.getTotal() + "元，请查收！", true, false);
            return ResponseProcess.returnString(updated, "success", "fail");
        }
        return ResponseResult.fail(0, "等待卖家发货后方可收货");
    }

    //    出售记录
    @GetMapping("/orders/sell")
    public ResponseResult sellHistory() {
        String authorization = httpServletRequest.getHeader("authorization");
        String id = JWTUtil.parseJWT(authorization).getId();
        List<OrderInfo> list = orderInfoService.getSellerHistory(Long.valueOf(id));

        return ResponseProcess.returnList(list);
    }

    //    购买记录
    @GetMapping("/orders/buy")
    public ResponseResult buyHistory() {
        String authorization = httpServletRequest.getHeader("authorization");
        String id = JWTUtil.parseJWT(authorization).getId();
        List<OrderInfo> list = orderInfoService.getBuyerHistory(Long.valueOf(id));
        return ResponseProcess.returnList(list);
    }

    //    获取待发货，待收货，交易记录数量，我的收益
    @GetMapping("/orders/count")
    public ResponseResult getCounts() {
        String authorization = httpServletRequest.getHeader("authorization");
        String id = JWTUtil.parseJWT(authorization).getId();
        long dispatch = orderInfoService.count(new LambdaQueryWrapper<OrderInfo>()
                .eq(OrderInfo::getSellId, id)
                        .eq(OrderInfo::getStatus,1)
                .eq(OrderInfo::getSellerStatus, 0));
        long receive = orderInfoService.count(new LambdaQueryWrapper<OrderInfo>()
                .eq(OrderInfo::getBuyId, id)
                        .eq(OrderInfo::getStatus,1)
                .eq(OrderInfo::getBuyerStatus, 0));
        long buy = orderInfoService.count(new LambdaQueryWrapper<OrderInfo>()
                .eq(OrderInfo::getBuyId, id)
                .eq(OrderInfo::getSellerStatus, 1)
                .eq(OrderInfo::getBuyerStatus, 1));
        long sell = orderInfoService.count(new LambdaQueryWrapper<OrderInfo>()
                .eq(OrderInfo::getSellId, id)
                .eq(OrderInfo::getSellerStatus, 1)
                .eq(OrderInfo::getBuyerStatus, 1));
        long afterService = afterServiceService.count(new LambdaQueryWrapper<AfterService>()
                .ne(AfterService::getStatus, 0)
                .and(i -> i.eq(AfterService::getBuyerId, id).or().eq(AfterService::getSellerId, id)));
        long serviceList = afterServiceService.count(new LambdaQueryWrapper<AfterService>()
                .eq(AfterService::getStatus, 0)
                .eq(AfterService::getPlatformStatus, -1)
                .eq(AfterService::getSellerId, id));
        Users users = userService.getById(id);
        Map<String, String> map = new HashMap<>();
        map.put("dispatch", String.valueOf(dispatch));
        map.put("receive", String.valueOf(receive));
        map.put("buy", String.valueOf(buy));
        map.put("sell", String.valueOf(sell));
        map.put("profit", users.getProfit().toString());
        map.put("after", String.valueOf(afterService));
        map.put("waitDeal", String.valueOf(serviceList));

        return ResponseResult.ok(map, "success");
    }

    // 更新订单状态
    @GetMapping("/orderInfo/status")
    public ResponseResult updateStatus(Long id, Integer status) {
        boolean update = orderInfoService.update(new LambdaUpdateWrapper<OrderInfo>()
                .set(OrderInfo::getStatus, status)
                .eq(OrderInfo::getOrderId, id));
        return ResponseProcess.returnString(update, "success", "fail");
    }


    //    根据id获取订单信息
    @GetMapping("/orderInfo")
    public ResponseResult getOrderInfo(Long id) {
        OrderInfo orderInfo = orderInfoService.getOne(new LambdaQueryWrapper<OrderInfo>()
                .eq(OrderInfo::getOrderId, id));
        return ResponseProcess.returnObject(orderInfo);
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

