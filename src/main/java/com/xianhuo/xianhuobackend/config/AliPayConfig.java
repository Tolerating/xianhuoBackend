package com.xianhuo.xianhuobackend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "alipay")
public class AliPayConfig {
    private String appid;
    private String appPrivateKey;
    private String alipayPublicKey;
    private String notifyUrl;
}
