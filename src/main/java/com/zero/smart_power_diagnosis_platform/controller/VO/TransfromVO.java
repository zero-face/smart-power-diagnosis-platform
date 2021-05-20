package com.zero.smart_power_diagnosis_platform.controller.VO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author Zero
 * @Date 2021/5/12 22:16
 * @Since 1.8
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("变压器信息")
public class TransfromVO {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("变压器id")
    private Integer transId;

    @ApiModelProperty("站点id")
    private Integer siteId;

    @ApiModelProperty("变压器状态")
    private String status;

    @ApiModelProperty("变压器错误描述")
    private String errMsg;

}
