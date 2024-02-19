package com.xianhuo.xianhuobackend;

import com.xianhuo.xianhuobackend.entity.UniIdRequestBody;
import com.xianhuo.xianhuobackend.service.DispatchModeService;
import com.xianhuo.xianhuobackend.utils.HMACSha256;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class XianHuoBackendApplicationTests {
    @Autowired
    private DispatchModeService dispatchModeService;
    @Test
    void contextLoads() {
//        dispatchModeService.getUsableDispatchModeBySellId(1l);
        System.out.println(System.getProperty("user.dir"));
    }
    @Test
    void test1(){
        //注册uni-id
        String nonce = "sdfafdafsagjhhkbd";
        long timestamp = System.currentTimeMillis();
        Map<String, String> params = new HashMap<>();
        params.put("externalUid","1");
        params.put("nickname","测试");
        params.put("avatar","/img/1/avatar.jpg");
        String signature = HMACSha256.getSignature(params, nonce, timestamp);
        UniIdRequestBody uniIdRegisterParams = new UniIdRequestBody();
        uniIdRegisterParams.setExternalUid("1");
        uniIdRegisterParams.setNickname("测试");
        uniIdRegisterParams.setAvatar("/img/1/avatar.jpg");
        WebClient webClient = WebClient.create();
        Mono<String> mono = webClient.post().uri("https://fc-mp-cb2eb9b5-3cbb-47a7-aa08-383cdf5374d2.next.bspapp.com/uni-id-co/externalRegister")
                .header("uni-id-nonce", nonce)
                .header("uni-id-timestamp", String.valueOf(timestamp))
                .header("uni-id-signature", signature)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(uniIdRegisterParams), UniIdRequestBody.class)
                .retrieve().bodyToMono(String.class);
        System.out.println(mono.block());
    }

}
