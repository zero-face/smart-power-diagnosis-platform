package com.zero.smart_power_diagnosis_platform.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zero.common.controller.BaseController;
import com.zero.common.error.BusinessException;
import com.zero.common.error.EmBusinessError;
import com.zero.common.response.CommonReturnType;
import com.zero.common.util.JWTUtils;
import com.zero.smart_power_diagnosis_platform.controller.VO.UserVO;
import com.zero.smart_power_diagnosis_platform.controller.certificate.wxKey;
import com.zero.smart_power_diagnosis_platform.entity.UserInfo;
import com.zero.smart_power_diagnosis_platform.mapper.UserInfoMapper;
import com.zero.smart_power_diagnosis_platform.service.UserInfoService;
import com.zero.smart_power_diagnosis_platform.util.JWTUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zero
 * @since 2021-05-09
 */
@Slf4j
@RestController
@RequestMapping("/smart_power_diagnosis_platform/user-info")
@Api(tags = "用户信息管理")
public class UserInfoController extends BaseController {
    @Value("${code.appid}")
    private String appid; //appid用来获取openID
    @Value("${code.secret}")
    private String secret; //secret用来获取openID
    @Value("${code.sign}")
    private String SIGN;
    @Autowired
    private UserInfoService userInfoService;

    //获取openid接口
    private String GETOPENIDURL= "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 用户登录接口(页面刷新发现有token、根据前端返回的token请求登录)
     */
    @PostMapping("/login")
    @ApiOperation("用户登录接口")
    public CommonReturnType login(HttpServletRequest request,
                                  @RequestParam(value = "nickName", required = false)String nickName,
                                  @RequestParam(value = "avatarUrl",required = false)String avatarUrl,
                                  @RequestParam(value = "gender",required = false)String gender
                                  ) throws IllegalAccessException, BusinessException, InvocationTargetException {

        //拿着本地的token来获取数据库用户信息（过期则需要重新登录），返回给前端
        String token = request.getHeader("token");
        //验证token、通过并且获取里面的openid
        String openId = JWTUtil.getOpenId(token, SIGN);
        log.info("openid:{}",openId);
        //数据库中拿出用户信息返回给前端
        UserInfo userInfo = userInfoService.getUserInfoByOpenId(openId);
        if(null == userInfo) {
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        UserVO userVO = userInfoService.convertFromUserInfoToUserVO(userInfo);
        if(!StringUtils.isEmpty(nickName) && !StringUtils.isEmpty(avatarUrl) && !StringUtils.isEmpty(gender)) {
            UserInfo userInfo1 = new UserInfo();
            userInfo1.setOpenid(openId);
            userInfo1.setAvatarUrl(avatarUrl);
            userInfo1.setGender(gender);
            userInfo1.setNickName(nickName);
            UpdateWrapper<UserInfo> wrapper = new UpdateWrapper<>();
            wrapper.isNotNull("openid");
            userInfoService.saveOrUpdate(userInfo1,wrapper);
        }
        return CommonReturnType.create(userVO);
    }
    /**
     * 接收code,获取openid接口
     */
    @PostMapping("/code")
    @ApiOperation("根据code获取openid并得到登录token")
    public CommonReturnType receiveCode(@RequestParam(value = "code") String code) throws IllegalAccessException, BusinessException, InvocationTargetException {
        log.info("获取code开始=========》");
        //接收到临时登录的code，向微信服务器发起请求，获取openid,session_key，unionid
        CloseableHttpClient client = null;
        Map<String,String> map = new HashMap<>();
        map.put("appid", appid);
        map.put("code", code);
        map.put("secret", secret);
        String cretificate = userInfoService.getOpenIdByCode(GETOPENIDURL, map);
        log.info("这json字符串：{}",cretificate);
        //解析这个json字符串
        log.info("开始解析字符串=========》");
        JSONObject object = JSONObject.parseObject(cretificate);
        wxKey wxKey = JSON.toJavaObject(object, wxKey.class);
        String openid = wxKey.getOpenid();
        log.info("获取到openid:{}=========》",openid);
        //查询数据库中是否含有这个openID，如果有，说明已经授权，查询用户信息，返回信息以及token；没有则保存，但是其他的用户信息是空，返回用户信息以及token
        UserInfo userInfo = userInfoService.getUserInfoByOpenId(openid);
        String token = userInfoService.getToken(userInfo, openid);
        Map<String,Object> reToken = new HashMap<>();
        reToken.put("token", token);
        return CommonReturnType.create(reToken);
    }

    /**
     * 用户查询接口
     */

    /**
     * 用户删除接口
     */
    @DeleteMapping("/deleteuser")
    public CommonReturnType deleteUser(@RequestParam(value = "openid")String openid) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openid);
        userInfoService.remove(wrapper);
        return CommonReturnType.create("删除成功");
    }

    /**
     * 获取所有用户
     */
    /**
     * 用户查询接口
     */
    @GetMapping("select/{openid}")
    public CommonReturnType SelectUser(@PathVariable Integer openid) {
        log.info("{}",openid);
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.lambda().eq(UserInfo::getOpenid,openid);
        UserInfo userInfo = userInfoMapper.selectOne(userInfoQueryWrapper);
        return CommonReturnType.create(userInfo);
    }

    /**
     * 用户删除接口
     */
    @PostMapping("/delete")
    public CommonReturnType deleteUser(Integer openid) {
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("openid", openid);
        int delete = userInfoMapper.delete(userInfoQueryWrapper);
        if(delete == 0) {
            return CommonReturnType.create("用户不存在");
        }
        return CommonReturnType.create("删除成功");
    }

    /**
     * 获取所有用户
     */
    @Autowired
    private UserInfoMapper userInfoMapper;
    @PostMapping("/getAll")
    public CommonReturnType getAllUser() {
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.isNotNull("openid");
        List<UserInfo> user = userInfoMapper.selectList(userInfoQueryWrapper);
        return CommonReturnType.create(user);
    }



}

