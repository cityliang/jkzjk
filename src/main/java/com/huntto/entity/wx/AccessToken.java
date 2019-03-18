package com.huntto.entity.wx;

import lombok.Data;

@Data
public class AccessToken {
	/**
     * 微信公众号的 错误代码
     */
    private String errcode;
    /**
     * 微信公众号的 错误信息
     */
    private String errmsg;
    /**
     * 微信公众号的  access_token
     */
    private String access_token;
    /**
     * 微信公众号的 access_token 过期时间
     */
    private String expires_in;
}
