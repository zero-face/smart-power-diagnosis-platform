package com.zero.smart_power_diagnosis_platform.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.zero.smart_power_diagnosis_platform.common.error.BusinessException;
import com.zero.smart_power_diagnosis_platform.common.error.EmBusinessError;
import com.zero.smart_power_diagnosis_platform.controller.VO.TransfromVO;
import com.zero.smart_power_diagnosis_platform.controller.VO.WarnVO;
import com.zero.smart_power_diagnosis_platform.entity.EleSite;
import com.zero.smart_power_diagnosis_platform.entity.TransRepair;
import com.zero.smart_power_diagnosis_platform.entity.Transfrom;
import com.zero.smart_power_diagnosis_platform.mapper.EleSiteMapper;
import com.zero.smart_power_diagnosis_platform.mapper.TransRepairMapper;
import com.zero.smart_power_diagnosis_platform.mapper.TransfromMapper;
import com.zero.smart_power_diagnosis_platform.service.TransfromService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zero
 * @since 2021-05-10
 */
@Service
@Slf4j
public class TransfromServiceImpl extends ServiceImpl<TransfromMapper, Transfrom> implements TransfromService {
    @Autowired
    private TransfromMapper transfromMapper;
    @Autowired
    private EleSiteMapper eleSiteMapper;
    @Autowired
    private TransRepairMapper transTimeMapper;

    @Override
    public List<Transfrom> getTransWarn() {
        QueryWrapper<Transfrom> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 0);
        List<Transfrom> transfroms = transfromMapper.selectList(wrapper);
        return transfroms;
    }

    @Override
    public TransfromVO convertTranfromToTranVO(Transfrom transfrom) throws BusinessException, InvocationTargetException, IllegalAccessException {
        if(null == transfrom) {
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR,"变压器不存在");
        }
        TransfromVO transfromVO = new TransfromVO();
        BeanUtils.copyProperties(transfromVO, transfrom);
        transfromVO.setStatus(transfrom.getStatus() == true?"正常" : "异常");
        return transfromVO;
    }

    @Override
    public List<Transfrom> getAllErrTransBySiteId(Integer siteId) {
        final QueryWrapper<Transfrom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("site_id", siteId)
                .eq("status", 0);
        return transfromMapper.selectList(queryWrapper);
    }

    @Override
    public List<TransfromVO> convertFromTransfromToTransVO(List<Transfrom> list) {
        List<TransfromVO> transfromVOS = list.stream().map(transfrom -> {
            TransfromVO transfromVO = null;
            try {
                transfromVO = convertTranfromToTranVO(transfrom);
            } catch (BusinessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return transfromVO;
        }).collect(Collectors.toList());
        return transfromVOS;
    }

    @Override
    public WarnVO convertFromTransfromToWarnVO(Transfrom transfrom) throws InvocationTargetException, IllegalAccessException {
        if(null == transfrom) {
            return null;
        }
        WarnVO warnVO = new WarnVO();
        BeanUtils.copyProperties(warnVO, transfrom);
        warnVO.setLocation(getLocationBySiteId(transfrom.getSiteId()));
        warnVO.setErrMsg(transfrom.getErrMsg());
        return warnVO;
    }

    @Override
    public String getLocationBySiteId(Integer siteId) {
        QueryWrapper<EleSite> wrapper = new QueryWrapper<>();
        wrapper.eq("site_id", siteId);
        EleSite eleSite = eleSiteMapper.selectOne(wrapper);
        return eleSite.getLocation();
    }

    @Override
    public List<WarnVO> convertFromTransfromToWarnsVO(List<Transfrom> transfrom) {
        final List<WarnVO> collect = transfrom.stream().map(transfrom1 -> {
            WarnVO warnVO = null;
            try {
                warnVO = convertFromTransfromToWarnVO(transfrom1);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return warnVO;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public Boolean addRepairTrans(Integer siteId, Integer transId) {
        TransRepair transRepair = new TransRepair();
        transRepair.setSiteId(siteId);
        transRepair.setTransId(transId);
        int insert = transTimeMapper.insert(transRepair);
        if(insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Transfrom getTransBySiteAndTransId(Integer siteId, Integer transId) {
        final QueryWrapper<Transfrom> wrapper = new QueryWrapper<>();
        wrapper.eq("site_id", siteId)
                .eq("trans_id", transId);
        Transfrom transfrom = transfromMapper.selectOne(wrapper);
        return transfrom;
    }

}
