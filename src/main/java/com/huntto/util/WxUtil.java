package com.huntto.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huntto.config.WeiXinConfig;
@RestController
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
		String urlStr = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+wConfig.getAPPID()+"&secret="+wConfig.getAPPSECRET();
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
		String access_token = WeiXinUtil.ACCESS_TOKEN;
		String urlStr = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token=" + access_token;
		String string = processUrl(urlStr);
		return string;
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
		String jsSdkSign = getJsSdkSign1(noncestr, tsapiTicket, timestamp, url);
		return jsSdkSign;
	}

	private String processUrl(String urlStr) {
		URL url;
		String sCurrentLine = "";
		String sTotalString = "";
		try {
			url = new URL(urlStr);
			URLConnection URLconnection = url.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
			int responseCode = httpConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream urlStream = httpConnection.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlStream));
				while ((sCurrentLine = bufferedReader.readLine()) != null) {
					sTotalString += sCurrentLine;
				}
				return sTotalString;
			} else {
				System.err.println("失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		String content = "jsapi_ticket=" + tsapiTicket + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url="
				+ url;
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
	 * 获得随机串
	 */
	public static String create_noncestr() {
		return UUID.randomUUID().toString();
	}
}
