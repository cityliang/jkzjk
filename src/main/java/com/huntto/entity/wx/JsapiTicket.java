package com.huntto.entity.wx;

import lombok.Data;

@Data
public class JsapiTicket {
    /**
     * 微信公众号的 错误代码
     */
    private String errcode;
    /**
     * 微信公众号的 错误信息
     */
    private String errmsg;
    /**
     * 微信公众号的 ticket
     */
    private String ticket;
    /**
     * 微信公众号的 ticket 过期时间
     */
    private String expires_in;
}
