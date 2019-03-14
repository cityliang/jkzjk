package com.huntto.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huntto.config.FaceRecognConfig;
import com.huntto.util.FaceRecognUtil;

@RestController
public class CyryManagerController {
	private Logger log = LoggerFactory.getLogger(CyryManagerController.class);
	
	@Autowired
	private FaceRecognConfig faceRecognConfig;
	
	@Autowired
	private FaceRecognUtil faceRecognUtil;
	
	@Value("${ak_id}")
	private String ak_id; //用户ak
	@Value("${ak_secret}")
	private String ak_secret; // 用户ak_secret
	@Value("${verifyUrl}")
	private String verifyUrl;// 人脸对比API接口调用地址
	@Value("${detectUrl}")
	private String detectUrl;// 人脸检测API接口调用地址
	
	@GetMapping(value = "/testLog")
    public String testLog() throws Exception {
        log.info("---------------------------");
        log.debug("debug debug");
        log.info("info info info");
        log.warn("warn warn warn");
        log.error("error error error ");
        log.info("---------------------------");
        faceRecognUtil = new FaceRecognUtil();
        String url = "https://dtplus-cn-shanghai.data.aliyuncs.com/face/verify"; //参考：https://faceRecognConfig.data.aliyun.com/console
        String body = "{type\":0,\"image_url_1\":\"http://e.hiphotos.baidu.com/image/pic/item/dbb44aed2e738bd4d78823fba88b87d6267ff94b.jpg\",\"image_url_2\":\"http://e.hiphotos.baidu.com/image/pic/item/dbb44aed2e738bd4d78823fba88b87d6267ff94b.jpg\"}";//参考：https://help.aliyun.com/knowledge_detail/53520.html?spm=5176.7753399.6.553.i4Hm7s";
        System.out.println("response body:" + faceRecognUtil.sendPost(faceRecognConfig.getVerifyUrl(), body,faceRecognConfig.getAk_id(),faceRecognConfig.getAk_secret()));
        String result = "response body:" + faceRecognUtil.sendPost(faceRecognConfig.getVerifyUrl(), body,faceRecognConfig.getAk_id(),faceRecognConfig.getAk_secret());
        return "ok"+"ak_id: "+ak_id+" ak_secret: "+ak_secret+"url: "+url+"faceRecognConfig id: "+faceRecognConfig.getAk_id()+"faceRecognConfig secret: "+faceRecognConfig.getAk_secret()+"face url: "+faceRecognConfig.getVerifyUrl()+ "response: "+result;
    }
	
	public String register() {

		return "";
	}
}
