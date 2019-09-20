package com.yjx.service;

import com.yjx.pojo.UserInfo;

public interface IUserInfoService {
	// 绑定用户openid
	public int bind(UserInfo userInfo);

	// 添加基本信息
	public int update(UserInfo userInfo);

	// 查询信息
	public UserInfo getUserInfo(String openid);

}
