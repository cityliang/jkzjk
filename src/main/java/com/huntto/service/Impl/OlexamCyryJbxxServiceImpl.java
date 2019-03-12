package com.huntto.service.Impl;

import com.alibaba.cloud.faceengine.*;
import com.huntto.dao.OlexamCyryJbxxDao;
import com.huntto.entity.CyryQRIMG;
import com.huntto.entity.CyryVo1;
import com.huntto.entity.CyryVo3;
import com.huntto.service.OlexamCyryJbxxService;
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
        CyryVo1 cyryVo1 = new CyryVo1();
        if (null != list && !list.isEmpty() && !"[]".equals(list.toString())) {
            cyryVo1 = list.get(0);
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
        CyryVo1 cyryVo1 = new CyryVo1();
        if (null != list && !list.isEmpty() && !"[]".equals(list.toString())) {
            cyryVo1 = list.get(0);
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
    public Map verifyPicture(String NAME, String PHOTO) {
//    	log.info("传入的人脸头像编码： "+PHOTO);
    	Map map = new HashMap();
        byte[] photo = Base64.decodeBase64(PHOTO.getBytes());
        String photoFeature = getFeatureStr(photo);
        if(photoFeature == null) {
        	log.error("没有获取到传入的人脸头像特征");
        	return map;
        }
        String photoFeature1 = "";
        String idcard = "";
        List<CyryVo3> list = olexamCyryJbxxDao.getCYRYbyName(NAME.trim());
        if (null != list && !list.isEmpty() && !"[]".equals(list.toString())) {
            if (list.size() == 1) {
                CyryVo3 cyryVo3 = (CyryVo3) list.get(0);
                if (cyryVo3.getFEATURE() == null && cyryVo3.getPHOTO() != null && cyryVo3.getIDCARD() != null) {
                    photoFeature1 = getFeatureStr(cyryVo3.getPHOTO());
                    int count = updateFeature(photoFeature1, cyryVo3.getIDCARD());
                    log.info("更新了cyryquery 表的 FEATURE" + count + "条数据");
                    if (count != 1) {
                        log.error("更新cyryquery 表的 FEATURE数据失败");
                    } else {
                        // 调用对比接口API获取相似度
//                        photoFeature1 = getFeatureStr(cyryVo3.getPHOTO());
                        if(cyryVo3.getFEATURE() == null) {
                        	log.error("没有获取到数据库的人脸头像特征");
                        	float similarity = Tools.compareFeatures(photoFeature, cyryVo3.getFEATURE());
                        	if(similarity > 70) {
                        		idcard = cyryVo3.getIDCARD();
                        		return getJKZXXbyIdcard(idcard);
                        	}else {
                        		return map;
                        	}
                        }
                    }
                } else {
                	// 调用对比接口API获取相似度
//                  photoFeature1 = getFeatureStr(cyryVo3.getPHOTO());
					if(cyryVo3.getFEATURE() == null) {
					  	log.error("没有获取到数据库的人脸头像特征");
					  	float similarity = Tools.compareFeatures(photoFeature, cyryVo3.getFEATURE());
					  	if(similarity > 70) {
					  		idcard = cyryVo3.getIDCARD();
					  		return getJKZXXbyIdcard(idcard);
					  	}else {
					  		return map;
					  	}
					}
                }
            } else if (list.size() > 1) {
                // 更新人脸特征码为空的数据
                for (CyryVo3 cyryVo3 : list) {
                    if (cyryVo3.getFEATURE() == null && cyryVo3.getPHOTO() != null && cyryVo3.getIDCARD() != null) {
                        photoFeature1 = getFeatureStr(cyryVo3.getPHOTO());
                        int count = updateFeature(photoFeature1, cyryVo3.getIDCARD());
                        log.info("更新了cyryquery 表的 FEATURE" + count + "条数据");
                        if (count != 1) {
                            log.error("更新cyryquery 表的 FEATURE数据失败");
                        }
                    }
                }

                Map map1 = new HashMap<>();
                for (CyryVo3 cyryVo3 : list) {
                    if (cyryVo3.getFEATURE() != null && cyryVo3.getPHOTO() != null && cyryVo3.getIDCARD() != null) {
                        // 调用对比接口API获取相似度
                        float similarity = Tools.compareFeatures(photoFeature, cyryVo3.getFEATURE());
                        map1.put(similarity, cyryVo3.getIDCARD());
                    } else {
                        if (photoFeature != null && cyryVo3.getFEATURE() != null) {
                            float similarity = Tools.compareFeatures(photoFeature, cyryVo3.getFEATURE());
                            map1.put(similarity, cyryVo3.getIDCARD());
                        }
                    }
                }
                if (null != map1 && !"{}".equals(map1.toString())) {
                    // 调用对比接口API获取相似度
                    idcard = String.valueOf(map1.get(getMaxKey(map1)));
                    float similarity = (float)getMaxKey(map1);
                    if(similarity > 70) {
				  		return getJKZXXbyIdcard(idcard);
				  	}else {
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
        CyryVo1 cyryVo1 = new CyryVo1();
        if (null != list && !list.isEmpty() && !"[]".equals(list.toString())) {
            cyryVo1 = list.get(0);
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

        return faceRegister.extractFeature(image, faces[0], ModelType.MODEL_SMALL);
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