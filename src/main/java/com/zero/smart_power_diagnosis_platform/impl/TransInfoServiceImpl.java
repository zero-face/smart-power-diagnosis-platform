package com.zero.smart_power_diagnosis_platform.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.zero.smart_power_diagnosis_platform.common.error.BusinessException;
import com.zero.smart_power_diagnosis_platform.common.error.EmBusinessError;
import com.zero.smart_power_diagnosis_platform.common.response.CommonReturnType;
import com.zero.smart_power_diagnosis_platform.common.util.OSSUtils;
import com.zero.smart_power_diagnosis_platform.controller.VO.SiteDetailVO;
import com.zero.smart_power_diagnosis_platform.controller.VO.TransVO;
import com.zero.smart_power_diagnosis_platform.entity.TransInfo;
import com.zero.smart_power_diagnosis_platform.mapper.TransInfoMapper;
import com.zero.smart_power_diagnosis_platform.service.TransInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



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
public class TransInfoServiceImpl extends ServiceImpl<TransInfoMapper, TransInfo> implements TransInfoService {
    @Autowired
    private TransInfoMapper transInfoMapper;
    @Autowired
    private OSSUtils ossUtils;
    @Override
    public TransInfo convertFromVOToTransInfo(TransVO transVO) throws BusinessException, InvocationTargetException, IllegalAccessException {
        if(null == transVO) {
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR,"传递的信息为空");
        }
        TransInfo transInfo = new TransInfo();
        BeanUtils.copyProperties(transInfo, transVO);
        return transInfo;
    }

    @Override
    public CommonReturnType uploadImg(MultipartFile file) throws BusinessException {
        if(null == file) {
            return CommonReturnType.create("文件为空","fail");
        }
        return ossUtils.uploadImg(file);
    }

    @Override
    public TransInfo getLastInfo(Integer siteId, Integer transId) {
        QueryWrapper<TransInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("trans_id", transId)
                .eq("site_id", siteId)
                .orderBy(true, false, "collect_time")
                .last("limit 1");
        return transInfoMapper.selectOne(wrapper);
    }

    @Override
    public List<TransInfo> getAllInfo(Integer siteId, Integer transId,Integer day) {
        String sql = "collect_time >= date_sub(current_date(), interval {0} day)";
        QueryWrapper<TransInfo> wrapper = new QueryWrapper<>();
//        wrapper.apply(sql, day)
        wrapper.eq("site_id",siteId)
                .eq("trans_id", transId);
        return transInfoMapper.selectList(wrapper);
    }

    @Override
    public TransVO convertTransInfoToVO(TransInfo transInfo) throws InvocationTargetException, IllegalAccessException, BusinessException {
        if(null == transInfo) {
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        TransVO transVO = new TransVO();
        BeanUtils.copyProperties(transVO,transInfo);
        return transVO;
    }

    @Override
    public String judgeStatus(TransInfo transInfo) throws BusinessException {
        if(null == transInfo) {
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR,"设备不存在");
        }
        String string = "";
        if(transInfo.getHumidity() == null || transInfo.getHumidity() >= 60) {
            string += "湿度";
        }
        if(transInfo.getSmokeConcentration() == null || transInfo.getSmokeConcentration() >= 65) {
            string += "烟雾浓度";
        }
        if(transInfo.getTemperature() == null || transInfo.getTemperature() >=155) {
            string += "温度";
        }
        return string;
    }

    @Override
    public List<TransVO> converFromTransInfoToTransVO(List<TransInfo> allInfo) {
        List<TransVO> transVOS = allInfo.stream().map(info -> {
            TransVO transVO = null;
            try {
                transVO = convertTransInfoToVO(info);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (BusinessException e) {
                e.printStackTrace();
            }
            return transVO;
        }).collect(Collectors.toList());
        return transVOS;
    }

    @Override
    public SiteDetailVO convertFromTranInfoToDetailVO(TransInfo transInfo) throws InvocationTargetException, IllegalAccessException, BusinessException {
        if(transInfo == null) {
            return null;
        }
        SiteDetailVO siteDetailVO = new SiteDetailVO();
        BeanUtils.copyProperties(siteDetailVO,transInfo);
        siteDetailVO.setStatus(judgeStatus(transInfo).length() == 0 ? "正常" : "异常");
        return siteDetailVO;
    }


    @Override
    public List<SiteDetailVO> convertListFromTranInfoToDetailVO(List<TransInfo> transInfos) {
        List<SiteDetailVO> collect = transInfos.stream().map(transInfo -> {
            SiteDetailVO siteDetailVO = null;
            try {
                siteDetailVO = convertFromTranInfoToDetailVO(transInfo);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (BusinessException e) {
                e.printStackTrace();
            }
            return siteDetailVO;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public Map<String, Object> makeResult(List<TransVO> transVOS) {
        if(transVOS == null) {
            return null;
        }
        Integer siteId = transVOS.get(0).getSiteId();
        Integer transid = transVOS.get(0).getTransId();
        List<Double> temperature = new ArrayList<>();
        List<Double> humidity = new ArrayList<>();
        List<Double> smokeConcentration = new ArrayList<>();
        List<String> picture = new ArrayList<>();
        for ( TransVO transVO:transVOS
             ) {
            temperature.add(transVO.getTemperature());
            humidity.add(transVO.getHumidity());
            smokeConcentration.add(transVO.getSmokeConcentration());
            picture.add(transVO.getPicture());
        }
        Map<String,Object> maps = new HashMap<>();
        maps.put("temperature",temperature.toArray(new Double[temperature.size()]));
        maps.put("humidity",humidity.toArray(new Double[humidity.size()]));
        maps.put("smokeConcentration",smokeConcentration.toArray(new Double[smokeConcentration.size()]));
        maps.put("picture", picture.toArray(new String[picture.size()]));
        maps.put("siteId", siteId);
        maps.put("transId", transid);
        return maps;
    }
}
