package com.zero.smart_power_diagnosis_platform.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zero.common.controller.BaseController;
import com.zero.common.response.CommonReturnType;
import com.zero.smart_power_diagnosis_platform.entity.EleSite;
import com.zero.smart_power_diagnosis_platform.service.EleSiteService;
import com.zero.smart_power_diagnosis_platform.service.TransInfoService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
@RequestMapping("/smart_power_diagnosis_platform/ele-site")
@Api(tags = "站点信息管理")
public class EleSiteController extends BaseController {
    @Autowired
    private EleSiteService eleSiteService;

    /**
     * 查询所有站点的状态
     */
    @GetMapping("/getallstatus")
    public CommonReturnType getStatus() {
        QueryWrapper<EleSite> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 0);
        int errCount = eleSiteService.count(wrapper);
        Map<String, Object> map = eleSiteService.getMap(wrapper);
        if(null == map) {
            Map<String, Object> result = new HashMap<>();
            errCount = 0;
            result.put("count",errCount);
            return CommonReturnType.create(result);
        }
        map.put("count", errCount);
        return CommonReturnType.create(map);
    }




    /**
     * 站点增加
     */

    /**
     * 站点删除
     */

    /**
     * 站点查询
     */

    /**
     * 站点
     */


}

