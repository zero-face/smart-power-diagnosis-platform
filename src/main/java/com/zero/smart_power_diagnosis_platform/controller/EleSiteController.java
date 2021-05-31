package com.zero.smart_power_diagnosis_platform.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.zero.smart_power_diagnosis_platform.common.controller.BaseController;
import com.zero.smart_power_diagnosis_platform.common.error.BusinessException;
import com.zero.smart_power_diagnosis_platform.common.error.EmBusinessError;
import com.zero.smart_power_diagnosis_platform.common.response.CommonReturnType;
import com.zero.smart_power_diagnosis_platform.controller.VO.EleSiteVO;
import com.zero.smart_power_diagnosis_platform.entity.EleSite;
import com.zero.smart_power_diagnosis_platform.entity.Transfrom;
import com.zero.smart_power_diagnosis_platform.service.EleSiteService;
import com.zero.smart_power_diagnosis_platform.service.TransInfoService;
import com.zero.smart_power_diagnosis_platform.service.TransfromService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/smart_power_diagnosis_platform/ele-site")
@Api(tags = "站点信息管理")
public class EleSiteController extends BaseController {
    @Autowired
    private EleSiteService eleSiteService;
    @Autowired
    private TransfromService transfromService;

    /**
     * 首页概括页面
     * @return
     */
    @GetMapping("/getallstatus")
    public CommonReturnType getStatus() {
        int errCount = transfromService.count(new QueryWrapper<Transfrom>().eq("status", 0));
        int errSiteCount = eleSiteService.count(new QueryWrapper<EleSite>().eq("status", 0));
        Integer siteCount = eleSiteService.count(new QueryWrapper<EleSite>());
        Integer transCount = transfromService.count(new QueryWrapper<Transfrom>());
        Integer repairCount = eleSiteService.getRepairCount();
        Map<String,Object> maps = new HashMap<>();
        maps.put("errSiteCount", errCount);
        maps.put("siteCount", siteCount);
        maps.put("transCount", transCount);
        maps.put("status",errCount == 0 ? "正常" : "异常");
        maps.put("commonSiteCount", siteCount - errSiteCount);
        maps.put("repairCount",repairCount);
        return CommonReturnType.success(maps,"首页概况数据");
    }

    @GetMapping("/get_all_siteinfo")
    public CommonReturnType getAllSite() throws BusinessException {
        List<EleSite> allSiteInfo = eleSiteService.getAllSiteInfo();
        if(null == allSiteInfo) {
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR,"没有获取到任何站点信息");
        }
        List<EleSiteVO> eleSiteVOS = eleSiteService.convertListFromEleSiteToEleVO(allSiteInfo);
        return CommonReturnType.success(eleSiteVOS,"返回的是所有站点的信息");
    }

}

