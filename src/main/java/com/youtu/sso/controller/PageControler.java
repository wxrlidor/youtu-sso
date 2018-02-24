package com.youtu.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面跳转控制器
 *@author:王贤锐
 *@date:2018年1月24日  上午10:27:27
**/
@Controller
@RequestMapping("/page")
public class PageControler {

	@RequestMapping("/register")
	public String showRegister(){
		return "register";
	}
	/**
	 * 跳转到登录页面，并且接收回调url
	 * @param redirect
	 * @param model
	 * @return
	 */
	@RequestMapping("/login")
	public String showlogin(String redirect,Model model){
		//把回调的url传给login.jsp，执行登陆操作后跳转到回调页面
		model.addAttribute("redirect", redirect);
		return "login";
	}
}
