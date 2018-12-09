package com.aotain.payInterface.util;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sun.misc.*;  

import java.io.File;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;



public class Tools {
	
	
	public static void main(String[] args) {
		long s = System.currentTimeMillis();
		System.out.println("s:"+s);
		String time = s + "";
		String code = "wx_51talk_000" +"$" +"15767544315" + "$" + "1" + "$" + time +"$"+ "aotain_wechat_pay_code_123456xx";
		String x = Tools.GetMD5Codes(code);
		String y = Tools.getBase64Code(x);
		System.out.println(y);
	
		
	}
	
	// 加密  
    @SuppressWarnings("restriction")
	public static String getBase64Code(String str) {  
        byte[] b = null;  
        String s = null;  
        try {  
            b = str.getBytes("utf-8");  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
        if (b != null) {  
            s = new BASE64Encoder().encode(b);  
        }  
        return s;  
    }  
  
    // 解密  
    @SuppressWarnings("restriction")
	public static String getFromBase64Value(String s) {  
        byte[] b = null;  
        String result = null;  
        if (s != null) {  
            BASE64Decoder decoder = new BASE64Decoder();  
            try {  
                b = decoder.decodeBuffer(s);  
                result = new String(b, "utf-8");  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        return result;  
    }  
	   
	   public static long ConvetIp(String strIp){
		   if(strIp.equals("0:0:0:0:0:0:0:1")) strIp="127.0.0.1";
		   long[] ip = new long[4];
	       int position1 = strIp.indexOf(".");
	       int position2 = strIp.indexOf(".", position1 + 1);
	       int position3 = strIp.indexOf(".", position2 + 1);
	       ip[0] = Long.parseLong(strIp.substring(0, position1));
	       ip[1] = Long.parseLong(strIp.substring(position1+1, position2));
	       ip[2] = Long.parseLong(strIp.substring(position2+1, position3));
	       ip[3] = Long.parseLong(strIp.substring(position3+1));
	       return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
	   }
	   
	   //MD5加密
	   public final static String GetMD5Codes(String s) {   
		   char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',   
		     'a', 'b', 'c', 'd', 'e', 'f' };   
		   try {   
		    byte[] strTemp = s.getBytes();   
		    MessageDigest mdTemp = MessageDigest.getInstance("MD5");   
		    mdTemp.update(strTemp);   
		    byte[] md = mdTemp.digest();   
		    int j = md.length;   
		    char str[] = new char[j * 2];   
		    int k = 0;   
		    for (int i = 0; i < j; i++) {   
		     byte byte0 = md[i];   
		     str[k++] = hexDigits[byte0 >>> 4 & 0xf];   
		     str[k++] = hexDigits[byte0 & 0xf];   
		    }   
		    return new String(str).toUpperCase();   
		   } catch (Exception e) {   
		    return null;   
		   }   
	  }   
	   
	   //MD5加密
	   public final static String GetMD5Codes(String s,String key) {   
		   return GetMD5Codes(s+key).toLowerCase();
	   }


	 /// <summary>
	    /// 获取字符串型数据
	    /// </summary>
	    /// <param name="Obj">字符串型对象</param>
	    /// <param name="defaultVal">如果对象不能转换为字符串型，则返回一个默认值</param>
	    /// <returns></returns>
	    public static String GetString(Object Obj, String defaultVal)
	    {
	        try
	        {
	            if (Obj == null || StringUtil.isEmptyString(Obj.toString()))
	                return defaultVal;
	            return  Obj.toString();
	        }
	        catch(Exception e)
	        {
	            return defaultVal;
	        }
	    }
	   
	    /* 获得客户端IP
	     * @param request
	     * @return
	     */
	    public static String getIpAddr(HttpServletRequest request) { 
	        String ip = request.getHeader("x-forwarded-for"); 
	        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	            ip = request.getHeader("Proxy-Client-IP"); 
	        } 
	        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	            ip = request.getHeader("WL-Proxy-Client-IP"); 
	        } 
	        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	            ip = request.getRemoteAddr(); 
	        } 
	        return ip; 
	    } 
	    
	    /// <summary>
	    /// 获取长整型数据
	    /// </summary>
	    /// <param name="Obj">数据对象</param>
	    /// <param name="defaultVal">如果对象不能转换为整型，则返回一个默认值</param>
	    /// <returns></returns>
	    public static long GetDateLong(Object Obj, long defaultVal)
	    {
	        try
	        {
	            if (Obj == null || StringUtil.isEmptyString(Obj.toString()))
	                return defaultVal;
	            Date d = DateUtils.parseDate(Obj.toString(), "yyyy-MM-dd");
	            if(d == null) return defaultVal;
	            else return d.getTime()/1000;
	        }
	        catch(Exception e)
	        {
	            return defaultVal;
	        }
	    }

		/**
		 * 判断referer是否在白名单设置IP内
		 * @param request
		 * @return
		 */
		 public static boolean ValidReferer( HttpServletRequest request) {		
			String referer = request.getHeader("referer");
			if(referer == null) return false; 
			List<String> host=ReadAllowIPXml();
			if(host!=null&&host.size()>0){
				for(int i =0;i<host.size();i++){
					if (referer.startsWith("http://"+host.get(i))) return true;
				}
			}
	        return false;
	    }
		 
	private static List<String> ReadAllowIPXml()
	{
		List<String> strs=null;
		try {
			File f = new File(Tools.class.getClassLoader().getResource("conf/allowIP.xml").getPath()); 
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
			DocumentBuilder builder = factory.newDocumentBuilder(); 
			Document doc = builder.parse(f); 
			NodeList nl = doc.getElementsByTagName("ip"); 
			if(nl!=null&&nl.getLength()>0) strs=new ArrayList<String>();			
			for (int i = 0; i < nl.getLength(); i++) { 				
				strs.add(nl.item(i).getFirstChild().getNodeValue());
			} 				
		} catch (Exception e) { 
			e.printStackTrace(); 
		} 
		return strs;
	}		
}
