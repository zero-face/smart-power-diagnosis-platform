package com.zero.smart_power_diagnosis_platform.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.zero.smart_power_diagnosis_platform.common.error.BusinessException;
import com.zero.smart_power_diagnosis_platform.common.error.EmBusinessError;
import com.zero.smart_power_diagnosis_platform.common.util.JWTUtils;
import com.zero.smart_power_diagnosis_platform.controller.VO.UserVO;
import com.zero.smart_power_diagnosis_platform.controller.certificate.wxKey;
import com.zero.smart_power_diagnosis_platform.entity.UserInfo;
import com.zero.smart_power_diagnosis_platform.mapper.UserInfoMapper;
import com.zero.smart_power_diagnosis_platform.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zero
 * @since 2021-05-10
 */
@Slf4j
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Value("${code.sign}")
    private String SIGN;

    @Override
    public UserInfo getUserInfoByOpenId(String openid) {
        //根据openid查询用户信息
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("openid", openid);
        UserInfo userInfo = userInfoMapper.selectOne(userInfoQueryWrapper);
        return userInfo;
    }

    @Override
    public String getOpenIdByCode(String url, Map<String, String> map) throws BusinessException, IOException {

            /**
             * 在4.0及以上httpclient版本中，post需要指定重定向的策略，如果不指定则按默认的重定向策略。
             * 获取httpclient客户端
             */
            log.info("获取openid开始=========》");
            CloseableHttpClient httpclient = HttpClientBuilder.create().setRedirectStrategy( new LaxRedirectStrategy()).build();
            String jsonString = "";
            CloseableHttpResponse response = null;
            try {
                StringBuffer params = new StringBuffer();
                params.append("?appid=" + map.get("appid")+"&secret="+map.get("secret") + "&"+"js_code=" + map.get("code") + "&" + "grant_type=authorization_code");

                //创建一个get连接
                HttpGet httpGet = new HttpGet(url + params);
                log.info(url+params);
                response = httpclient.execute(httpGet);
                log.info("response:{}",response);
                if(200==response.getStatusLine().getStatusCode()){
                    HttpEntity entity = response.getEntity();
                    jsonString = EntityUtils.toString(entity, "utf-8");
                } else {
                    throw new BusinessException(EmBusinessError.UNKNOWN_ERROR);
                }
            } finally {
                if(null!=response){
                    try {
                        response.close();
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
                if(null!=httpclient){
                    try {
                        httpclient.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return jsonString;
    }

    @Override
    public String doPost(String url) throws IOException, BusinessException {
        return getOpenIdByCode(url, null);
    }

    @Override
    public UserVO convertFromUserInfoToUserVO(UserInfo userInfo) throws IllegalAccessException, BusinessException, InvocationTargetException {
        if(null == userInfo) {
            //如果用户未空，说明不存在这样的用户，直接报错
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userVO,userInfo);
        return userVO;
    }

    @Override
    public String getToken(UserInfo userInfo) {
        if(null == userInfo) {
            return null;
        }
        Map<String,String> tokenInfo = new HashMap<>();
        tokenInfo.put("nickName", userInfo.getNickName());
        tokenInfo.put("openid",userInfo.getOpenid());
        tokenInfo.put("avatarUrl",userInfo.getAvatarUrl());
        tokenInfo.put("gender", userInfo.getGender());
        String token = JWTUtils.getToken(tokenInfo, SIGN);
        return token;
    }

    @Override
    public String parseJson(String str) throws BusinessException {
        JSONObject object = JSONObject.parseObject(str);
        wxKey wxKey = JSON.toJavaObject(object, wxKey.class);
        if(null== wxKey) {
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR);
        }
        if(null != wxKey.getErrcode() && 40029==wxKey.getErrcode()){
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR, "无效的code");
        }
        if(null != wxKey.getErrcode() && 45011==wxKey.getErrcode()){
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR, "登录过于频繁，请稍后再试.....");
        }if(null != wxKey.getErrcode() && -1==wxKey.getErrcode()){
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR, "系统繁忙");
        }
        String openid = wxKey.getOpenid();
        return openid;
    }
}
