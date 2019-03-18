package com.huntto.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class FaceRecognConfig {
    /**
     * 用户ak
     */
    @Value("${ak_id}")
    private String ak_id;
    /**
     * 用户ak_secret
     */
	@Value("${ak_secret}")
    private String ak_secret;
    /**
     * 用人脸对比API接口调用地址
     */
	@Value("${verifyUrl}")
    private String verifyUrl;
    /**
     * 人脸检测API接口调用地址
     */
	@Value("${detectUrl}")
    private String detectUrl;
}
