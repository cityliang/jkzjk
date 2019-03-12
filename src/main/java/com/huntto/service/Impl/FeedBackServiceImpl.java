package com.huntto.service.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huntto.dao.CyryDao;
import com.huntto.dao.FeedBackDao;
import com.huntto.dao.OlexamCyryJbxxDao;
import com.huntto.entity.CyryVo1;
import com.huntto.entity.FeedBack;
import com.huntto.service.FeedBackService;
import com.huntto.util.ConvertUtil;
import com.huntto.util.DateUtil;
import com.huntto.util.Nulls;
import com.huntto.util.UUIDTool;

@Service
public class FeedBackServiceImpl implements FeedBackService {

	private Map map;
	private FeedBack feedBack;

	@Autowired
	private CyryDao cyryDao;
	@Autowired
	private FeedBackDao feedBackDao;
	@Autowired
	private OlexamCyryJbxxDao olexamCyryJbxxDao;

	public FeedBackServiceImpl() {
	}

	@Override
	public FeedBack selectFeedBackByID(String ID) {
		return feedBackDao.selectFeedBackByID(ID);
	}

	@Override
	public int insertFeedBack(String IDCARD, String FBType, String FBContent) {
		int count = 0;
		List<CyryVo1> list = cyryDao.selectCYRYID(IDCARD);
		if(null != list && !list.isEmpty() && !"[]".equals(list.toString())) {
			CyryVo1 cyryVo1 = list.get(0);
			feedBack = new FeedBack();
			feedBack.setID(UUIDTool.randomNumber());
			feedBack.setFBType(FBType);
			feedBack.setIS_CL("0");
			feedBack.setIDCARD(IDCARD);
			feedBack.setFBContent(FBContent);
			
			if(Nulls.isNotEmpty(cyryVo1)) {
				if(Nulls.isNotEmpty(cyryVo1.getMEDI_INTID())) {
					feedBack.setMEDI_INTID(cyryVo1.getMEDI_INTID());
				}else {
					feedBack.setMEDI_INTID(toTJJGID(IDCARD));
				}
			}else {
				feedBack.setMEDI_INTID(toTJJGID(IDCARD));
			}
			feedBack.setCREATE_TIME(DateUtil.Date2String2(new Date()));
			count = feedBackDao.insertFeedBack(feedBack);
		}	
		return count;
	}
	
	public String toTJJGID(String IDCARD) {
		String MEDI_INTID = "";
		List<String> list1 = feedBackDao.selectTJJGID(IDCARD);
		if(null != list1 && !list1.isEmpty() && !"[]".equals(list1.toString())) {
			MEDI_INTID = list1.get(0);
			if(Nulls.isNotEmpty(MEDI_INTID)) {
				return MEDI_INTID;
			}
		}
		return MEDI_INTID;
	}

	@Override
	public List<FeedBack> selectFeedBack(FeedBack feedBack) {
		List<FeedBack> list = feedBackDao.selectFeedBack(feedBack);
		if (null != list && !list.isEmpty() && !"[]".equals(list.toString())) {
			for (FeedBack f : list) {
				if (null != f) {
					f.setFBType(ConvertUtil.toFBType(f.getFBType()));
				}
			}
			return list;
		}else {
			return new ArrayList<>();
		}
	}

	@Override
	public int updateFeedBack(FeedBack feedBack) {
		feedBack.setUPDATE_TIME(DateUtil.Date2String2(new Date()));
		return feedBackDao.updateFeedBack(feedBack);
	}

}
