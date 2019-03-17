package com.huntto.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huntto.config.WeiXinConfig;
import com.huntto.entity.wx.AccessToken;
import com.huntto.entity.wx.JsapiTicket;
import com.huntto.entity.wx.WxIPList;

import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@Configuration
public class WxUtil {
	
	@Autowired
	private WeiXinConfig wConfig;
	/**
	 * 获取access_token
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Test
	@RequestMapping("/getAccessToken")
	public String getAccessToken() throws Exception {
		List<String> list = wConfig.getIplist();
		for(int i = 0; i < list.size(); i++) {
    		String urlStr = "https://"+list.get(i)+"/cgi-bin/token?grant_type=client_credential&appid="+wConfig.getAPPID()+"&secret="+wConfig.getAPPSECRET(); 
            log.info("当前访问的微信urlStr为: "+urlStr);
            try {
            	String str = processUrl(urlStr);
        		AccessToken aToken =  JsonUtil.readValue(str, AccessToken.class);
        		return aToken.getAccess_token();
            }catch (ConnectException e) {
            	log.info("当前访问的微信urlStr为: "+urlStr);
            	e.printStackTrace();
				continue;
			} catch (IOException e) {
				log.info("当前访问的微信urlStr为: "+urlStr);
				e.printStackTrace();
				continue;
			}
    	}
		return null;
	}
	
	@Test
	@RequestMapping("/getcallbackip")
	public String getcallbackip() throws Exception {
//		urlStr += "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token="+WeiXinUtil.ACCESS_TOKEN;
		WeiXinUtil.ACCESS_TOKEN = getAccessToken();
		List<String> ipList = wConfig.getIplist();
		String urlStr = "";
		if(ipList != null && !ipList.isEmpty()) {
			for(String str:ipList) {
				urlStr += "https://"+str+"/cgi-bin/getcallbackip?access_token="+WeiXinUtil.ACCESS_TOKEN;
				log.info("当前访问的微信IP为: "+str);
				log.info("当前访问的微信urlStr为: "+urlStr);
				try {
					String string = processUrl(urlStr);
					log.info("string: "+string);
					WxIPList wIpList = JsonUtil.readValue(string, WxIPList.class);
					String [] strings = wIpList.getIp_list();
					log.info("微信服务器IP数组为： "+strings);
//					List list = Arrays.asList(strings);
//					if(!ListUtils.isEqualCollection(list,wConfig.getIplist())) {
//						str = (String) list.get(random.nextInt(ipList.size()));
//						urlStr += "https://"+str+"/cgi-bin/getcallbackip?access_token="+WeiXinUtil.ACCESS_TOKEN;
//						WxIPList wIpList1 = mapper.readValue(processUrl(urlStr), WxIPList.class);
//						String [] strings1 = wIpList.getIp_list();
//						List list1 = Arrays.asList(strings);
//						wConfig.setIplist(list1);
//					}
				} catch (ConnectException e) {
                	log.info("当前访问的微信urlStr为: "+urlStr);
                	e.printStackTrace();
					continue;
				} catch (IOException e) {
					log.info("当前访问的微信urlStr为: "+urlStr);
					e.printStackTrace();
					continue;
				}
			}
		}
		return "";
	}

	@Test
	@RequestMapping("/getcallbackip")
	public String getcallbackip() throws Exception {
		String urlStr = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token="+WeiXinUtil.ACCESS_TOKEN;
		String string = processUrl(urlStr);
		return string;
	}

	/**
	 * 获取jsapi_ticket
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/getJsapiTicket")
	public String getJsapiTicket() throws Exception {
		List<String> list = wConfig.getIplist();
		for(int i = 0; i < list.size(); i++) {
    		String urlStr = "https://"+list.get(i)+"/cgi-bin/ticket/getticket?access_token="+WeiXinUtil.ACCESS_TOKEN+"&type=jsapi";
            log.info("当前访问的微信urlStr为: "+urlStr);
            try {
            	String str = processUrl(urlStr);
            	JsapiTicket jTicket =  JsonUtil.readValue(str, JsapiTicket.class);
        		return jTicket.getTicket();
            }catch (ConnectException e) {
            	log.info("当前访问的微信urlStr为: "+urlStr);
            	e.printStackTrace();
				continue;
			} catch (IOException e) {
				log.info("当前访问的微信urlStr为: "+urlStr);
				e.printStackTrace();
				continue;
			}
    	}
		return null;
	}

	/**
	 * 获取签名signature
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/getJsSdkSign")
	public String getJsSdkSign(String noncestr,String tsapiTicket,String timestamp,String url) throws Exception {
//		String noncestr = request.getParameter("noncestr");
//		String tsapiTicket = request.getParameter("jsapi_ticket");
//		String timestamp = request.getParameter("timestamp");
//		String url = request.getParameter("url");
		String jsSdkSign = getJsSdkSign1(WxUtil.getNoncestr(), tsapiTicket, WxUtil.getTimestamp(), "http://hzjkz.hzwsjsw.gov.cn");
		return jsSdkSign;
	}

	private String processUrl(String urlStr) throws ConnectException,IOException {
		URL url;
		String sCurrentLine = "";
		String sTotalString = "";
		url = new URL(urlStr);
		URLConnection URLconnection = url.openConnection();
		HttpsURLConnection httpConnection = (HttpsURLConnection) URLconnection;
		httpConnection.setHostnameVerifier(new WeiXinUtil().new TrustAnyHostnameVerifier());
		int responseCode = httpConnection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			InputStream urlStream = httpConnection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlStream));
			while ((sCurrentLine = bufferedReader.readLine()) != null) {
				sTotalString += sCurrentLine;
			}
			return sTotalString;
		} else {
			log.info("该服务器访问失败");
		}
		return sTotalString;
	}

	/**
	 * 获得加密后的签名
	 * 
	 * @param noncestr
	 * @param tsapiTicket
	 * @param timestamp
	 * @param url
	 * @return
	 */
	public static String getJsSdkSign1(String noncestr, String tsapiTicket, String timestamp, String url) {
		String content = "jsapi_ticket=" + tsapiTicket + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url=" + url;
		String ciphertext = getSha1(content);
		return ciphertext;
	}

	/**
	 * 进行sha1加密
	 * 
	 * @param str
	 * @return
	 */
	public static String getSha1(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
			mdTemp.update(str.getBytes("UTF-8"));

			byte[] md = mdTemp.digest();
			int j = md.length;
			char buf[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
				buf[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(buf);
		} catch (Exception e) {
			return null;
		}
	}
	
	/** 
	 * 获取精确到秒的时间戳 
	 * @param date 
	 * @return 
	 */  
	public static String getTimestamp(){  
	    String timestamp = String.valueOf(new Date().getTime()/1000);  
	    return timestamp;  
	}

	/**
	 * 获得随机串
	 */
	public static String getNoncestr() {
		return UUID.randomUUID().toString();
	}
}
