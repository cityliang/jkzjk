package com.huntto.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.cloud.demo.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huntto.dao.OlexamCyryJbxxDao;
import com.huntto.entity.CyryVo;
import com.huntto.entity.face.FaceDetectResult;
import com.huntto.service.CyryService;
import com.huntto.service.OlexamCyryJbxxService;
import com.huntto.util.FaceRecognUtil;
import com.huntto.util.JsonUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * Description: 类描述 <br/>
 * Copyright: 版权归浙江创得信息技术有限公司所有<br/>
 * Project_name jkzjk<br/>
 * Package_name com.huntto.controller<br/>
 * Author 梁城市<br/>
 * Email city_wangyi@163.com<br/>
 * Create_time 2018/12/8 9:16<br/>
 */
//@Api("健康证-测试接口")
@Slf4j
@RestController
public class TestController {
	@Autowired
	private OlexamCyryJbxxDao olexamCyryJbxxDao;
	@Autowired
	private FaceRecognUtil faceRecognUtil;
	
	@RequestMapping(value = { "/testP" }, method = RequestMethod.POST)
	public String test() throws Exception {
		byte[] jpegData = Utils.loadFile(Utils.PICTURE_ROOT + "620502199207234346.jpg");
		log.info("jpegData length: "+jpegData.length);
    	String img1 = Base64.encodeBase64String(jpegData);
    	log.info("img1 length: "+img1.length());
		FaceDetectResult fDetectResult = faceRecognUtil.faceDetect(img1);
		return fDetectResult.toString();
	}
	@Autowired
	private OlexamCyryJbxxService olexamCyryJbxxService;
	public static void main(String[] args) throws Exception {
		// encode
				String toBeEncode = "123";
				
				sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
				String result = encoder.encode(toBeEncode.getBytes("UTF-8"));
				System.out.println(result);
				
				System.out.println("----");
				
				byte[] encodeBase64 = Base64.encodeBase64(toBeEncode.getBytes("UTF-8"));
				System.out.println(new String(encodeBase64));
				
				System.out.println("####");
				
				// decode
				String toBeDecode = "MTIz";
		 
				sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
				byte[] decodeResult = decoder.decodeBuffer(toBeDecode);
				System.out.println(new String(decodeResult, "UTF-8"));		
				
				System.out.println("----");
				
				byte[] decodeResult2 = Base64.decodeBase64(toBeDecode);
			    System.out.println(new String(decodeResult2, "UTF-8"));	
	}
	
	/**
	 * 获取人员信息接口
	 *
	 * @param idCard 身份证号
	 * @return String
	 * @throws JsonProcessingException JsonProcessingException
	 *//*
	@ApiOperation(value = "获取人员信息接口", notes = "用来获取人员健康证信息")
	@ApiImplicitParam(paramType = "query", name = "idCard", value = "体检人员身份证号", dataType = "String", required = true)
	@RequestMapping(value = { "/getPersonMsg2" }, method = RequestMethod.POST)
	public String getPersonMsg2(String idCard) throws JsonProcessingException {
		String json = "";
		if (IdCardUtil.isIdcard(idCard)) {
			Map map;
			map = olexamCyryJbxxService.selectCYRYVo1(idCard);
			if (null != map && !"{}".equals(map.toString())) {
				ObjectMapper mapper = new ObjectMapper();
				json = mapper.writeValueAsString(map);
			} else if ("{}".equals(map.toString())) {
				json = JsonUtil.jsonStr("msg", "办理中");
			} else {
				json = JsonUtil.jsonStr("msg", "该用户没有健康证");
			}
		} else {
			json = JsonUtil.jsonStr("msg", "身份证不正确，请输入正确的身份证");
		}
//		json = Base64.encodeBase64String(json.getBytes());
		return json;
	}*/

	@Autowired
	private CyryService cyryService;

	@Deprecated
	@ApiOperation(value = "导出二维码和人员头像", notes = "待测，未完成")
	@RequestMapping(value = { "/tbPhoto" }, method = RequestMethod.POST)
	public String photo(String FZSJ) throws JsonProcessingException {
		String json;
		Map map = cyryService.queryPhoto(FZSJ);
		if (null != map) {
			ObjectMapper mapper = new ObjectMapper();
			json = mapper.writeValueAsString(map);
		} else {
			json = JsonUtil.jsonStr("err", "未查到该人员信息");
		}
		return json;
	}

	@ApiOperation(value = "导出二维码和人员头像", notes = "待测，未完成")
	@RequestMapping(value = { "/tbPhoto1" }, method = RequestMethod.POST)
	public String test(String FZSJ) throws JsonProcessingException {
		String json = "test";
		List<CyryVo> cyryVo = olexamCyryJbxxDao.selectCYRYVo(FZSJ);
		for (CyryVo c : cyryVo) {
			System.out.println(c.toString());
		}
		return json;
	}

//    @RequestMapping("/")
//	public String test() {
//		return "Hello World";
//	}
}
