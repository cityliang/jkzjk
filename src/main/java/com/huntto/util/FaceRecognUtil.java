package com.huntto.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huntto.config.FaceRecognConfig;
import com.huntto.entity.face.FaceDetectResult;
import com.huntto.entity.face.FaceVerifyResult;
import com.huntto.util.WeiXinUtil.TrustAnyHostnameVerifier;

import lombok.Data;

@Data
@Component
public class FaceRecognUtil {
	@Autowired
	private FaceRecognConfig faceRecognConfig;
	
	public static void main(String[] args) throws Exception {
        // 发送POST请求示例
//        String ak_id = "LTAIpfMXLqPGY81V"; //用户ak
//        String ak_secret = "Fxrwy8YeBjviLI4zivYWWsjF7oqVLR"; // 用户ak_secret
//        String url = "https://dtplus-cn-shanghai.data.aliyuncs.com/face/verify"; //参考：https://face.data.aliyun.com/console
//        String body = "{\"type\":\"0\",\"image_url_1\":\"http://e.hiphotos.baidu.com/image/pic/item/dbb44aed2e738bd4d78823fba88b87d6267ff94b.jpg\",\"image_url_2\":\"http://e.hiphotos.baidu.com/image/pic/item/dbb44aed2e738bd4d78823fba88b87d6267ff94b.jpg\"}";//参考：https://help.aliyun.com/knowledge_detail/53520.html?spm=5176.7753399.6.553.i4Hm7s";
//        System.out.println(body);
//        FaceRecognUtil faceRecognUtil = new FaceRecognUtil();
//        System.out.println("response body:" + faceRecognUtil.sendPost(url, body,ak_id,ak_secret));
        
		String result = "{\"confidence\":19.866025924682617,\"thresholds\":[61.0,69.0,75.0],\"rectA\":[50,53,17,24],\"rectB\":[8,15,55,75],\"errno\":0,\"request_id\":\"0c9bb5c3-c309-4648-b7f2-eab3570c6a17\"}";
//		JSONPObject jsonpObject = new JSONPObject(result,String.class);
//		jsonpObject.getValue();
//		System.out.println(jsonpObject.getValue().g.toString());
		FaceVerifyResult result2 = JsonUtil.readValue(result, FaceVerifyResult.class);
		System.out.println(result2.toString());
    }
	
	/**
	 * 人脸检测API接口调用
	 * 发送POST请求
	 * @param img1 传入的图片Base64编码
	 * @return FaceDetectResult
	 * @throws Exception Exception
	 */
	public FaceDetectResult faceDetect(String img1) throws Exception {
       PrintWriter out = null;
       BufferedReader in = null;
       String result = "";
       String body = "{\"type\":\"1\",\"content\":\""+img1+"\"}";
       int statusCode = 200;
       try {
           URL realUrl = new URL(faceRecognConfig.getDetectUrl());
           /*
            * http header 参数
            */
           String method = "POST";
           String accept = "application/json";
           String content_type = "application/json";
           String path = realUrl.getFile();
           String date = toGMTString(new Date());
           // 1.对body做MD5+BASE64加密
           String bodyMd5 = MD5Base64(body);
           String stringToSign = method + "\n" + accept + "\n" + bodyMd5 + "\n" + content_type + "\n" + date + "\n" + path;
           // 2.计算 HMAC-SHA1
           String signature = HMACSha1(stringToSign, faceRecognConfig.getAk_secret());
           // 3.得到 authorization header
           String authHeader = "Dataplus " + faceRecognConfig.getAk_id() + ":" + signature;
           // 打开和URL之间的连接
           URLConnection conn = realUrl.openConnection();
           // 设置通用的请求属性
           conn.setRequestProperty("accept", accept);
           conn.setRequestProperty("content-type", content_type);
           conn.setRequestProperty("date", date);
           conn.setRequestProperty("Authorization", authHeader);
           // 发送POST请求必须设置如下两行
           conn.setDoOutput(true);
           conn.setDoInput(true);
           HttpsURLConnection httpConnection = (HttpsURLConnection) conn;
           httpConnection.setHostnameVerifier(new WeiXinUtil().new TrustAnyHostnameVerifier());
           // 获取URLConnection对象对应的输出流
           out = new PrintWriter(conn.getOutputStream());
           // 发送请求参数
           out.print(body);
           // flush输出流的缓冲
           out.flush();
           // 定义BufferedReader输入流来读取URL的响应
           statusCode = ((HttpsURLConnection)httpConnection).getResponseCode();
           if(statusCode != 200) {
               in = new BufferedReader(new InputStreamReader(((HttpsURLConnection)httpConnection).getErrorStream()));
           } else {
               in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
           }
           String line;
           while ((line = in.readLine()) != null) {
               result += line;
           }
       } catch (Exception e) {
           e.printStackTrace();
       } finally {
           try {
               if (out != null) {
                   out.close();
               }
               if (in != null) {
                   in.close();
               }
           } catch (IOException ex) {
               ex.printStackTrace();
           }
       }
       if (statusCode != 200) {
           throw new IOException("\nHttp StatusCode: "+ statusCode + "\nErrorMessage: " + result);
       }
       return JsonUtil.readValue(result, FaceDetectResult.class);
   }
	
