package com.yjx.service;

import com.yjx.pojo.Book;

import net.sf.json.JSONArray;

public interface IBookService {
	// 查找历史记录
	//public JSONArray getHistory(String id);

	// 查找收藏记录
	//public JSONArray getCollect(String id);
	
	public Book getBook(String id);

	// 绑定openid
	public int addOpenid(String id);

	// 添加历史记录
	public int updateHistory(Book book);

	// 添加收藏记录
	public int updateCollect(Book book);

}
