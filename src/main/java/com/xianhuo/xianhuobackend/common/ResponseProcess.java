package com.xianhuo.xianhuobackend.common;

import java.util.List;

public class ResponseProcess<T> {

    //返回List<T>类型的数据
    public static <T> ResponseResult<List<T>> returnList(List<T> data){
        if(data.isEmpty()){
            return ResponseResult.fail(null,"获取到的数据为空");
        }else{
            return ResponseResult.ok(data,"success");
        }
    }

    public static <T> ResponseResult<T> returnObject(T data){
        if (data.equals(null)){
            return ResponseResult.fail(null,"fail");
        }else{
            return ResponseResult.ok(data,"success");
        }
    }

    public static ResponseResult<String> returnString(Boolean flag,String success,String fail){
        if(flag){
            return ResponseResult.ok("1",success);
        }
        return ResponseResult.fail("0",fail);

    }

    public static ResponseResult<String> returnLong(Long flag,String success,String fail){
        if(flag>0){
            return ResponseResult.ok(flag.toString(),success);
        }
        return ResponseResult.fail(flag.toString(),fail);

    }
}
