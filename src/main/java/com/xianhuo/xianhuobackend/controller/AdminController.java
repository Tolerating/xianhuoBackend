package com.xianhuo.xianhuobackend.controller;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xianhuo.xianhuobackend.common.ResponseProcess;
import com.xianhuo.xianhuobackend.common.ResponseResult;
import com.xianhuo.xianhuobackend.common.SocketSessions;
import com.xianhuo.xianhuobackend.entity.*;
import com.xianhuo.xianhuobackend.service.*;
import com.xianhuo.xianhuobackend.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.Session;
import javax.xml.ws.Response;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class AdminController {

    @Resource
    private AdminUserService adminUserService;
    @Autowired
    HttpServletRequest httpServletRequest;
    @Resource
    private ComplainService complainService;
    @Resource
    private RequireInfoService requireInfoService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private UserService userService;
    @Resource
    private NoticeService noticeService;
    @Resource
    private ProductService productService;
    @Resource
    private ProductRequireService productRequireService;
    @Resource
    private ChatUserLinkService chatUserLinkService;
    @Resource
    private ChatMessageService chatMessageService;
    @Resource
    private OrderInfoService orderInfoService;
    @Resource
    private AfterServiceService afterServiceService;

    //    登录
    @PostMapping("/admin/login")
    public ResponseResult adminLogin(@RequestBody AdminUser adminUser) {
        AdminUser user = adminUserService.getOne(new LambdaQueryWrapper<AdminUser>()
                .eq(AdminUser::getAccount, adminUser.getAccount())
                .eq(AdminUser::getPassword, adminUser.getPassword()));
        if (user != null) {
            String token = JWTUtil.createJWT(user.getId().toString(), "", "");
            System.out.println("token：" + token);
            return ResponseResult.ok(token, "登录成功");
        } else {

            return ResponseResult.fail(user, "用户名或密码错误！");
        }
    }

    //    获取管理员信息
    @GetMapping("/admin/info")
    public ResponseResult adminInfo() {
        String authorization = httpServletRequest.getHeader("authorization");
        String id = JWTUtil.parseJWT(authorization).getId();
        AdminUser adminUser = adminUserService.getById(id);
        return ResponseProcess.returnObject(adminUser);
    }

    //    分页获取待处理的帖子投诉
    @GetMapping("/complain/waitfor/post")
    public ResponseResult complainWaitForPost(Long current, Long size, String startTime, String endTime) {
        Page<Complain> page = new Page<>(current, size);
        IPage<Complain> post = adminUserService.getComplainWaitForPost(page, startTime, endTime, false);
        return ResponseProcess.returnObject(post);
    }


    //    根据id获取求购信息
    @GetMapping("/complain/post/{id}")
    public ResponseResult getInfoById(@PathVariable("id") Long id) {
        RequireInfo id1 = requireInfoService.getById(id);
        Category category = categoryService.getById(id1.getCategoryId());
        id1.setCategoryName(category.getName());
        return ResponseProcess.returnObject(id1);
    }

    //   帖子投诉处理
    @GetMapping("/complain/post/deal")
    public ResponseResult dealPostComplain(Long id, Long postId, Long complainantId, Long sellerId, Integer type) {
        String authorization = httpServletRequest.getHeader("authorization");
        String adminId = JWTUtil.parseJWT(authorization).getId();
        Complain complain = new Complain();
        complain.setId(id);
        complain.setDealUser(Long.valueOf(adminId));
        complain.setDealTime(new DateTime());
//        投诉人通知
        Notice complaintNotice = new Notice();
        complaintNotice.setTitle("投诉处理结果通知");
        complaintNotice.setAttachType(0);
        complaintNotice.setAttach(postId);
        complaintNotice.setPublisher(sellerId);
        complaintNotice.setReceiverId(complainantId);
//        发帖人通知
        Notice sellerNotice = new Notice();
        sellerNotice.setReceiverId(sellerId);
        sellerNotice.setTitle("投诉处理结果通知");
        sellerNotice.setAttach(postId);
        sellerNotice.setAttachType(0);
        sellerNotice.setPublisher(sellerId);
        if (type == 0) {
            String msg = "亲爱的用户您好，您的投诉已经受理，处理意见如下：帖子不存在所诉的违规内容，投诉无效";
//            投诉无效
            complain.setDealMethod("帖子不存在所诉的违规内容，投诉无效");
//            通知投诉人处理结果
            complaintNotice.setContent(msg);
            //声明一个map，封装直接发送信息数据返回前端
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("title", "投诉结果通知");
            resultMap.put("content", msg);
            resultMap.put("dealTime", complain.getDealTime());
            resultMap.put("postId", postId);
            resultMap.put("type", "complain");
            SocketSessions.sendMsg(complainantId, resultMap);
//           将通知存入数据库
            noticeService.save(complaintNotice);
        } else {
//            投诉有效
            complain.setDealMethod("帖子存在所诉的违规内容，已做下架处理");
            complaintNotice.setContent("亲爱的用户您好，您的投诉已经受理，处理意见如下：帖子存在所诉的违规内容，已做下架处理");
            sellerNotice.setContent("亲爱的用户您好，您的帖子存在违规情况，处理结果如下：帖子存在所诉的违规内容，已做下架处理");
            //声明一个map，封装直接发送信息数据返回前端
            Map<String, Object> complaintResult = new HashMap<>();
            complaintResult.put("title", "投诉结果通知");
            complaintResult.put("content", complaintNotice.getContent());
            complaintResult.put("dealTime", complain.getDealTime());
            complaintResult.put("postId", postId);
            complaintResult.put("type", "complain");

            Map<String, Object> sellerResult = new HashMap<>();
            sellerResult.put("title", "投诉结果通知");
            sellerResult.put("content", sellerNotice.getContent());
            sellerResult.put("dealTime", complain.getDealTime());
            sellerResult.put("postId", postId);
            sellerResult.put("type", "complain");
            SocketSessions.sendMsg(complainantId, complaintResult);
            SocketSessions.sendMsg(sellerId, sellerResult);
            ArrayList<Notice> notices = new ArrayList<>();
            notices.add(complaintNotice);
            notices.add(sellerNotice);
            noticeService.saveBatch(notices);
//            对帖子进行下架处理
            RequireInfo requireInfo = new RequireInfo();
            requireInfo.setId(postId);
            requireInfo.setStatus(-1);
            requireInfoService.updateById(requireInfo);
        }
        boolean updated = complainService.updateById(complain);
        return ResponseProcess.returnString(updated, "操作成功", "操作失败");
    }


    //    分页获取待处理的商品投诉
    @GetMapping("/complain/waitfor/product")
    public ResponseResult complainWaitForProduct(Long current, Long size, String startTime, String endTime) {
        Page<Complain> page = new Page<>(current, size);
        IPage<Complain> post = adminUserService.getComplainWaitForProduct(page, startTime, endTime, false);
        return ResponseProcess.returnObject(post);
    }

    //    根据商品id获取商品详情
    @GetMapping("/complain/product/{id}")
    public ResponseResult getProductInfoByID(@PathVariable("id") Long id) {
        Product product = productService.getById(id);
        Category category = categoryService.getById(product.getCategoryId());
        Users users = userService.getById(product.getUserId());
        List<ProductRequire> list = productRequireService.list();
        product.setCategoryName(category.getName());
        product.setPublisher(users.getName());
        String reqName = list.stream().filter(item -> product.getProductRequireId().indexOf(item.getId().toString()) != -1).map(item -> item.getName()).collect(Collectors.joining(","));
        product.setRequireNames(reqName);
        return ResponseProcess.returnObject(product);
    }

    //    处理商品投诉
    @GetMapping("/complain/product/deal")
    public ResponseResult dealProductComplain(Long id, Long productId, Long complainantId, Long sellerId, Integer type) {
        String authorization = httpServletRequest.getHeader("authorization");
        String adminId = JWTUtil.parseJWT(authorization).getId();
        Complain complain = new Complain();
        complain.setId(id);
        complain.setDealUser(Long.valueOf(adminId));
        complain.setDealTime(new DateTime());
//        投诉人通知
        Notice complaintNotice = new Notice();
        complaintNotice.setTitle("投诉处理结果通知");
        complaintNotice.setAttachType(1);
        complaintNotice.setAttach(productId);
        complaintNotice.setPublisher(sellerId);
        complaintNotice.setReceiverId(complainantId);
//        发帖人通知
        Notice sellerNotice = new Notice();
        sellerNotice.setReceiverId(sellerId);
        sellerNotice.setTitle("投诉处理结果通知");
        sellerNotice.setAttach(productId);
        sellerNotice.setAttachType(1);
        sellerNotice.setPublisher(sellerId);
        if (type == 0) {
            String msg = "亲爱的用户您好，您的投诉已经受理，处理意见如下：商品不存在所诉的违规内容，投诉无效";
//            投诉无效
            complain.setDealMethod("商品不存在所诉的违规内容，投诉无效");
//            通知投诉人处理结果
            complaintNotice.setContent(msg);
            //声明一个map，封装直接发送信息数据返回前端
            Map<String, Object> resultMap = SocketSessions.createMessageTemplate("投诉结果通知", msg, complain.getDealTime(), productId, SocketSessions.messageType.COMPLAIN);
            SocketSessions.sendMsg(complainantId, resultMap);
//           将通知存入数据库
            noticeService.save(complaintNotice);
        } else {
//            投诉有效
            complain.setDealMethod("帖子存在所诉的违规内容，已做下架处理");
            complaintNotice.setContent("亲爱的用户您好，您的投诉已经受理，处理意见如下：商品存在所诉的违规内容，已做下架处理");
            sellerNotice.setContent("亲爱的用户您好，您的商品存在违规情况，处理结果如下：商品存在所诉的违规内容，已做下架处理");
            //声明一个map，封装直接发送信息数据返回前端
            Map<String, Object> complaintResult = SocketSessions.createMessageTemplate("投诉结果通知", complaintNotice.getContent(), complain.getDealTime(), productId, SocketSessions.messageType.COMPLAIN);

            Map<String, Object> sellerResult = SocketSessions.createMessageTemplate("投诉结果通知", sellerNotice.getContent(), complain.getDealTime(), productId, SocketSessions.messageType.COMPLAIN);
            SocketSessions.sendMsg(complainantId, complaintResult);
            SocketSessions.sendMsg(sellerId, sellerResult);
            ArrayList<Notice> notices = new ArrayList<>();
            notices.add(complaintNotice);
            notices.add(sellerNotice);
            noticeService.saveBatch(notices);
//            对商品进行下架处理
            Product product = new Product();
            product.setId(productId);
            product.setStatus(-1);
            productService.updateById(product);
        }
        boolean updated = complainService.updateById(complain);
        return ResponseProcess.returnString(updated, "操作成功", "操作失败");
    }

    //    分页获取投诉历史
    @GetMapping("/complain/history")
    public ResponseResult complainHistory(Long current, Long size, String startTime, String endTime, Integer type) {
        if (type == 0) {
//        查询帖子投诉历史
            Page<Complain> page = new Page<>(current, size);
            IPage<Complain> post = adminUserService.getComplainWaitForPost(page, startTime, endTime, false);
            return ResponseProcess.returnObject(post);
        } else {
//         查询商品历史
            Page<Complain> page = new Page<>(current, size);
            IPage<Complain> product = adminUserService.getComplainWaitForProduct(page, startTime, endTime, true);
            return ResponseProcess.returnObject(product);
        }

    }

    //    分页获取待处理售后或售后历史
    @GetMapping("/afterService/waitfor")
    public ResponseResult waitforAfterservice(Long current, Long size, String startTime, String endTime, Integer type) {
        if (type == 0) {
//        查询待处理售后
            Page<AfterService> page = new Page<>(current, size);
            IPage<AfterService> warifor = adminUserService.afterServiceWaitDealList(page, startTime, endTime, false);
            return ResponseProcess.returnObject(warifor);
        } else {
//         查询售后历史
            Page<AfterService> page = new Page<>(current, size);
            IPage<AfterService> historyAfter = adminUserService.afterServiceWaitDealList(page, startTime, endTime, true);
            return ResponseProcess.returnObject(historyAfter);
        }
    }

//    查看聊天记录
    @GetMapping("/afterService/chatHistory")
    public ResponseResult getChatHistory(Long toUser, Long fromUser) {
        ChatUserLink one = chatUserLinkService.getOne(new LambdaQueryWrapper<ChatUserLink>()
                .eq(ChatUserLink::getToUser, toUser)
                .eq(ChatUserLink::getFromUser, fromUser)
                .or()
                .eq(ChatUserLink::getToUser, fromUser)
                .eq(ChatUserLink::getFromUser, toUser));
        if (one != null) {
            List<ChatMessage> chatMessages = chatMessageService.list(new LambdaQueryWrapper<ChatMessage>()
                    .eq(ChatMessage::getLinkId, one.getLinkId())
                    .orderByAsc(ChatMessage::getSendTime));
            return ResponseProcess.returnList(chatMessages);
        }
        return ResponseResult.fail(-1, "无聊天记录");
    }

//    平台处理售后
    @GetMapping("/afterService/deal")
    public ResponseResult dealAfterService(Long id, Long buyerId, Long sellerId, Long orderId, Long productId, String result, Integer type) {
        String authorization = httpServletRequest.getHeader("authorization");
        String adminId = JWTUtil.parseJWT(authorization).getId();
        Product product = productService.getById(productId);
        AfterService afterService = new AfterService();
        afterService.setId(id);
        afterService.setPlatformUser(Long.valueOf(adminId));
        afterService.setPlatformDealTime(new Date());
//        标记平台处理完成
        afterService.setPlatformStatus(1);
        if (type == 0) {
//            拒绝售后，
//            将订单状态恢复为已支付【1】，
            orderInfoService.update(new LambdaUpdateWrapper<OrderInfo>()
                    .set(OrderInfo::getStatus, 1)
                    .eq(OrderInfo::getOrderId, orderId));
//            标记售后状态为失败【-1】
            afterService.setStatus(-1);
            String msg = "亲爱的用户您好，您的售后申请已被受理，售后未通过审核，原因如下：" + result;
            afterService.setPlatformResult(result);
//            将通知存入数据库
            Notice notice = new Notice("售后结果通知", msg, buyerId, 1, productId, sellerId);
            noticeService.save(notice);
//            通知消费者
            Map<String, Object> map = SocketSessions.createMessageTemplate("售后结果通知", msg, afterService.getPlatformDealTime(), productId, SocketSessions.messageType.PRODUCT);
            SocketSessions.sendMsg(buyerId, map);

        } else {
//            同意售后
//           将售后状态变为待发货【11】
            afterService.setStatus(11);
            afterService.setPlatformResult("平台介入售后处理成功，买家符合售后条件");
//            扣除商家我的收益中的余额【减去商品的价格】，由于暂时未做余额提现操作，所以不做余额判断
            userService.update(new LambdaUpdateWrapper<Users>()
                    .setSql("profit = profit - " + product.getCurrentPrice().toString())
                    .eq(Users::getId, sellerId));
//            通知商家
            String sellerMsg = "亲爱的用户您好，您出售的商品平台介入售后处理成功，买家符合售后条件，现已通知买家尽快与您联系。";
            Map<String, Object> sellerMap = SocketSessions.createMessageTemplate("售后结果通知", sellerMsg, afterService.getPlatformDealTime(), productId, SocketSessions.messageType.PRODUCT);
            SocketSessions.sendMsg(sellerId, sellerMap);
            noticeService.save(new Notice("售后结果通知", "售后处理成功，买家符合售后条件", sellerId, 1, productId, sellerId));
//            通知消费者
            String buyerMsg = "亲爱的用户您好，您申请的的平台介入处理成功，现已通知卖家与您尽快联系。";
            Map<String, Object> buyerMap = SocketSessions.createMessageTemplate("售后结果通知", buyerMsg, afterService.getPlatformDealTime(), productId, SocketSessions.messageType.PRODUCT);
            SocketSessions.sendMsg(buyerId, buyerMap);
            noticeService.save(new Notice("售后结果通知", "您申请的的平台介入处理成功，现已通知卖家与您尽快联系", buyerId, 1, productId, sellerId));
        }

        boolean update = afterServiceService.updateById(afterService);
        return ResponseProcess.returnString(update, "处理成功", "处理失败");
    }
//    获取订单的售后历史
    @GetMapping("/order/afterHistory")
    public ResponseResult productAfterHistory(Long orderId){
        List<AfterService> list = afterServiceService.list(new LambdaQueryWrapper<AfterService>()
                .eq(AfterService::getOrderId, orderId)
                .ne(AfterService::getStatus, 0));
        return ResponseProcess.returnList(list);
    }

    //    分页获取商品
    @GetMapping("/admin/products")
    public ResponseResult getAllProduct(Long current, Long size, String startTime, String endTime, String detail) {
            Page<Product> page = new Page<>(current, size);
            IPage<Product> historyAfter = adminUserService.getAllProducts(page, startTime, endTime, detail);
            return ResponseProcess.returnObject(historyAfter);
    }

    @GetMapping("/sendMsg")
    public void sendMSG(Long toUser) {
        Session sendSession = SocketSessions.map.get(toUser);
        if (sendSession != null && sendSession.isOpen()) {
            sendSession.getAsyncRemote().sendText("ceshi");
        }
    }
}
