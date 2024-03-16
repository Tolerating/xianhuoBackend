package com.xianhuo.xianhuobackend.controller;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xianhuo.xianhuobackend.common.ResponseProcess;
import com.xianhuo.xianhuobackend.common.ResponseResult;
import com.xianhuo.xianhuobackend.entity.Complain;
import com.xianhuo.xianhuobackend.service.ComplainService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 投诉表(Complain)表控制层
 *
 * @author makejava
 * @since 2024-03-12 21:28:12
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
public class ComplainController {
    /**
     * 服务对象
     */
    @Resource
    private ComplainService complainService;

   @PostMapping("/complain")
    public ResponseResult addComplain(@RequestBody Complain complain){
       Complain one = complainService.getOne(new LambdaQueryWrapper<Complain>()
               .eq(Complain::getComplainantId, complain.getComplainantId())
               .eq(Complain::getComplainantSubject, complain.getComplainantSubject()));
       if(one==null){
           boolean saved = complainService.save(complain);
           return ResponseProcess.returnString(saved,"投诉成功","投诉失败");
       }
       return ResponseResult.fail(null,"请勿重复投诉");
   }
}

