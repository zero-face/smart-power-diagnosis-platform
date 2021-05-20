package com.zero.smart_power_diagnosis_platform.controller.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author Zero
 * @Date 2021/5/11 16:33
 * @Since 1.8
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("数据信息接口类")
public class TransVO {

    @ApiModelProperty("信息id")
    private Integer id;

    @ApiModelProperty("设备id")
    private Integer transId;

    @ApiModelProperty("站点id")
    private Integer siteId;

    @ApiModelProperty("温度信息")
    private Double temperature;

    @ApiModelProperty("湿度信息")
    private Double humidity;

    @ApiModelProperty("烟雾浓度信息")
    private Double smokeConcentration;

    @ApiModelProperty("图片地址")
    private String picture;

}
