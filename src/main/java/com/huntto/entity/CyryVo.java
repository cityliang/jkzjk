package com.huntto.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Description: 宁波Vo <br/>
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
public class CyryVo {
    @Id
    private String ID;// ID
    private String name;// 姓名
    private Integer sex;// 性别
    private Integer age;// 年龄
    private String idcard;// 身份证号
    private Integer requestType;// 类别
    private byte[] photo;// 图片

    private Integer isEligible;// 体检结果
    private String healthCardCode;// 编号
    private byte[] QRIMG;// 二维码
    private Date UPDATE_TIME;// 发证时间

}
