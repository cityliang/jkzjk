package com.huntto.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.huntto.entity.FeedBack;

@Mapper
@Repository
public interface FeedBackDao {
	/**
	 * 通过身份证号查询体检机构id
	 * @param idcard 身份证号
	 * @return String 体检机构id
	 */
	List<String> selectTJJGID(String idcard);
	
	/**
	 * 修改反馈数据
	 * @param feedBack
	 * @return
	 */
	int updateFeedBack(FeedBack feedBack);
	/**
	 * 查询所有反馈信息
	 * 
	 * @param feedBack
	 * @return
	 */
	List<FeedBack> selectFeedBack(FeedBack feedBack);

	/**
	 * 通过ID查询反馈信息
	 * 
	 * @param ID ID
	 * @return
	 */
	FeedBack selectFeedBackByID(@Param("ID") String ID);

	/**
	 * 插入反馈数据
	 * 
	 * @param FBType    反馈类型
	 * @param FBContent 反馈内容
	 * @param FeedBack  feedBack
	 * @return
	 */
	int insertFeedBack(FeedBack feedBack);
}
