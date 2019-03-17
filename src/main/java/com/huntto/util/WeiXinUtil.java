package com.huntto.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.huntto.config.WeiXinConfig;
import com.huntto.entity.wx.AccessToken;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Configuration
public class WeiXinUtil {
	
	@Autowired
	private WeiXinConfig wConfig;
	
    /**全局token 所有与微信有交互的前提 */  
    public static String ACCESS_TOKEN;  
      
    /**全局token上次获取事件 */  
    public static long LASTTOKENTIME;  
      
    /** 
     * 获取全局token方法 
     * 该方法通过使用HttpClient发送http请求，HttpGet()发送请求 
     * 微信返回的json中access_token是我们的全局token 
     */  
    public synchronized String getAccess_token(){
    	URL url;
    	String sCurrentLine = "";
		String sTotalString = "";
        if(ACCESS_TOKEN == null || System.currentTimeMillis() - LASTTOKENTIME > 7000*1000){
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
    					sTotalString += sCurrentLine;
    				}
					//字符串转json  
					AccessToken aToken =  JsonUtil.readValue(sTotalString, AccessToken.class);
					//输出access_token  
					System.out.println(aToken.getAccess_token());  
					//给静态变量赋值，获取到access_token  
					ACCESS_TOKEN = aToken.getAccess_token();  
					//给获取access_token时间赋值，方便下此次获取时进行判断  
					LASTTOKENTIME = System.currentTimeMillis();
					return ACCESS_TOKEN;
    			} else {
    				System.err.println("失败");
    			}
            } catch (ClientProtocolException e) {  
                e.printStackTrace();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }
        }
    	return ACCESS_TOKEN;
    }
    
	// 定制Verifier
 	class TrustAnyHostnameVerifier implements HostnameVerifier {
 		public boolean verify(String hostname, SSLSession session) {
 			return true;
 		}
 	}
    
    public synchronized String getAccess_token1(){
    	URL url;
    	String sCurrentLine = "";
		String sTotalString = "";
        if(ACCESS_TOKEN == null || System.currentTimeMillis() - LASTTOKENTIME > 7000*1000){
            //请求access_token地址 
        	List<String> list = wConfig.getIplist();
        	for(int i = 0; i < list.size(); i++) {
        		long startTime=System.currentTimeMillis();   //获取开始时间
        		String urlStr = "https://"+list.get(i)+"/cgi-bin/token?grant_type=client_credential&appid="+wConfig.getAPPID()+"&secret="+wConfig.getAPPSECRET(); 
                log.info("当前访问的微信urlStr为: "+urlStr);
                try {
                	url = new URL(urlStr);
                	URLConnection URLconnection = url.openConnection();
                	HttpsURLConnection httpConnection = (HttpsURLConnection) URLconnection;
                	httpConnection.setHostnameVerifier(new WeiXinUtil().new TrustAnyHostnameVerifier());
                	int responseCode = httpConnection.getResponseCode();
                	if (responseCode == HttpURLConnection.HTTP_OK) {
                		log.info("可以使用的IP为"+list.get(i));
                		InputStream urlStream = httpConnection.getInputStream();
                		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlStream));
                		while ((sCurrentLine = bufferedReader.readLine()) != null) {
                			sTotalString += sCurrentLine;
                		}
                		AccessToken aToken =  JsonUtil.readValue(sTotalString, AccessToken.class);
                		//给静态变量赋值，获取到access_token  
                		ACCESS_TOKEN = aToken.getAccess_token();  
                		//给获取access_token时间赋值，方便下此次获取时进行判断  
                		LASTTOKENTIME = System.currentTimeMillis();
                		long endTime=System.currentTimeMillis(); //获取结束时间
                		log.info("程序运行时间： "+(endTime-startTime)+"ms");
                		if(ACCESS_TOKEN != null) {
                			return ACCESS_TOKEN;
                		}
                	}else {
                		log.info("不可以使用的IP为"+list.get(i));
                		continue;
                	}
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
        }
        return ACCESS_TOKEN;
    }
    
    public static void main(String[] args) {  
//        getAccess_token();  
    }  
}
