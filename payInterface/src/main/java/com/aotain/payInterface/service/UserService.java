package com.aotain.payInterface.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.aotain.payInterface.bean.WechatPayBean;

/**
 * 
 * @author xgchen
 *
 */
public interface UserService {
	
	String weixinPay( HttpServletRequest request,Map<String, Object> requestParam) throws Exception;
	
	void updatePayLog(String outTradeNo,String wechatNotifyFlag,String wechatNotifyData) throws Exception;
	
	String selectPayUser(String outTradeNo);
	
	void updateNotifyFlag(String outTradeNo);
}
