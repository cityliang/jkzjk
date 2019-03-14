package com.huntto.service.Impl;

import com.alibaba.cloud.faceengine.*;
import com.huntto.dao.OlexamCyryJbxxDao;
import com.huntto.entity.CyryQRIMG;
import com.huntto.entity.CyryVo1;
import com.huntto.entity.CyryVo3;
import com.huntto.service.OlexamCyryJbxxService;
import com.huntto.util.FaceDetectResult;
import com.huntto.util.FaceRecognUtil;
import com.huntto.util.FaceVerifyResult;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OlexamCyryJbxxServiceImpl implements OlexamCyryJbxxService {

    private Logger log = LoggerFactory.getLogger(OlexamCyryJbxxServiceImpl.class);

    private OlexamCyryJbxxDao olexamCyryJbxxDao;
    /**
     * 人脸检测
     */
    private FaceDetect faceDetect = FaceDetect.createInstance(Mode.TERMINAL);
    /**
     * 人脸注册
     */
    private FaceRegister faceRegister = FaceRegister.createInstance();
    /**
     * 人脸识别
     */
    private FaceRecognize faceRecognize = FaceRecognize.createInstance(Mode.TERMINAL);


    @Autowired
    public OlexamCyryJbxxServiceImpl(OlexamCyryJbxxDao olexamCyryJbxxDao) {
        this.olexamCyryJbxxDao = olexamCyryJbxxDao;
    }
    
    @Autowired
	private FaceRecognUtil faceRecognUtil;

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Map selectCYRYVO(String idCard) {
        return getJKZXXbyIdcard(idCard);
    }

    @Override
    public CyryVo1 selectCYRYID(String idCard) {
        return olexamCyryJbxxDao.selectCYRYID(idCard);
    }

    @Override
    public String selectFZJG(String cyry_id) {
        return olexamCyryJbxxDao.selectFZJG(cyry_id);
    }

    @Override
    public Map selectCYRYVo1(String idCard) {
        Map map = new HashMap();
        List<CyryVo1> list = olexamCyryJbxxDao.selectCYRYVo1(idCard.trim());
        if (null != list && !list.isEmpty() && !"[]".equals(list.toString())) {
        	CyryVo1 cyryVo1 = (CyryVo1)list.get(0);
            if (null != cyryVo1) {
                if (StringUtils.isNoneBlank(cyryVo1.getBH())) {
                    map.put("BH", cyryVo1.getBH());
                } else {
                    map.put("BH", "");
                }
                if (StringUtils.isNoneBlank(cyryVo1.getXM())) {
                    map.put("XM", cyryVo1.getXM());
                } else {
                    map.put("XM", "");
                }
                if (StringUtils.isNoneBlank(cyryVo1.getXB())) {
                    map.put("XB", cyryVo1.getXB());
                } else {
                    map.put("XB", "");
                }
                if (StringUtils.isNoneBlank(cyryVo1.getLB())) {
                    map.put("LB", cyryVo1.getLB());
                } else {
                    map.put("LB", "");
                }
                if (StringUtils.isNoneBlank(cyryVo1.getTJJG())) {
                    map.put("TJJG", cyryVo1.getTJJG());
                } else {
                    map.put("TJJG", "");
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
                if (null != cyryVo1.getFZJG()) {
                    map.put("FZJG", cyryVo1.getFZJG());
                } else {
                    map.put("FZJG", "");
                }
                if (cyryVo1.getID() != null) {
                    List<CyryQRIMG> list1 = olexamCyryJbxxDao.selectQRCODE1(cyryVo1.getID());
                    if (null != list1 && !list1.isEmpty() && !"[]".equals(list1.toString())) {
                        CyryQRIMG QRIMG = list1.get(0);
                        if (null != QRIMG) {
                            if (null != QRIMG.getQRIMG()) {
                                map.put("QRIMG", Base64.encodeBase64String(QRIMG.getQRIMG()));
                            } else {
                                map.put("QRIMG", "");
                            }
                        }
                    }
//				String FZJG = olexamCyryJbxxDao.selectFZJG(cyryVo1.getID());
//				if (null == cyryVo1.getFZJG()) {
//					map.put("FZJG", FZJG);
//				}else {
//					map.put("FZJG","");
//				}
                }

                // 优化 单表查询
//			if (null != cyryVo1.getQRIMG()) {
//				String QRIMG = Base64.encodeBase64String(cyryVo1.getQRIMG());
//				map.put("QRIMG", QRIMG);
//			} else {
//				map.put("QRIMG", "");
//			}
                if (null != cyryVo1.getNL()) {
                    map.put("NL", cyryVo1.getNL());
                } else {
                    map.put("NL", null);
                }
            }
        }
        return map;
    }

    @Override
    public List<CyryVo3> getCYRYbyName(String XM) {
        Map map = new HashMap();
        List<CyryVo3> list = olexamCyryJbxxDao.getCYRYbyName(XM.trim());
        List<CyryVo3> list1 = new ArrayList<>();
        if (null != list && !list.isEmpty() && !"[]".equals(list.toString())) {
            for (CyryVo3 cyryVo3 : list) {
                if (null != cyryVo3) {
                    if (StringUtils.isNoneBlank(cyryVo3.getXM())) {
                        map.put("XM", cyryVo3.getXM());
                    } else {
                        map.put("XM", "");
                    }
                    if (StringUtils.isNoneBlank(cyryVo3.getIDCARD())) {
                        map.put("IDCARD", cyryVo3.getIDCARD());
                    } else {
                        map.put("IDCARD", "");
                    }
                    if (null != cyryVo3.getPHOTO()) {
                        String PHOTO = Base64.encodeBase64String(cyryVo3.getPHOTO());
                        cyryVo3.setPHOTO(PHOTO.getBytes());
                        map.put("PHOTO", PHOTO);
                    } else {
                        map.put("PHOTO", "");
                    }
                    list1.add(cyryVo3);
                }
            }
        }
        return list;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map getCYRYbyName1(String XM) {
        Map map = new HashMap();
        List<CyryVo1> list = olexamCyryJbxxDao.getCYRYbyName1(XM.trim());
//		List<CyryVo1> list1 = new ArrayList<>();
        if (null != list && !list.isEmpty() && !"[]".equals(list.toString())) {
            if (list.size() == 1) {
                map = toMap(list, map);
                return toMap(list, map);
            } else if (list.size() > 1) {
                // 不合格的也列表显示  童志宏提的 2019-03-07
//				for(CyryVo1 cyryVo1:list) {
//					if(cyryVo1 != null && StringUtils.isNoneBlank(cyryVo1.getTJJG()) && !"不合格".equals(cyryVo1.getTJJG())) {
//						list1.add(cyryVo1);
//					}
//				}
                if (null != list && !list.isEmpty() && !"[]".equals(list.toString())) {
                    if (list.size() == 1) {
                        map = toMap(list, map);
                        return toMap(list, map);
                    } else if (list.size() > 1) {
                        map.put("list", list);
//						map = toMapList(list,map);
                        return map;
                    }
                }
                return map;
            }

        }
//		if(null != list && !list.isEmpty() && !"[]".equals(list.toString())) {
//			if(list.size() == 1) {
//				map = toMap(list,map);
//				return toMap(list,map);
//			}else if(list.size() > 1) {
//				map.put("list", list);
////				map = toMapList(list,map);
//				return map;
//			}
//			
//		}
        return map;
    }

    public Map toMap(List<CyryVo1> list, Map map) {
        if (null != list && !list.isEmpty() && !"[]".equals(list.toString())) {
        	CyryVo1 cyryVo1 = list.get(0);
            if (cyryVo1 != null) {
                if (StringUtils.isNoneBlank(cyryVo1.getBH())) {
                    map.put("BH", cyryVo1.getBH());
                } else {
                    map.put("BH", "");
                }
                if (StringUtils.isNoneBlank(cyryVo1.getXM())) {
                    map.put("XM", cyryVo1.getXM());
                } else {
                    map.put("XM", "");
                }
                if (StringUtils.isNoneBlank(cyryVo1.getXB())) {
                    map.put("XB", cyryVo1.getXB());
                } else {
                    map.put("XB", "");
                }
                if (StringUtils.isNoneBlank(cyryVo1.getLB())) {
                    map.put("LB", cyryVo1.getLB());
                } else {
                    map.put("LB", "");
                }
                if (StringUtils.isNoneBlank(cyryVo1.getTJJG())) {
                    map.put("TJJG", cyryVo1.getTJJG());
                } else {
                    map.put("TJJG", "");
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
                if (cyryVo1.getPHOTO() != null) {
                    String PHOTO = Base64.encodeBase64String(cyryVo1.getPHOTO());
                    map.put("PHOTO", PHOTO);
                } else {
                    map.put("PHOTO", "");
                }
                if (cyryVo1.getFZJG() != null) {
                    map.put("FZJG", cyryVo1.getFZJG());
                } else {
                    map.put("FZJG", "");
                }
                if (cyryVo1.getID() != null) {
                    List<CyryQRIMG> list1 = olexamCyryJbxxDao.selectQRCODE1(cyryVo1.getID());
                    if (null != list1 && !list1.isEmpty() && !"[]".equals(list1.toString())) {
                        CyryQRIMG QRIMG = (CyryQRIMG)list1.get(0);
                        if (QRIMG != null) {
                            if (null != QRIMG.getQRIMG()) {
                                map.put("QRIMG", Base64.encodeBase64String(QRIMG.getQRIMG()));
                            } else {
                                map.put("QRIMG", "");
                            }
                        }
                    } else {
                        map.put("QRIMG", "");
                    }
//				String FZJG = olexamCyryJbxxDao.selectFZJG(cyryVo1.getID());
//				if (null == cyryVo1.getFZJG()) {
//					map.put("FZJG", FZJG);
//				}else {
//					map.put("FZJG","");
//				}
                }

                // 优化 单表查询
//			if (null != cyryVo1.getQRIMG()) {
//				String QRIMG = Base64.encodeBase64String(cyryVo1.getQRIMG());
//				map.put("QRIMG", QRIMG);
//			} else {
//				map.put("QRIMG", "");
//			}
                if (null != cyryVo1.getNL()) {
                    map.put("NL", cyryVo1.getNL());
                } else {
                    map.put("NL", null);
                }
            }
        }
        return map;
    }

    public Map toMapList(List<CyryVo1> list, Map map) {
        List<Map> list2 = new ArrayList<>();
        if (null != list && !list.isEmpty() && !"[]".equals(list.toString())) {
            for (CyryVo1 cyryVo1 : list) {
                if (null != cyryVo1) {
//					if (StringUtils.isNoneBlank(cyryVo1.getBH())) {
//						map.put("BH", cyryVo1.getBH());
//					} else {
//						map.put("BH", "");
//					}
                    if (StringUtils.isNoneBlank(cyryVo1.getXM())) {
                        map.put("XM", cyryVo1.getXM());
                    } else {
                        map.put("XM", "");
                    }
//					if (StringUtils.isNoneBlank(cyryVo1.getXB())) {
//						map.put("XB", cyryVo1.getXB());
//					} else {
//						map.put("XB", "");
//					}
//					if (StringUtils.isNoneBlank(cyryVo1.getLB())) {
//						map.put("LB", cyryVo1.getLB());
//					} else {
//						map.put("LB", "");
//					}
//					if (StringUtils.isNoneBlank(cyryVo1.getTJJG())) {
//						map.put("TJJG", cyryVo1.getTJJG());
//					} else {
//						map.put("TJJG", "");
//					}
//					if (StringUtils.isNoneBlank(cyryVo1.getFZSJ())) {
//						map.put("FZSJ", cyryVo1.getFZSJ());
//					} else {
//						map.put("FZSJ", "");
//					}
                    if (StringUtils.isNoneBlank(cyryVo1.getIDCARD())) {
                        map.put("IDCARD", cyryVo1.getIDCARD());
                    } else {
                        map.put("IDCARD", "");
                    }
//					if (null != cyryVo1.getPHOTO()) {
//						String PHOTO = Base64.encodeBase64String(cyryVo1.getPHOTO());
//						map.put("PHOTO", PHOTO);
//					} else {
//						map.put("PHOTO", "");
//					}
//					if (null != cyryVo1.getFZJG()) {
//						map.put("FZJG", cyryVo1.getFZJG());
//					}else {
//						map.put("FZJG","");
//					}
//					if (null != cyryVo1.getID()) {
//						List<CyryQRIMG> list1 = olexamCyryJbxxDao.selectQRCODE1(cyryVo1.getID());
//						if(null != list1 && !list1.isEmpty() && !"[]".equals(list1.toString())) {
//							CyryQRIMG QRIMG = list1.get(0);
//							if (null != QRIMG) {
//								if(null != QRIMG.getQRIMG()) {
//									map.put("QRIMG", Base64.encodeBase64String(QRIMG.getQRIMG()));
//								}else {
//									map.put("QRIMG","");
//								}
//							}
//						}else {
//							map.put("QRIMG","");
//						}
//					}
//					if (null != cyryVo1.getNL()) {
//						map.put("NL", cyryVo1.getNL());
//					} else {
//						map.put("NL", null);
//					}
                }
                list2.add(map);
            }
        }
        map.put("list", list2);
        return map;
    }
    
    @Override
    public Map verifyPicture(String NAME, String PHOTO){
    	Map map = new HashMap();
        String img1 = "";
        String img2 = "";
        String idcard = "";
        List<CyryVo3> list = olexamCyryJbxxDao.getCYRYbyName(NAME.trim());
        try {
        	if (null != list && !list.isEmpty() && !"[]".equals(list.toString())) {
        		CyryVo3 cyryVo3;
        		float similarity;
        		if (list.size() == 1) {
        			cyryVo3 = (CyryVo3) list.get(0);
        			if (cyryVo3.getPHOTO() != null) {
        				img1 = PHOTO;
        				img2 = Base64.encodeBase64String(cyryVo3.getPHOTO());
        				FaceDetectResult fDetectResult = faceRecognUtil.faceDetect(img1);
        				if(fDetectResult.getErrno() == 0) {
        					if (fDetectResult.getFace_num() == 0) {
        						map.put("msg", "请传入带有人脸的图片");
            					return map;
							}else if(fDetectResult.getFace_num() > 0) {
		        				FaceVerifyResult fVerifyResult =  faceRecognUtil.faceVerify(img1, img2);
		        				log.info("result : "+fVerifyResult);
		        				if(fVerifyResult.getErrno() == 0) {
		        					if(fVerifyResult.getConfidence() > 75.45F) {
		        						return getJKZXXbyIdcard(cyryVo3.getIDCARD());
		        					}else {
		        						map.put("msg", "未查询到相匹配的人员信息");
		            					return map;
		        					}
		    					}else {
		    						map.put("msg", "人脸识别接口请求失败！");
		        					return map;
		    					}
							}
        				}else {
        					map.put("msg", "人脸检测接口请求失败！");
        					return map;
        				}
        				// 只有人脸对比接口调用
//        				FaceVerifyResult fVerifyResult =  faceRecognUtil.faceVerify(img1, img2);
//        				log.info("result : "+fVerifyResult);
//        				if(fVerifyResult.getErrno() == 0) {
//        					if(fVerifyResult.getConfidence() == 0.0F) {
//        						map.put("msg", "请传入带有人脸的图片");
//            					return map;
//        					}else if(fVerifyResult.getConfidence() > 75.45F) {
//        						return getJKZXXbyIdcard(cyryVo3.getIDCARD());
//        					}else {
//        						map.put("msg", "未查询到相匹配的人员信息");
//            					return map;
//        					}
//    					}else {
//    						map.put("msg", "人脸识别接口请求失败！");
//        					return map;
//    					}
        			} else {
        				// 调用对比接口API获取相似度
        				log.info("数据库人员头像图片为空，请确认该人员是否录入头像！");
        				map.put("msg", "数据库人员头像图片为空，请确认该人员是否录入头像！");
        				return map;
        			}
        		} else if (list.size() > 1) {
        			Map map1 = new HashMap<>();
        			for (int i=0;i<list.size();i++) {
        				CyryVo3 cyryVo = (CyryVo3) list.get(i);
        				int count;
        				if(cyryVo.getPHOTO() != null) {
        					img1 = PHOTO;
        					img2 = Base64.encodeBase64String(cyryVo.getPHOTO());
        					
        					FaceDetectResult fDetectResult = faceRecognUtil.faceDetect(img1);
            				if(fDetectResult.getErrno() == 0) {
            					if (fDetectResult.getFace_num() == 0) {
            						map.put("msg", "请传入带有人脸的图片");
                					return map;
    							}else if(fDetectResult.getFace_num() > 0) {
    	        					FaceVerifyResult fVerifyResult =  faceRecognUtil.faceVerify(img1, img2);
    	        					log.info("result "+i+" : "+fVerifyResult);
    	        					if(fVerifyResult.getErrno() == 0) {
    	        						map1.put(fVerifyResult.getConfidence(), cyryVo.getIDCARD());
    	        					}else {
    	        						map.put("msg", "人脸识别接口请求失败！");
    	            					return map;
    	        					}
    							}
            				}else {
            					map.put("msg", "人脸检测接口请求失败！");
            					return map;
            				}
        					// 只有人脸对比接口调用
//        					FaceVerifyResult fVerifyResult =  faceRecognUtil.faceVerify(img1, img2);
//        					log.info("result "+i+" : "+fVerifyResult);
//        					if(fVerifyResult.getErrno() == 0) {
//        						map1.put(fVerifyResult.getConfidence(), cyryVo.getIDCARD());
//        					}else {
//        						map.put("msg", "人脸识别接口请求失败！");
//            					return map;
//        					}
        				} else {
        					log.info("数据库人员头像图片为空，请确认该人员是否录入头像！");
        					map.put("msg", "数据库人员头像图片为空，请确认该人员是否录入头像！");
        					return map;
        				}
        			}
        			if (null != map1 && !"{}".equals(map1.toString())) {
        				// 调用对比接口API获取相似度
        				idcard = String.valueOf(map1.get(getMaxKey(map1)));
        				similarity = (float)getMaxKey(map1);
        				if(similarity > 75.45F) {
        					return getJKZXXbyIdcard(idcard);
        				}else {
        					map.put("msg", "未查询到相匹配的人员信息");
        					return map;
        				}
        			}
        		}
        	}
        }catch (Exception e) {
        	log.info("服务端出现异常！");
        	e.printStackTrace();
        	map.put("msg", "服务端出现异常！");
			return map;
		}
        return map;
    }

    @Override
    public Map verifyPicture1(String NAME, String PHOTO) {
//    	log.info("传入的人脸头像编码： "+PHOTO);
    	Map map = new HashMap();
        byte[] photo = Base64.decodeBase64(PHOTO.getBytes());
        String photoFeature = getFeatureStr(photo);
        if(photoFeature == null) {
        	log.error("没有获取到传入的人脸头像特征");
        	map.put("msg", "没有获取到传入的人脸头像特征");
        	return map;
        }
        String photoFeature1 = "";
        String photoFeature2 = "";
        String idcard = "";
        List<CyryVo3> list = olexamCyryJbxxDao.getCYRYbyName(NAME.trim());
        if (null != list && !list.isEmpty() && !"[]".equals(list.toString())) {
        	CyryVo3 cyryVo3;
			float similarity;
            if (list.size() == 1) {
                cyryVo3 = (CyryVo3) list.get(0);
                if (cyryVo3.getFEATURE() == null && cyryVo3.getPHOTO() != null && cyryVo3.getIDCARD() != null) {
                    photoFeature1 = getFeatureStr(cyryVo3.getPHOTO());
                    if(photoFeature1 == null) {
                    	log.error("没有获取到数据库的人脸头像特征");
                    	map.put("msg", "没有获取到传入的人脸头像特征");
                    	return map;
                    }
                    photoFeature2 = cyryVo3.getFEATURE();
                	if(photoFeature1 != photoFeature2) {
                		int count = updateFeature(photoFeature1, cyryVo3.getIDCARD());
                		log.info("更新了cyryquery 表的 FEATURE" + count + "条数据");
                		if (count != 1) {
                			log.error("更新cyryquery 表的 FEATURE数据失败");
                		} else if(cyryVo3.getFEATURE() == null){
                			// 调用对比接口API获取相似度
//                          photoFeature1 = getFeatureStr(cyryVo3.getPHOTO());
            				log.error("没有获取到数据库的人脸头像特征");
            				similarity = Tools.compareFeatures(photoFeature, cyryVo3.getFEATURE());
            				if(similarity > 70.0F) {
            					idcard = cyryVo3.getIDCARD();
            					return getJKZXXbyIdcard(idcard);
            				}else {
            					map.put("msg", "未查询到相匹配的人员信息");
            					return map;
            				}
                		}
                	}
                    
                } else if(cyryVo3.getFEATURE() == null) {
                	// 调用对比接口API获取相似度
//                  photoFeature1 = getFeatureStr(cyryVo3.getPHOTO());
				  	log.error("没有获取到数据库的人脸头像特征");
				  	similarity = Tools.compareFeatures(photoFeature, cyryVo3.getFEATURE());
				  	if(similarity > 70.0F) {
				  		idcard = cyryVo3.getIDCARD();
				  		return getJKZXXbyIdcard(idcard);
				  	}else {
				  		map.put("msg", "未查询到相匹配的人员信息");
				  		return map;
				  	}
                }
            } else if (list.size() > 1) {
                // 更新人脸特征码为空的数据
                for (int i=0;i<list.size();i++) {
                	CyryVo3 cyryVo = (CyryVo3) list.get(i);
                	int count;
                    if (cyryVo.getFEATURE() == null) {
                    	if(cyryVo.getPHOTO() == null ) {
                    		log.info("数据库人员头像图片为空");
                			map.put("msg", "数据库人员头像图片为空");
                			return map;
                    	}
                    	if (cyryVo.getIDCARD() == null) {
							this.log.info("数据库人员身份证号为空");
							map.put("msg", "数据库人员身份证号为空");
							return map;
						}
                    	photoFeature1 = getFeatureStr(cyryVo.getPHOTO());
						if (photoFeature1 == null) {
							this.log.error("没有获取到数据库的人脸头像特征");
							return map;
						}
						if (!photoFeature1.equals(cyryVo.getFEATURE())) {
							count = this.updateFeature(photoFeature1, cyryVo.getIDCARD());
							log.info("更新了cyryquery 表的 FEATURE" + count + "条数据");
							if (count != 1) {
								log.error("更新cyryquery 表的 FEATURE数据失败");
							}
						}
                    }else if(cyryVo.getPHOTO() != null ) {
                		photoFeature1 = getFeatureStr(cyryVo.getPHOTO());
						if (!photoFeature1.equals(cyryVo.getFEATURE())) {
                			count = updateFeature(photoFeature1, cyryVo.getIDCARD());
                			log.info("更新了cyryquery 表的 FEATURE" + count + "条数据");
                			if (count != 1) {
                				log.error("更新cyryquery 表的 FEATURE数据失败");
                			}
                		}
                	}
                    
                }

                Map map1 = new HashMap<>();
                for (int i=0;i<list.size();i++) {
                	CyryVo3 cyryVo = (CyryVo3) list.get(i);
                    if (cyryVo.getFEATURE() != null && cyryVo.getPHOTO() != null && cyryVo.getIDCARD() != null) {
                        // 调用对比接口API获取相似度
                        similarity = Tools.compareFeatures(photoFeature, cyryVo.getFEATURE());
                        map1.put(similarity, cyryVo.getIDCARD());
                    } else if (photoFeature != null && cyryVo.getFEATURE() != null) {
                        similarity = Tools.compareFeatures(photoFeature, cyryVo.getFEATURE());
                        map1.put(similarity, cyryVo.getIDCARD());
                    }
                }
                if (null != map1 && !"{}".equals(map1.toString())) {
                    // 调用对比接口API获取相似度
                    idcard = String.valueOf(map1.get(getMaxKey(map1)));
                    similarity = (float)getMaxKey(map1);
                    if(similarity > 70.0F) {
				  		return getJKZXXbyIdcard(idcard);
				  	}else {
				  		map.put("msg", "未查询到相匹配的人员信息");
				  		return map;
				  	}
                }
            }
        }

        return map;
    }

    /**
     * 求Map<K,V>中Key(键)的最大值
     *
     * @param map
     * @return
     */
    Object getMaxKey(Map map) {
        if (map == null) return null;
        Set set = map.keySet();
        Object[] obj = set.toArray();
        Arrays.sort(obj);
        return obj[obj.length - 1];
    }

    /**
     * 通过身份证号获取健康证信息
     *
     * @param idCard
     * @return
     */
    Map getJKZXXbyIdcard(String idCard) {
        Map map = new HashMap();
        List<CyryVo1> list = olexamCyryJbxxDao.selectCYRYVo1(idCard.trim());
        if (null != list && !list.isEmpty() && !"[]".equals(list.toString())) {
        	CyryVo1 cyryVo1 = list.get(0);
            if (null != cyryVo1) {
                if (StringUtils.isNoneBlank(cyryVo1.getBH())) {
                    map.put("BH", cyryVo1.getBH());
                } else {
                    map.put("BH", "");
                }
                if (StringUtils.isNoneBlank(cyryVo1.getXM())) {
                    map.put("XM", cyryVo1.getXM());
                } else {
                    map.put("XM", "");
                }
                if (StringUtils.isNoneBlank(cyryVo1.getXB())) {
                    map.put("XB", cyryVo1.getXB());
                } else {
                    map.put("XB", "");
                }
                if (StringUtils.isNoneBlank(cyryVo1.getLB())) {
                    map.put("LB", cyryVo1.getLB());
                } else {
                    map.put("LB", "");
                }
                if (StringUtils.isNoneBlank(cyryVo1.getTJJG())) {
                    map.put("TJJG", cyryVo1.getTJJG());
                } else {
                    map.put("TJJG", "");
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
                if (null != cyryVo1.getFZJG()) {
                    map.put("FZJG", cyryVo1.getFZJG());
                } else {
                    map.put("FZJG", "");
                }
                if (null != cyryVo1.getID()) {
                    List<CyryQRIMG> list1 = olexamCyryJbxxDao.selectQRCODE1(cyryVo1.getID());
                    if (null != list1 && !list1.isEmpty() && !"[]".equals(list1.toString())) {
                        CyryQRIMG QRIMG = list1.get(0);
                        if (null != QRIMG) {
                            if (null != QRIMG.getQRIMG()) {
                                map.put("QRIMG", Base64.encodeBase64String(QRIMG.getQRIMG()));
                            } else {
                                map.put("QRIMG", "");
                            }
                        }
                    } else {
                        map.put("QRIMG", "");
                    }
//				String FZJG = olexamCyryJbxxDao.selectFZJG(cyryVo1.getID());
//				if (null == cyryVo1.getFZJG()) {
//					map.put("FZJG", FZJG);
//				}else {
//					map.put("FZJG","");
//				}
                }

                // 优化 单表查询
//			if (null != cyryVo1.getQRIMG()) {
//				String QRIMG = Base64.encodeBase64String(cyryVo1.getQRIMG());
//				map.put("QRIMG", QRIMG);
//			} else {
//				map.put("QRIMG", "");
//			}
                if (null != cyryVo1.getNL()) {
                    map.put("NL", cyryVo1.getNL());
                } else {
                    map.put("NL", null);
                }
            }
        }
        return map;
    }

    /**
     * 获取图片人脸特征码
     *
     * @param photo
     * @return
     */
    private String getFeatureStr(byte[] photo) {
        // 下面注释的是用来测试用的代码
//		byte[] imageData = Utils.loadFile("./pictures/liudehua_feature1.jpg");
        Image image = new Image();
        image.data = photo;
        image.format = ImageFormat.ImageFormat_UNKNOWN;
        Face faces[] = faceDetect.detectPicture(image);
        return faces != null ? faceRegister.extractFeature(image, faces[0], 1) : null;
    }

    /**
     * 通过身份证号更新人脸特征码
     *
     * @param FEATURE 人脸特征码
     * @param idcard  身份证号
     * @return
     */
    int updateFeature(String FEATURE, String idcard) {
        return olexamCyryJbxxDao.updateFEATURE(idcard, FEATURE);
    }

}