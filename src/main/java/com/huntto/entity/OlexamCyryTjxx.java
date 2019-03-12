package com.huntto.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OlexamCyryTjxx {
    private String id;
    private String cyryId;
    private Integer isEligible;
    private String optionB;
    private Integer isDelete;
    private String delMemo;
    private java.util.Date createTime;
    private String createPerson;
    private java.util.Date updateTime;
    private String updatePerson;
    private String rOrgcode;
    private java.util.Date examTime;
    private java.util.Date hepDate;
    private java.util.Date dysDate;
    private java.util.Date typDate;
    private java.util.Date tubDate;
    private java.util.Date derDate;
    private java.util.Date othDate;
    private Integer heartIsgood;
    private Integer lungIsgood;
    private Integer spleenIsgood;
    private Integer liverIsgood;
    private Integer skinIsgood;
    private String otherIsgood;
    private Integer xRayIsgood;
    private Integer shigellaIsgood;
    private Integer typhoidIsgood;
    private Integer gptIsgood;
    private Integer havigmIsgood;
    private Integer antiHavigmIsgood;
    private Integer rprIsgood;
    private Integer hivIsgood;
    private String tjdptName;
    private String tjdptId;
    private String healthCardCode;
    private Integer isPrint;
    private String printTime;
}
