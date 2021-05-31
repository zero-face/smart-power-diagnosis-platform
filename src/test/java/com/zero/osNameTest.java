package com.zero;

import com.zero.smart_power_diagnosis_platform.service.UserInfoService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author Zero
 * @Date 2021/5/11 17:05
 * @Since 1.8
 **/
@SpringBootTest
class osNameTest {

    @Value("${os.name}")
    private String osName;


    @Test
    void testOSName() {
        System.out.println(osName); //Windows 10
    }
}
