package com.yjx.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yjx.dao.IUserInfoDao;
import com.yjx.pojo.UserInfo;
import com.yjx.service.IUserInfoService;

@Service
@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
public class UserInfoServiceImpl implements IUserInfoService {

	@Autowired
	IUserInfoDao userInfoDao;
	
	public int bind(UserInfo userInfo) {
		// TODO Auto-generated method stub
		return userInfoDao.bind(userInfo);
	}

	public int update(UserInfo userInfo) {
		// TODO Auto-generated method stub
		return userInfoDao.update(userInfo);
	}

	public UserInfo getUserInfo(String openid) {
		// TODO Auto-generated method stub
		return userInfoDao.getUserInfo(openid);
	}

}
