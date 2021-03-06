package com.huntto.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huntto.config.WeiXinConfig;
import com.huntto.entity.CyryVo1;
import com.huntto.entity.CyryVo2;
import com.huntto.entity.FeedBack;
import com.huntto.service.CyryService;
import com.huntto.service.FeedBackService;
import com.huntto.service.OlexamCyryJbxxService;
import com.huntto.util.IdCardUtil;
import com.huntto.util.JsonUtil;
import com.huntto.util.Nulls;
import com.huntto.util.WeiXinUtil;
import com.huntto.util.WxUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 从业人员接口 <br/>
 * Copyright: 版权归浙江创得信息技术有限公司所有<br/>
 * Project_name jkzjk<br/>
 * Package_name com.huntto.controller<br/>
 * Author 梁城市<br/>
 * Email city_wangyi@163.com<br/>
 * Create_time 2018/12/10 15:40<br/>
 */
@Slf4j
@RestController
@Api("健康证-从业人员相关接口")
@RequestMapping("/api")
public class CyryController {
	@Autowired
	private CyryService cyryService;
	
	@Autowired
	private FeedBackService feedBackService;
	
	@Autowired
	private OlexamCyryJbxxService olexamCyryJbxxService;
	
	@Autowired
	private WeiXinConfig wConfig;
	
	@Autowired
	private WeiXinUtil wUtil;
	
	@Autowired
	private WxUtil wxUtil;
	
	/**
	 * 通过config接口注入权限验证配置
	 * @return url 
	 * @throws Exception
	 */
	@ApiOperation(value = "获取微信接口相关信息", notes = "用来获取前端需要的信息")
	@ApiImplicitParam(paramType = "query", name = "url", value = "JS安全域名的三个之一", dataType = "String", required = true)
    @GetMapping(value = "/getWxToken")
    public String test(String url) throws Exception {
		String access_token = null;
		String ticket = null;
		try {
			access_token = wUtil.getAccess_token();
			ticket = wUtil.getWXJsapiTicket();
			String timestamp = WxUtil.getTimestamp();
			String nonceStr = WxUtil.getNoncestr();
			String signature = wxUtil.getJsSdkSign(nonceStr, ticket, timestamp, url);
			log.info("url："+url);
			log.info("access_token："+access_token);
			log.info("timestamp："+timestamp);
			log.info("ticket："+ticket);
			log.info("nonceStr："+nonceStr);
			log.info("signature："+signature);
			Map map = new HashMap<>();
			map.put("access_token", access_token);
			map.put("ticket", ticket);
			map.put("appId", wConfig.getAPPID());
			map.put("timestamp", timestamp);
			map.put("nonceStr", nonceStr);
			map.put("signature", signature);
			return JsonUtil.toJSon(map);
		} catch (IOException e) {
			log.info("连接超时。。。。。");
			log.info("access_token："+access_token);
			log.info("ticket："+ticket);
		}
		return url;
    }
	
	/**
	 * 获取人员信息接口
	 *
	 * @param idCard 身份证号
	 * @return String
	 * @throws JsonProcessingException JsonProcessingException
	 */
	@Deprecated
	@ApiOperation(value = "获取人员信息接口", notes = "用来获取人员健康证信息")
	@ApiImplicitParam(paramType = "query", name = "idCard", value = "体检人员身份证号", dataType = "String", required = true)
	@RequestMapping(value = { "/getPersonMsg2" }, method = RequestMethod.POST)
	public String getPersonMsg2(String idCard) throws JsonProcessingException {
        String json;
        if (Nulls.validateValue(idCard) && IdCardUtil.isIdcard(idCard)) {
			Map map;
			map = cyryService.selectCYRYVo1(idCard);
			if (null != map && !"{}".equals(map.toString())) {
				ObjectMapper mapper = new ObjectMapper();
				json = mapper.writeValueAsString(map);
            } else if ("{}".equals(map != null ? map.toString() : null)) {
                json = JsonUtil.jsonStr("msg", "未查到该人员信息或在办理中");
			} else {
				json = JsonUtil.jsonStr("msg", "该用户没有健康证");
			}
		} else {
			json = JsonUtil.jsonStr("msg", "身份证不正确，请输入正确的身份证");
		}
//		json = Base64.encodeBase64String(json.getBytes());
		return json;
	}

