package com.zero.smart_power_diagnosis_platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.text.StyledEditorKit;

/**
 * <p>
 * 
 * </p>
 *
 * @author zero
 * @since 2021-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="EleSite对象", description="")
public class EleSite implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer siteId;

    private Integer transNumbers;

    @TableLogic
    private Boolean deleted;

    private String location;

    private Boolean status;

}
