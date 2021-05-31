package com.zero.smart_power_diagnosis_platform.interceptor;


import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.zero.smart_power_diagnosis_platform.common.error.BusinessException;
import com.zero.smart_power_diagnosis_platform.common.error.EmBusinessError;
import com.zero.smart_power_diagnosis_platform.common.util.JWTUtils;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author Zero
 * @Date 2021/5/10 19:08
 * @Since 1.8
 **/
@Component
public class JWTInterceptor implements HandlerInterceptor {
    @Value("${code.sign}")
    private String SIGN;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String token = request.getHeader("token");
        try {
            JWTUtils.verifyToken(token, SIGN);
        } catch (SignatureVerificationException e) {
            throw new BusinessException(EmBusinessError.WRONG_TOKEN);
        } catch (TokenExpiredException e) {
            throw new BusinessException(EmBusinessError.JWT_TOKEN_EXPIRED);
        } catch (AlgorithmMismatchException e) {
            throw new BusinessException(EmBusinessError.WRONG_TOKEN);
        } catch (Exception e) {
            throw new BusinessException(EmBusinessError.WRONG_TOKEN);
        }
        return true;
    }
}
