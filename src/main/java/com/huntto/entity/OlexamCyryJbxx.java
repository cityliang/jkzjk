package com.huntto.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlTransient;
import java.sql.Blob;

@Data
@NoArgsConstructor
public class OlexamCyryJbxx {
    private String id;
    private String name;
    private Integer sex;
    private String idcard;
    private Integer age;
    private Integer requestType;
    private String jobType;
    private String compName;
    private String compId;
    private String compNo;
    private Integer isDelete;
    private String delMemo;
    private java.util.Date createTime;
    private String createPerson;
    private java.util.Date updateTime;
    private String updatePerson;
    private String rOrgcode;
    private String mediIntid;
    private String locAddr;
    private String locAddrCode;
    private String education;
    private Integer workAge;
    private String photo;
    private java.util.Date registTime;
    private String examRegistNumber;
    private String compAddr;
    private String compAddrCode;
    private String contactNumber;
    private String cardtype;

    @XmlTransient
    private Blob CYRYPHOTO;
}
