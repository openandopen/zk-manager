package com.dj.zk.manager.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.dj.zk.manager.config.prop.UserInfo;
import com.dj.zk.manager.utils.json.JsonUtils;
import com.dj.zk.manager.utils.response.ResponseUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.dj.zk.manager.commons.Constants;

import com.dj.zk.manager.service.UserInfoService;

import java.util.HashMap;
import java.util.Map;


/**
 *
 * @description:登录Action
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:20:16
 */
@Controller
@RequestMapping(value = Constants.BASE_PATH + "login")
public class LoginAction {

	@Autowired
	private UserInfoService userInfoService;



	@RequestMapping(value = "login", method = {RequestMethod.POST,RequestMethod.GET})
	public void login(HttpServletRequest request, HttpServletResponse response, UserInfo userInfo, ModelMap model) {
//		String verifyCode = request.getParameter("verifyCode");
//		if(!verifyCode.equals(request.getSession().getAttribute(Constants.VERIFY_CODE))) {
//			model.clear();
//			model.put("msg","failse");
//			return;
//		}

 		if(userInfo != null && StringUtils.isNotEmpty(userInfo.getUserName()) && StringUtils.isNotEmpty(userInfo.getUserPwd())) {
			UserInfo ui =userInfoService.findByUserNameAndPwd(userInfo.getUserName(),userInfo.getUserPwd());
	        if(ui != null && StringUtils.isNotBlank(ui.getUserName())) {
	        	request.getSession().setAttribute(Constants.USER_INFO_SESSION, ui);
	        	request.getSession().removeAttribute(Constants.VERIFY_CODE);
				model.clear();
				model.put("msg","ok");
				Map<String,String> params = new HashMap<>();
				params.put("msg","ok");
				ResponseUtils.responseJson(response, JsonUtils.toJson(params));
				return;
	        }
		}
		model.clear();
		model.put("msg","failse");
		Map<String,String> params = new HashMap<>();
		params.put("msg","failse");
		ResponseUtils.responseJson(response, JsonUtils.toJson(params));
	}

	@RequestMapping(value = "logout", method = {RequestMethod.GET})
	public ModelAndView logout(HttpServletRequest request) {
 		request.getSession().removeAttribute(Constants.USER_INFO_SESSION);
		return new ModelAndView("login/login");
	}


}
