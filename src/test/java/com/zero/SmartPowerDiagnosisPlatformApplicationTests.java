package com.zero;


import com.zero.smart_power_diagnosis_platform.common.error.BusinessException;
import com.zero.smart_power_diagnosis_platform.common.util.JWTUtils;
import com.zero.smart_power_diagnosis_platform.service.UserInfoService;
import com.zero.smart_power_diagnosis_platform.util.JWTUtil;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class SmartPowerDiagnosisPlatformApplicationTests {
    @Autowired
    private UserInfoService userInfoService;
    @Value("${code.appid}")
    private String appid; //appid用来获取openID
    @Value("${code.secret}")
    private String secret; //secret用来获取openID
    @Value("${code.sign}")
    private String SIGN;
    @Test
    void testGetappid() {
        System.out.println(appid);
    }
    @Test
    void t() {
        Map<String,String> map = new HashMap<>();
        map.put("openid", "143256342");
        System.out.println(JWTUtils.getToken(map, SIGN));
    }
    @Test
    void contextLoads() throws IOException, BusinessException {
        Map<String,String> map = new HashMap<>();
        map.put("wd","td");
        System.out.println(userInfoService.getOpenIdByCode("https://www.baidu.com/",map));
    }
    @Test
    void te() {
        String url = "https://common-zero.oss-cn-beijing.aliyuncs.com/files/20210513/020543Nbfn4.jpg";
        //https://common-zero.oss-cn-beijing.aliyuncs.com/files/20210513/020543Nbfn4.jpg
        final String[] split = url.split("\\.");
        for (int i = 0; i < split.length; i++) {
            System.out.println(split[i]);
        }
        System.out.println(split[3].substring(4));
    }

}
