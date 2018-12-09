package com.aotain.payInterface.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class TradeNumUtil {

	/**
	 * 用于生成20位的商户订单号
	 * @author Administrator
	 *
	 */
	public static String getNumString() {
		
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		Random random = new Random();
		int x = random.nextInt(9999 - 1000 + 1) + 1000;
		String y = String.valueOf(x);
		String numString = "AT"+dateString+y;
		return numString;

	}

}
