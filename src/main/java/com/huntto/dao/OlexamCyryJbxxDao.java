package com.huntto.dao;

import com.huntto.entity.CyryQRIMG;
import com.huntto.entity.CyryVo;
import com.huntto.entity.CyryVo1;
import com.huntto.entity.CyryVo3;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OlexamCyryJbxxDao {
	/**
	 * 通过身份证号更新人脸特征码
	 * @param idCard 从业人员身份证号
	 * @param FEATURE 人脸特征码
	 * @return int
	 */
	int updateFEATURE(@Param("idCard")String idCard,@Param("FEATURE")String FEATURE);
	/**
	 * 通过从业人员姓名查询从业人员信息
	 * @param XM 从业人员姓名
	 * @return List
	 */
	List<CyryVo3> getCYRYbyName(String XM);
	
	/**
	 * 通过从业人员姓名查询从业人员信息1
	 * @param XM 从业人员姓名
	 * @return List
	 */
	List<CyryVo1> getCYRYbyName1(String XM);
	
	/**
	 * 通过从业人员ID查询体检机构名称
	 * @param cyry_id 从业人员ID
	 * @return 体检机构名称
	 */
	String selectFZJG(String cyry_id);

	/**
	 * 通过身份证号查询从业人员信息
	 * 
	 * @param idCard 身份证号
	 * @return CyryVo
	 */
	CyryVo1 selectCYRYVO(String idCard);

	List<CyryVo> selectCYRYVo(String FZSJ);
	List<CyryVo1> selectCYRYVo1(String idCard);

	/**
	 * 通过身份证号查询人员id 和该人员二维码信息
	 * 
	 * @param idCard idCard
	 * @return 人员id 和该人员二维码信息
	 */
	CyryVo1 selectCYRYID(String idCard);

	/**
	 * 通过人员ID 查询该人员二维码
	 * 
	 * @param ID ID
	 * @return 人员二维码
	 */
	List<CyryQRIMG> selectQRCODE1(String ID);
}