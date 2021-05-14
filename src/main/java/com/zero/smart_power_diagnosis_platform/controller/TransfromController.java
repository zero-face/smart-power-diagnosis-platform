package com.zero.smart_power_diagnosis_platform.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zero.common.controller.BaseController;
import com.zero.common.error.BusinessException;
import com.zero.common.error.EmBusinessError;
import com.zero.common.response.CommonReturnType;
import com.zero.smart_power_diagnosis_platform.controller.VO.TransfromVO;
import com.zero.smart_power_diagnosis_platform.entity.EleSite;
import com.zero.smart_power_diagnosis_platform.entity.Transfrom;
import com.zero.smart_power_diagnosis_platform.service.EleSiteService;
import com.zero.smart_power_diagnosis_platform.service.TransfromService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;
/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zero
 * @since 2021-05-09
 */
@Api(tags = "变压器管理")
@RestController
@RequestMapping("/smart_power_diagnosis_platform/transfrom")
public class TransfromController extends BaseController {
    @Autowired
    private TransfromService transfromService;
    @Autowired
    private EleSiteService eleSiteService;

    /**
     * 获取所有异常变压器用于预警页面展示
     * @return
     */
    @GetMapping("/getwarntrans")
    @ApiOperation("获取所有的异常变压器信息")
    public CommonReturnType getWarnTrans() {
        List<Transfrom> transfroms = transfromService.getTransWarn();
        List<TransfromVO> transfromVOS = transfroms.stream().map(transfrom -> {
            TransfromVO transfromVO = null;
            try {
                transfromVO = transfromService.convertTranfromToTranVO(transfrom);
            } catch (BusinessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return transfromVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(transfromVOS);
    }

    /**
     * 根据请求模块id获取所有的变压器信息
     * @param siteId
     * @return
     */
    @ApiOperation("获取指定站点的变压器信息")
    @GetMapping("/gettrans/{siteid}")
    public CommonReturnType getTransBySiteId(@PathVariable(value = "siteid")Integer siteId) {
        QueryWrapper<Transfrom> wrapper = new QueryWrapper<>();
        wrapper.eq("site_id", siteId);
        List<Transfrom> list = transfromService.list(wrapper);
        List<TransfromVO> transfromVOS = list.stream().map(transfrom -> {
            TransfromVO transfromVO = null;
            try {
                transfromVO = transfromService.convertTranfromToTranVO(transfrom);
            } catch (BusinessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return transfromVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(transfromVOS);
    }

    /**
     * 根据站点id和变压器id拿到变压器位置和状态信息
     * @param siteId
     * @param transId
     * @return
     * @throws BusinessException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @ApiOperation("获取指定变压器的信息")
    @GetMapping("/gettransinfo/{siteid}/{transid}")
    public CommonReturnType getTranInfoByTranId(@PathVariable(value = "siteid")Integer siteId,
                                                @PathVariable(value = "transid")Integer transId) throws BusinessException, IllegalAccessException, InvocationTargetException {
        QueryWrapper<Transfrom> wrapper = new QueryWrapper<>();
        wrapper.eq("site_id", siteId)
                .eq("trans_id", transId);
        Transfrom transfrom = transfromService.getOne(wrapper);
        if(null == transfrom) {
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR,"不存在该设备");
        }
        TransfromVO transfromVO = transfromService.convertTranfromToTranVO(transfrom);
        return CommonReturnType.create(transfromVO);
    }

    /**
     * 改变状态，检测该站点是否还有错误状态
     * @param siteId
     * @param transId
     * @return
     */
    @ApiOperation("恢复指定变压器的状态")
    @GetMapping("/restore/{siteid}/{transid}")
    public CommonReturnType restoreStatus(@PathVariable(value = "siteid")Integer siteId,
                                          @PathVariable(value = "transid")Integer transId) throws BusinessException {
        UpdateWrapper<Transfrom> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("site_id",  siteId)
                .eq("trans_id", transId)
                .set("status", 1);
        boolean update = transfromService.update(updateWrapper);
        if(!update) {
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR,"设备不存在");
        }
        //查询该变压器所在站点的所有变压器状态
        List<Transfrom> allErrTransBySiteId = transfromService.getAllErrTransBySiteId(siteId);
        if(null == allErrTransBySiteId || allErrTransBySiteId.isEmpty()) {
            UpdateWrapper<EleSite> wrapper = new UpdateWrapper<>();
            wrapper.eq("site_id", siteId)
                    .set("status", 1);
            eleSiteService.update(wrapper);
        }
        return CommonReturnType.create("修改成功");
    }
}

