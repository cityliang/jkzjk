package com.huntto.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
@Builder
public class FeedBack {

    private String ID;// ID
    @ApiModelProperty(hidden = true)
    private String FBType;// 反馈类型
    @ApiModelProperty(hidden = true)
    private String FBContent;// 反馈内容
    private String IDCARD;// 反馈人身份证号
    @ApiModelProperty(hidden = true)
    private String CREATE_TIME;// 创建时间
    @ApiModelProperty(hidden = true)
    private String UPDATE_TIME;// 修改时间
    @ApiModelProperty(hidden = true)
    @Builder.Default
    private String IS_CL = "0"; // 是否处理 0未处理 1已处理
    @ApiModelProperty(hidden = true)
    private String MEDI_INTID; // 体检机构id

}
