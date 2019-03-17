package com.huntto.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Clob;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 转换工具类
 */
public final class ConvertUtil {
    protected static final Log log = LogFactory.getLog(ConvertUtil.class);

    /**
     * Checkstyle rule: utility classes should not have public constructor
     */
    private ConvertUtil() {
    }

    /**
	 * String 2 Long
	 */
	public static Long toLong(String str){
		Long i = null;
		try {
			if(Nulls.isNotEmpty(str))
				i = new Long(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	
	/**
	 * String 2 Long,有默认值
	 */
	public static Long toLong(String str, long defaultVal){
        Long i = defaultVal;
        try {
			if(Nulls.isNotEmpty(str)){
				i = new Long(str);
			}
        } catch (Exception ignored) {
        }
		return i;
	}
	
	/**
	 * BigDecimal类型转换：转不了，返回null
	 */
	public static BigDecimal toBigDecimal(String str){
		BigDecimal bd = null;
		try {
			if(Nulls.isNotEmpty(str)){
				bd = new BigDecimal(str);
			}
		} catch (Exception e) {
		}
		return bd;
	}
	
	/**
	 * BigDecimal类型转换：转不了，抛异常
	 */
	public static BigDecimal toBigDecimal(String str, String expMsg){
		BigDecimal bd = null;
		try {
			if(Nulls.isNotEmpty(str)){
				bd = new BigDecimal(str);
			} else {
				throw new RuntimeException(expMsg);
			}
		} catch (Exception e) {
			throw new RuntimeException(expMsg);
		}
		return bd;
	}
	
	/**
	 * BigDecimal类型转换：转不了，默认值
	 */
	public static BigDecimal toBigDecimal(String str, double defaultVal){
		BigDecimal bd = new BigDecimal(defaultVal);
		try {
			if(Nulls.isNotEmpty(str)){
				bd = new BigDecimal(str);
			}
		} catch (Exception e) {
		}
		return bd;
	}
	
	/**
	 * Short类型转换
	 */
	public static Short toShort(String str){
		Short st = null;
		if(null != str && !"".equals(str)){
			st = new Short(str);
		}
		return st;
	}
	
	/**
	 * Integer类型转换
	 */
	public static Integer toInteger(String str){
		Integer i = null;
		if(null != str && !"".equals(str)){
			i = new Integer(str.trim());
		}
		return i;
	}
	/**
	 * Float类型转换
	 */
	public static Float toFloat(String str){
		Float i = null;
		if(null != str && !"".equals(str)){
			i = new Float(str);
		}
		return i;
	}
	/**
	 * Double类型转换
	 */
	public static Double toDouble(String str){
		Double i = null;
		if(null != str && !"".equals(str)){
			i = new Double(str);
		}
		return i;
	}
	/**
	 * Float类型转换
	 */
	public static Float toFloat(String str,float defaultVal){
		Float i = defaultVal;
		if(null != str && !"".equals(str)){
			i = new Float(str);
		}
		return i;
	}
		
	/**
	 * 字符串转日期，优先考虑DateConverter
	 */
	public static Date toParseDate(String time)  {
		/**
		 * 字符串转换为java.util.Date 支持格式为:
		 * yyyy.MM.dd G 'at' hh:mm:ss z 如 '2002-1-1 AD at 22:10:59 PSD' 
		 * yy/MM/dd HH:mm:ss 如 '2002/1/1 17:55:00' 
		 * yy/MM/dd HH:mm:ss pm 如 '2002/1/1 17:55:00 pm' 
		 * yy-MM-dd HH:mm:ss 如 '2002-1-1 17:55:00' 
		 * yy-MM-dd HH:mm:ss am 如 '2002-1-1 17:55:00 am'
		 * 
		 * @param time
		 *            String 字符串
		 * @return Date 日期
		 */
		try
		{
		SimpleDateFormat formatter;
		
		int tempPos = time.indexOf("AD");
		time = time.trim();
		formatter = new SimpleDateFormat(" yyyy.MM.dd G 'at' hh:mm:ss z ");
		if (tempPos > -1) {
			time = time.substring(0, tempPos) + " 公元 "
			+ time.substring(tempPos + " AD ".length()); // china
			formatter = new SimpleDateFormat(" yyyy.MM.dd G 'at' hh:mm:ss z ");
		}
		
		tempPos = time.indexOf("-");

            if (time.contains(".")) {
                time = time.substring(0, time.indexOf("."));
		}
		
		if (tempPos > -1) {//包含“-”
            if (!time.contains(":")) {
                formatter = new SimpleDateFormat("yyyy-MM-dd");
			} else if (time.indexOf(":") == time.lastIndexOf(":")) {
				formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			} else {
				formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			}
        } else if (time.contains("/")) {//包含“/”
            if (!time.contains(":")) {
                formatter = new SimpleDateFormat("yyyy/MM/dd");
			}else{
				formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			}
        } else if (!time.substring(0, 8).contains("-")) {
            time = time.substring(0, 8);
			formatter = new SimpleDateFormat("yyyyMMdd");
		} else {
			formatter = new SimpleDateFormat("HH:mm");
		}
		
		ParsePosition pos = new ParsePosition(0);
            return formatter.parse(time, pos);

        }
		catch(Exception ex)
		{ return null;}
		
	}
	
	/**
	 * 格式化日期
	 */
	public static String dataFormat(Date date, String pattern){
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		try {
			return df.format(date);
		} catch (Exception e) {
			return "";
		}
		
	}
	
	/**
	 * 获取当前日期第一天
	 */
	public static Date getFirstDayByMonth(Date date) throws Exception{
		Calendar cal = Calendar.getInstance();   
		cal.setTime(date);   
		int stDate = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-");
		String startDate1 = sdf.format(date).concat(String.valueOf(stDate));
		return toParseDate(startDate1);
	}
	
	/**
	 * 获取当前提前最后一天
     * @param date date
     * @return date
     * @throws Exception  Exception
     */
	public static Date getLastDayByMonth(Date date) throws Exception{
		Calendar cal = Calendar.getInstance();   
		cal.setTime(date);   
		int enDate = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-");
		String endDate1 = sdf.format(date).concat(String.valueOf(enDate));
		return toParseDate(endDate1);
	}
	
	/**
	 * 获取当前日期下一天
	 */
	public static Date getNexDate(Date date)throws Exception{
		Calendar calendar=Calendar.getInstance();   
		calendar.setTime(date);   
		int day=calendar.get(Calendar.DATE);   
		calendar.set(Calendar.DATE,day+1);
		Date dat = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   
		String  date1   = sdf.format(dat) ; 
		Date d = sdf.parse(date1);
		return d;
	}
	/**
	 * 获取当前日期前一天
	 */
	public static Date getForwardOneDate(Date date){
		try {
			Calendar calendar = Calendar.getInstance();   
			calendar.setTime(date);   
			int day=calendar.get(Calendar.DATE);   
			calendar.set(Calendar.DATE,day - 1);
			Date dat = calendar.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   
			String  date1  = sdf.format(dat) ; 
			Date d = sdf.parse(date1);
			return d;
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 获取当前日期 N年后 的前一天
	 * @param year	几年后
	 * @param date	当前日期
     * @return Date
     */
	public static Date getForwardOneDateAfterNYear(int year,Date date){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   
			Calendar calendar = Calendar.getInstance();   
			calendar.setTime(date); 
			String thisYear = sdf.format(date).substring(0,4);
			int day=calendar.get(Calendar.DATE);   
			calendar.set(Calendar.DATE,day - 1);
			calendar.set(Calendar.YEAR,Integer.valueOf(thisYear)+year);
			Date dat = calendar.getTime();
			String  date1  = sdf.format(dat) ; 
			Date d = sdf.parse(date1);
			return d;
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 获取当前日期前10天
	 */
	public static Date getForwardTenDate(Date date){
	  try {
	    Calendar calendar = Calendar.getInstance();   
	    calendar.setTime(date);   
	    int day=calendar.get(Calendar.DATE);   
	    calendar.set(Calendar.DATE,day - 10);
	    Date dat = calendar.getTime();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
          String date1 = sdf.format(dat);
          return sdf.parse(date1);
    } catch (Exception e) {
      return null;
    }
		
	}
	/**
	 * 获取当前日期前几天
	 */
	public static Date getForwardSumeDate(Date date,int days){
		try {
			Calendar calendar = Calendar.getInstance();   
			calendar.setTime(date);   
			int day=calendar.get(Calendar.DATE);   
			calendar.set(Calendar.DATE,day - days);
			Date dat = calendar.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date1 = sdf.format(dat);
            return sdf.parse(date1);
        } catch (Exception e) {
			return null;
		}
		
	}
	/**
	 * 获取当前日期后7天
	 */
	public static Date getSevenDate(Date date){
	  try {
	    Calendar calendar = Calendar.getInstance();   
	    calendar.setTime(date);   
	    int day=calendar.get(Calendar.DATE);   
	    calendar.set(Calendar.DATE,day + 6);
	    Date dat = calendar.getTime();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
          String date1 = sdf.format(dat);
          return sdf.parse(date1);
    } catch (Exception e) {
      return null;
    }
		
	}
	
	/**
	 * 处理当前日期去掉小时分钟秒
	 */
	public static Date getCurrentDate(Date date)throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   
		String  date1   = sdf.format(date) ; 
		Date d = sdf.parse(date1);
		return d;
	}
	
	/**
	 * 处理行政区划代码
	 * 只取行政编码的有效位
	 */
	public static String handleXZQH(String qhdm){
		StringBuffer sb = new StringBuffer();
		if(qhdm.length() == 9){
			String sheng = qhdm.substring(0,2);//省
			String shi = qhdm.substring(2,4);//市
			String xq = qhdm.substring(4,6);//县区
			String jd = qhdm.substring(6,9);//乡镇街道
            if (!sheng.contains("00")) {
                sb.append(sheng);
			}
            if (!shi.contains("00")) {
                sb.append(shi);
			}
            if (!xq.contains("00")) {
                sb.append(xq);
			}
            if (!jd.contains("000")) {
                sb.append(jd);
			}
			return sb.toString();
		}else{
			return qhdm;
		}
	}
	/**
	 * 处理行政区划代码
	 * 将行政区划代码补齐九位
	 */
	public static String handleXZQH2Nine(String qhdm){
		StringBuffer sb = new StringBuffer();
		int len = qhdm.length();
		if(len != 9){
			sb.append(qhdm);
			for (int i = 0; i < 9 - len; i++) {
				sb.append("0");
			}
			return sb.toString();
		}else{
			return qhdm;
		}
	}
	/**
	 * 处理行政区划代码
	 * 将行政区划代码补齐6位
	 */
	public static String handleXZQH2Six(String qhdm){
		StringBuffer sb = new StringBuffer();
		int len = qhdm.length();
		if(len < 6){
			sb.append(qhdm);
			for (int i = 0; i < 6 - len; i++) {
				sb.append("0");
			}
			return sb.toString();
		}else{
			return qhdm.substring(0,6);
		}
	}
	
	/**
	 * 生成sql select语句中的regioncode
	 * @param regionCode 行政区划代码，9位
	 * @param regionCol 表中的根据行政区划查询的列名
     * @return String
     */
	public static String generateSelectRegionCode(String regionCode,String regionCol){
		String str = handleXZQH(regionCode);
		switch (str.length()) {
		case 2:
			return "substr("+regionCol+",1,4)||'00000'";
		case 4:
			return "substr("+regionCol+",1,6)||'000'";
		case 6:
			return "substr("+regionCol+",1,9)";
		default:
			return "substr("+regionCol+",1,9)";
		}
	}
	
	/**
	 * 生成sql where like语句中的regioncode
	 * @param regionCode 行政区划代码，9位
     * @return String
     */
	public static String generateWhereLikeRegionCode(String regionCode){
		String str = handleXZQH(regionCode);
		switch (str.length()) {
		case 2:
			return "'"+str+"%00000'";
		case 4:
			return "'"+str+"%000'";
		case 6:
			return "'"+str+"%'";
		default:
			return str;
		}
	}
	
	/**
	 * 生成sql where <>语句中的regioncode
	 * @param regionCode 行政区划代码，9位
     * @return String
     */
	public static String generateWhereNQRegionCode(String regionCode){
		String str = handleXZQH(regionCode);
		switch (str.length()) {
		case 2:
			return "'"+str+"0000000'";
		case 4:
			return "'"+str+"00000'";
		case 6:
			return "'"+str+"000'";
		default:
			return str;
		}
	}
	
	/**
	 * 生成sql group by语句中的regioncode
	 * @param regionCode 行政区划代码，9位
     * @return String
     */
	public static String generateGroupByRegionCode(String regionCode,String regionCol){
		String str = handleXZQH(regionCode);
		switch (str.length()) {
		case 2:
			return "substr("+regionCol+",1,4)";
		case 4:
			return "substr("+regionCol+",1,6)";
		case 6:
			return "substr("+regionCol+",1,9)";
		default:
			return "substr("+regionCol+",1,9)";
		}
	}
	
	/**
	 * 系统生成标识管理相对人的唯一编号
	 * 推荐使用S18，管理相对人所属行政区划（精确至区县，六位）的编码和系统日期（六位）、系统自动生成的流水号（六位)、两位随机码
	 * @param qhdm 地区编码 （精确至区县，六位）
	 */
	public static String generateCOMP_NO(int qhdm){
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(qhdm).substring(0, 6));
		String dateStr = dataFormat(new Date(),"yyMMdd");
		sb.append(dateStr);
		String timeStr = dataFormat(new Date(),"HHmmss");
//		timeStr=timeStr.substring(timeStr.length()-9,timeStr.length()-3);
		sb.append(timeStr);
		//------------------防止重复最后追加两位随机数-------------2014/05/16---曹加入---------------
		sb.append(getRandomNum(2));
		return sb.toString();
	}
	
	/**
	 * 系统生成标识案件查处编号的唯一编号
	 * 推荐使用S18，管理相对人所属行政区划（精确至区县，六位）的编码和系统日期（六位）、系统自动生成的流水号（六位）
	 * @param qhdm 地区编码 （精确至区县，六位）
	 */
	public static String generateDIS_ID(int qhdm){
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(qhdm).substring(0, 6));
		String dateStr = dataFormat(new Date(),"yyMMdd");
		sb.append(dateStr);
		String timeStr = dataFormat(new Date(),"HHmmss");
//		timeStr=timeStr.substring(timeStr.length()-9,timeStr.length()-3);
		sb.append(timeStr);
		return sb.toString();
	}
	
	/** 
     * 获取指定位数的随机数(纯数字) 
     * @param length 随机数的位数 
     * @return String 
     */  
    public static String getRandomNum(int length) {  
        if (length <= 0) {  
            length = 1;  
        }  
        StringBuilder res = new StringBuilder();  
        Random random = new Random();  
        int i = 0;  
        while (i < length) {  
            res.append(random.nextInt(10));  
            i++;  
        }  
        return res.toString();  
    }  
	
	/**
	 * 系统生32位唯一编号
	 */
	public static String generateID(){
		String uuid=java.util.UUID.randomUUID().toString();
		return uuid.substring(uuid.length()-32);
	}
	/**
	 *生成一个时间戳字符串，用于附件上传 
	 * @return 时间戳字符串，例如“20130101090101”
	 */
	public static String generateTimeStamp(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");   
		Date now = new Date(); 
		return sdf.format(now);
	}
	
	/**
	 * 生成一个当前时间字符串，格式是"yyyy-MM-dd HH:mm:ss"
	 * @return 当前时间字符串
	 */
	public static String getTimeOfNow(){
		Date now = new Date();
		return formatDate(now, "yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 返回一个当前日期的字符串，格式是yyyy-MM-dd
	 * @return
	 */
	public static String getDateOfNow(){
		Date today = new Date();
		return formatDate(today, "yyyy-MM-dd");
	}
	
	/**
	 * 返回一个当前日期的字符串，格式是yyyy年MM月dd日
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getDateOfNow2(){
		Date today = new Date();
		String year = String.valueOf(today.getYear()+1900);
		int mm = today.getMonth()+1;
		String month = String.valueOf(mm);
		if(mm<10){
			month = "0"+month;
		}
		int dd = today.getDate();
		String day = String.valueOf(dd);
		if(dd<10){
			day = "0"+day;
		}
		String time = year+"年  "+month+"月  "+day+"日";
		return time;
	}
	
	public static String formatDate(Date date, String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);   
		return sdf.format(date);
	}
	
	/**
	 * 生成附件上传的唯一标示，每个pojo类统一字段FJUPLOADID
     * @return String
     */
	public static String generateFJUploadID(){
		return ConvertUtil.generateID();
	}
	
	/**
	 * 生成报告卡头
	 */
	public static String prepBGKXML(String bizType,String speCode,
			String operate,String regionCode,String sendTime,String sendCode){
        StringBuilder sb = new StringBuilder();
        sb.append("<DSCR>\n");
		sb.append("<BIZTYPE>"+bizType+"</BIZTYPE>\n");
		sb.append("<SPECODE>"+speCode+"</SPECODE>\n");
		sb.append("<OPERATE>"+operate+"</OPERATE>\n");
		sb.append("<REGIONCODE>"+regionCode+"</REGIONCODE>\n");
		sb.append("<SENDTIME>"+sendTime+"</SENDTIME>\n");
		sb.append("<VERSIONS>2012000001</VERSIONS>\n");
		sb.append("<SENDCODE>"+sendCode+"</SENDCODE>\n");
		sb.append("</DSCR>\n");
		return sb.toString();
	}
	
	/**
	 * 获取当前日期前一个月
	 */
	public static String getForwardOneMon(String date){
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(toParseDate(date));
			//calendar.add(Calendar.DATE, -1); // 得到前一天
			calendar.add(Calendar.MONTH, -1); // 得到前一个月
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			int day= calendar.get(Calendar.DAY_OF_MONTH);
			return String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 许可证里取流水号
     * @param hEALTH_LICENSE hEALTH_LICENSE
     * @return 许可证里取流水号
     */
	public static String getLicenseNum(String hEALTH_LICENSE) {
		return getLicenseNum(hEALTH_LICENSE, "第", "号");
	}
	
	/**
	 * 许可证里取流水号
     * @param hEALTH_LICENSE hEALTH_LICENSE
     * @return hEALTH_LICENSE
     */
	private static String getLicenseNum(String hEALTH_LICENSE, String begin,String end) {
		if(hEALTH_LICENSE == null) return "";
		
		int beginIndex = hEALTH_LICENSE.lastIndexOf(begin);
		int endIndex = hEALTH_LICENSE.lastIndexOf(end);
		
		if(beginIndex==-1 || endIndex==-1 || beginIndex > endIndex){
			return "";
		}
		
		return hEALTH_LICENSE.substring(beginIndex+1, endIndex);
	}
	
	/**
	 * 转化为字符串（可设定小数点位数以及舍入模式）
	 * @param value
	 * @param maxFraDigits
	 * @param roundingMode
	 * @return
	 */
	public static String toString(Double value, int maxFraDigits, RoundingMode roundingMode) {
		if(value == null) return "0";
		DecimalFormat df = new DecimalFormat("0.#");
		df.setRoundingMode(roundingMode);
		df.setMaximumFractionDigits(maxFraDigits);
		String result = df.format(value);
		return result;
	}
	/**
	 * 转化为字符串（可设定小数点位数以及舍入模式）
	 * @param value
	 * @param maxFraDigits
	 * @param roundingMode
	 * @return
	 */
	public static String toString(Integer value, int maxFraDigits, RoundingMode roundingMode) {
		if(value == null) return "0";
		DecimalFormat df = new DecimalFormat("0.#");
		df.setRoundingMode(roundingMode);
		df.setMaximumFractionDigits(maxFraDigits);
		String result = df.format(value);
		return result;
	}
	
	/**
	 * 为空时，取第二个值
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static Date nvl(Date value, Date defaultValue) {
		return (value!=null?value:defaultValue);
	}
	
	/**
	 * 转换为大写字符
	 * @param value
	 * @return
	 */
	public static String toUpperCase(String value){
		return (value != null ? value.toUpperCase():"");
	}
	
	/**
	 * 日期计算，加碱年或曰或日
	 * @param type Y:年 M 月 D 日
	 * @param date
	 * @return
	 */
	public static Date addDate(char type, Date date, int offset){
		int nType = 0;
		
		if(type == 'Y') nType = Calendar.YEAR;
		else if(type == 'M') nType = Calendar.MONTH;
		else if(type == 'D') nType = Calendar.DATE;
		else throw new RuntimeException("类型不支持[" + type + "]");
		
	    Calendar calendar = Calendar.getInstance();   
	    calendar.setTime(date);
	    calendar.add(nType, offset);
	    
	    date = calendar.getTime();
		return date;
	}
	
	/**
	 * 日期计算，同时加碱年曰日
	 * @return
	 */
	public static Date addDate(Date date, int year, int month, int day){
		
	    Calendar calendar = Calendar.getInstance();   
	    calendar.setTime(date);
	    
	    if(year != 0 ) calendar.add(Calendar.YEAR, year);
	    if(month != 0 ) calendar.add(Calendar.MONTH, month);
	    if(day != 0 ) calendar.add(Calendar.DATE, day);
	    
	    date = calendar.getTime();
		return date;
	}

	/**
	 * 消毒产品生产企业许可证格式化
	 */
	public static String formatXdcpLicense(String commaValues) {
		try {
			String[] values = commaValues.split(",");
			return String.format("%s卫消证字[%s]第%s号", values[0].trim(), values[1].trim(), values[2].trim());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return commaValues;
	}

	/**
	 * 餐饮具集中消毒单位许可证格式化
	 */
	public static String formatCyjxdLicense(String commaValues) {
		try {
			String[] values = commaValues.split(",");
			return String.format("%s卫餐消证字[%s]第%s号", values[0].trim(), values[1].trim(), values[2].trim());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return commaValues;
	}
	
	/**
	 * sql单引号处理
	 * @param value
	 * @return
	 */
	public static String escapeSQL(String value) {
		return SQLUtil.escapeSQL(value);
	}
	
	/**
	 * 经济类型
	 */
	public static String toECONOMIC_CODE(Object str){
		String strValue = (str == null?"":str.toString());
		if("11".equals(strValue)) strValue = "国有全资";
		if("12".equals(strValue)) strValue = "集体全资";
		if("13".equals(strValue)) strValue = "股份合作";
		if("14".equals(strValue)) strValue = "联营";
		if("15".equals(strValue)) strValue = "有限责任（公司）";
		if("16".equals(strValue)) strValue = "股份有限（公司）";
		if("17".equals(strValue)) strValue = "私有";
		if("19".equals(strValue)) strValue = "其它内资";
		if("20".equals(strValue)) strValue = "港、澳、台投资";
		if("21".equals(strValue)) strValue = "内地和港澳台合资";
		if("22".equals(strValue)) strValue = "内地和港澳台合作";
		if("23".equals(strValue)) strValue = "港、澳、台独资";
		if("24".equals(strValue)) strValue = "港澳台投资股份有限（公司）";
		if("29".equals(strValue)) strValue = "其它港澳台投资";
		if("30".equals(strValue)) strValue = "国外投资";
		if("31".equals(strValue)) strValue = "中外合资";
		if("32".equals(strValue)) strValue = "中外合作";
		if("33".equals(strValue)) strValue = "外资";
		if("34".equals(strValue)) strValue = "国外投资股份有限（公司）";
		if("39".equals(strValue)) strValue = "其它国外投资";
		if("41".equals(strValue)) strValue = "个体";
		if("90".equals(strValue)) strValue = "其他";
		return strValue;
	}
	/**
	 *消毒产品生产企业 产品种类 转换 
	 */
	public static String toXDCP_PRODUCT_TYPE(String code){
		if(code == null) return "";
		code = code.trim();
		
		if("070101".equals(code)) return "消毒剂";
		if("070102".equals(code)) return "消毒器械";
		if("070103".equals(code)) return "卫生用品";
		return "";
	}
	/**
	 *消毒产品生产企业 兼营项目 转换 
	 */
	public static String toSEC_PRODUCT_TYPE(String str){
		String strValue = (str == null?"":str.toString());
		if("070101".equals(strValue)) strValue = "消毒剂";
		if("070102".equals(strValue)) strValue = "消毒器械";
		if("07010215".equals(strValue)) strValue = "生物指示物";
		if("07010216".equals(strValue)) strValue = "化学指示物";
		if("07010217".equals(strValue)) strValue = "灭菌包装物";
		if("07010301".equals(strValue)) strValue = "纸巾（纸）";
		if("07010302".equals(strValue)) strValue = "卫生巾/护垫/尿布等排泄物卫生用品";
		if("07010303".equals(strValue)) strValue = "纸质餐饮具";
		if("07010304".equals(strValue)) strValue = "抗（抑）菌制剂";
		if("07010305".equals(strValue)) strValue = "隐形眼镜护理用品";
		if("07010306".equals(strValue)) strValue = "卫生棉/化妆棉";
		if("07010307".equals(strValue)) strValue = "湿巾/卫生湿巾";
		if("07010308".equals(strValue)) strValue = "手（指）套";
		return strValue;
	}
	/**
	 * 消毒产品生产企业 主营项目转换
	 */
	public static String toMAIN_PRODUCT_TYPE(String str){
		String strValue = (str == null?"":str.toString());
		if("070101".equals(strValue)) strValue = "消毒剂";
		if("070102".equals(strValue)) strValue = "消毒器械";
		if("07010215".equals(strValue)) strValue = "生物指示物";
		if("07010216".equals(strValue)) strValue = "化学指示物";
		if("07010217".equals(strValue)) strValue = "灭菌包装物";
		if("07010301".equals(strValue)) strValue = "纸巾（纸）";
		if("07010302".equals(strValue)) strValue = "卫生巾/护垫/尿布等排泄物卫生用品";
		if("07010303".equals(strValue)) strValue = "纸质餐饮具";
		if("07010304".equals(strValue)) strValue = "抗（抑）菌制剂";
		if("07010305".equals(strValue)) strValue = "隐形眼镜护理用品";
		if("07010306".equals(strValue)) strValue = "卫生棉/化妆棉";
		if("07010307".equals(strValue)) strValue = "湿巾/卫生湿巾";
		if("07010308".equals(strValue)) strValue = "手（指）套";
		return strValue;
	}
	/**
	 * 消毒产品经营单位产品类别转换
	 */
	public static String toCPLB(String str){
		String strValue = (str == null?"":str.toString());
		if("07010101".equals(strValue)) strValue = "粉剂";
		if("07010102".equals(strValue)) strValue = "片剂";
		if("07010103".equals(strValue)) strValue = "颗粒剂";
		if("07010104".equals(strValue)) strValue = "液体";
		if("07010105".equals(strValue)) strValue = "喷雾剂";
		if("07010106".equals(strValue)) strValue = "凝胶";
		if("07010201".equals(strValue)) strValue = "压力蒸汽灭菌器";
		if("07010202".equals(strValue)) strValue = "环氧乙烷灭菌器";
		if("07010203".equals(strValue)) strValue = "戊二醛灭菌柜";
		if("07010204".equals(strValue)) strValue = "等离子体灭菌器";
		if("07010205".equals(strValue)) strValue = "臭氧消毒柜";
		if("07010206".equals(strValue)) strValue = "电热消毒柜";
		if("07010207".equals(strValue)) strValue = "静电空气消毒机";
		if("07010208".equals(strValue)) strValue = "紫外线杀菌灯";
		if("07010209".equals(strValue)) strValue = "紫外线消毒器";
		if("07010210".equals(strValue)) strValue = "甲醛消毒器";
		if("07010211".equals(strValue)) strValue = "酸性氧化电位水生成器";
		if("07010212".equals(strValue)) strValue = "次氯酸钠发生器";
		if("07010213".equals(strValue)) strValue = "二氧化氯发生器";
		if("07010214".equals(strValue)) strValue = "臭氧发生器、臭氧水发生器";
		if("07010215".equals(strValue)) strValue = "生物指示物";
		if("07010216".equals(strValue)) strValue = "化学指示物";
		if("07010217".equals(strValue)) strValue = "灭菌包装物";
		if("07010299".equals(strValue)) strValue = "其他消毒器械";
		if("07010301".equals(strValue)) strValue = "纸巾（纸）";
		if("07010302".equals(strValue)) strValue = "卫生巾/护垫/尿布等排泄物卫生用品";
		if("07010303".equals(strValue)) strValue = "纸质餐饮具";
		if("07010304".equals(strValue)) strValue = "抗（抑）菌制剂";
		if("07010305".equals(strValue)) strValue = "隐形眼镜护理用品";
		if("07010306".equals(strValue)) strValue = "卫生棉/化妆棉";
		if("07010307".equals(strValue)) strValue = "湿巾/卫生湿巾";
		if("07010308".equals(strValue)) strValue = "手（指）套";
		return strValue;
	}
	/**
	 * 其他传染病防治 单位类别转换
	 */
	public static String toCOMP_TYPE(String COMP_TYPE,String YLJG_INFO, String HOSPTIAL_LEVEL,String JBYFKZJG_ADDR){
		String comp_type = (COMP_TYPE == null?"":COMP_TYPE.toString());                //单位类别
		String yljg_info = (YLJG_INFO == null?"":YLJG_INFO.toString());                //机构信息
		String hosptial_level = (HOSPTIAL_LEVEL == null?"":HOSPTIAL_LEVEL.toString()); //医院等级
		String jbyfkzjg_addr = (JBYFKZJG_ADDR == null?"":JBYFKZJG_ADDR.toString());    //疾病预防控制机构
		
		if("0703".equals(comp_type)){
			if("070301".equals(jbyfkzjg_addr)) comp_type = "省级疾病预防控制机构";
			if("070302".equals(jbyfkzjg_addr)) comp_type = "市级疾病预防控制机构";
			if("070303".equals(jbyfkzjg_addr)) comp_type = "县级疾病预防控制机构";
			if("".equals(jbyfkzjg_addr)) comp_type = "疾病预防控制机构";
		}
		if("06".equals(comp_type)){
			if("0601".equals(yljg_info)) yljg_info = "医院";
			if("0602".equals(yljg_info)) yljg_info = "妇幼保健院";
			if("0603".equals(yljg_info)) yljg_info = "社区卫生服务机构";
			if("0604".equals(yljg_info)) yljg_info = "卫生院";
			if("0605".equals(yljg_info)) yljg_info = "疗养院";
			if("0606".equals(yljg_info)) yljg_info = "门诊部";
			if("0607".equals(yljg_info)) yljg_info = "诊所";
			if("0608".equals(yljg_info)) yljg_info = "村卫生室（所）";
			if("0609".equals(yljg_info)) yljg_info = "急救中心（站）";
			if("0610".equals(yljg_info)) yljg_info = "临床检验中心";
			if("0611".equals(yljg_info)) yljg_info = "专科疾病防治机构";
			if("0612".equals(yljg_info)) yljg_info = "护理院（站）";
			if("0613".equals(yljg_info)) yljg_info = "健康体检机构";
			if("0699".equals(yljg_info)) yljg_info = "其他";
			
			if("1".equals(hosptial_level)) hosptial_level = "三甲";
			if("2".equals(hosptial_level)) hosptial_level = "三乙";
			if("3".equals(hosptial_level)) hosptial_level = "二甲";
			if("4".equals(hosptial_level)) hosptial_level = "二乙";
			if("5".equals(hosptial_level)) hosptial_level = "一级及以下";
			hosptial_level = "".equals(hosptial_level)?"":("医院等级为"+hosptial_level);
			comp_type = "医疗机构："+hosptial_level+yljg_info;
		}
		if("08".equals(comp_type)) comp_type = "采供血机构";
		if("01".equals(comp_type)) comp_type = "公共场所";
		if("02".equals(comp_type)) comp_type = "生活饮用水";
		if("05".equals(comp_type)) comp_type = "学校";
		if("0799".equals(comp_type)) comp_type = "其他有关单位";
		if("0798".equals(comp_type)) comp_type = "个人";
		return comp_type;
	}
	/**
	 * 统计时 专业类别编码号  转变为 中文 
	 */
	public static String toLocal_BizType(String local_bizType){
		String strValue = (local_bizType == null?"":local_bizType.toString());
		if("0109".equals(strValue)) local_bizType = "沐浴场所";
		if("0118".equals(strValue)) local_bizType = "游泳场所";
		if("0191".equals(strValue)) local_bizType = "住宿场所";
		if("0192".equals(strValue)) local_bizType = "理发美容场所";
		if("0193".equals(strValue)) local_bizType = "其他公共场所";
		if("0201".equals(strValue)) local_bizType = "集中式供水";
		if("0202".equals(strValue)) local_bizType = "二次供水";
		if("0203".equals(strValue)) local_bizType = "涉水产品生产企业";
		if("0204".equals(strValue)) local_bizType = "涉水产品经营单位";
		if("0301".equals(strValue)) local_bizType = "化学品毒性鉴定机构";
		if("0302".equals(strValue)) local_bizType = "放射卫生技术服务机构";
		if("0303".equals(strValue)) local_bizType = "职业健康检查机构";
		if("0304".equals(strValue)) local_bizType = "职业病诊断机构";
		if("0400".equals(strValue)) local_bizType = "放射卫生";
		if("0500".equals(strValue)) local_bizType = "学校卫生";
		if("0701".equals(strValue)) local_bizType = "消毒产品生产企业";
		if("0704".equals(strValue)) local_bizType = "餐饮具集中消毒单位";
		if("0799".equals(strValue)) local_bizType = "其他传染病防治监督";
		if("0600".equals(strValue)) local_bizType = "医疗机构";
		if("0608".equals(strValue)) local_bizType = "采供血机构";
		if("0628".equals(strValue)) local_bizType = "无证行医";
		if("9000".equals(strValue)) local_bizType = "建设项目";
		if("9291".equals(strValue)) local_bizType = "机构信息";
		if("9292".equals(strValue)) local_bizType = "部门信息";
		if("9293".equals(strValue)) local_bizType = "人员信息";
		return local_bizType;
	}
	/**
	 * 统计时 业务类型编码 转变为中文
	 */
	public static String toBizType(String BizType){
		String strValue = (BizType == null?"":BizType.toString());
		if("02".equals(strValue)) BizType = "基本信息";
		if("03".equals(strValue)) BizType = "监督信息";
		if("05".equals(strValue)) BizType = "监测信息";
		if("04".equals(strValue)) BizType = "行政处罚信息";
		if("11".equals(strValue)) BizType = "数据字典";
		return BizType;
	}
	
	/**
	 * 统计时 将专业类型代码转换为  中文表示形式
	 */
	public static String toZhuanYe(String zhuanye){
		String strValueString = (zhuanye == null?"":zhuanye.toString());
		if("01".equals(strValueString)) zhuanye="公共场所卫生";
		if("02".equals(strValueString)) zhuanye="生活饮用水卫生";
		if("03".equals(strValueString)) zhuanye="职业卫生";
		if("04".equals(strValueString)) zhuanye="放射卫生";
		if("05".equals(strValueString)) zhuanye="学校卫生";
		if("06".equals(strValueString)) zhuanye="医疗卫生";
		if("07".equals(strValueString)) zhuanye="传染病防治管理";
		if("90".equals(strValueString)) zhuanye="建设项目";
		return zhuanye;
	}
	
	/**
	 * 公共场所主兼营代码名称转换
	 */
	public static String toGgcsCompTypeName(String code){
		String typeName = "";
		if("0101".equals(code)) typeName = "宾馆";
		if("0102".equals(code)) typeName = "饭馆";
		if("0103".equals(code)) typeName = "旅店";
		if("0104".equals(code)) typeName = "招待所";
		if("0105".equals(code)) typeName = "车马店";
		if("0106".equals(code)) typeName = "咖啡馆";
		if("0107".equals(code)) typeName = "酒吧";
		if("0108".equals(code)) typeName = "茶座";
		if("0109".equals(code)) typeName = "公共浴室";
		if("0110".equals(code)) typeName = "理发店";
		if("0111".equals(code)) typeName = "美容店";
		if("0112".equals(code)) typeName = "影剧院";
		if("0113".equals(code)) typeName = "录像厅（室）";
		if("0114".equals(code)) typeName = "游艺厅（室）";
		if("0115".equals(code)) typeName = "舞厅";
		if("0116".equals(code)) typeName = "音乐厅";
		if("0118".equals(code)) typeName = "游泳场（馆）";
		if("0120".equals(code)) typeName = "展览馆";
		if("0121".equals(code)) typeName = "博物馆";
		if("0122".equals(code)) typeName = "美术馆";
		if("0123".equals(code)) typeName = "图书馆";
		if("0124".equals(code)) typeName = "商场（店）";
		if("0125".equals(code)) typeName = "书店";
		if("0126".equals(code)) typeName = "候诊室";
		if("0127".equals(code)) typeName = "候车（机、船）室";
		return typeName;
	}
	
	/**
	 * 处理量化等级
	 * @param type
	 * @return
	 */
	public static String getLhfj(Map<String, Object> entity, String type) {
		String level = null;
		String compLevels = (String)entity.get("COMP_LEVEL");
		if (Nulls.isNotEmpty(compLevels)) {
			String[] COMP_LEVELs = compLevels.split(",");
			for (String comp_level : COMP_LEVELs) {
				comp_level = comp_level.trim();
				if(Nulls.isEmpty(comp_level)) continue;
				String[] temp = comp_level.split(":");
				if (type.equals(temp[0])) {
					level = temp[1];
					break;
				}
			}
		} else {
			level = "09"; // 默认为未分级
		}
		return level;
	}
	
	/**
	 * 许可类别代码名称转换
	 */
	public static String toRequestTypeName(String code){
		String typeName = "";
		if("1".equals(code)) typeName = "新发";
		if("2".equals(code)) typeName = "变更";
		if("3".equals(code)) typeName = "延续";
		if("4".equals(code)) typeName = "注销";
		return typeName;
	}
	
	/**
	 * 数据库的CLOB转换为字符串
	 * @param clob
	 * @return
	 * @throws Exception
	 */
	public static String toString(Clob clob) throws Exception {
		if(clob == null) return "";
		Reader is = clob.getCharacterStream();
		BufferedReader br = new BufferedReader(is);
		StringBuffer sb = new StringBuffer();
		String s;
		while ((s=br.readLine()) != null) {
			sb.append(s);
		}
		return sb.toString();
	}
	/**
	 * 数字类型的转换为字符型
	 * @param obj
	 * @return
	 */
	public static String toString(Number obj){
		if(obj == null){
			return null;
		}
		return obj.toString();
	}
	/**
	 * 取固定长度，不足右边补字符
	 * @param value
	 * @param len
	 * @param defaultChar
	 * @return
	 */
	public static String rfix(String value, int len, char defaultChar) {
		value = (value == null ? "": value);
		if(value.length()==len) return value;
		if(value.length()>len) return value.substring(0, len);
		
		StringBuilder buff = new StringBuilder(value);
		for(int i=value.length(); i<len; i++) buff.append(defaultChar);
		return buff.toString();
	}
	/**
	 * 转换为下载显示文件名（为了避免乱码）
	 * @return
	 */
	public static String toDownloadFileName(String filename) {
		try {
			return new String(filename.getBytes(),"ISO8859-1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 学校类别
	 * @param str
	 * @param nine
	 * @param gaoji
	 * @return
	 */
	public static String toCompType(Object str,Object nine,Object gaoji){
		String strValue = (str == null ? "" : str.toString());
		if ("0501".equals(strValue)) strValue = "小学";
		if ("0502".equals(strValue) && nine == null) strValue = "初级中学";
		if (nine != null && "050201".equals(nine)) strValue = "初级中学(九年一贯制)";
		if ("0503".equals(strValue) && gaoji == null) strValue = "高级中学";
		if (gaoji != null && "050301".equals(gaoji)) strValue = "高级中学(十二年一贯制)";
		if (gaoji != null && "050302".equals(gaoji)) strValue = "高级中学(完全中学)";
		if (gaoji != null && "050303".equals(gaoji)) strValue = "高级中学(职业中学)";
		if ("0504".equals(strValue)) strValue = "普通高校";
		return strValue;
	}
	
	/**
	 * 学校办学性质
	 * @param str
	 * @return
	 */
	public static String toPROPERTY(Object str){
		String strValue = (str == null?"":str.toString());
		if("01".equals(strValue)) strValue = "公办";
		if("02".equals(strValue)) strValue = "民办";
		if("03".equals(strValue)) strValue = "其他";
		return strValue;
	}
	
	/**
	 * 学校所在区域
	 * @param code
	 * @return
	 */
	public static String toXxwsSzqy(String code){
		String typeName = (code == null?"":code.toString());
		if("01".equals(typeName)) typeName = "城区";
		if("02".equals(typeName)) typeName = "镇区";
		if("03".equals(typeName)) typeName = "乡村";
		return typeName;
	}
	
	public static String toXxwsSzqy(Object code){
		String typeName = (code == null?"":code.toString());
		if("01".equals(typeName)) typeName = "城区";
		if("02".equals(typeName)) typeName = "镇区";
		if("03".equals(typeName)) typeName = "乡村";
		return typeName;
	}
	
	/**
	 * 导出word模板时，判断数据 
	 * @param value
	 * @return
	 */
	public static String toYesOrNo(Object value){
		String strValue = (value == null?"":value.toString());
		if ("1".equals(strValue)) 
			strValue= "√";
		if ("0".equals(strValue)) 
			strValue= "╳";
		if ("9".equals(strValue)) 
			strValue= "缺项";
		return strValue;
	}
	
	/**
	 * 返回两个相同类的不同对象属性值不同的属性
     * @param clzzOld 待比较的对象1
     * @param clzzNew 待比较的对象2
     * @return String   AA/BB/CC
	 * @author cao.yazhen
	 */
	public static String compareObj(Object clzzOld,Object clzzNew){
		StringBuilder sb = new StringBuilder();
		String[] fields = ReflectionUtils.getFieldName(clzzOld);
		for (String feild:fields) {
			Object oldFeildValue = ReflectionUtils.getFieldValue(clzzOld, feild);
			Object newFeildValue = ReflectionUtils.getFieldValue(clzzNew, feild);
			//如果新的对象属性不为空的话，在做比较，因为新对象是在老对象的基础上合并的，所以不用考虑新对象为空而老对象不为空的情况
			if (Nulls.isNotEmpty(newFeildValue)) {
				if (!String.valueOf(newFeildValue).equals(String.valueOf(oldFeildValue))) {
					sb.append(feild);
					sb.append("/");
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 左侧不补齐
	 * @param s 原始字符串
	 * @param n 补齐位数
	 * @param replace 代替字符
	 * @return
	 */
	public static String lPad(String s, int n, String replace) {
		while (s.length() < n) {
			s = replace + s;
		}
		return s;
	}
	/**
	 * 右侧不补齐
	 * @param s 原始字符串
	 * @param n 补齐位数
	 * @param replace 代替字符
	 * @return
	 */
	public static String rRad(String s, int n, String replace) {
		while (s.length() < n) {
			s = s + replace;
		}
		return s;
	}

	/**
	 * 根据量化得分计算信誉度等级计算<br/>
	 * A:86—100; B:70—85; C:60—69
	 * @param lhdf
	 * @return
	 */
	public static Integer toXYDDJ(Double lhdf) {
		if(lhdf == null || lhdf < 60) return 4;
		if(lhdf >= 60 || lhdf < 70) return 3;
		if(lhdf >= 70 || lhdf < 86) return 2;
		if(lhdf >= 86) return 1;
		return 4;
	}
	
	public static String toYesShow(Object src){
		String srcValue = ((src == null||src=="") ? "" : src.toString());
		if ("1".equals(srcValue)) {
			srcValue="是";
		}
		if ("0".equals(srcValue)) 
			srcValue= "否";
		return srcValue;	
	}
	
	public static String toWSPJ(Object src){
		String srcValue = ((src == null||src=="") ? "" : src.toString());
		if ("1".equals(srcValue)) srcValue="优秀";
		if ("2".equals(srcValue)) srcValue="合格";
		if ("3".equals(srcValue)) srcValue="不合格";
		return srcValue;	
	}
	
	public static String toNvl(Object src){
        return ((src == null || src == "") ? "0" : src.toString());
    }
	
	public static String toBgkCompType(Object local_bizType){
		String strValue = ((local_bizType == null||"".equals(local_bizType))?"—":local_bizType.toString());
		if("1000".equals(strValue)) local_bizType = "公共场所卫生";
		if("1100".equals(strValue)) local_bizType = "学校卫生";
		if("1200".equals(strValue)) local_bizType = "医疗卫生";
		if("1300".equals(strValue)) local_bizType = "爱国卫生";
		if("1400".equals(strValue)) local_bizType = "无证信息";
		if("2000".equals(strValue)) local_bizType = "生活饮用水";
		if("3000".equals(strValue)) local_bizType = "传染病防治";
		if("6000".equals(strValue)) local_bizType = "职业卫生";
		if("7001".equals(strValue)) local_bizType = "放射卫生";
		if("8000".equals(strValue)) local_bizType = "采供血卫生";
		if("1001".equals(strValue)) local_bizType = "住宿场所";
		if("1005".equals(strValue)) local_bizType = "游泳场所(管)";
		if("1003".equals(strValue)) local_bizType = "公共浴室";
		if("1004".equals(strValue)) local_bizType = "理发场所";
		if("1006".equals(strValue)) local_bizType = "美容场所";
		if("1002".equals(strValue)) local_bizType = "文化娱乐场所";
		if("1007".equals(strValue)) local_bizType = "展览馆、博物馆、美术管、图书馆";
		if("1001".equals(strValue)) local_bizType = "住宿场所";
		if("1008".equals(strValue)) local_bizType = "商场(店)、书店";
		if("1009".equals(strValue)) local_bizType = "医院侯诊室";
		if("1012".equals(strValue)) local_bizType = "饭馆";
		if("1011".equals(strValue)) local_bizType = "公共交通等候室";
		if("0201".equals(strValue)||"2001".equals(strValue)) local_bizType = "集中式供水单位";
		if("0202".equals(strValue)||"2002".equals(strValue)) local_bizType = "二次供水";
		if("0203".equals(strValue)||"2003".equals(strValue)) local_bizType = "涉水产品生产企业";
		if("0204".equals(strValue)||"2005".equals(strValue)) local_bizType = "涉水产品经营单位";
		if("3001".equals(strValue)) local_bizType = "餐饮具集中消毒单位";
		if("3002".equals(strValue)) local_bizType = "消毒产品生产企业";
		if("3003".equals(strValue)) local_bizType = "消毒产品经营单位";
		if("3004".equals(strValue)) local_bizType = "其他传染病防治管理";
		if("6001".equals(strValue)) local_bizType = "职业健康检查机构";
		if("6002".equals(strValue)) local_bizType = "职业病诊断机构";
		if("6003".equals(strValue)) local_bizType = "放射卫生技术服务机构";
		if("6005".equals(strValue)) local_bizType = "职业病诊断医师";
		return (String)local_bizType;
	}
	
	public static String toOperateName(Object local_bizType){
		String strValue = ((local_bizType == null||"".equals(local_bizType))?"":local_bizType.toString());
		if("1".equals(strValue)) local_bizType = "新增";
		if("2".equals(strValue)) local_bizType = "原有记录基础新增";
		if("3".equals(strValue)) local_bizType = "修改";
		if("4".equals(strValue)) local_bizType = "删除";
		return (String)local_bizType;
	}
	
	/**
	 * 转换性别
	 * 
	 * @param xb xb
     * @return 性别
     */
	public static String toXB(String xb) {
		if (StringUtils.isNotBlank(xb)) {
			if ("1".equals(xb)) {
				xb = "男";
			} else {
				xb = "女";
			}
		} else {
			xb = "未录入性别";
		}
		return xb;
	}

	/**
	 * 转换体检结果
	 * 
	 * @param LB LB
     * @return 体检结果
     */
	public static String toLB(String LB) {
		if (StringUtils.isNotBlank(LB)) {
			if ("1".equals(LB)) {
				LB = "食品卫生";
			} else if ("2".equals(LB)) {
				LB = "公共场所卫生";
			} else if ("3".equals(LB)) {
				LB = "食品/公共场所";
			} else if ("4".equals(LB)) {
				LB = "其他";
			}
		} else {
			LB = "未查到类别";
		}
		return LB;
	}

	public static String toTJJG(String TJJG) {
		if (StringUtils.isNotBlank(TJJG)) {
			if ("1".equals(TJJG)) {
				TJJG = "合格";
			} else if ("0".equals(TJJG)) {
				TJJG = "不合格";
            } else if ("''".equals(TJJG)) {
                TJJG = "办理中";
			}
		} else {
			if(null == TJJG || "".equals(TJJG)) {
				TJJG = "办理中";
			}else {
				TJJG = "未体检";
			}
		}
		return TJJG;
	}
	
	public static String toFBType(String FBType) {
		// 1姓名错误 2性别错误 3类别错误 4体检结果错误 5身份证号错误 6二维码错误 7头像错误
		StringBuffer sb = new StringBuffer();
		if (StringUtils.isNoneBlank(FBType)) {
			String[] str = FBType.split(",");
			if (StringUtils.isNoneBlank(str)) {
				List<String> list = Arrays.asList(str);
                if (!list.isEmpty()) {
                    if (list.contains("1")) {
						sb.append("姓名错误");
						sb.append(",");
					}
					if (list.contains("2")) {
						sb.append("性别错误");
						sb.append(",");
					}
					if (list.contains("3")) {
						sb.append("类别错误");
						sb.append(",");
					}
					if (list.contains("4")) {
						sb.append("体检结果错误");
						sb.append(",");
					}
					if (list.contains("5")) {
						sb.append("身份证号错误");
						sb.append(",");
					}
					if (list.contains("6")) {
						sb.append("二维码错误");
						sb.append(",");
					}
					if (list.contains("7")) {
						sb.append("头像错误");
					}
				}
			}
			return sb.toString();
		} else {
			return "";
		}
	}
}

