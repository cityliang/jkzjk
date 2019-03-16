package com.huntto.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huntto.config.FaceRecognConfig;
import com.huntto.config.WeiXinConfig;
import com.huntto.util.FaceDetectResult;
import com.huntto.util.FaceRecognUtil;
import com.huntto.util.FaceVerifyResult;
import com.huntto.util.WeiXinUtil;

@RestController
public class CyryManagerController {
	private Logger log = LoggerFactory.getLogger(CyryManagerController.class);
	
	@Autowired
	private FaceRecognConfig faceRecognConfig;
	
	@Autowired
	private FaceRecognUtil faceRecognUtil;
	
	@Autowired
	private WeiXinConfig wConfig;
	
	@GetMapping(value = "/test")
    public String test() throws Exception {
		String string =  WeiXinUtil.getAccess_token();
		
		return string;
    }
	
	public String register() {

		return "";
	}
}
