package com.huntto.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: 类描述 <br/>
 * Copyright: 版权归浙江创得信息技术有限公司所有<br/>
 * Project_name jkzjk<br/>
 * Package_name com.huntto.util<br/>
 * Author 梁城市<br/>
 * Email city_wangyi@163.com<br/>
 * Create_time 2018/12/8 21:13<br/>
 */
public class IdCardUtil {
	private final static Map<Integer, String> zoneNum = new HashMap<Integer, String>();

	static {
		zoneNum.put(11, "北京");
		zoneNum.put(12, "天津");
		zoneNum.put(13, "河北");
		zoneNum.put(14, "山西");
		zoneNum.put(15, "内蒙古");
		zoneNum.put(21, "辽宁");
		zoneNum.put(22, "吉林");
		zoneNum.put(23, "黑龙江");
		zoneNum.put(31, "上海");
		zoneNum.put(32, "江苏");
		zoneNum.put(33, "浙江");
		zoneNum.put(34, "安徽");
		zoneNum.put(35, "福建");
		zoneNum.put(36, "江西");
		zoneNum.put(37, "山东");
		zoneNum.put(41, "河南");
		zoneNum.put(42, "湖北");
		zoneNum.put(43, "湖南");
		zoneNum.put(44, "广东");
		zoneNum.put(45, "广西");
		zoneNum.put(46, "海南");
		zoneNum.put(50, "重庆");
		zoneNum.put(51, "四川");
		zoneNum.put(52, "贵州");
		zoneNum.put(53, "云南");
		zoneNum.put(54, "西藏");
		zoneNum.put(61, "陕西");
		zoneNum.put(62, "甘肃");
		zoneNum.put(63, "青海");
		zoneNum.put(64, "宁夏");
		zoneNum.put(65, "新疆");
		zoneNum.put(71, "台湾");
		zoneNum.put(81, "香港");
		zoneNum.put(82, "澳门");
		zoneNum.put(91, "国外");
	}

	private final static int[] PARITYBIT = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};// 校验码
	private final static int[] POWER_LIST = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};// 加权因子wi

	/**
	 * 验证身份证号有效性
	 *
	 * @param idCard:身份证号
	 * @return true/false
	 */
	public static boolean isIdcard(String idCard) {

		//号码长度应为15位或18位
		if (idCard == null || (idCard.length() != 15 && idCard.length() != 18)) {
			return false;
		}
		//校验区位码
		if (!zoneNum.containsKey(Integer.valueOf(idCard.substring(0, 2)))) {
			return false;
		}
		//校验年份
		String year = idCard.length() == 15 ? "19" + idCard.substring(6, 8) : idCard.substring(6, 10);
		final int iyear = Integer.parseInt(year);
		if (iyear < 1900 || iyear > Calendar.getInstance().get(Calendar.YEAR)) {
			return false;// 1900年的PASS，超过今年的PASS
		}
		//校验月份
		String month = idCard.length() == 15 ? idCard.substring(8, 10) : idCard.substring(10, 12);
		final int imonth = Integer.parseInt(month);
		if (imonth < 1 || imonth > 12) {
			return false;
		}
		//校验天数
		String day = idCard.length() == 15 ? idCard.substring(10, 12) : idCard.substring(12, 14);
		final int iday = Integer.parseInt(day);
		if (iday < 1 || iday > 31) {
			return false;
		}
		//校验一个合法的年月日
//      if (!isValidDate(year + imonth + iday)) {
//          return false;
//      }
		//校验位数
		int power = 0;
		final char[] cs = idCard.toUpperCase().toCharArray();
		for (int i = 0; i < cs.length; i++) {// 循环比正则表达式更快
			if (i == cs.length - 1 && cs[i] == 'X') {
				break;// 最后一位可以是X或者x
			}
			if (cs[i] < '0' || cs[i] > '9') {
				return false;
			}
			if (i < cs.length - 1) {
				power += (cs[i] - '0') * POWER_LIST[i];
			}
		}
		//校验“校验码”
		return idCard.length() == 15 || cs[cs.length - 1] == PARITYBIT[power % 11];

	}
}