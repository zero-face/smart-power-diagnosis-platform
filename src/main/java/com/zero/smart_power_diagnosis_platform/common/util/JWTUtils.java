package com.zero.smart_power_diagnosis_platform.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.zero.smart_power_diagnosis_platform.common.error.BusinessException;
import com.zero.smart_power_diagnosis_platform.common.error.EmBusinessError;
import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @Author Zero
 * @Date 2021/4/20 23:28
 * @Since 1.8
 **/
public class JWTUtils {

    //生成token header payload sign
    public static String getToken(Map<String,String> map,String SIGN) {
        if(null == SIGN) {
            try {
                throw new BusinessException(EmBusinessError.UNKNOWN_ERROR);
            } catch (BusinessException e) {
                e.printStackTrace();
            }
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);
        JWTCreator.Builder builder = JWT.create();

        map.forEach((k,v) -> {
            builder.withClaim(k,v);
        });
        final String token = builder.withExpiresAt(calendar.getTime())
                .sign(Algorithm.HMAC256(SIGN));
        return token;
    }

    /**
     * 获取decodejwt
     * @param token
     * @param SIGN
     * @return
     */
    public static DecodedJWT verifyToken(String token,String SIGN) {
        if(null == token) {
            try {
                throw new BusinessException(EmBusinessError.WRONG_TOKEN);
            } catch (BusinessException e) {
                e.printStackTrace();
            }
        }
        if(SIGN == null){
            try {
                throw new BusinessException(EmBusinessError.UNKNOWN_ERROR);
            } catch (BusinessException e) {
                e.printStackTrace();
            }
        }
        return JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
    }
    /**
     * 验证是token否过期
     */
    public boolean isExpired(String token,String sign) throws IllegalAccessException, BusinessException {
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(sign)) {
            throw new IllegalAccessException("参数不合法");
        }
        if(new Date().getTime() < parseExpiredTime(token, sign).getTime()){
            return false;
        }
        return true;
    }

    /**
     * 解析过期时间
     * @return
     */
    public static Date parseExpiredTime(String token, String sign) throws BusinessException {
        return verifyToken(token, sign).getExpiresAt();

    }
}

