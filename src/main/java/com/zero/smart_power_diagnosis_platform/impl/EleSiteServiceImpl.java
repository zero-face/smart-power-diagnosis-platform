package com.zero.smart_power_diagnosis_platform.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zero.smart_power_diagnosis_platform.controller.VO.EleSiteVO;
import com.zero.smart_power_diagnosis_platform.entity.EleSite;
import com.zero.smart_power_diagnosis_platform.entity.TransRepair;
import com.zero.smart_power_diagnosis_platform.entity.Transfrom;
import com.zero.smart_power_diagnosis_platform.mapper.EleSiteMapper;
import com.zero.smart_power_diagnosis_platform.mapper.TransRepairMapper;
import com.zero.smart_power_diagnosis_platform.mapper.TransfromMapper;
import com.zero.smart_power_diagnosis_platform.service.EleSiteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
public class EleSiteServiceImpl extends ServiceImpl<EleSiteMapper, EleSite> implements EleSiteService {
    @Autowired
    private EleSiteMapper eleSiteMapper;
    @Autowired
    private TransfromMapper transfromMapper;
    @Autowired
    private TransRepairMapper repairMapper;

    @Override
    public List<EleSite> getAllSiteInfo() {
        QueryWrapper<EleSite> wrapper = new QueryWrapper<>();
        wrapper.isNotNull("id");
        List<EleSite> eleSites = eleSiteMapper.selectList(wrapper);
        return eleSites;
    }

    @Override
    public EleSiteVO convertFromElesiteToElesiteVO(EleSite eleSite) throws InvocationTargetException, IllegalAccessException {
        EleSiteVO eleSiteVO = new EleSiteVO();
        BeanUtils.copyProperties(eleSiteVO,eleSite);
        eleSiteVO.setStatus(eleSite.getStatus() == true?"正常" : "异常");
        return eleSiteVO;
    }

    @Override
    public List<EleSiteVO> convertListFromEleSiteToEleVO(List<EleSite> eleSites) {
        List<EleSiteVO> eleSiteVOS = eleSites.stream().map(eleSite -> {
            EleSiteVO eleSiteVO = null;
            try {
                eleSiteVO = convertFromElesiteToElesiteVO(eleSite);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return eleSiteVO;
        }).collect(Collectors.toList());
        return eleSiteVOS;
    }

    @Override
    public List<Integer> getTransIdBysiteId(Integer siteId) {
        QueryWrapper<Transfrom> wrapper = new QueryWrapper<>();
        wrapper.eq("site_id", siteId);
        List<Transfrom> transfroms = transfromMapper.selectList(wrapper);
        final List<Integer> collect = transfroms.stream().map(transfrom -> {
            return (transfrom.getTransId());
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public Integer getRepairCount() {
        QueryWrapper<TransRepair> wrapper = new QueryWrapper<>();
        Integer repairCount = repairMapper.selectCount(wrapper);
        return repairCount;
    }
}
