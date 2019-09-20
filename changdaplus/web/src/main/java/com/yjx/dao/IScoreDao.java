package com.yjx.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.yjx.pojo.Score;

@Mapper
public interface IScoreDao {
	//添加成绩
	@Insert("insert into score values(#{no},#{data},#{time})")
	public int addScore(Score score);
	
	//修改成绩
	@Update("update score set data=#{data},time=#{time} where no=#{no}")
	public int updateScore(Score score);
	
	//查询成绩
	@Select("select *from score where no=#{no}")
	public Score getScore(String no);
}
