package com.huntto.entity.wx;

import lombok.Data;

@Data
public class WxIPList {
	private String errcode;
	private String errmsg;
	private String[] ip_list;
}
