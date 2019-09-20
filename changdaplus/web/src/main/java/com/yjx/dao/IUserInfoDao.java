package com.yjx.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.yjx.pojo.UserInfo;

@Mapper
public interface IUserInfoDao {
	//绑定用户openid
	@Insert("insert into userInfo(`id`,`time`) values(#{openId},#{time})")
	public int bind(UserInfo userInfo);
	
	//添加基本信息
	@Update("update userInfo set no=#{no},pwd=#{pwd},name=#{name},sex=#{sex},campus=#{campus} where id=#{openId}")
	public int update(UserInfo userInfo);
	
	//查询信息
	@Select("select id as openid ,no as no, pwd as pwd, name as name, sex as sex, campus as campus , time as time from userInfo where id=#{openid}")
	public UserInfo getUserInfo(String openid);
	

}
