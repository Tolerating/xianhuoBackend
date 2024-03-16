package com.xianhuo.xianhuobackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xianhuo.xianhuobackend.mapper.NoticeMapper;
import com.xianhuo.xianhuobackend.entity.Notice;
import com.xianhuo.xianhuobackend.service.NoticeService;
import org.springframework.stereotype.Service;

/**
 * 官方通知(Notice)表服务实现类
 *
 * @author makejava
 * @since 2024-03-16 16:56:26
 */
@Service("noticeService")
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

}

