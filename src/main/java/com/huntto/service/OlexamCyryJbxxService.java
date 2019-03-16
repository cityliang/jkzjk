package com.huntto.service;

import java.util.List;
import java.util.Map;
import com.huntto.entity.CyryVo1;
import com.huntto.entity.CyryVo3;

public interface OlexamCyryJbxxService {
	
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
	Map getCYRYbyName1(String XM);
	
	/**
	 * 通过从业人员ID查询体检机构名称
	 * @param cyry_id
	 * @return
	 */
	String selectFZJG(String cyry_id);

	/**
	 * 通过身份证号查询人员id
	 * 
	 * @param idCard idCard
	 * @return
	 */
	CyryVo1 selectCYRYID(String idCard);
	
	Map selectCYRYVo1(String idCard);

	/**
	 * 通过身份证号查询二维码
	 * 
	 * @param idCard
	 * @return
	 */
	Map selectCYRYVO(String idCard);
	/**
	 * 人脸识别对比查询二维码
	 * 在线调用 /face/verify
	 * @param NAME
	 * @param PHOTO
	 * @return
	 */
	Map verifyPicture(String NAME, String PHOTO);
	
	/**
	 * 人脸识别对比查询二维码
	 * 离线SDK调用   已弃用
	 * @param NAME
	 * @param PHOTO
	 * @return
	 */
//	@Deprecated
//	Map verifyPicture1(String NAME, String PHOTO);

}