package com.huntto.dao;

import com.huntto.entity.FeedBack;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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
     * @param feedBack feedBack
     * @return int
     */
	int updateFeedBack(FeedBack feedBack);
	/**
	 * 查询所有反馈信息
	 *
     * @param feedBack feedBack
     * @return List
     */
	List<FeedBack> selectFeedBack(FeedBack feedBack);

	/**
	 * 通过ID查询反馈信息
	 * 
	 * @param ID ID
     * @return FeedBack
     */
	FeedBack selectFeedBackByID(@Param("ID") String ID);

	/**
	 * 插入反馈数据
	 *
     * FBType    反馈类型
     * FBContent 反馈内容
     * FeedBack  feedBack
     * @return int
     */
	int insertFeedBack(FeedBack feedBack);
}