	/**
	 * 获取人员信息接口
	 *
	 * @param idCard 身份证号
	 * @return String
	 * @throws JsonProcessingException JsonProcessingException
	 */
	@Deprecated
	@ApiOperation(value = "获取人员信息接口", notes = "用来获取人员健康证信息")
	@ApiImplicitParam(paramType = "query", name = "idCard", value = "体检人员身份证号", dataType = "String", required = true)
	@RequestMapping(value = { "/getPersonMsg1" }, method = RequestMethod.POST)
	public String getPersonMsg1(String idCard) throws JsonProcessingException {
        String json;
        if (Nulls.validateValue(idCard) && IdCardUtil.isIdcard(idCard)) {
			Map map;
			map = olexamCyryJbxxService.selectCYRYVO(idCard);
			if (null != map && !"{}".equals(map.toString())) {
				if("不合格".equals(map.get("TJJG"))) {
					json = JsonUtil.jsonStr("msg", "请联系有关体检机构，咨询相关信息，谢谢！");
				}else {
					ObjectMapper mapper = new ObjectMapper();
					json = mapper.writeValueAsString(map);
				}
            } else if ("{}".equals(map != null ? map.toString() : null)) {
                json = JsonUtil.jsonStr("msg", "未查到该人员信息或在办理中");
			} else {
				json = JsonUtil.jsonStr("msg", "该用户没有健康证");
			}
		} else {
			json = JsonUtil.jsonStr("msg", "身份证不正确，请输入正确的身份证");
		}
//		json = Base64.encodeBase64String(json.getBytes());
		return json;
	}
	
