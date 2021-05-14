package com.zero.smart_power_diagnosis_platform.controller;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zero.common.controller.BaseController;
import com.zero.common.error.BusinessException;
import com.zero.common.error.EmBusinessError;
import com.zero.common.response.CommonReturnType;
import com.zero.common.util.MailUtils;
import com.zero.smart_power_diagnosis_platform.controller.VO.TransVO;
import com.zero.smart_power_diagnosis_platform.entity.EleSite;
import com.zero.smart_power_diagnosis_platform.entity.TransInfo;
import com.zero.smart_power_diagnosis_platform.entity.Transfrom;
import com.zero.smart_power_diagnosis_platform.service.EleSiteService;
import com.zero.smart_power_diagnosis_platform.service.TransInfoService;
import com.zero.smart_power_diagnosis_platform.service.TransfromService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.print.DocFlavor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
@RequestMapping("/smart_power_diagnosis_platform/trans-info")
@Api(tags = "信息收集")
public class TransInfoController extends BaseController {
    @Autowired
    private TransInfoService transInfoService;
    @Autowired
    private TransfromService transfromService;
    @Autowired
    private EleSiteService eleSiteService;
    @Resource
    private MailUtils mailUtils;

    /**
     * 获取最近的一次的信息
     */
    @GetMapping("/getlastinfo/{siteid}/{transid}")
    public CommonReturnType getLastImg(@PathVariable(value = "siteid")Integer siteId,
                                       @PathVariable(value = "transid")Integer transId) throws BusinessException, InvocationTargetException, IllegalAccessException {
        TransInfo transInfo = transInfoService.getLastInfo(siteId, transId);
        if(null == transInfo) {
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR,"设备不存在");
        }
        TransVO transVO = transInfoService.convertTransInfoToVO(transInfo);
        return CommonReturnType.create(transVO);
    }

    /**
     * 获取当前日期前指定天数历史信息，默认7天
     */
    @ApiOperation("获取指定天数的数据信息")
    @ApiParam(value = "day")
    @GetMapping("/gethistoryinfo/{siteid}/{transid}")
    public CommonReturnType getHistoryImgs(@PathVariable(value = "siteid")Integer siteid,
                                           @PathVariable(value = "transid")Integer transid,
                                           @RequestParam(value = "day", required = false, defaultValue = "7") Integer day) throws BusinessException {
        List<TransInfo> allInfo = transInfoService.getAllInfo(siteid, transid, day);
        if(allInfo == null || allInfo.isEmpty()) {
            throw  new BusinessException(EmBusinessError.UNKNOWN_ERROR,"设备不存在");
        }
        List<TransVO> transVOS = allInfo.stream().map(info -> {
            TransVO transVO = null;
            try {
                transVO = transInfoService.convertTransInfoToVO(info);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (BusinessException e) {
                e.printStackTrace();
            }
            return transVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(transVOS);
    }


    /**
     * 接收一台设备的数据
     */
    @PostMapping("/getinfo/{siteid}/{transid}")
    @ApiOperation("获取一台设备的数据")
    public CommonReturnType receiveOne(@PathVariable(value = "siteid") Integer siteId,
                                       @PathVariable(value = "transid") Integer transId,
                                       Double humidity,
                                       Double smokeConcentration,
                                       Double temperature,
                                       MultipartFile file) throws IOException, BusinessException, IllegalAccessException, InvocationTargetException, MessagingException {
        String picUrl = null;
        if(null != file) {
            CommonReturnType returnType = transInfoService.uploadImg(file);
            if(returnType.getStatus().equals("success")) {
                picUrl = ((String) returnType.getData());
            } else {
                throw new BusinessException(EmBusinessError.UNKNOWN_ERROR,"网络异常");
            }
        }
        TransVO transVO = new TransVO();
        transVO.setSiteId(siteId);
        transVO.setTransId(transId);
        transVO.setHumidity(humidity);
        transVO.setTemperature(temperature);
        transVO.setSmokeConcentration(smokeConcentration);
        transVO.setPicture(picUrl);
        //将视图模型转换为数据模型
        TransInfo transInfo = transInfoService.convertFromVOToTransInfo(transVO);
        //将数据保存进数据库
        transInfoService.save(transInfo);
        //判断状态
        if(!transInfoService.judgeStatus(transInfo)){
            //根据siteid和transid更新状态
            UpdateWrapper<Transfrom> wrapper = new UpdateWrapper<>();
            wrapper.eq("site_id", siteId)
                    .eq("trans_id", transId)
                    .eq("status",1)
                    .set("status", 0);
            transfromService.update(wrapper);
            UpdateWrapper<EleSite> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("site_id", siteId)
                    .eq("status", 1)
                    .set("status", 0);
            eleSiteService.update(updateWrapper);
        }
        return CommonReturnType.create("数据上传成功");
    }

}

