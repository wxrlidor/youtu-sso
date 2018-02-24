package com.youtu.sso.service;
/**
 *@author:王贤锐
 *@date:2018年1月23日  下午3:37:54
**/

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.youtu.common.pojo.YouTuResult;
import com.youtu.pojo.TbUser;

public interface UserService {
	YouTuResult checkData(String data, Integer type);

	YouTuResult registerUser(TbUser user);

	YouTuResult login(String username, String password, HttpServletRequest request, HttpServletResponse response);

	YouTuResult getUserByToken(String token);
	
	YouTuResult userLoginOut(String token);
}
