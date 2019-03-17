package com.huntto.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class WeiXinConfig {
	
	@Value("${APPID}")
	private String APPID; // 微信公众号的id
	
	@Value("${APPSECRET}")
	private String APPSECRET;// 微信公众号的密钥
	
	@Value("#{'${IPLIST}'.split(',')}")
	private List<String> iplist;
}
