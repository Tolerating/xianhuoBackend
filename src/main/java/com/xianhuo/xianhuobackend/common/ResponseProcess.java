package com.xianhuo.xianhuobackend.common;

import java.util.List;

public class ResponseProcess<T> {

    //返回List<T>类型的数据
    public static <T> ResponseResult<List<T>> returnList(List<T> data){
        if(data.isEmpty()){
            return ResponseResult.fail(null,"fail");
        }else{
            return ResponseResult.ok(data,"success");
        }
    }
}
