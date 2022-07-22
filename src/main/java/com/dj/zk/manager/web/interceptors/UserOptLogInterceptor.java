package com.dj.zk.manager.web.interceptors;

import com.dj.zk.manager.commons.Constants;
import com.dj.zk.manager.config.prop.UserInfo;
import com.dj.zk.manager.config.prop.ZkProperties;
import com.dj.zk.manager.utils.DateUtils;
import com.dj.zk.manager.utils.NetUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Enumeration;

/**
 *
 * @description:日志过滤
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:29:29
 */
@Slf4j
public class UserOptLogInterceptor implements AsyncHandlerInterceptor {

	/**
	 * 分隔符
	 */
	private static final String SPLITTER = ";";
	private String excludeStr = "*.js;*.jpg;*.htm;*.html;*.gif;*.png;*.css;*.swf";

	private ZkProperties zkProperties;

	public UserOptLogInterceptor(ZkProperties zkProperties) {
		this.zkProperties = zkProperties;
	}

	private boolean matchUrls(ServletRequest request, String  matchs) {
		try {
			if(StringUtils.isEmpty(matchs)) {
				return false;
			}
			String[] excludes = matchs.split(SPLITTER);
			HttpServletRequest httprequest = (HttpServletRequest) request;
			for (int i = 0; i < excludes.length; i++) {
				String path = httprequest.getRequestURI();
				String contextPath = httprequest.getContextPath();
				String regx = excludes[i].replaceAll("\\.", "\\\\.");
				regx = regx.replaceAll("\\*", "\\.*");
				if (excludes[i].endsWith("/")) {
					regx = regx + ".*";
				}
				if (regx.startsWith("/")) {
					regx = regx.replaceFirst("/", contextPath + "/");
					regx = regx.replaceAll("//", "/");
				}
				if (path.matches(regx)) {
					return true;
				}

			}
		} catch (Throwable localThrowable) {
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		try {
			zkProperties.setEnableLog(false);
			if(!zkProperties.getEnableLog()) {
				return true;
			}
			if (matchUrls(request,excludeStr)) {
				return true;
			}

			StringBuffer buf = new StringBuffer();
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Object userObj = request.getSession().getAttribute(Constants.USER_INFO_SESSION);
			if(userObj != null) {
				String userName = ((UserInfo)userObj).getUserName();
				buf.append("用户:"+userName+"---");
			}
 			String requestIp = NetUtils.getRequestIp(request);
 			buf.append("调用者IP:"+requestIp+"---");
			String requestTime = DateUtils.format(new Date(), DateUtils.DATE_HH_MM_SS);
			buf.append("时间:"+requestTime+"---");
			String url = request.getRequestURI().toString();
			buf.append("调用URI:"+url+"---");
			String className = handlerMethod.getBean().getClass().getName();
			String methodName = handlerMethod.getMethod().getName();
			buf.append("调用类及方法:"+className+"."+methodName+"---");
			buf.append("【");
			Enumeration<String> paramNames = request.getParameterNames();
			while(paramNames.hasMoreElements()) {
				String name = paramNames.nextElement();
				String nameValue = request.getParameter(name);
				buf.append(name).append("=").append(nameValue).append("|");
			}
			buf.append("】");
			log.info(buf.toString());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return true;
	}




}
