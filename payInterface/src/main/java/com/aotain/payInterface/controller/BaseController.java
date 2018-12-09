package com.aotain.payInterface.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.aotain.payInterface.bean.ConstantBean;
import com.aotain.payInterface.util.Constants;
import com.aotain.payInterface.util.Tools;

@SuppressWarnings("unchecked")
public class BaseController {

	public void outPrint(HttpServletResponse response, String result) throws IOException {
		PrintWriter out = response.getWriter();
		out.print(result);
		out.flush();
		out.close();
	}

	protected Map<String, Object> success(Object data) {
		return toMap("data", data, "result", ConstantBean.SUCCESS);
	}

	protected Map<String, Object> error(Object data) {
		return toMap("data", data, "result", ConstantBean.ERROR);
	}

	protected Map<String, Object> error(Throwable t) {
		return toMap("data", t.getMessage(), "result", ConstantBean.ERROR);
	}

	public static Map toMap(Object... params) {
		Map map = new LinkedHashMap();
		Assert.notNull(params);
		Assert.isTrue(params.length % 2 == 0);
		for (int i = 0; i < params.length; i++) {
			map.put(params[i++], params[i]);
		}
		return map;
	}

	/**
	 * 对请求进行验证，符合的就放行
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> parseRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String productId = request.getParameter("productId");
		String phone = request.getParameter("phone");
		String isnotify = request.getParameter("isnotify");
		String timestamp = request.getParameter("timestamp");
		String authenticator = request.getParameter("auth");
		Map<String, Object> parameter = new HashMap<String, Object>();
		String result = "失败，产品ID错误";
		int resultCode = -1;
		String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
		Pattern p = Pattern.compile(regExp);

		/**
		 * 判断时间戳，超过两小时请求不处理
		 */
		if (phone != null) {
			Matcher m = p.matcher(phone);
			if (m.matches()) {
				if (productId != null && timestamp != null && isnotify != null && authenticator != null) {
					long reqTimesta = Long.parseLong(timestamp);
					long difftime = (System.currentTimeMillis() - reqTimesta) / (1000 * 60);
					if (difftime < 120) {
						String secertKey = Constants.getProValue("secertKey");
						String beforeAuth = productId + "$" + phone + "$" + isnotify + "$" + timestamp + "$" + secertKey;
						String auth = Tools.getBase64Code(Tools.GetMD5Codes(beforeAuth));
						if (auth.equals(authenticator)) {
							parameter.put("productId", productId);
							parameter.put("phone", phone);
							parameter.put("greenIsNotify", Integer.parseInt(isnotify));
							result = "成功";
							resultCode = 0;
						} else {
							result = "失败，身份验证失败";
							resultCode = 1;
						}
					} else {
						result = "失败，时间戳不正确";
						resultCode = 1;
					}

				}
			}
		}

		parameter.put("result", result);
		parameter.put("resultCode", resultCode);
		return parameter;
	}

}
