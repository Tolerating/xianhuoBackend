package com.xianhuo.xianhuobackend;

import com.xianhuo.xianhuobackend.service.DispatchModeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class XianHuoBackendApplicationTests {
    @Autowired
    private DispatchModeService dispatchModeService;
    @Test
    void contextLoads() {
        dispatchModeService.getUsableDispatchModeBySellId(1l);
    }

}
