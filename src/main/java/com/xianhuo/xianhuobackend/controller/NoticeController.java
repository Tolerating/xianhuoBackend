package com.xianhuo.xianhuobackend.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xianhuo.xianhuobackend.common.ResponseProcess;
import com.xianhuo.xianhuobackend.common.ResponseResult;
import com.xianhuo.xianhuobackend.entity.Notice;
import com.xianhuo.xianhuobackend.service.NoticeService;
import com.xianhuo.xianhuobackend.utils.JWTUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 官方通知(Notice)表控制层
 *
 * @author makejava
 * @since 2024-03-16 16:56:26
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
public class NoticeController {
    /**
     * 服务对象
     */
    @Resource
    private NoticeService noticeService;
    @Resource
    private HttpServletRequest httpServletRequest;

    @GetMapping("/notices")
    public ResponseResult allNotices(){
        String authorization = httpServletRequest.getHeader("authorization");
        String id = JWTUtil.parseJWT(authorization).getId();
        List<Notice> list = noticeService.list(new LambdaQueryWrapper<Notice>()
                .eq(Notice::getReceiverId, id)
                .orderByDesc(Notice::getCreateTime));
        return ResponseProcess.returnList(list);
    }
//    删除通知
    @GetMapping("/notice/del")
    public ResponseResult delNotice(Long id){
        boolean removed = noticeService.removeById(id);
        return ResponseProcess.returnString(removed,"删除成功","删除失败");
    }
}

