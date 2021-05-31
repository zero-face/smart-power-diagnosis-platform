package com.zero.smart_power_diagnosis_platform.service;


import com.zero.smart_power_diagnosis_platform.common.error.BusinessException;
import com.zero.smart_power_diagnosis_platform.controller.VO.UserVO;
import com.zero.smart_power_diagnosis_platform.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zero
 * @since 2021-05-10
 */

public interface UserInfoService extends IService<UserInfo> {
    /**
     * 根据openid拿到数据库用户信息
     * @param openid
     * @return String
     * @throws IllegalAccessException
     * @throws BusinessException
     * @throws InvocationTargetException
     */

    UserInfo getUserInfoByOpenId(String openid) throws IllegalAccessException, BusinessException, InvocationTargetException, BusinessException;

    /**
     * 根据前端返回的code请求openid
     * @param url
     * @param map
     * @return String
     */
    String getOpenIdByCode(String url, Map<String,String> map) throws BusinessException, IOException;

    /**
     *
     * @param  url
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws BusinessException
     */
    String doPost(String url) throws IOException, BusinessException;
    /**
     * 将数据库user对象转换为用户模型对象
     * @param userInfo
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws BusinessException
     */
    UserVO convertFromUserInfoToUserVO(UserInfo userInfo) throws InvocationTargetException, IllegalAccessException, BusinessException;

    /**
     *
     * @param userInfo
     * @return
     */
    String getToken(UserInfo userInfo);

    String parseJson(String str) throws BusinessException;
}
