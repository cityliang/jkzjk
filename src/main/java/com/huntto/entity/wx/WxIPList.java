package com.huntto.entity.wx;

import lombok.Data;

@Data
public class WxIPList {
    /**
     * 微信公众号的 错误代码
     */
    private String errcode;
    /**
     * 微信公众号的  错误信息
     */
    private String errmsg;
    /**
     * 微信公众号的 IP 列表
     */
    private String[] ip_list;
}