	/**
	 * 获取人员信息接口
	 *
	 * @param idCard XM 姓名或身份证号
	 * @return String
	 * @throws JsonProcessingException JsonProcessingException
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "获取人员信息接口 通过姓名或身份证查询", notes = "通过姓名则返回姓名和身份证号列表")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "NAME", value = "体检人员姓名", dataType = "String", required = true),
		@ApiImplicitParam(paramType = "query", name = "idCard", value = "体检人员身份证号", dataType = "String", required = true)
	})
	@RequestMapping(value = { "/getPersonMsg" }, method = RequestMethod.POST)
	public String getPersonMsg3(String NAME,String idCard) throws JsonProcessingException {
		long start222 = System.currentTimeMillis();
		Map map;
		String json = "";
		ObjectMapper mapper;
		if(Nulls.isNotEmpty(NAME) && Nulls.isEmpty(idCard)) {// 姓名不为空，身份证号为空
			long start = System.currentTimeMillis();
			map = olexamCyryJbxxService.getCYRYbyName1(NAME);
			long end = System.currentTimeMillis();
			log.info("使用姓名查询 getCYRYbyName1 方法执行花费时间"+(end - start));
			if(map.containsKey("list")) {
				List<CyryVo1> list = (List<CyryVo1>)map.get("list");
//				List<CyryVo2> list2 = new ArrayList<>();
//				if(list != null && !list.isEmpty() && !"[]".equals(list.toString())) {
//					for(CyryVo1 cyryVo:list) {
//						CyryVo2 cyryVo2 = new CyryVo2();
//						if(StringUtils.isNoneBlank(cyryVo.getTJJG()) && !"不合格".equals(cyryVo.getTJJG())) {
//							if (StringUtils.isNoneBlank(cyryVo.getXM())) {
//								cyryVo2.setXM(cyryVo.getXM());
//							} else {
//								cyryVo2.setXM("");
//							}
//							if (StringUtils.isNoneBlank(cyryVo.getIDCARD())) {
//								cyryVo2.setIDCARD(cyryVo.getIDCARD());
//							} else {
//								cyryVo2.setIDCARD("");
//							}
//							list2.add(cyryVo2);
//						}
//					}
//				}
//				if (null != list2 && !"[]".equals(list2.toString())) {
//					ObjectMapper mapper = new ObjectMapper();
//					json = mapper.writeValueAsString(list2);
//				} else if ("[]".equals(map.toString())) {
//					json = JsonUtil.jsonStr("msg", "未查到该人员信息或在办理中");
//				} else {
//					json = JsonUtil.jsonStr("msg", "该用户没有健康证");
//				}
				long start1 = System.currentTimeMillis();
				map = toMapList(list,map);
				long end1 = System.currentTimeMillis();
				log.info("使用姓名查询 getCYRYbyName1 方法转换执行花费时间1"+(end1 - start1));
				if (null != map && !"{}".equals(map.toString())) {
					mapper = new ObjectMapper();
					json = mapper.writeValueAsString(map);
                } else if ("{}".equals(map != null ? map.toString() : null)) {
                    json = JsonUtil.jsonStr("msg", "未查到该人员信息或在办理中");
				} else {
					json = JsonUtil.jsonStr("msg", "该用户没有健康证");
				}
            } else if (!"{}".equals(map.toString())) {
                if("不合格".equals(map.get("TJJG"))) {
					json = JsonUtil.jsonStr("msg", "请联系有关体检机构，咨询相关信息，谢谢！");
				}else {
					mapper = new ObjectMapper();
					json = mapper.writeValueAsString(map);
				}
			} else if ("{}".equals(map.toString())) {
				json = JsonUtil.jsonStr("msg", "未查到该人员信息或在办理中");
			} else {
				json = JsonUtil.jsonStr("msg", "该用户没有健康证");
			}
		}else if((Nulls.isNotEmpty(NAME) && Nulls.isNotEmpty(idCard)) || 
				 (Nulls.isEmpty(NAME) && Nulls.isNotEmpty(idCard))) {// 姓名不为空，身份证号不为空 或 姓名为空，身份证号不为空 
			if (Nulls.validateValue(idCard) && IdCardUtil.isIdcard(idCard)) {
				map = olexamCyryJbxxService.selectCYRYVO(idCard);
				if (null != map && !"{}".equals(map.toString())) {
					if("不合格".equals(map.get("TJJG"))) {
						json = JsonUtil.jsonStr("msg", "请联系有关体检机构，咨询相关信息，谢谢！");
					}else {
						mapper = new ObjectMapper();
						json = mapper.writeValueAsString(map);
					}
                } else if ("{}".equals(map != null ? map.toString() : null)) {
                    json = JsonUtil.jsonStr("msg", "未查到该人员信息或在办理中");
				} else {
					json = JsonUtil.jsonStr("msg", "该用户没有健康证");
				}
			} else {
				json = JsonUtil.jsonStr("msg", "身份证不正确，请输入正确的身份证");
			}
		}else if(Nulls.isEmpty(NAME) && Nulls.isEmpty(idCard)) {// 姓名为空，身份证号为空
			json = JsonUtil.jsonStr("msg", "姓名和身份证不正确，请输入正确的姓名和身份证");
		}
		long end333 = System.currentTimeMillis();
		log.info("通过姓名或身份证查询总共花费"+(end333 - start222));
		return json;
	}

    private Map toMapList(List<CyryVo1> list, Map map) {
        map.clear();
        List<CyryVo2> list2 = new ArrayList<>();
        if (null != list && !list.isEmpty() && !"[]".equals(list.toString())) {
            for (CyryVo1 cyryVo1 : list) {
                CyryVo2 cyryVo2 = new CyryVo2();
                if (null != cyryVo1) {
                    if (StringUtils.isNoneBlank(cyryVo1.getXM())) {
                        cyryVo2.setXM(cyryVo1.getXM());
                    } else {
                        cyryVo2.setXM("");
                    }
                    if (StringUtils.isNoneBlank(cyryVo1.getIDCARD())) {
                    	// 加密身份证号 改到前端做
//                    	cyryVo2.setIDCARD(cyryVo1.getIDCARD().replaceAll("(\\d{4})\\d{10}(\\d{4})","$1****$2"));
                    	cyryVo2.setIDCARD(cyryVo1.getIDCARD());
                    } else {
                        cyryVo2.setIDCARD("");
                    }
                    list2.add(cyryVo2);
                    // 不合格的也显示
//                    if (StringUtils.isNoneBlank(cyryVo1.getTJJG()) && !"不合格".equals(cyryVo1.getTJJG())) {
//                        if (StringUtils.isNoneBlank(cyryVo1.getXM())) {
//                            cyryVo2.setXM(cyryVo1.getXM());
//                        } else {
//                            cyryVo2.setXM("");
//                        }
//                        if (StringUtils.isNoneBlank(cyryVo1.getIDCARD())) {
//                            cyryVo2.setIDCARD(cyryVo1.getIDCARD());
//                        } else {
//                            cyryVo2.setIDCARD("");
//                        }
//                        list2.add(cyryVo2);
//                    }
                }
            }
        }
        map.put("list", list2);
        return map;
    }

	/**
	 * 反馈接口
	 * 
	 * @param FBType    反馈类型
	 * @param FBContent 反馈内容
     * @return String
     * @throws JsonProcessingException JsonProcessingException
	 */
	@ApiOperation(value = "反馈接口", notes = "插入反馈信息")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "IDCARD", value = "反馈人身份证号", dataType = "String", required = true),
			@ApiImplicitParam(paramType = "query", name = "FBType", value = "反馈类型", dataType = "String", required = true),
			@ApiImplicitParam(paramType = "query", name = "FBContent", value = "反馈内容", dataType = "String", required = true) })
