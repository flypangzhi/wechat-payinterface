package com.aotain.payInterface.dao;

import java.util.List;
import java.util.Map;

import com.aotain.payInterface.bean.WechatPayBean;

public interface WechatPayMapper {

	int insertPayLog(Map<String, Object> parameter);
	
	void updatePayLog(WechatPayBean wPayBean);
	
	String  selectPayUser(String outTradeNo) ;
	
	void updateNotifyFlag(String outTradeNo);
}
