package com.zero.smart_power_diagnosis_platform.controller.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author Zero
 * @Date 2021/5/12 21:01
 * @Since 1.8
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("站点信息接口类")
public class EleSiteVO {

    @ApiModelProperty("站点id")
    private Integer siteId;

    @ApiModelProperty("站点的变压器数量")
    private Integer transNumbers;

    @ApiModelProperty("站点位置")
    private String location;

    @ApiModelProperty("站点的状态")
    private String status;

}
