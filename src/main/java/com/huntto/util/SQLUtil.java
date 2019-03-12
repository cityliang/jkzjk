package com.huntto.util;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.Query;

/**
 * SQL工具
 */
public final class SQLUtil {

	private SQLUtil() {
	}

	/**
	 * 添加=查询条件
	 * 
	 * @param sqlWhere
	 * @param fieldName
	 * @param value
	 */
	public static void eq(StringBuilder sqlWhere, String fieldName, String paramName, String value) {
		where(sqlWhere, fieldName, paramName, value, "=");
	}

	/**
	 * 添加 like 查询条件
	 * 
	 * @param sqlWhere
	 * @param fieldName
	 * @param value
	 */
	public static void like(StringBuilder sqlWhere, String fieldName, String paramName, String value) {
		where(sqlWhere, fieldName, paramName, value, "like");
	}

	/**
	 * 添加 like '%x%' 查询条件
	 * 
	 * @param sqlWhere
	 * @param fieldName
	 * @param value
	 */
	public static void contains(StringBuilder sqlWhere, String fieldName, String paramName, String value) {
		where(sqlWhere, fieldName, paramName, value, "like");
	}

	/**
	 * 添加 like 'x%' 查询条件
	 * 
	 * @param sqlWhere
	 * @param fieldName
	 * @param value
	 */
	public static void startsWith(StringBuilder sqlWhere, String fieldName, String paramName, String value) {
		where(sqlWhere, fieldName, paramName, value, "like");
	}

	/**
	 * 添加 like '%x' 查询条件
	 * 
	 * @param sqlWhere
	 * @param fieldName
	 * @param value
	 */
	public static void endWith(StringBuilder sqlWhere, String fieldName, String paramName, String value) {
		where(sqlWhere, fieldName, paramName, value, "like");
	}

	/**
	 * 添加where条件参数
	 * 
	 * @param sqlWhere
	 * @param fieldName
	 * @param value
	 * @param type
	 */
	public static void where(StringBuilder sqlWhere, String fieldName, String paramName, String value, String type) {
		if (Nulls.isNotEmpty(value)) {
			sqlWhere.append(sqlWhere.length() > 0 ? " and " : " where ");
			sqlWhere.append(fieldName + " " + type + " :" + paramName);
		}
	}

	/**
	 * 添加=查询条件值
	 * 
	 * @param sqlWhere
	 * @param fieldName
	 * @param value
	 */
	public static void eq(Query query, String paramName, String value) {
		where(query, paramName, value);
	}

	/**
	 * 添加 like '%x%' 查询条件值
	 * 
	 * @param sqlWhere
	 * @param fieldName
	 * @param value
	 */
	public static void contains(Query query, String paramName, String value) {
		if (Nulls.isNotEmpty(value)) {
			where(query, paramName, "%" + value + "%");
		}
	}

	/**
	 * 添加 like 'x%' 查询条件值
	 * 
	 * @param sqlWhere
	 * @param fieldName
	 * @param value
	 */
	public static void startsWith(Query query, String paramName, String value) {
		if (Nulls.isNotEmpty(value)) {
			where(query, paramName, value + "%");
		}
	}

	/**
	 * 添加 like '%x' 查询条件值
	 * 
	 * @param sqlWhere
	 * @param fieldName
	 * @param value
	 */
	public static void endWith(Query query, String paramName, String value) {
		if (Nulls.isNotEmpty(value)) {
			where(query, paramName, "%" + value);
		}
	}

	/**
	 * 添加where条件参数值
	 * 
	 * @param sqlWhere
	 * @param fieldName
	 * @param value
	 * @param type
	 */
	public static void where(Query query, String paramName, Object value) {
		if (Nulls.isNotEmpty(value)) {
			if (value instanceof String) {
				query.setString(paramName, (String) value);
			} else if (value instanceof Integer) {
				query.setInteger(paramName, (Integer) value);
			} else if (value instanceof Long) {
				query.setLong(paramName, (Long) value);
			} else if (value instanceof Float) {
				query.setFloat(paramName, (Float) value);
			} else if (value instanceof Double) {
				query.setDouble(paramName, (Double) value);
			} else if (value instanceof BigDecimal) {
				query.setBigDecimal(paramName, (BigDecimal) value);
			} else if (value instanceof Date) {
				query.setDate(paramName, (Date) value);
			} else {
				throw new RuntimeException("不支持数据类型");
			}
		}
	}

	/**
	 * sql单引号处理
	 * 
	 * @param value
	 * @return
	 */
	public static String escapeSQL(String value) {
		if (value == null)
			return "";
		if (value.indexOf("'") == -1)
			return value;
		return value.replace("'", "''");
	}
}
