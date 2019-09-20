package com.yjx.service;

import java.util.List;

import com.yjx.pojo.BookInfo;

public interface IBookInfoService {
	
	//添加书本信息
	public int addBookInfo(BookInfo bookInfo);
	
	//修改书本信息
	public int updateBookInfo(BookInfo bookInfo);
	
	//查询书本信息
	public BookInfo getBookInfo(String no);
	
	public List<BookInfo> getIsbnData(String isbn);

}
