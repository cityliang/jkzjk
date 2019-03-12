package com.huntto.service;

import java.util.List;

import com.huntto.entity.FeedBack;

public interface FeedBackService {
	
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
	 * @param ID
	 * @return
	 */
	FeedBack selectFeedBackByID(String ID);

	/**
	 * 插入反馈数据
	 * 
	 * @param FBType    反馈类型
	 * @param FBContent 反馈内容
	 * @return
	 */
	int insertFeedBack(String IDCARD, String FBType, String FBContent);

}
