package com.huntto.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class FaceRecognConfig {
	@Value("${ak_id}")
	private String ak_id; //用户ak
	@Value("${ak_secret}")
	private String ak_secret; // 用户ak_secret
	@Value("${url}")
	private String url; // 用户ak_secret
}
