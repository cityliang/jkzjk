package com.huntto.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.huntto.config.WeiXinConfig;
import com.huntto.entity.AccessToken;

public class WeiXinUtil {
	
	@Autowired
	private WeiXinConfig wConfig;
	
	public static final String APPID = "wx5c488c49d8ce4125";  
    public static final String APPSECRET = "81e1cab42c562842ccba781090b4e961";  
      
    /**全局token 所有与微信有交互的前提 */  
    public static String ACCESS_TOKEN;  
      
    /**全局token上次获取事件 */  
    public static long LASTTOKENTIME;  
      
    /** 
     * 获取全局token方法 
     * 该方法通过使用HttpClient发送http请求，HttpGet()发送请求 
     * 微信返回的json中access_token是我们的全局token 
     */  
    public static synchronized String getAccess_token(){
    	URL url;
    	String sCurrentLine = "";
		String sTotalString = "";
        if(ACCESS_TOKEN == null || System.currentTimeMillis() - LASTTOKENTIME > 7000*1000){
            try {  
                //请求access_token地址  
                String urlStr = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx5c488c49d8ce4125&secret=81e1cab42c562842ccba781090b4e961";  
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
    public static void main(String[] args) {  
        getAccess_token();  
    }  
}
