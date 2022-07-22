package com.dj.zk.manager.commons;

import java.util.HashMap;
import java.util.Map;



/**
 * @description:常量
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:22:08
 */
public abstract class Constants {

	public final static String BASE_PATH = "/zookeeper/";

	public final static String ZOOKEEPER_CONNECT_URL = "zookeeper.connect.url";

	/**
	 * 用户session
	 */
	public static final String USER_INFO_SESSION = "user_info_session";



	/**
	 * digest密钥
	 */
//	public final static String ZOO_DIGEST_SECRET = "config:zooadmin";

	public final static String ZOO_DIGEST_SECRET_="zoo.digest.secret";

	/**
	 * 全局密钥(暂时没用)
	 */
//	public final static String ZOO_GLOBAL_SECRET = "super:jzORiXADeFIm9iTxrMVzeI5GLJE=";

	public final static String charset = "UTF-8";

	public final static String VERIFY_CODE = "verifyCode";

	/**
	 * 存储所有数据信息(根路径)
	 */
	public final static String ZOO_CONF_DATA = "/confusers";

	public final static String MD5_SALT="zuiwoxing:";




	static Map<String, String> zooCommandMap = new HashMap<String, String>();





	static {


		zooCommandMap.put("conf", "conf_取得配置文件信息");
		zooCommandMap.put("cons", "cons_取得连接信息");
//		zooCommandMap.put("crst", "crst_重置所有统计信息");
		zooCommandMap.put("dump", "dump_取得所有等待会话");
		zooCommandMap.put("envi", "envi_取得zkServer启动信息");
		zooCommandMap.put("ruok", "ruok_检查服务是否正常");
		zooCommandMap.put("stat", "stat_取得连接状态及相关信息");
		zooCommandMap.put("srvr", "srvr_获取统计信息");
//		zooCommandMap.put("srst", "srst_重置Server统计信息");
		zooCommandMap.put("wchs", "wchs_取得Watcher统计信息");
		zooCommandMap.put("wchc", "wchc_Watcher信息(按Session分组)");
		zooCommandMap.put("wchp", "wchp_Watcher信息(按节点分组)");
		zooCommandMap.put("mntr", "mntr_取得统计总信息");
	}







	public static Map<String, String> getZooCommandMap() {
		return zooCommandMap;
	}


}
