package com.zero.smart_power_diagnosis_platform.controller.VO;

/**
 * @Author Zero
 * @Date 2021/5/10 19:20
 * @Since 1.8
 **/

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 返回给前端的用户
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户信息类")
public class UserVO {

    @ApiModelProperty("微信用户名")
    private String nickName;

    @ApiModelProperty("用户身份唯一标识")
    private String openid;

    @ApiModelProperty("图片地址")
    private String avatarUrl;

    @ApiModelProperty("性别")
    private String gender;

}
