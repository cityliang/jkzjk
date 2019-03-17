package com.huntto.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
public class WeiXinConfig {

	/**
	 * 微信公众号的 APPID
	 */
	@Value("${APPID}")
	private String APPID; // 微信公众号的id

	/**
	 * 微信公众号的 密钥 APPSECRET
	 */
	@Value("${APPSECRET}")
	private String APPSECRET;// 微信公众号的密钥

	/**
	 * 微信公众号的 IP 列表
	 */
	@Value("#{'${IPLIST}'.split(',')}")
	private List<String> iplist;
}
