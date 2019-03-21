package com.huntto.util;

import com.huntto.config.WeiXinConfig;
import com.huntto.entity.wx.AccessToken;
import com.huntto.entity.wx.JsapiTicket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
@Slf4j
@Component
public class WeiXinUtil {
	
	@Autowired
	private WeiXinConfig wConfig;
	
    /**全局token 所有与微信有交互的前提 */  
    public static String WX_ACCESS_TOKEN;
      
    /**全局token上次获取事件 */  
    public static long WX_ACCESS_TOKEN_LASTTOKENTIME;
    
    /**全局token 所有与微信有交互的前提 */  
    public static String WX_TICKET;
    
    /**全局token上次获取事件 */  
    public static long WX_TICKET_LASTTOKENTIME;

    /** 
     * 获取全局token方法 
     * 该方法通过使用HttpClient发送http请求，HttpGet()发送请求 
     * 微信返回的json中access_token是我们的全局token 
     */
    @Deprecated
    public synchronized String getAccess_token1(){
    	URL url;
    	String sCurrentLine = "";
		StringBuilder sTotalString = new StringBuilder();
		if(WX_ACCESS_TOKEN == null || System.currentTimeMillis() - WX_ACCESS_TOKEN_LASTTOKENTIME > 7000*1000){
            try {  
                //请求access_token地址  
                String urlStr = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+wConfig.getAPPID()+"&secret="+wConfig.getAPPSECRET(); 
                log.info("当前访问的微信urlStr为: "+urlStr);
                url = new URL(urlStr);
    			URLConnection URLconnection = url.openConnection();
    			HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
    			int responseCode = httpConnection.getResponseCode();
    			if (responseCode == HttpURLConnection.HTTP_OK) {
    				InputStream urlStream = httpConnection.getInputStream();
    				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlStream));
    				while ((sCurrentLine = bufferedReader.readLine()) != null) {
						sTotalString.append(sCurrentLine);
					}
					//字符串转json  
					AccessToken aToken = JsonUtil.readValue(sTotalString.toString(), AccessToken.class);
					//输出access_token  
					if (aToken != null) {
						System.out.println(aToken.getAccess_token());
						//给静态变量赋值，获取到access_token
						WX_ACCESS_TOKEN = aToken.getAccess_token();
						//给获取access_token时间赋值，方便下此次获取时进行判断
						WX_ACCESS_TOKEN_LASTTOKENTIME = System.currentTimeMillis();
						return WX_ACCESS_TOKEN;
					}
				} else {
    				System.err.println("失败");
    			}
			} catch (IOException e) {
				e.printStackTrace();
            }
        }
    	return WX_ACCESS_TOKEN;
    }
    
	// 定制Verifier
 	class TrustAnyHostnameVerifier implements HostnameVerifier {
 		public boolean verify(String hostname, SSLSession session) {
 			return true;
 		}
 	}
    /**
     * 获取全局 ACCESS_TOKEN
     * 捕获异常
     * @return
     */
 	@Deprecated
    public synchronized String getAccess_token2(){
    	URL url;
    	String sCurrentLine = "";
		String sTotalString = "";
        if(WX_ACCESS_TOKEN == null || System.currentTimeMillis() - WX_ACCESS_TOKEN_LASTTOKENTIME > 7000*1000){
            //请求access_token地址 
        	List<String> list = wConfig.getIplist();
			for (String aList : list) {
				long startTime = System.currentTimeMillis();   //获取开始时间
				String urlStr = "https://" + aList + "/cgi-bin/token?grant_type=client_credential&appid=" + wConfig.getAPPID() + "&secret=" + wConfig.getAPPSECRET();
				log.info("当前访问的微信urlStr为: " + urlStr);
				try {
					url = new URL(urlStr);
					URLConnection URLconnection = url.openConnection();
					HttpsURLConnection httpConnection = (HttpsURLConnection) URLconnection;
					httpConnection.setHostnameVerifier(new WeiXinUtil().new TrustAnyHostnameVerifier());
					int responseCode = httpConnection.getResponseCode();
					if (responseCode == HttpURLConnection.HTTP_OK) {
						log.info("可以使用的IP为" + aList);
						InputStream urlStream = httpConnection.getInputStream();
						BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlStream));
						while ((sCurrentLine = bufferedReader.readLine()) != null) {
							sTotalString += sCurrentLine;
						}
						AccessToken aToken = JsonUtil.readValue(sTotalString, AccessToken.class);
						//给静态变量赋值，获取到access_token
						if (aToken != null) {
							if(aToken.getErrcode() != null && aToken.getErrcode().equals("40164")) {
								return sTotalString;
							}else {
								WX_ACCESS_TOKEN = aToken.getAccess_token();
								//给获取access_token时间赋值，方便下此次获取时进行判断
								WX_ACCESS_TOKEN_LASTTOKENTIME = System.currentTimeMillis();
								long endTime = System.currentTimeMillis(); //获取结束时间
								log.info("程序运行时间： " + (endTime - startTime) + "ms");
								if (WX_ACCESS_TOKEN != null) {
									return WX_ACCESS_TOKEN;
								}
							}
						}else {
							return sTotalString;
						}
					} else {
						log.info("不可以使用的IP为" + aList);
					}
				} catch (IOException e) {
					log.info("当前访问的微信urlStr为: " + urlStr);
					e.printStackTrace();
				}
			}
		}
        return WX_ACCESS_TOKEN;
    }
 	/**
     * 
 	 * @Title  getAccess_token 
 	 * @Description  获取全局 ACCESS_TOKEN 抛出异常
 	 * @param  @return String
 	 * @param  @throws IOException 
 	 * @return  String 
 	 */
    public synchronized String getAccess_token() throws IOException{
    	URL url;
    	String sCurrentLine = "";
		String sTotalString = "";
        if(WX_ACCESS_TOKEN == null || System.currentTimeMillis() - WX_ACCESS_TOKEN_LASTTOKENTIME > 7000*1000){
            //请求access_token地址 
        	List<String> list = wConfig.getIplist();
			for (String aList : list) {
				long startTime = System.currentTimeMillis();   //获取开始时间
				String urlStr = "https://" + aList + "/cgi-bin/token?grant_type=client_credential&appid=" + wConfig.getAPPID() + "&secret=" + wConfig.getAPPSECRET();
				log.info("当前访问的微信urlStr为: " + urlStr);
				url = new URL(urlStr);
				URLConnection URLconnection = url.openConnection();
				HttpsURLConnection httpConnection = (HttpsURLConnection) URLconnection;
				httpConnection.setHostnameVerifier(new WeiXinUtil().new TrustAnyHostnameVerifier());
				int responseCode = httpConnection.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) {
					log.info("可以使用的IP为" + aList);
					InputStream urlStream = httpConnection.getInputStream();
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlStream));
					while ((sCurrentLine = bufferedReader.readLine()) != null) {
						sTotalString += sCurrentLine;
					}
					AccessToken aToken = JsonUtil.readValue(sTotalString, AccessToken.class);
					//给静态变量赋值，获取到access_token
					if (aToken != null) {
						if(aToken.getErrcode() != null && aToken.getErrcode().equals("40164")) {
							return sTotalString;
						}else {
							WX_ACCESS_TOKEN = aToken.getAccess_token();
							//给获取access_token时间赋值，方便下此次获取时进行判断
							WX_ACCESS_TOKEN_LASTTOKENTIME = System.currentTimeMillis();
							long endTime = System.currentTimeMillis(); //获取结束时间
							log.info("程序运行时间： " + (endTime - startTime) + "ms");
							if (WX_ACCESS_TOKEN != null) {
								return WX_ACCESS_TOKEN;
							}
						}
					}else {
						return sTotalString;
					}
				} else {
					log.info("不可以使用的IP为" + aList);
				}
			}
		}
        return WX_ACCESS_TOKEN;
    }
    /**
     * 获取全局Ticket
     * 捕获异常
     * @return
     */
    @Deprecated
    public synchronized String getWXJsapiTicket1(){
    	URL url;
    	String sCurrentLine = "";
		String sTotalString = "";
        if(WX_TICKET == null || System.currentTimeMillis() - WX_TICKET_LASTTOKENTIME > 7000*1000){
        	List<String> list = wConfig.getIplist();
			for (String aList : list) {
				long startTime = System.currentTimeMillis();   //获取开始时间
				String urlStr = "https://" + aList + "/cgi-bin/ticket/getticket?access_token=" + WeiXinUtil.WX_ACCESS_TOKEN + "&type=jsapi";
				log.info("当前访问的微信urlStr为: " + urlStr);
				try {
					url = new URL(urlStr);
					URLConnection URLconnection = url.openConnection();
					HttpsURLConnection httpConnection = (HttpsURLConnection) URLconnection;
					httpConnection.setHostnameVerifier(new WeiXinUtil().new TrustAnyHostnameVerifier());
					int responseCode = httpConnection.getResponseCode();
					if (responseCode == HttpURLConnection.HTTP_OK) {
						log.info("可以使用的IP为" + aList);
						InputStream urlStream = httpConnection.getInputStream();
						BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlStream));
						while ((sCurrentLine = bufferedReader.readLine()) != null) {
							sTotalString += sCurrentLine;
						}
						JsapiTicket jTicket = JsonUtil.readValue(sTotalString, JsapiTicket.class);
						//给静态变量赋值，获取到access_token
						if (jTicket != null) {
							if(jTicket.getErrcode() != null && jTicket.getErrcode().equals("40164")) {
								return sTotalString;
							}else {
								WX_TICKET = jTicket.getTicket();
								//给获取access_token时间赋值，方便下此次获取时进行判断
								WX_TICKET_LASTTOKENTIME = System.currentTimeMillis();
								long endTime = System.currentTimeMillis(); //获取结束时间
								log.info("程序运行时间： " + (endTime - startTime) + "ms");
								if (WX_TICKET != null) {
									return WX_TICKET;
								}
							}
						}else {
							return sTotalString;
						}
					} else {
						log.info("不可以使用的IP为" + aList);
					}
				} catch (IOException e) {
					log.info("当前访问的微信urlStr为: " + urlStr);
					e.printStackTrace();
				}
			}
		}
        return WX_TICKET;
    }
    
    /**
     * 
     * @Title  getWXJsapiTicket 
     * @Description  获取全局Ticket  抛出异常
     * @param  @return
     * @param  @throws IOException 
     * @return  String 
     * 
     */
    public synchronized String getWXJsapiTicket() throws IOException{
    	URL url;
    	String sCurrentLine = "";
		String sTotalString = "";
        if(WX_TICKET == null || System.currentTimeMillis() - WX_TICKET_LASTTOKENTIME > 7000*1000){
        	List<String> list = wConfig.getIplist();
			for (String aList : list) {
				long startTime = System.currentTimeMillis();   //获取开始时间
				String urlStr = "https://" + aList + "/cgi-bin/ticket/getticket?access_token=" + WeiXinUtil.WX_ACCESS_TOKEN + "&type=jsapi";
				log.info("当前访问的微信urlStr为: " + urlStr);
				url = new URL(urlStr);
				URLConnection URLconnection = url.openConnection();
				HttpsURLConnection httpConnection = (HttpsURLConnection) URLconnection;
				httpConnection.setHostnameVerifier(new WeiXinUtil().new TrustAnyHostnameVerifier());
				int responseCode = httpConnection.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) {
					log.info("可以使用的IP为" + aList);
					InputStream urlStream = httpConnection.getInputStream();
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlStream));
					while ((sCurrentLine = bufferedReader.readLine()) != null) {
						sTotalString += sCurrentLine;
					}
					JsapiTicket jTicket = JsonUtil.readValue(sTotalString, JsapiTicket.class);
					//给静态变量赋值，获取到access_token
					if (jTicket != null) {
						if(jTicket.getErrcode() != null && jTicket.getErrcode().equals("40164")) {
							return sTotalString;
						}else {
							WX_TICKET = jTicket.getTicket();
							//给获取access_token时间赋值，方便下此次获取时进行判断
							WX_TICKET_LASTTOKENTIME = System.currentTimeMillis();
							long endTime = System.currentTimeMillis(); //获取结束时间
							log.info("程序运行时间： " + (endTime - startTime) + "ms");
							if (WX_TICKET != null) {
								return WX_TICKET;
							}
						}
					}else {
						return sTotalString;
					}
				} else {
					log.info("不可以使用的IP为" + aList);
				}
			}
		}
        return WX_TICKET;
    }
}
