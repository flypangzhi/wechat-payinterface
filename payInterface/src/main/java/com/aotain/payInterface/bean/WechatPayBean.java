package com.aotain.payInterface.bean;

/**
 * 对greenet_wechat_pay_log表的映射
 * 
 * @author Administrator
 *
 */
public class WechatPayBean {

	private int logId;
	private String phone;
	private int greenIsNotify;
	private String outTradeNo;
	private String wechatReqData;
	private String wechatRespData;
	private String wechatCreatetime;
	private String wechatNotifyFlag;
	private String wechatNotifyData;
	private String wechatNotifytime;
	private int notifyFlag;
	private String greenNotifytime;

	public int getLogId() {
		return logId;
	}

	public void setLogId(int logId) {
		this.logId = logId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getGreenIsNotify() {
		return greenIsNotify;
	}

	public void setGreenIsNotify(int greenIsNotify) {
		this.greenIsNotify = greenIsNotify;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getWechatReqData() {
		return wechatReqData;
	}

	public void setWechatReqData(String wechatReqData) {
		this.wechatReqData = wechatReqData;
	}

	public String getWechatRespData() {
		return wechatRespData;
	}

	public void setWechatRespData(String wechatRespData) {
		this.wechatRespData = wechatRespData;
	}

	public String getWechatCreatetime() {
		return wechatCreatetime;
	}

	public void setWechatCreatetime(String wechatCreatetime) {
		this.wechatCreatetime = wechatCreatetime;
	}

	public String getWechatNotifyFlag() {
		return wechatNotifyFlag;
	}

	public void setWechatNotifyFlag(String wechatNotifyFlag) {
		this.wechatNotifyFlag = wechatNotifyFlag;
	}

	public String getWechatNotifyData() {
		return wechatNotifyData;
	}

	public void setWechatNotifyData(String wechatNotifyData) {
		this.wechatNotifyData = wechatNotifyData;
	}

	public String getWechatNotifytime() {
		return wechatNotifytime;
	}

	public void setWechatNotifytime(String wechatNotifytime) {
		this.wechatNotifytime = wechatNotifytime;
	}

	public int getNotifyFlag() {
		return notifyFlag;
	}

	public void setNotifyFlag(int notifyFlag) {
		this.notifyFlag = notifyFlag;
	}

	public String getGreenNotifytime() {
		return greenNotifytime;
	}

	public void setGreenNotifytime(String greenNotifytime) {
		this.greenNotifytime = greenNotifytime;
	}

}
