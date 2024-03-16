package com.xianhuo.xianhuobackend.controller;
import com.xianhuo.xianhuobackend.service.NoticeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 官方通知(Notice)表控制层
 *
 * @author makejava
 * @since 2024-03-16 16:56:26
 */
@RestController
@RequestMapping("notice")
public class NoticeController {
    /**
     * 服务对象
     */
    @Resource
    private NoticeService noticeService;

}

