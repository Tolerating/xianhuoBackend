package com.xianhuo.xianhuobackend.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum REnum {
    SUCCESS(200, "success"),
    FAIL(500, "fail");

    private final Integer code;
    private final String desc;
}