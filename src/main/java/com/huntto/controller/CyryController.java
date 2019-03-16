package com.huntto.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis.providers.java.MsgProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huntto.entity.CyryVo1;
import com.huntto.entity.CyryVo2;
import com.huntto.entity.FeedBack;
import com.huntto.service.CyryService;
import com.huntto.service.FeedBackService;
import com.huntto.service.OlexamCyryJbxxService;
import com.huntto.util.IdCardUtil;
import com.huntto.util.JsonUtil;
import com.huntto.util.Nulls;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * Description: 从业人员接口 <br/>
 * Copyright: 版权归浙江创得信息技术有限公司所有<br/>
 * Project_name jkzjk<br/>
 * Package_name com.huntto.controller<br/>
 * Author 梁城市<br/>
 * Email city_wangyi@163.com<br/>
 * Create_time 2018/12/10 15:40<br/>
 */
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
		String json = "";
		if (Nulls.validateValue(idCard) && IdCardUtil.isIdcard(idCard)) {
			Map map;
			map = cyryService.selectCYRYVo1(idCard);
			if (null != map && !"{}".equals(map.toString())) {
				ObjectMapper mapper = new ObjectMapper();
				json = mapper.writeValueAsString(map);
			} else if ("{}".equals(map.toString())) {
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
		String json = "";
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
			} else if ("{}".equals(map.toString())) {
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
		Map map;
		String json = "";
		ObjectMapper mapper;
		if(Nulls.isNotEmpty(NAME) && Nulls.isEmpty(idCard)) {// 姓名不为空，身份证号为空
			map = olexamCyryJbxxService.getCYRYbyName1(NAME);
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
				
				map = toMapList(list,map);
				if (null != map && !"{}".equals(map.toString())) {
					mapper = new ObjectMapper();
					json = mapper.writeValueAsString(map);
				} else if ("{}".equals(map.toString())) {
					json = JsonUtil.jsonStr("msg", "未查到该人员信息或在办理中");
				} else {
					json = JsonUtil.jsonStr("msg", "该用户没有健康证");
				}
			}else if (null != map && !"{}".equals(map.toString())) {
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
				} else if ("{}".equals(map.toString())) {
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

		return json;
	}
	
	public Map toMapList(List<CyryVo1> list, Map map) {
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
	 * @return
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
		String json = "";
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
		String json = "";
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
	 * @param FBType    反馈类型
	 * @param FBContent 反馈内容
	 * @return
	 * @throws JsonProcessingException JsonProcessingException
	 */
	@ApiOperation(value = "更新已处理字段接口", notes = "更新反馈是否处理信息")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "IS_CL", value = "是否处理字段", dataType = "String", required = true),
			@ApiImplicitParam(paramType = "query", name = "ID", value = "反馈数据ID", dataType = "String", required = true)
	})
	@RequestMapping(value = { "/updateFeedBack" }, method = RequestMethod.POST)
	public String updateFeedBack(FeedBack feedBack) throws JsonProcessingException {
		String json = "";
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
		String json = "";
		if (Nulls.validateValue(NAME,PHOTO)) {
			Map map = olexamCyryJbxxService.verifyPicture(NAME,PHOTO);
			if (null != map && !"{}".equals(map.toString())) {
				if(map.containsKey("msg")) {
					String mString = String.valueOf(map.get("msg"));
					json = JsonUtil.jsonStr("msg", mString);
				}
				if("不合格".equals(map.get("TJJG"))) {
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
		String json = "";
		if (Nulls.validateValue(NAME,PHOTO)) {
			Map map = new HashMap<>();
//			map = olexamCyryJbxxService.verifyPicture1(NAME,PHOTO);
			if (null != map && !"{}".equals(map.toString())) {
				if(map.containsKey("msg")) {
					String mString = String.valueOf(map.get("msg"));
					json = JsonUtil.jsonStr("msg", mString);
				}
				if("不合格".equals(map.get("TJJG"))) {
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
