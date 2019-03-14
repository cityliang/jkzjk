package com.huntto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import com.alibaba.cloud.faceengine.Error;
import com.alibaba.cloud.faceengine.FaceEngine;

/**
 * Description: 启动类 <br/>
 * Copyright: 版权归浙江创得信息技术有限公司所有<br/>
 * Project_name jkzjk<br/>
 * Package_name com.huntto<br/>
 * Author 梁城市<br/>
 * Email city_wangyi@163.com<br/>
 * Create_time 2018/12/8 8:33<br/>
 */
@SpringBootApplication
//        (exclude = { DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class })
//@EnableWebMvc
public class JkzjkAPP extends SpringBootServletInitializer {
	private static Logger log = LoggerFactory.getLogger(JkzjkAPP.class);
	/**
	 * 人脸识别授权码
	 */
	public String VENDOR_KEY = "eyJ2ZW5kb3JJZCI6ImNlc2hpX3ZlbmRvciIsInJvbGUiOjIsImNvZGUiOiIzRDE5RUIwNjY1OEE5MUExQzlCNDY0MzhDN0QwNDFGMyIsImV4cGlyZSI6IjIwMTkwMzMxIiwidHlwZSI6MX0=";
	public static void main(String[] args) {
		// 下面是 人脸识别SDK 授权认证  已弃用
//        int error = FaceEngine.authorize(VENDOR_KEY);
//        if (error != Error.OK) {
//            log.error("authorize error 人脸识别认证失败: " + error);
//            return;
//        } else {
//            log.info("authorize OK 人脸识别认证成功");
//        }
        // 启动springboot程序
        SpringApplication.run(JkzjkAPP.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(JkzjkAPP.class);
	}
}