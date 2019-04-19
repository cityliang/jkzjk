package com.alibaba.cloud.demo;

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

import org.apache.commons.codec.binary.Base64;
import net.coobird.thumbnailator.Thumbnails;
/**
 * 线上人脸对比demo
 */
public class Demo_face_verify {
	 /*
     * 计算MD5+BASE64
     */
    public static String MD5Base64(String s) {
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
//            BASE64Encoder b64Encoder = new BASE64Encoder();
//            encodeStr = b64Encoder.encode(md5Bytes);
        } catch (Exception e) {
            throw new Error("Failed to generate MD5 : " + e.getMessage());
        }
        return encodeStr;

    }

    /*
     * 计算 HMAC-SHA1
     */
    public static String HMACSha1(String data, String key) {
        String result;
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            result = Base64.encodeBase64String(rawHmac);
//            result = (new BASE64Encoder()).encode(rawHmac);
        } catch (Exception e) {
            throw new Error("Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }

    /*
     * 等同于javaScript中的 new Date().toUTCString();
     */
    public static String toGMTString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.UK);
        df.setTimeZone(new java.util.SimpleTimeZone(0, "GMT"));
        return df.format(date);
    }

    /*
     * 发送POST请求
     */
    public static String sendPost(String url, String body, String ak_id, String ak_secret) throws Exception {
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

    public static void main(String[] args) throws Exception {
        // 发送POST请求示例
        String ak_id = "LTAIpfMXLqPGY81V"; //用户ak
        String ak_secret = "Fxrwy8YeBjviLI4zivYWWsjF7oqVLR"; // 用户ak_secret
        String url = "https://dtplus-cn-shanghai.data.aliyuncs.com/face/verify"; //参考：https://face.data.aliyun.com/console
//        String body = "{type\":0,\"image_url_1\":\"https://note.youdao.com/yws/public/resource/45ade8b443dccfa0127d49cc769250f5/xmlnote/WEBRESOURCE13f101affe5e46c8aaf5aa672422659e/1859\",\"image_url_2\":\"https://note.youdao.com/yws/public/resource/45ade8b443dccfa0127d49cc769250f5/xmlnote/WEBRESOURCE2ef5ca99ca150dfce1ecd6ef143ff7ae/1869\"}";//参考：https://help.aliyun.com/knowledge_detail/53520.html?spm=5176.7753399.6.553.i4Hm7s";
//        System.out.println("response body:" + sendPost(url, body, ak_id, ak_secret));
        
        
        byte[] imageData1 = Utils.loadFile(Utils.PICTURE_ROOT + "5.jpg");
        System.out.println("imageData1 length: "+imageData1.length);
        String img1 =  Base64.encodeBase64String(imageData1);
        System.out.println("img2 length: "+img1.length());
//        ImgUtils.scale(Utils.PICTURE_ROOT + "4.jpg", Utils.PICTURE_ROOT + "5.jpg", 128, 128, true);// 等比例缩放 输出缩放图片
        byte[] imageData2 = Utils.loadFile(Utils.PICTURE_ROOT + "3.jpg");
        System.out.println("imageData2 length: "+imageData2.length);
        String img2 =  Base64.encodeBase64String(imageData2);
        System.out.println("img2 length: "+img2.length());
        Thumbnails.of(Utils.PICTURE_ROOT + "2.jpg").scale(1f).outputQuality(0.5f).toFile(Utils.PICTURE_ROOT + "3.jpg");
//        Thumbnails.of(Utils.PICTURE_ROOT + "1.jpg").scale(1f).outputQuality(0.75f).toFile(Utils.PICTURE_ROOT + "75.jpg");
//        Thumbnails.of(Utils.PICTURE_ROOT + "1.jpg").scale(1f).outputQuality(0.5f).toFile(Utils.PICTURE_ROOT + "50.jpg");
//        Thumbnails.of(Utils.PICTURE_ROOT + "1.jpg").scale(1f).outputQuality(0.25f).toFile(Utils.PICTURE_ROOT + "25.jpg");
//        byte[] imageData3 = Utils.loadFile(Utils.PICTURE_ROOT + "5.jpg");
//        System.out.println("imageData3 length: "+imageData3.length);
//        String img3 =  Base64.encodeBase64String(imageData3);
//        System.out.println();
//        System.out.println("img3 length: "+img3.length());
        
////        
//        String body = "{\"type\":\"1\",\"content_1\":\""+img1+"\",\"content_2\":\""+img2+"\"}";
//        System.out.println("response body:" + sendPost(url, body, ak_id, ak_secret));
//        
    }
}
