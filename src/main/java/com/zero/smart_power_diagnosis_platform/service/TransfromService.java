package com.zero.smart_power_diagnosis_platform.service;


import com.zero.smart_power_diagnosis_platform.common.error.BusinessException;
import com.zero.smart_power_diagnosis_platform.controller.VO.TransfromVO;
import com.zero.smart_power_diagnosis_platform.controller.VO.WarnVO;
import com.zero.smart_power_diagnosis_platform.entity.Transfrom;
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
public interface TransfromService extends IService<Transfrom> {
    /**
     * 返回所有有错误状态的变压器
     * @return
     */
    List<Transfrom> getTransWarn();

    /**
     * 将transfrom对象转化为tranvo对象
     * @param transfrom
     * @return
     * @throws BusinessException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    TransfromVO convertTranfromToTranVO(Transfrom transfrom) throws BusinessException, InvocationTargetException, IllegalAccessException, BusinessException;

    /**
     * 获取指定站点的错误变压器数
     * @param siteId
     * @return
     */
    List<Transfrom> getAllErrTransBySiteId(Integer siteId);

    /**
     * 将transfrom集合转化为transfromVO集合
     * @param transfrom
     * @return
     */
    List<TransfromVO> convertFromTransfromToTransVO(List<Transfrom> transfrom);

    /**
     * 将变压器对象转换为一个完整警告对象
     * @param transfrom
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    WarnVO convertFromTransfromToWarnVO(Transfrom transfrom) throws InvocationTargetException, IllegalAccessException;

    /**
     * 根据站点id获取站点位置
     * @param siteId
     * @return
     */
    String getLocationBySiteId(Integer siteId);

    /**
     * 将一个变压器对象列表转换为一个警告对象列表
     * @param transfrom
     * @return
     */
    List<WarnVO> convertFromTransfromToWarnsVO(List<Transfrom> transfrom);

    /**
     * 插入一条修复的变压器数据
     * @param siteId
     * @param transId
     * @return
     */
    Boolean addRepairTrans(Integer siteId, Integer transId);

    Transfrom getTransBySiteAndTransId(Integer siteId, Integer transId);

}
