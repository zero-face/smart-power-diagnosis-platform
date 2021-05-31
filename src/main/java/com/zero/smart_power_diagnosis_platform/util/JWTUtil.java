package com.zero.smart_power_diagnosis_platform.util;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.zero.smart_power_diagnosis_platform.common.error.BusinessException;
import com.zero.smart_power_diagnosis_platform.common.error.EmBusinessError;
import com.zero.smart_power_diagnosis_platform.common.util.JWTUtils;


/**
 * @Author Zero
 * @Date 2021/5/10 18:49
 * @Since 1.8
 **/
public class JWTUtil {
    /**
     * 从token中获取用户信息
     * @return
     */

    public static String getOpenId(String token, String SIGN) throws BusinessException {
        if(null == token) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //验证token
        DecodedJWT decodedJWT = JWTUtils.verifyToken(token, SIGN);
        //获取token中的openid
        String openid = decodedJWT.getClaims().get("openid").asString();
        return openid;
    }
}
