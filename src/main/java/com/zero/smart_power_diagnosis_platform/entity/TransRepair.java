package com.zero.smart_power_diagnosis_platform.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author Zero
 * @Date 2021/5/24 18:55
 * @Since 1.8
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransRepair {

    @ApiModelProperty("站点id")
    private Integer siteId;

    @ApiModelProperty("变压器id")
    private Integer transId;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT)
    private Date repairTime;

}
