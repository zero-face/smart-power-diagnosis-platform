package com.zero;


import com.zero.smart_power_diagnosis_platform.common.util.JWTUtils;

import com.zero.smart_power_diagnosis_platform.common.util.OSSUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Zero
 * @Date 2021/5/13 12:26
 * @Since 1.8
 **/
@SpringBootTest
public class GetToken {
    @Autowired
    private OSSUtils ossUtils;
    @Value("${code.sign}")
    private String SIGN;

    @Test
    void getToken() {
        Map<String ,String> userInfo = new HashMap<>();
        userInfo.put("openid", "242546");
        String token = JWTUtils.getToken(userInfo, SIGN);
        System.out.println(token);
    }

}
