package com.zero.smart_power_diagnosis_platform.controller.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author Zero
 * @Date 2021/5/19 19:12
 * @Since 1.8
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("变压器信息")
public class WarnVO {
    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("变压器id")
    private Integer transId;

    @ApiModelProperty("站点id")
    private Integer siteId;

    @ApiModelProperty("站点地址")
    private String location;

    @ApiModelProperty("错误信息")
    private String errMsg;

    @ApiModelProperty("变压器状态")
    private String status;
}