//	@ApiResponses(value = { @ApiResponse(code = 401, message = "反馈失败") })
	@RequestMapping(value = { "/feedback" }, method = RequestMethod.POST)
	public String feedback(String IDCARD, String FBType, String FBContent) throws JsonProcessingException {
        String json;
        if (Nulls.validateValue(IDCARD, FBType, FBContent)) {
			int count = feedBackService.insertFeedBack(IDCARD, FBType, FBContent);
			if (1 == count) {
				json = JsonUtil.jsonStr("msg", "反馈成功");
			} else {
				json = JsonUtil.jsonStr("msg", "反馈失败");
			}
		} else {
			json = JsonUtil.jsonStr("msg", "请选择反馈类型并输入反馈内容");
		}
		return json;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ApiOperation(value = "查询反馈信息", notes = "可以无参查询，也可以通过身份证号或反馈信息ID查询")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "IDCARD", value = "反馈人身份证号", dataType = "String", required = false),
			@ApiImplicitParam(paramType = "query", name = "ID", value = "反馈信息ID", dataType = "String", required = false) 
	})
	@RequestMapping(value = { "/selectFeedBack" }, method = RequestMethod.POST)
	public String selectFeedBack(FeedBack feedBack) throws JsonProcessingException {
		Map map = new HashMap<>();
        String json;
        if (Nulls.validateValue(feedBack.getID())) {
			feedBack = feedBackService.selectFeedBackByID(feedBack.getID());
			if (null != feedBack) {
				if (StringUtils.isNotBlank(feedBack.getFBType()) && StringUtils.isNotBlank(feedBack.getFBContent())) {
					ObjectMapper mapper = new ObjectMapper();
					map.put("data", feedBack);
					map.put("total", "1");
					json = mapper.writeValueAsString(map);
				} else {
					json = JsonUtil.jsonStr("msg", "反馈类型或反馈内容不存在");
				}
			} else {
				json = JsonUtil.jsonStr("msg", "反馈类型或反馈内容不存在");
			}
		} else {
			List<FeedBack> list = feedBackService.selectFeedBack(feedBack);
			if (null != list && !list.isEmpty() && !"[]".equals(list.toString())) {
				ObjectMapper mapper = new ObjectMapper();
				map.put("data", list);
				map.put("total", list.size());
				json = mapper.writeValueAsString(map);
			} else {
				json = JsonUtil.jsonStr("msg", "查询反馈信息失败");
			}
		}
		return json;
	}
	
	
	/**
	 * 反馈接口-更新已处理字段
	 *
     * FBType    反馈类型
     * FBContent 反馈内容
     * @return String
     * @throws JsonProcessingException JsonProcessingException
	 */
	@ApiOperation(value = "更新已处理字段接口", notes = "更新反馈是否处理信息")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "IS_CL", value = "是否处理字段", dataType = "String", required = true),
			@ApiImplicitParam(paramType = "query", name = "ID", value = "反馈数据ID", dataType = "String", required = true)
	})
	@RequestMapping(value = { "/updateFeedBack" }, method = RequestMethod.POST)
	public String updateFeedBack(FeedBack feedBack) throws JsonProcessingException {
        String json;
        if (Nulls.validateValue(feedBack.getIS_CL(), feedBack.getID())) {
			int count = feedBackService.updateFeedBack(feedBack);
			if (1 == count) {
				json = JsonUtil.jsonStr("msg", "更新数据成功");
			} else {
				json = JsonUtil.jsonStr("msg", "更新数据失败");
			}
		} else {
			json = JsonUtil.jsonStr("msg", "请选择反馈类型并输入反馈内容");
		}
		return json;
	}
	
	
	/**
	 * 人脸识别验证接口
	 * 在线调用 /face/verify
	 * @param NAME 体检人员姓名
	 * @param PHOTO 体检人员头像
	 * @return String
	 * @throws JsonProcessingException JsonProcessingException
	 */
	@ApiOperation(value = "人脸识别验证接口", notes = "用来获取人员健康证信息")
	@ApiImplicitParams({
	@ApiImplicitParam(paramType = "query", name = "NAME", value = "体检人员姓名", dataType = "String", required = true),
	@ApiImplicitParam(paramType = "query", name = "PHOTO", value = "体检人员头像", dataType = "String", required = true)})
	@RequestMapping(value = { "/faceRecognition" }, method = RequestMethod.POST)
	public String faceRecognition(String NAME,String PHOTO) throws JsonProcessingException {
        String json;
        long start = System.currentTimeMillis();
        if (Nulls.validateValue(NAME,PHOTO)) {
			Map map = olexamCyryJbxxService.verifyPicture(NAME,PHOTO);
			if (null != map && !"{}".equals(map.toString())) {
				if(map.containsKey("msg")) {
					String mString = String.valueOf(map.get("msg"));
					json = JsonUtil.jsonStr("msg", mString);
                } else if ("不合格".equals(map.get("TJJG"))) {
                    json = JsonUtil.jsonStr("msg", "请联系有关体检机构，咨询相关信息，谢谢！");
				}else {
					ObjectMapper mapper = new ObjectMapper();
					json = mapper.writeValueAsString(map);
				}
            } else if ("{}".equals(map != null ? map.toString() : null)) {
                json = JsonUtil.jsonStr("msg", "未查到该人员信息或在办理中");
			} else {
				json = JsonUtil.jsonStr("msg", "该用户没有健康证");
			}
		} else {
			json = JsonUtil.jsonStr("msg", "姓名和头像不正确，请重试！");
		}
        long end = System.currentTimeMillis();
        log.info("通过人脸识别接口总花费时间："+(end - start));
		return json;
	}
	
	/**
	 * 人脸识别验证接口
	 * 离线SDK调用   已弃用
	 * @param NAME 体检人员姓名
	 * @param PHOTO 体检人员头像
	 * @return String
	 * @throws JsonProcessingException JsonProcessingException
	 */
	@Deprecated
	@ApiOperation(value = "人脸识别验证接口", notes = "用来获取人员健康证信息")
	@ApiImplicitParams({
	@ApiImplicitParam(paramType = "query", name = "NAME", value = "体检人员姓名", dataType = "String", required = true),
	@ApiImplicitParam(paramType = "query", name = "PHOTO", value = "体检人员头像", dataType = "String", required = true)})
	@RequestMapping(value = { "/faceRecognition1" }, method = RequestMethod.POST)
	public String faceRecognition1(String NAME,String PHOTO) throws JsonProcessingException {
        String json;
        if (Nulls.validateValue(NAME,PHOTO)) {
			Map map = new HashMap<>();
//			map = olexamCyryJbxxService.verifyPicture1(NAME,PHOTO);
            if (!"{}".equals(map.toString())) {
                if(map.containsKey("msg")) {
					String mString = String.valueOf(map.get("msg"));
					json = JsonUtil.jsonStr("msg", mString);
                } else if ("不合格".equals(map.get("TJJG"))) {
                    json = JsonUtil.jsonStr("msg", "请联系有关体检机构，咨询相关信息，谢谢！");
				}else {
					ObjectMapper mapper = new ObjectMapper();
					json = mapper.writeValueAsString(map);
				}
			} else if ("{}".equals(map.toString())) {
				json = JsonUtil.jsonStr("msg", "未查到该人员信息或在办理中");
			} else {
				json = JsonUtil.jsonStr("msg", "该用户没有健康证");
			}
		} else {
			json = JsonUtil.jsonStr("msg", "姓名和头像不正确，请重试！");
		}
		return json;
	}

}
