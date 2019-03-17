package com.huntto.entity.wx;

import lombok.Data;

@Data
public class AccessToken {
	private String access_token;
	private String expires_in;
}
