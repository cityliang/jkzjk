package com.huntto.entity.wx;

import lombok.Data;

@Data
public class JsapiTicket {
	private String errcode;
	private String errmsg;
	private String ticket;
	private String expires_in;
}
