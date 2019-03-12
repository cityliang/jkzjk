package com.huntto.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.huntto.entity.CyryQRIMG;
import com.huntto.entity.CyryVo1;

@Mapper
@Repository
public interface CyryDao {
	/**
	 * 通过身份证号获取从业人员健康证信息 
	 * 联表查询
	 * @param idCard 身份证号
	 * @return CyryVo1
	 */
	List<CyryVo1> selectCYRYVo1(String idCard);
	
	/**
	 * 通过身份证号查询从业人员体检信息中 体检结果，发证机构，编号 
	 * 单表查询
	 * @param idCard 身份证号
	 * @return 体检结果 TJJG，发证机构 FZJG，编号 BH
	 */
	List<CyryVo1> selectTJXX(String idCard);
	/**
	 * 通过身份证号查询从业人员健康证信息
	 * @param idCard
	 * @return
	 */
	List<CyryVo1> selectCYRYID(String idCard);
	
	/**
	 * 通过从业人员ID 查询该人员二维码
	 * 单表查询
	 * @param ID ID
	 * @return
	 */
	List<CyryQRIMG> selectQRCODE(String ID);
	
	/**
	 * 判断用户名和密码是不是正确
	 * 
	 * @param map map
	 * @return
	 */
	List findLogin(Map map);

	/**
	 * 同步人员照片和身份证接口 从Oracle数据库导出到本地
	 * 
	 * @return
	 */
	List<CyryVo1> queryPhoto(String FZSJ);
}
