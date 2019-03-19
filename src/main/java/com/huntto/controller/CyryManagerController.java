package com.huntto.controller;

import com.huntto.config.FaceRecognConfig;
import com.huntto.config.WeiXinConfig;
import com.huntto.util.FaceRecognUtil;
import com.huntto.util.JsonUtil;
import com.huntto.util.WeiXinUtil;
import com.huntto.util.WxUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CyryManagerController {
	private Logger log = LoggerFactory.getLogger(CyryManagerController.class);
	
	@Autowired
	private FaceRecognConfig faceRecognConfig;
	
	@Autowired
	private FaceRecognUtil faceRecognUtil;
	
	@Autowired
	private WeiXinConfig wConfig;
	
	@Autowired
	private WeiXinUtil wUtil;
	
	@Autowired
	private WxUtil wxUtil;
	
	/**
	 * 通过config接口注入权限验证配置
	 * @return map
	 * @throws Exception
	 */
    @GetMapping(value = "/getWxToken")
    public String test(String url) throws Exception {
		String access_token =  wUtil.getAccess_token();
        Map map = new HashMap<>();
		map.put("access_token", access_token);
		map.put("appId", wConfig.getAPPID());
		map.put("timestamp", WxUtil.getTimestamp());
		map.put("nonceStr", WxUtil.getNoncestr());
		map.put("signature", WxUtil.getJsSdkSign1(WxUtil.getNoncestr(), wxUtil.getJsapiTicket(), WxUtil.getTimestamp(), url));
		return JsonUtil.toJSon(map);
    }
	@Deprecated
	public String register() {

		return "";
	}
}
