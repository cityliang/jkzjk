package com.huntto.scheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.huntto.config.WeiXinConfig;
import com.huntto.util.WeiXinUtil;
import com.huntto.util.WxUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WxScheduled {
	
	@Autowired
	private WeiXinConfig wConfig;
	
	@Autowired
	private WeiXinUtil wUtil;
	
	@Autowired
	private WxUtil wxUtil;
	static int count = 1;
	
	/**
	 * 定时获取微信 token 和 jsTicket
	 * 服务器启动时执行一次，之后每隔一个小时59分执行一次。
	 * @throws Exception
	 */
	@Scheduled(fixedRate=1000*60*59*2)//服务器启动时执行一次，之后每隔一个小时59分执行一次。
	public void updateWx() throws Exception {
		
		log.info("定时任务执行前的 ACCESS_TOKEN: "+wUtil.WX_ACCESS_TOKEN);
		log.info("定时任务执行前的 TICKET: "+wUtil.WX_TICKET);
//		String token=wxUtil.getAccessToken();
		String token = wUtil.getAccess_token();
		wUtil.WX_ACCESS_TOKEN = token;
//		String jsTicket=wxUtil.getJsapiTicket();
		String jsTicket = wUtil.getWXJsapiTicket();
		wUtil.WX_TICKET = jsTicket;
		log.info("定时获取微信执行了"+count+"次");
		count++;
		log.info("定时任务执行后的 ACCESS_TOKEN: "+wUtil.WX_ACCESS_TOKEN);
		log.info("定时任务执行后的 TICKET: "+wUtil.WX_TICKET);
	}
}
