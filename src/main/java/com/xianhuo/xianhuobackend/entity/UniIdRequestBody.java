package com.xianhuo.xianhuobackend.entity;

import lombok.Data;

@Data
public class UniIdRequestBody {
    private String externalUid;
    private String nickname;
    private String avatar;
    private Integer gender;

}


