package com.aotain.payInterface.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class Constants {
	private static Properties pro = new Properties();
	static {
		InputStream in = Constants.class.getClassLoader().getResourceAsStream("conf/weixinpay.properties");
		try {                                                       
			pro.load(new InputStreamReader(in,"UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	  /**
     * 读取配置文件 common.properties
     * @param source
     * @return
     */
    public static String getProValue(String source){
    	return pro.getProperty(source)==null?"":pro.getProperty(source);
    }
}
