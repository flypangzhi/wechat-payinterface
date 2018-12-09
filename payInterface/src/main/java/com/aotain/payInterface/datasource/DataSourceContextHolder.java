package com.aotain.payInterface.datasource;

/**
 * 当配置了多个数据源，可以使用这个来切换
 * @author Administrator
 *
 */
public class DataSourceContextHolder {
	/**
	 * 使用本地线程的方法
	 */
	private static final ThreadLocal<String> contextHolder=new ThreadLocal<String>();
		public static void setDbType(String dbType){
			contextHolder.set(dbType);
		}
		public static String getDbType(){
			return (contextHolder.get());
		}
		public static void clearDbType(){
			contextHolder.remove();
		}
}
