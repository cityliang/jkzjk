package com.huntto.util;

import java.math.BigDecimal;

public class Nulls {
	
	public static boolean validateValue(String... args) {
		for (String str : args) {
			if (isEmpty(str)) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isEmpty(String data) {
		boolean flag = false;
		if (data == null || "".equals(data.trim()) || "null".equals(data) || "undefined".equals(data)) {
			flag = true;
		}

		return flag;
	}

	public static void isEmpty(String data, String errorMsg) {
		if (data == null || "".equals(data.trim()) || "null".equals(data) || "undefined".equals(data)) {
			throw new RuntimeException(errorMsg);
		}
	}

	public static boolean isNotEmpty(String data) {
		return !isEmpty(data);
	}

	public static boolean isNotEmpty(Object data) {
		return !isEmpty(data);
	}

	public static boolean isEmpty(Object data) {
		if (data == null) {
			return true;
		} else {
			return data instanceof String && "".equals((String) data);
		}
	}

	public static String nullToString(Object obj) {
		if (obj == null) {
			return "";
		} else {
			return obj instanceof Integer && (Integer) obj == 0 ? "" : obj.toString();
		}
	}

	public static double nullToDouble(Object obj) {
		return obj != null && !isEmpty(obj.toString()) ? Double.parseDouble(obj.toString()) : 0.0D;
	}

	public static int nullToInt(Object obj) {
		if (obj == null) {
			return 0;
		} else if (obj instanceof Integer) {
			return (Integer) obj;
		} else {
			return isEmpty(obj.toString()) ? 0 : Integer.parseInt(obj.toString());
		}
	}

	public static BigDecimal nullToBigDecimal(Object obj) {
		return obj == null ? new BigDecimal("0") : (BigDecimal) obj;
	}

	public static Long nullToLong(Object obj) {
		return obj == null ? new Long("0") : (Long) obj;
	}

	public static String nvl(String obj) {
		return obj != null ? obj : "";
	}

	public static String nvl2(Object obj) {
		return obj != null ? obj.toString() : "";
	}
}
