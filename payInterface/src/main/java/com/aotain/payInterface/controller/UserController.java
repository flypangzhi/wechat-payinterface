package com.aotain.payInterface.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aotain.payInterface.service.UserService;
import com.aotain.payInterface.util.Constants;
import com.aotain.payInterface.util.PayToolUtil;
import com.aotain.payInterface.util.QRUtil;
import com.aotain.payInterface.util.XMLUtil4jdom;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * 获取微信支付二维码以及给微信回复消息
 * 
 * @author
 *
 */
@Controller
@RequestMapping("/wechat")
public class UserController extends BaseController {

	private static Logger logger = Logger.getLogger(UserController.class);

	@Inject
	UserService userApplication;

	/**
	 * 获取二维码并返回
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 */
	@ResponseBody
	@RequestMapping("/requestCode.do")
	public void qrcode(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		try {
			String resResult = "{\"returnCode\":\"-1\",\"returnMsg\":\"productId is error\"}";
			Map<String, Object> requestParam = parseRequest(request, response);
			int resultCode = (Integer) requestParam.get("resultCode");
			String resultMsg = (String) requestParam.get("result");
			String clientRquestData = request.getQueryString();
			switch (resultCode) {
			case -1:
				outPrint(response, resResult);
				logger.warn("【clientRequest】" + resultMsg + "|" + clientRquestData);
				break;
			case 1:
				resResult = "{\"returnCode\":\"1\",\"returnMsg\":\"parameter is error\"}";
				logger.warn("【clientRequest】" + resultMsg + "|" + clientRquestData);
				outPrint(response, resResult);
				break;
			case 0:
				String text = userApplication.weixinPay(request, requestParam);
				if (text != null && text.length() > 0) {
					int width = 300;
					int height = 300;
					// 二维码的图片格式
					String format = "gif";
					Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
					// 内容所使用编码
					hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
					BitMatrix bitMatrix;
					try {
						bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
						QRUtil.writeToStream(bitMatrix, format, response.getOutputStream());
					} catch (WriterException e) {
						resResult = "{\"returnCode\":\"2\",\"returnMsg\":\"system exception\"}";
						outPrint(response, resResult);
					}
				} else {
					logger.warn("【clientRequest】" + resultMsg + "|" + clientRquestData);
					resResult = "{\"returnCode\":\"-1\",\"returnMsg\":\"productId is error\"}";
					outPrint(response, resResult);
				}
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 微信平台发起的回调方法， 调用我们这个系统的这个方法接口，将扫描支付的处理结果告知我们系统
	 * 
	 * @throws JDOMException
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/hadPay.do")
	public void weixinNotify(HttpServletRequest request, HttpServletResponse response) throws JDOMException, Exception {
		// 读取参数
		InputStream inputStream;
		StringBuffer sb = new StringBuffer();
		inputStream = request.getInputStream();
		String s;
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		while ((s = in.readLine()) != null) {
			sb.append(s);
		}
		in.close();
		inputStream.close();

		// 解析xml成map
		Map<String, String> m = new HashMap<String, String>();
		m = XMLUtil4jdom.doXMLParse(sb.toString());
		// 过滤空 设置 TreeMap
		SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
		Iterator<String> it = m.keySet().iterator();
		while (it.hasNext()) {
			String parameter = (String) it.next();
			String parameterValue = m.get(parameter);
			String v = "";
			if (null != parameterValue) {
				v = parameterValue.trim();
			}
			packageParams.put(parameter, v);
		}

		// 账号信息
		String key = Constants.getProValue("api_key");

		if (PayToolUtil.isTenpaySign("UTF-8", packageParams, key)) {
			// ------------------------------
			// 处理业务开始
			// ------------------------------
			String resXml = "";
			if ("SUCCESS".equals((String) packageParams.get("result_code"))) {
				// 通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
				logger.warn("【notifyGreenetUrl】http://wechatpay.greenet.cn:8101/payInterface/wechat/hadPay.do" + "【notifyGreenetResponseFromWechat】SUCCESS|" + sb.toString());
				resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";

			} else {
				logger.warn("【notifyUrl】http://wechatpay.greenet.cn:8101/payInterface/wechat/hadPay.do" + "【notifyResponse】FAIL|" + sb.toString());
				resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
			}
			// 对数据库进行操作
			String outTradeNo = (String) packageParams.get("out_trade_no");
			String wechatNotifyFlag = (String) packageParams.get("return_code");
			String wechatNotifyData = sb.toString();
			userApplication.updatePayLog(outTradeNo, wechatNotifyFlag, wechatNotifyData);

			// ------------------------------
			// 处理业务完毕
			// ------------------------------
			BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
			out.write(resXml.getBytes());
			out.flush();
			out.close();
		} else {
			logger.debug("校验签名失败");
		}

	}

}
