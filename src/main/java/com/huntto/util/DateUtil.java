package com.huntto.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description: 类描述 <br/>
 * Copyright: 版权归浙江创得信息技术有限公司所有<br/>
 * Project_name jkzjk<br/>
 * Package_name com.huntto.util<br/>
 * Author 梁城市<br/>
 * Email city_wangyi@163.com<br/>
 * Create_time 2018/12/8 21:19<br/>
 */
public class DateUtil {
	/**
	 * 日期转字符串 格式 yyyy-MM-dd 年-月-日
	 * 
	 * @param date date
     * @return 日期转字符串
     */
	public static String Date2String1(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(date);
		return str;
	}

	/**
	 * 日期转字符串 格式 yyyy-MM-dd HH:mm:ss 年-月-日 时:分:秒
	 * 
	 * @param date date
     * @return 日期转字符串
     */
	public static String Date2String2(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = sdf.format(date);
		return str;
	}
}
