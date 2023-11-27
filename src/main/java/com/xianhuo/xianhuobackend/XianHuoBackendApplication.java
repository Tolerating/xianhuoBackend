package com.xianhuo.xianhuobackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xianhuo.xianhuobackend.mapper")
public class XianHuoBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(XianHuoBackendApplication.class, args);
    }

}
