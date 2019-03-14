package com.huntto.util;

import lombok.Data;

@Data
public class Result {
	private int errno;// 0为成功，非0为失败，详细说明参见错误码
	private String err_msg;// 处理失败时的说明
	private String request_id;// request_id 请求id信息
	float confidence;// 两张图片中最大人脸属于同一个人的置信度：0-100，如某张图片中没有人脸，返回置信度为0
	float[] thresholds;// 误识率在10e-3,10e-4,10e-5时对应的置信度分类阈值
	int[] rectA;// 图片1最大人脸矩形框(left, top, width, height)，如图片中没有人脸，返回矩形框数值均为0
	int[] rectB;// 图片2最大人脸矩形框(left, top, width, height)，如图片中没有人脸，返回矩形框数值均为0
}
