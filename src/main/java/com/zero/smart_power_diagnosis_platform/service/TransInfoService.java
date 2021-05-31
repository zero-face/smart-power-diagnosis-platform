package com.zero.smart_power_diagnosis_platform.service;


import com.zero.smart_power_diagnosis_platform.common.error.BusinessException;
import com.zero.smart_power_diagnosis_platform.common.response.CommonReturnType;
import com.zero.smart_power_diagnosis_platform.controller.VO.SiteDetailVO;
import com.zero.smart_power_diagnosis_platform.controller.VO.TransVO;
import com.zero.smart_power_diagnosis_platform.entity.TransInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zero
 * @since 2021-05-10
 */
public interface TransInfoService extends IService<TransInfo> {
    /**
     * 将视图数据模型转换为数据库信息模型
     * @param transVO
     * @return
     * @throws BusinessException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    TransInfo convertFromVOToTransInfo(TransVO transVO) throws BusinessException, InvocationTargetException, IllegalAccessException;

    /**
     * 上传文件到 oss
     * @param file
     * @return
     * @throws BusinessException
     */
    CommonReturnType uploadImg(MultipartFile file) throws BusinessException, BusinessException;

    /**
     * 获取最近的一条设备信息数据
     * @param siteId
     * @param transId
     * @return
     */
    TransInfo getLastInfo(Integer siteId,Integer transId);

    /**
     * 获取某台设备的当前日期前指定天数的数据信息，默认为7天
     * @param siteId
     * @param transId
     * @return
     */
    List<TransInfo> getAllInfo(Integer siteId, Integer transId, Integer day);

    /**
     * 将数据库对象转化为视图对象
     * @param transInfo
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws BusinessException
     */
    TransVO convertTransInfoToVO(TransInfo transInfo) throws InvocationTargetException, IllegalAccessException, BusinessException;

    /**
     * 验证状态: 验证标准为湿度大于0.6%，烟雾浓度大于0.65$,温度大于155摄氏度（拟定传入的数据都是统一乘以10的4次方的）
     * @param transInfo
     * @return
     * @throws BusinessException
     */
    String judgeStatus(TransInfo transInfo) throws BusinessException;

    /**
     * 将变压器信息集合传化为变压器视图集合
     * @param allInfo
     * @return
     */
    List<TransVO> converFromTransInfoToTransVO(List<TransInfo> allInfo);

    SiteDetailVO convertFromTranInfoToDetailVO(TransInfo transInfo) throws InvocationTargetException, IllegalAccessException, BusinessException;

    List<SiteDetailVO> convertListFromTranInfoToDetailVO(List<TransInfo> transInfos);

    Map<String,Object> makeResult(List<TransVO> transVOS);
}
