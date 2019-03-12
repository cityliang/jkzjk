package com.huntto.util;

import java.util.Random;
import java.util.UUID;

public class UUIDTool {
	public UUIDTool() {
	}

	/**
	 * 自动生成32位的UUid，对应数据库的主键id进行插入用。
	 * 
	 * @return
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 生成指定长度的一个数字字符串
	 *
	 * @param num
	 * @return
	 */
	public static String randomNumber(int num) {
		if (num < 1) {
			num = 1;
		}
		Random random = new Random();
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < num; i++) {
			str.append(random.nextInt(10));
		}
		return str.toString();
	}

	/**
	 * 生成 32 位随机数 19位随机数+13位毫秒时间
	 * 
	 * @return
	 */
	public static String randomNumber() {
		Random random = new Random();
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < 19; i++) {
			str.append(random.nextInt(10));
		}
		str.append(System.currentTimeMillis());
		return str.toString();
	}

	public static void main(String[] args) {
//		String[] ss = getUUID(10);
//		for (int i = 0; i < 10; i++) {
//			System.out.println("ss[" + i + "]=====" + getUUID());
//		}

//		for (int i = 0; i < 10; i++) {
//			System.out.println("ss[" + i + "]=====" + getOrderNo());
//		}
//
//		String s = "302775171761755340";
//		System.out.println(s.length());
		System.out.println(randomNumber());
		System.out.println(randomNumber().length());
	}
}
