package com.zero.smart_power_diagnosis_platform.service;

import com.zero.smart_power_diagnosis_platform.controller.VO.EleSiteVO;
import com.zero.smart_power_diagnosis_platform.entity.EleSite;
import com.baomidou.mybatisplus.extension.service.IService;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zero
 * @since 2021-05-10
 */
public interface EleSiteService extends IService<EleSite> {

    /**
     * 获取所有站点信息
     * @return
     */
    List<EleSite> getAllSiteInfo();

    /**
     * 将核心领域站点模型转换为视图模型
     * @param eleSite
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    EleSiteVO convertFromElesiteToElesiteVO(EleSite eleSite) throws InvocationTargetException, IllegalAccessException;

    /**
     * 将核心领域站点模型集合转换为视图模型集合
     * @param eleSites
     * @return
     */
    List<EleSiteVO> convertListFromEleSiteToEleVO(List<EleSite> eleSites);

    /**
     * 根据站点id获取所有变压器
     * @param siteId
     * @return
     */
    List<Integer> getTransIdBysiteId(Integer siteId);

    Integer getRepairCount();

}
