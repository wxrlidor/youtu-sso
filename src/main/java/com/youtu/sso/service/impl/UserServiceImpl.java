package com.youtu.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.youtu.common.pojo.YouTuResult;
import com.youtu.common.utils.CookieUtils;
import com.youtu.common.utils.JsonUtils;
import com.youtu.mapper.TbUserMapper;
import com.youtu.pojo.TbUser;
import com.youtu.pojo.TbUserExample;
import com.youtu.pojo.TbUserExample.Criteria;
import com.youtu.sso.dao.JedisClient;
import com.youtu.sso.service.UserService;

/**
 * 用户相关service
 * 
 * @author:王贤锐
 * @date:2018年1月23日 下午3:39:03
 **/
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private TbUserMapper userMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${REDIS_USER_SESSION_KEY}")
	private String REDIS_USER_SESSION_KEY;
	@Value("${SSO_SESSION_EXPIRE}")
	private Integer SSO_SESSION_EXPIRE;

	/**
	 * 接收两个参数：内容、内容类型。根据内容类型查询tb_user表返回Taotaoresult对象。
	 * Data属性值：返回数据，true：数据可用，false：数据不可用
	 */
	@Override
	public YouTuResult checkData(String data, Integer type) {
		// 创建查询条件
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		// 对数据进行校验：1、2、3,4分别代表username、phone、email、nickName
		// 用户名校验
		if (1 == type) {
			criteria.andUsernameEqualTo(data);
			// 电话校验
		} else if (2 == type) {
			criteria.andPhoneEqualTo(data);
			// email校验
		} else if(3== type){
			criteria.andEmailEqualTo(data);
		}else if(4==type){
			//昵称校验
			criteria.andNicknameEqualTo(data);
		}
		// 执行查询
		List<TbUser> list = userMapper.selectByExample(example);
		if (list == null || list.size() == 0) {
			return YouTuResult.ok(true);
		}
		return YouTuResult.ok(false);
	}

	/**
	 * 接收Tbuser对象，补全属性，向表中插入记录,返回YouResult， 密码使用MD5加密（spring框架自带）
	 */
	@Override
	public YouTuResult registerUser(TbUser user) {
		// 补全pojo
		user.setCreated(new Date());
		user.setUpdated(new Date());
		//用户状态 1--启用  2--注销
		user.setStatus((byte) 1);
		// 使用spring自带工具对密码进行MD5加密
		String md5Pwd = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pwd);
		// 插入数据库中
		userMapper.insert(user);
		return YouTuResult.ok();
	}

	/**
	 * 接收两个参数用户名、密码。调用dao层查询用户信息。生成token，
	 * 把用户信息对象序列化成json写入redis。返回token。使用TaotaoResult包装。
	 */
	@Override
	public YouTuResult login(String username, String password,
			HttpServletRequest request, HttpServletResponse response) {
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		//登陆时过滤掉已经被注销的用户
		criteria.andStatusEqualTo((byte)1);
		List<TbUser> list = userMapper.selectByExample(example);
		// 如果没有此用户名
		if (null == list || list.size() == 0) {
			return YouTuResult.build(400, "用户名不存在");
		}
		TbUser user = list.get(0);
		// 比对密码
		if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
			return YouTuResult.build(400, "密码错误");
		}
		// 生成token
		String token = UUID.randomUUID().toString();
		// 保存用户之前，把用户对象中的密码清空。
		user.setPassword(null);
		// 把用户信息写入redis
		jedisClient.set(REDIS_USER_SESSION_KEY + ":" + token, JsonUtils.objectToJson(user));
		// 设置session的过期时间
		jedisClient.expire(REDIS_USER_SESSION_KEY + ":" + token, SSO_SESSION_EXPIRE);
		//向cookie中写入token,关闭浏览器时失效
		CookieUtils.setCookie(request, response, "YOUTU_TOKEN", token);
		// 返回token
		return YouTuResult.ok(token);
	}

	/**
	 * 接收token，调用dao，到redis中查询token对应的用户信息。返回用户信息并更新过期时间。
	 */
	@Override
	public YouTuResult getUserByToken(String token) {
		// 根据token从redis中查询用户信息
		String json = jedisClient.get(REDIS_USER_SESSION_KEY + ":" + token);
		// 判断是否为空
		if (StringUtils.isBlank(json)) {
			return YouTuResult.build(400, "此session已经过期，请重新登录");
		}
		// 更新过期时间
		jedisClient.expire(REDIS_USER_SESSION_KEY + ":" + token, SSO_SESSION_EXPIRE);
		// 返回用户信息
		return YouTuResult.ok(JsonUtils.jsonToPojo(json, TbUser.class));
	}
	/**
	 * 接收token，调用dao，从redis中删除用户信息，返回youturesult包装的结果
	 */
	@Override
	public YouTuResult userLoginOut(String token) {
		// 删除token对应的用户信息session
		jedisClient.del(REDIS_USER_SESSION_KEY + ":" + token);
		
		return YouTuResult.ok();
	}
}
