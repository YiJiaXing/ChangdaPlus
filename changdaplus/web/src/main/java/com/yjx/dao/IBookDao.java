package com.yjx.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.yjx.pojo.Book;

import net.sf.json.JSONArray;

@Mapper
public interface IBookDao {
	//查找历史记录
	//public String getHistory(String id);
	
	//查找收藏记录
	//public String getCollect(String id);
	@Select("select * from book where id=#{id};")
	public Book getBook(String id);
	
	
	//绑定openid
	@Insert("insert into book (`id`) values(#{id})")
	public int  addOpenid(String id);
	
	//添加历史记录
	@Update("update book set history=#{history} where id=#{id};")
	public int updateHistory(Book book);
	
	//添加收藏记录
	@Update("update book set collect=#{collect} where id=#{id};")
	public int updateCollect(Book book);

}
