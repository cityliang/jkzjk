package com.huntto.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.cloudauth.model.v20180916.CompareFacesRequest;
import com.aliyuncs.cloudauth.model.v20180916.CompareFacesResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;


public class FaceRecognUtil3 {
	public static void main(String[] args) {
		//创建DefaultAcsClient实例并初始化
		DefaultProfile profile = DefaultProfile.getProfile(
		        "cn-hangzhou",             //默认
		        "LTAIpfMXLqPGY81V",         //您的Access Key ID
		        "Fxrwy8YeBjviLI4zivYWWsjF7oqVLR");    //您的Access Key Secret
		IAcsClient client = new DefaultAcsClient(profile);
		//创建API请求并设置参数
		//CompareFaces接口文档：https://help.aliyun.com/document_detail/59317.html
		CompareFacesRequest request = new CompareFacesRequest();
		request.setMethod(MethodType.POST);
		//传入图片资料，请控制单张图片大小在 2M 内，避免拉取超时
		request.setSourceImageType("FacePic");
		request.setSourceImageValue("base64://iVBORw0KGgoAAAANSUhEUgAAAHMAAABzCAIAAAAkIaqxAAACp0lEQVR42u3bS1bDQAwF0ex/0zBkwIHYeiVhNeVhyMd9O0fRp3l9ePVcLwl6ZV/Z9fV2d578/VVX3ue3xfz8PrVHEg1lld0uey+U3HlVzZraob51/fQqZZU9RvZWVA1DXi2q3rqN2q9FoqGsssq+XduVP9U+InxEWWWVHZalcrUwe6ttlbLK/kNZKt2hIl1tG2qreHR1q6yyU7JUf/aMR57Y+VZW2WZZbGDZnwnVonMtBD9oKq6sslOytb5JuEh8iFurr4amNcoqu032IXOXvvvBn3M1ziqr7BJZqkNKTXyplkp4KC3pOyur7FLZcLXUXdZGv1TdGG5e/RdMWWWfLTt5AmOgr0r1la5vsLLKLpXF57KhCJVs1SquWuqprLKHydZuLuzI1OJamHVRdeOb/qyyyh4kSwVBKuT11VfsPSur7FJZ6ngE3v3E27L4KoD+rLLKPkk2PIERRrEwsaO+BGy4V1bZY2Spow9IdwNs5vadNWmpbpVVdly2uz3xEf/bFV4TUqDKKnuYLDVcCXcoHMeGQ2Uqn1NW2dWyNT68NVOjwdM4ZKuUVXap7CQofsgj7Ks2xWJlld0uS7UwwkMe4frxdnNhOcoqe55s34yTGreEXZvabdzuyCir7FrZcEBLZUuTc+Lw1MibGkxZZffLTk5qqVqOmh4lVaKyym6XpSaj4XP6zpFQKRrZ61JW2W2y+OGMgfBaqwlrV8u0Rlll/04WL8aotfUVWk19XmWVXSrLhpjWnCac8VDl2dX+rLLKrpWtRdWkVinnT1Qa17Tlyip7jOxAqzQM5WEM7b4NZZVVFjlLgYfOgaRtKM4qq+wS2bDCoeIslcbVWkXKKnuYLNXLoEYpeAe579ugrLJnyFKjFDyK1QYneEy//unKKrtU1gu/lO26PgFQJgJ/DzD9lAAAAABJRU5ErkJggg=="); //base64方式上传图片, 格式为"base64://图片base64字符串", 以"base64://"开头且图片base64字符串去掉头部描述(如"data:image/png;base64,"), 并注意控制接口请求的Body在8M以内
		request.setTargetImageType("FacePic"); //若为身份证芯片照则传"IDPic"
		request.setTargetImageValue("http://image-demo.img-cn-hangzhou.aliyuncs.com/example.jpg"); //http方式上传图片, 此http地址须可公网访问
		//发起请求并处理异常
		try {
		    CompareFacesResponse response = client.getAcsResponse(request);
		    //后续业务处理
		} catch (ServerException e) {
		    e.printStackTrace();
		} catch (ClientException e) {
		    e.printStackTrace();
		}
		//常见问题：https://help.aliyun.com/document_detail/57640.html
	}
}
