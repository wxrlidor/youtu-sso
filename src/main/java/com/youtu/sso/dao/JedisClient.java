package com.youtu.sso.dao;
/**
 *@author:王贤锐
 *@date:2018年1月19日  下午9:12:08
**/
public interface JedisClient {
	String get(String key);
	String set(String key, String value);
	String hget(String hkey, String key);
	long hset(String hkey, String key, String value);
	/**
	 * 自增1
	 * @param key
	 * @return
	 */
	long incr(String key);
	/**
	 * 设置有效时间，秒为单位
	 * @param key
	 * @param second
	 * @return
	 */
	long expire(String key, int second);
	/**
	 * 查询剩余有效时间，当返回-2时为失效
	 * @param key
	 * @return
	 */
	long ttl(String key);
	long del(String key);
	long hdel(String hkey, String key);
}
