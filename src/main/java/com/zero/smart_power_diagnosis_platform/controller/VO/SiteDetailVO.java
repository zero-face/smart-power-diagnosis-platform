package com.zero.smart_power_diagnosis_platform.controller.VO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author Zero
 * @Date 2021/5/20 22:34
 * @Since 1.8
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class SiteDetailVO {

    private Integer id;

    private Integer transId;

    private Integer siteId;

    private Double temperature;

    private Double humidity;

    private Double smokeConcentration;

    private String picture;

    private String status;

}
