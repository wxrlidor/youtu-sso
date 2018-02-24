package com.youtu.sso.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.youtu.common.pojo.YouTuResult;
import com.youtu.common.utils.ExceptionUtil;
import com.youtu.pojo.TbUser;
import com.youtu.sso.service.UserService;

/**
 * @author:王贤锐
 * @date:2018年1月23日 下午3:44:20
 **/
@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

	/**
	 * 从url中接收两个参数，调用Service进行校验，在调用Service之前，先对参数进行校验，
	 * 例如type必须是1、2、3、4其中之一。返回TaotaoResult。需要支持jsonp。
	 * 
	 * @param data
	 * @param type
	 * @return
	 */
	@RequestMapping("/check/{data}/{type}")
	@ResponseBody
	public Object checkData(@PathVariable String data, @PathVariable Integer type, String callback) {
		YouTuResult result = null;
		// 检验数据和数据类型的有效性，其实当其中一个为空时，都不能执行这个get
		/*
		 * if(StringUtils.isBlank(data)){ result = YouTuResult.build(400,
		 * "校验数据不能为空"); } if(type == null ){ result = YouTuResult.build(400,
		 * "校验类型不能为空"); }
		 */
		if (type != 1 && type != 2 && type != 3 && type != 4) {
			result = YouTuResult.build(400, "校验类型必须是1，2，3，4");
		}
		// 如果前面校验通过，则调用service
		if (null == result) {
			try {
				result = userService.checkData(data, type);
			} catch (Exception e) {
				e.printStackTrace();
				result = YouTuResult.build(500, ExceptionUtil.getStackTrace(e));
			}
		}
		// 判断是否有callback，决定返回的是jsonp数据还是youtuResult
		if (!StringUtils.isBlank(callback)) {
			// 把返回结果包装成jsonp
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		} else {
			// 如果没有回调方法，返回youtuResult
			return result;
		}

	}

	/**
	 * 使用tbUser接收参数，再逐一校验username、phone、email、昵称是否通过，
	 * 通过则调用service添加数据，返回youturesult结果
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public YouTuResult registerUser(TbUser user) {
		try {

			// 先依次校验username、phone、email、nickName
			YouTuResult userNameCheck = userService.checkData(user.getUsername(), 1);
			if (!(boolean) userNameCheck.getData()) {
				return YouTuResult.build(401, "用户名已存在，不可用");
			}
			YouTuResult nickCheck = userService.checkData(user.getNickname(),4);
			if(!(boolean)nickCheck.getData()){
				return YouTuResult.build(404, "昵称已存在，不可用");
			}
			YouTuResult phoneCheck = userService.checkData(user.getPhone(), 2);
			if (!(boolean) phoneCheck.getData()) {
				return YouTuResult.build(402, "手机号码已存在，不可用");
			}
			YouTuResult emailCheck = userService.checkData(user.getEmail(), 3);
			if (!(boolean) emailCheck.getData()) {
				return YouTuResult.build(403, "电子邮箱已存在，不可用");
			}
			
			// 校验通过，执行注册
			YouTuResult result = userService.registerUser(user);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return YouTuResult.build(400, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 接收表单，包含用户、密码。调用Service进行登录返回TaotaoResult。
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public YouTuResult userLogin(String username, String password,
			HttpServletRequest request,HttpServletResponse response) {
		try {

			YouTuResult result = userService.login(username, password,request,response);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return YouTuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	/**
	 * 接收token调用Service返回用户信息，使用TaotaoResult包装。
	 * @param token
	 * @param callback
	 * @return
	 */
	@RequestMapping("/token/{token}")
	@ResponseBody
	public Object getUserByToken(@PathVariable String token, String callback) {
		YouTuResult result = null;
		try {
			result = userService.getUserByToken(token);
		} catch (Exception e) {
			e.printStackTrace();
			result = YouTuResult.build(500, ExceptionUtil.getStackTrace(e));
		}

		// 判断是否为jsonp调用
		if (StringUtils.isBlank(callback)) {
			return result;
		} else {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}

	}
	/**
	 * 接收token调用Service删除用户session，使用TaotaoResult包装。
	 * @param token
	 * @param callback
	 * @return
	 */
	@RequestMapping("/logout/{token}")
	@ResponseBody
	public Object logout(@PathVariable String token, String callback,HttpServletResponse response) {
		YouTuResult result = null;
		try {
			result = userService.userLoginOut(token);
		} catch (Exception e) {
			e.printStackTrace();
			result = YouTuResult.build(500, ExceptionUtil.getStackTrace(e));
		}

		// 判断是否为jsonp调用
		if (StringUtils.isBlank(callback)) {
			try {
				response.sendRedirect("http://localhost:8082");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		} else {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}

	}
}
