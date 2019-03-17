package com.huntto.service.Impl;

import com.huntto.dao.CyryDao;
import com.huntto.dao.OlexamCyryJbxxDao;
import com.huntto.entity.CyryQRIMG;
import com.huntto.entity.CyryVo1;
import com.huntto.service.CyryService;
import com.huntto.util.ConvertUtil;
import com.huntto.util.FileUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CyryServiceImpl implements CyryService {

	private Map map;
	private CyryDao cyryDao;
	private OlexamCyryJbxxDao olexamCyryJbxxDao;

	@Autowired
	public CyryServiceImpl(CyryDao cyryDao,OlexamCyryJbxxDao olexamCyryJbxxDao) {
		this.cyryDao = cyryDao;
		this.olexamCyryJbxxDao = olexamCyryJbxxDao;
	}

	@Override
	public Map selectCYRYVo1(String idCard) {
		Map map = new HashMap();
		List<CyryVo1> list = cyryDao.selectCYRYVo1(idCard);
		if (null != list && !list.isEmpty() && !"[]".equals(list.toString())) {
			for(CyryVo1 cyryVo1 : list) {
				if (null != cyryVo1) {
					// 如果连表查询就打开注释 否则不打开  不要删除
//					map.put("TJJG", ConvertUtil.toTJJG(cyryVo1.getTJJG()));
//					if (StringUtils.isNoneBlank(cyryVo1.getBH())) {
//						map.put("BH", cyryVo1.getBH());
//					} else {
//						map.put("BH", "");
//					}
//					if (StringUtils.isNoneBlank(cyryVo1.getFZJG())) {
//						map.put("FZJG", cyryVo1.getFZJG());
//					} else {
//						map.put("FZJG", "");
//					}
					if (null != cyryVo1.getID()) {
						List<CyryQRIMG> list1 = olexamCyryJbxxDao.selectQRCODE1(cyryVo1.getID());
						if(null != list1 && !list1.isEmpty() && !"[]".equals(list1.toString())) {
							CyryQRIMG QRIMG = list1.get(0);
							if (null != QRIMG) {
								if(null != QRIMG.getQRIMG()) {
									map.put("QRIMG", Base64.encodeBase64String(QRIMG.getQRIMG()));
								}else {
									map.put("QRIMG","");
								}
							}
						}else {
							map.put("QRIMG","");
						}
						
						
						// 如果不连表查询就不要注释下面代码  不要删除
						List<CyryVo1> list2 = cyryDao.selectTJXX(idCard);
						CyryVo1 c = new CyryVo1();
						if(null != list2 && !list2.isEmpty() && !"[]".equals(list2.toString())) {
							c = list2.get(0);
							if (null == cyryVo1.getFZJG() && null != c) {
								if(null != c.getFZJG()) {
									map.put("FZJG", c.getFZJG());
								}else {
									map.put("FZJG", "");
								}
							}else {
								if(null != cyryVo1.getFZJG()) {
									map.put("FZJG",cyryVo1.getFZJG());
								}else {
									map.put("FZJG","");
								}
							}
							if (null == cyryVo1.getBH() && null != c) {
								if(null != c.getBH()) {
									map.put("BH", c.getBH());
								}else {
									map.put("BH", "");
								}
							}else {
								if(null != cyryVo1.getBH()) {
									map.put("BH",cyryVo1.getBH());
								}else {
									map.put("BH","");
								}
							}
							if (null == cyryVo1.getTJJG() && null != c) {
								if(null != c.getTJJG()) {
									map.put("TJJG", c.getTJJG());
								}else {
									map.put("TJJG", "");
								}
							}else {
								if(null != cyryVo1.getTJJG()) {
									map.put("TJJG",cyryVo1.getTJJG());
								}else {
									map.put("TJJG","");
								}
							}
						}
					}
					
					
					if (StringUtils.isNoneBlank(cyryVo1.getXM())) {
						map.put("XM", cyryVo1.getXM());
					} else {
						map.put("XM", "");
					}
					if (StringUtils.isNoneBlank(cyryVo1.getXB())) {
						map.put("XB", ConvertUtil.toXB(cyryVo1.getXB()));
					} else {
						map.put("XB", "");
					}
					if (StringUtils.isNoneBlank(cyryVo1.getLB())) {
						map.put("LB", ConvertUtil.toLB(cyryVo1.getLB()));
					} else {
						map.put("LB", "");
					}
					if (StringUtils.isNoneBlank(cyryVo1.getFZSJ())) {
						map.put("FZSJ", cyryVo1.getFZSJ());
					} else {
						map.put("FZSJ", "");
					}
					if (StringUtils.isNoneBlank(cyryVo1.getIDCARD())) {
						map.put("IDCARD", cyryVo1.getIDCARD());
					} else {
						map.put("IDCARD", "");
					}
					if (null != cyryVo1.getPHOTO()) {
						String PHOTO = Base64.encodeBase64String(cyryVo1.getPHOTO());
						map.put("PHOTO", PHOTO);
					} else {
						map.put("PHOTO", "");
					}
					if (null != cyryVo1.getNL()) {
						map.put("NL", cyryVo1.getNL());
					} else {
						map.put("NL", null);
					}
				}
			}
		}
		return map;
	}

	@Override
	public boolean findLogin(String user_id, String psw) {
		map = new HashMap<>();
		map.put("user_id", user_id);
		map.put("psw", psw);
		List list = cyryDao.findLogin(map);
        return list.size() > 0;
    }

	@Override
	public Map queryPhoto(String FZSJ) {
		map = new HashMap<>();
		List<CyryVo1> list = cyryDao.queryPhoto(FZSJ);
		if (null != list && !list.isEmpty()) {
			for (CyryVo1 c : list) {
				if (null != c) {
					String tarPath = "C:\\STS\\IMG\\psb2.jpg";
					String sourcePath = "C:\\STS\\psb.jpg";
					if (null != c.getPHOTO()) {
						FileUtil.photo2Img(sourcePath, tarPath, c.getPHOTO());
					}
					if (null != c.getQRIMG()) {
						FileUtil.photo2Img(sourcePath, tarPath, c.getQRIMG());
					}
				}
			}
		} else {
			map.put("msg", "未查询到可导出图片");
		}
		return map;
	}
	
}
