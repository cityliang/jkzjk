package com.huntto.util;

import com.huntto.config.WeiXinConfig;
import com.huntto.entity.wx.AccessToken;
import com.huntto.entity.wx.JsapiTicket;
import com.huntto.entity.wx.WxIPList;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
@Slf4j
@RestController
@Configuration
public class WxUtil {
	
	@Autowired
	private WeiXinConfig wConfig;
	/**
	 * 获取access_token
	 *
     * @throws Exception Exception
     */
	@Test
	@RequestMapping("/getAccessToken")
	public String getAccessToken() throws Exception {
		List<String> list = wConfig.getIplist();
        for (String aList : list) {
            String urlStr = "https://" + aList + "/cgi-bin/token?grant_type=client_credential&appid=" + wConfig.getAPPID() + "&secret=" + wConfig.getAPPSECRET();
            log.info("当前访问的微信urlStr为: " + urlStr);
            try {
                String str = processUrl(urlStr);
                AccessToken aToken = JsonUtil.readValue(str, AccessToken.class);
                if (aToken != null) {
                    return aToken.getAccess_token();
                }
            } catch (IOException e) {
                log.info("当前访问的微信urlStr为: " + urlStr);
                e.printStackTrace();
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
                    String[] strings = wIpList != null ? wIpList.getIp_list() : new String[0];
                    log.info("微信服务器IP数组为： " + Arrays.toString(strings));
//					List list = Arrays.asList(strings);
//					if(!ListUtils.isEqualCollection(list,wConfig.getIplist())) {
//						str = (String) list.get(random.nextInt(ipList.size()));
//						urlStr += "https://"+str+"/cgi-bin/getcallbackip?access_token="+WeiXinUtil.ACCESS_TOKEN;
//						WxIPList wIpList1 = mapper.readValue(processUrl(urlStr), WxIPList.class);
//						String [] strings1 = wIpList.getIp_list();
//						List list1 = Arrays.asList(strings);
//						wConfig.setIplist(list1);
//					}
				} catch (IOException e) {
					log.info("当前访问的微信urlStr为: "+urlStr);
					e.printStackTrace();
                }
            }
		}
		return "";
	}

	/**
	 * 获取jsapi_ticket
	 *
     * @throws Exception Exception
     */
	@RequestMapping("/getJsapiTicket")
	public String getJsapiTicket() throws Exception {
		List<String> list = wConfig.getIplist();
        for (String aList : list) {
            String urlStr = "https://" + aList + "/cgi-bin/ticket/getticket?access_token=" + WeiXinUtil.ACCESS_TOKEN + "&type=jsapi";
            log.info("当前访问的微信urlStr为: " + urlStr);
            try {
                String str = processUrl(urlStr);
                JsapiTicket jTicket = JsonUtil.readValue(str, JsapiTicket.class);
                return jTicket != null ? jTicket.getTicket() : null;
            } catch (IOException e) {
                log.info("当前访问的微信urlStr为: " + urlStr);
                e.printStackTrace();
            }
        }
        return null;
	}

	/**
	 * 获取签名signature
     * @throws Exception Exception
     */
	@RequestMapping("/getJsSdkSign")
	public String getJsSdkSign(String noncestr,String tsapiTicket,String timestamp,String url) throws Exception {
//		String noncestr = request.getParameter("noncestr");
//		String tsapiTicket = request.getParameter("jsapi_ticket");
//		String timestamp = request.getParameter("timestamp");
//		String url = request.getParameter("url");
        return getJsSdkSign1(WxUtil.getNoncestr(), tsapiTicket, WxUtil.getTimestamp(), url);
    }

    /**
     * 访问URL
     *
     * @param urlStr 需要访问的 URL
     * @return String 响应的数据
     * @throws ConnectException ConnectException
     * @throws IOException      IOException
     */
    private String processUrl(String urlStr) throws ConnectException,IOException {
        URL url;
        String sCurrentLine;
        StringBuilder sTotalString = new StringBuilder();
        url = new URL(urlStr);
        URLConnection URLconnection = url.openConnection();
        HttpsURLConnection httpConnection = (HttpsURLConnection) URLconnection;
        httpConnection.setHostnameVerifier(new WeiXinUtil().new TrustAnyHostnameVerifier());
        int responseCode = httpConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream urlStream = httpConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlStream));
            while ((sCurrentLine = bufferedReader.readLine()) != null) {
                sTotalString.append(sCurrentLine);
            }
            return sTotalString.toString();
        } else {
            log.info("该服务器访问失败");
        }
        return sTotalString.toString();
    }

	/**
	 * 获得加密后的签名
     *
     * @param noncestr 生成签名的随机串
     * @param tsapiTicket tsapiTicket
     * @param timestamp 生成签名的时间戳
     * @param url 需要使用的JS接口列表
     * @return 加密后的签名
     */
	public static String getJsSdkSign1(String noncestr, String tsapiTicket, String timestamp, String url) {
		String content = "jsapi_ticket=" + tsapiTicket + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url=" + url;
        return getSha1(content);
    }

	/**
	 * 进行sha1加密
     *
     * @param str 需要加密的字符串
     * @return str 加密之后的字符串
     */
    private static String getSha1(String str) {
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
            for (byte byte0 : md) {
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
     * @return 时间戳
     */
    public static String getTimestamp() {
        return String.valueOf(new Date().getTime()/1000);
    }

	/**
	 * 获得随机串
	 */
	public static String getNoncestr() {
		return UUID.randomUUID().toString();
	}
}
