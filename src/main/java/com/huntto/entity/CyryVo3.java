package com.huntto.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Description: 健康证查询人员信息Vo <br/>
 * Copyright: 版权归浙江创得信息技术有限公司所有<br/>
 * Project_name jkzjk<br/>
 * Package_name com.huntto.entity<br/>
 * Author 梁城市<br/>
 * Email city_wangyi@163.com<br/>
 * Create_time 2018/12/8 12:02<br/>
 */
@Data
@NoArgsConstructor
@Entity
public class CyryVo3 {
    @Id
//	private String ID;// ID
//	private String BH;// 编号
    private String XM;// 姓名
    //	private String XB;// 性别
//	private Integer NL;// 年龄
//	private String LB;// 类别
//	private String TJJG;// 体检结果
//	private String FZSJ;// 发证时间
    private String IDCARD;// 身份证号
    private byte[] PHOTO;// 图片
    //	private byte[] QRIMG;// 二维码
//	
//	private String FZJG;// 发证机构
//	private String MEDI_INTID;// 体检机构ID
    private String FEATURE;//人脸识别特征码

}
