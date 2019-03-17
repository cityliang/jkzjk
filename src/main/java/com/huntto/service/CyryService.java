package com.huntto.service;

import java.util.Map;

public interface CyryService {
	/**
	 * 通过身份证号获取从业人员健康证信息
	 * @param idCard 身份证号
	 * @return CyryVo1
	 */
	Map selectCYRYVo1(String idCard);
	
	
	/**
	 * 判断用户名和密码是不是正确
	 *
     * @param user_id user_id
     * @return
	 */
	boolean findLogin(String user_id, String psw);

	/**
	 * 同步人员照片和身份证接口 从Oracle数据库导出到本地
	 * 
	 * @return
	 */
	Map queryPhoto(String FZSJ);
}
