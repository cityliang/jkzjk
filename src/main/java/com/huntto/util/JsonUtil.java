package com.huntto.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: 类描述 <br/>
 * Copyright: 版权归浙江创得信息技术有限公司所有<br/>
 * Project_name jkzjk<br/>
 * Package_name com.huntto.util<br/>
 * Author 梁城市<br/>
 * Email city_wangyi@163.com<br/>
 * Create_time 2018/12/10 10:57<br/>
 */
public class JsonUtil {
	public static String jsonStr(String msg, String str) throws JsonProcessingException {
		Map<String, String> map = new HashMap<>();
		map.put(msg, str);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(map);
	}
}
