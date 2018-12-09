package com.aotain.payInterface.service.impl;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.aotain.payInterface.bean.WechatPayBean;
import com.aotain.payInterface.dao.WechatPayMapper;
import com.aotain.payInterface.datasource.DataSourceContextHolder;
import com.aotain.payInterface.service.UserService;
import com.aotain.payInterface.util.Constants;
import com.aotain.payInterface.util.HttpUtil;
import com.aotain.payInterface.util.PayToolUtil;
import com.aotain.payInterface.util.XMLUtil4jdom;

@Named("UserService")
public class UserServiceImpl implements UserService {

	@Autowired
	private WechatPayMapper payDao;

	private static Logger logger = Logger.getLogger(UserServiceImpl.class);

	public String weixinPay(HttpServletRequest request, Map<String, Object> requestParam) throws Exception {
		String productId = (String) requestParam.get("productId");
		String title = Constants.getProValue("title_" + productId);
		// 如果配置文件没有这个商品，直接返回
		if (title == "") {
			return null;
		}
		// 生成随机数
		String currTime = PayToolUtil.getCurrTime();
		String strTime = currTime.substring(8, currTime.length());
		String strRandom = PayToolUtil.buildRandom(4) + "";
		String nonceStr = strTime + strRandom;
		// 商户订单号,自定义
		String outTradeNum = "AT" + currTime + strRandom;
		String appId = Constants.getProValue("appid");
		String mchId = Constants.getProValue("mch_id");
		String notifyUrl = Constants.getProValue("notify_url");
		String tradeType = Constants.getProValue("trade_type");
		String key = Constants.getProValue("api_key");
		String ufdoderUrl = Constants.getProValue("ufdoder_url");
		String fee = Constants.getProValue("fee_" + productId);
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appId);
		packageParams.put("mch_id", mchId);
		packageParams.put("notify_url", notifyUrl);
		packageParams.put("trade_type", tradeType);
		packageParams.put("body", title);
		packageParams.put("total_fee", fee);
		packageParams.put("nonce_str", nonceStr);
		packageParams.put("out_trade_no", outTradeNum);
		String sign = PayToolUtil.createSign("UTF-8", packageParams, key);
		packageParams.put("sign", sign);
		String requestXML = PayToolUtil.getRequestXml(packageParams);
		String resXml = HttpUtil.postData(ufdoderUrl, requestXML);
		// 对数据库操作
		requestParam.put("outTradeNo", outTradeNum);
		requestParam.put("wechatReqData", requestXML);
		requestParam.put("wechatRespData", resXml);
		DataSourceContextHolder.setDbType("greenet");
		payDao.insertPayLog(requestParam);
		// 获取二维码字符串
		Map<String, String> map = XMLUtil4jdom.doXMLParse(resXml);
		String urlCode = map.get("code_url");
		String returnCode = map.get("return_code");
		String result = (String) requestParam.get("result");
		String clientRquestData = request.getQueryString();
		logger.warn("【clientRequest】" + result + "|" + clientRquestData + "【requestToWechatUrl】" + ufdoderUrl + "【requestToWechatParam】" + requestXML + "【responseFrom】" + returnCode + "|" + resXml);
		return urlCode;
	}

	/**
	 * 将微信发送给回调地址的数据写进数据库
	 */
	public void updatePayLog(String outTradeNo, String wechatNotifyFlag, String wechatNotifyData) throws Exception {
		WechatPayBean wPayBean = new WechatPayBean();
		wPayBean.setOutTradeNo(outTradeNo);
		wPayBean.setWechatNotifyFlag(wechatNotifyFlag);
		wPayBean.setWechatNotifyData(wechatNotifyData);
		payDao.updatePayLog(wPayBean);
	}

	public String selectPayUser(String outTradeNo) {
		String phone = payDao.selectPayUser(outTradeNo);
		return phone;
	}

	public void updateNotifyFlag(String outTradeNo) {
		payDao.updateNotifyFlag(outTradeNo);

	}

}