	/**
	 * 人脸对比API接口调用
	 * 发送POST请求
	 * @param img1 传入的图片base64编码
	 * @param img2 传入的图片base64编码
	 * @return
	 * @throws Exception Exception
	 */
	public FaceVerifyResult faceVerify(String img1, String img2) throws Exception {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        String body = "{\"type\":\"1\",\"content_1\":\""+img1+"\",\"content_2\":\""+img2+"\"}";
        int statusCode = 200;
        try {
            URL realUrl = new URL(faceRecognConfig.getVerifyUrl());
            /*
             * http header 参数
             */
            String method = "POST";
            String accept = "application/json";
            String content_type = "application/json";
            String path = realUrl.getFile();
            String date = toGMTString(new Date());
            // 1.对body做MD5+BASE64加密
            String bodyMd5 = MD5Base64(body);
            String stringToSign = method + "\n" + accept + "\n" + bodyMd5 + "\n" + content_type + "\n" + date + "\n" + path;
            // 2.计算 HMAC-SHA1
            String signature = HMACSha1(stringToSign, faceRecognConfig.getAk_secret());
            // 3.得到 authorization header
            String authHeader = "Dataplus " + faceRecognConfig.getAk_id() + ":" + signature;
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", accept);
            conn.setRequestProperty("content-type", content_type);
            conn.setRequestProperty("date", date);
            conn.setRequestProperty("Authorization", authHeader);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            HttpsURLConnection httpConnection = (HttpsURLConnection) conn;
            httpConnection.setHostnameVerifier(new WeiXinUtil().new TrustAnyHostnameVerifier());
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(body);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            statusCode = ((HttpsURLConnection)conn).getResponseCode();
            if(statusCode != 200) {
                in = new BufferedReader(new InputStreamReader(((HttpsURLConnection)httpConnection).getErrorStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
            }
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (statusCode != 200) {
            throw new IOException("\nHttp StatusCode: "+ statusCode + "\nErrorMessage: " + result);
        }
        return JsonUtil.readValue(result, FaceVerifyResult.class);
    }
	
	/**
	 * 计算MD5+BASE64
	 * @param s 需要加密的字符串
	 * @return 加密之后的字符串
	 */
    String MD5Base64(String s) {
        if (s == null)
            return null;
        String encodeStr = "";
        byte[] utfBytes = s.getBytes();
        MessageDigest mdTemp;
        try {
            mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(utfBytes);
            byte[] md5Bytes = mdTemp.digest();
            encodeStr = Base64.encodeBase64String(md5Bytes);
        } catch (Exception e) {
            throw new Error("Failed to generate MD5 : " + e.getMessage());
        }
        return encodeStr;

    }

    /**
     * 计算 HMAC-SHA1
     * @param data stringToSign
     * @param key ak_secret
     * @return HMAC-SHA1
     */
    String HMACSha1(String data, String key) {
        String result;
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            result = Base64.encodeBase64String(rawHmac);
        } catch (Exception e) {
            throw new Error("Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }

    /**
     * 等同于javaScript中的 new Date().toUTCString();
     * @param date 时间
     * @return 格式化之后的时间
     */
    String toGMTString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.UK);
        df.setTimeZone(new java.util.SimpleTimeZone(0, "GMT"));
        return df.format(date);
    }

    /*
     * 发送POST请求
     */
    public String sendPost(String url, String body, String ak_id, String ak_secret) throws Exception {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        int statusCode = 200;
        try {
            URL realUrl = new URL(url);
            /*
             * http header 参数
             */
            String method = "POST";
            String accept = "application/json";
            String content_type = "application/json";
            String path = realUrl.getFile();
            String date = toGMTString(new Date());
            // 1.对body做MD5+BASE64加密
            String bodyMd5 = MD5Base64(body);
            String stringToSign = method + "\n" + accept + "\n" + bodyMd5 + "\n" + content_type + "\n" + date + "\n" + path;
            // 2.计算 HMAC-SHA1
            String signature = HMACSha1(stringToSign, ak_secret);
            // 3.得到 authorization header
            String authHeader = "Dataplus " + ak_id + ":" + signature;
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", accept);
            conn.setRequestProperty("content-type", content_type);
            conn.setRequestProperty("date", date);
            conn.setRequestProperty("Authorization", authHeader);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(body);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            statusCode = ((HttpURLConnection)conn).getResponseCode();
            if(statusCode != 200) {
                in = new BufferedReader(new InputStreamReader(((HttpURLConnection)conn).getErrorStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (statusCode != 200) {
            throw new IOException("\nHttp StatusCode: "+ statusCode + "\nErrorMessage: " + result);
        }
        return result;
    }
}
