package com.zero.smart_power_diagnosis_platform.mapper;

import com.zero.smart_power_diagnosis_platform.entity.TransInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zero
 * @since 2021-05-10
 */
@Mapper
@Repository
public interface TransInfoMapper extends BaseMapper<TransInfo> {

}
