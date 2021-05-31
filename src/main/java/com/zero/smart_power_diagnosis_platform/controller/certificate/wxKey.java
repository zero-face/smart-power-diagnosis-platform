package com.zero.smart_power_diagnosis_platform.controller.certificate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author Zero
 * @Date 2021/5/11 0:00
 * @Since 1.8
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class wxKey {
    private String openid;
    private String session_key;
    private String union_id;
    private Integer errcode;
    private String errmsg;
}
