package com.aotain.payInterface.bean;

public class ResultEntity<T> {

	public int resultCode=9999;	
	
	public int id;

	public T resultData;

	public String errorMsg = "";
	
	public String appId;
	
	public long timestamp;
	
	public String nonceStr;
	
	public String signature;
	
	public String prepayId;
	
	public String paySign;
	

}


