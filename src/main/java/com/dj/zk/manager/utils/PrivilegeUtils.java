package com.dj.zk.manager.utils;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;



import com.dj.zk.manager.commons.Constants;
import com.dj.zk.manager.config.prop.UserInfo;
import com.dj.zk.manager.enums.UserType;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;


/**
 *
 * @description:权限 URL 配置
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:29:00
 */
public class PrivilegeUtils {


	/**
	 * <url,角色>
	 */
	public static ConcurrentHashMap<String, UserType[]> resourceMap = new ConcurrentHashMap<String, UserType[]>();


	static {
		resourceMap.put("zoo/list", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("zoo/create", new UserType[]{UserType.ADMIN});
		resourceMap.put("zoo/delete", new UserType[]{UserType.ADMIN});
		resourceMap.put("zoo/save", new UserType[]{UserType.ADMIN});
		resourceMap.put("zoo/finddata", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("zoo/nodecopy", new UserType[]{UserType.ADMIN});
		resourceMap.put("used/query", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("zoo/config", new UserType[]{UserType.ADMIN,UserType.COMMON});


		resourceMap.put("login/login", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("index/index", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("page/index", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("page/main", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("page/zoo", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("page/login", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("page/usedapp", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("page/command", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("page/watcher", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("page/connector", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("verify/code", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("command/hostinfo", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("command/execute", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("connector/listconnectors", new UserType[]{UserType.ADMIN,UserType.COMMON});

		resourceMap.put("watcher/listwatcherbyclient", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("watcher/listwatcherbynode", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("watcher/clientInfo", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("page/watcherclient", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("page/monitor", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("monitor/serverstatus", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("monitor/monitorall", new UserType[]{UserType.ADMIN,UserType.COMMON});

	}

	/**
	 * @description 检查用户权限
	 * @param userInfo
	 * @return
	 */
	public static boolean checkPrivilege(HttpServletRequest request, UserInfo userInfo) {
		String uri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String prex = contextPath+Constants.BASE_PATH;
		String res = StringUtils.substringAfterLast(uri, prex);
		UserType [] userTypes = resourceMap.get(res);
		UserType ut = UserType.parse(userInfo.getUserType());
		return ArrayUtils.contains(userTypes, ut);
	}


}
