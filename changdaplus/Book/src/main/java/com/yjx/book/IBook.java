package com.yjx.book;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface IBook {
	
	public JSONObject getBook(String bookName,String page);	//查询书籍
	
	public JSONObject getBookInfo(String no);	//获得书的详细信息
	
	public JSONObject getDouBanBookInfo(String isbn);	//从豆瓣获得书的详细信息
	
	public JSONArray getCollectionInfo(String no);	//获得馆藏信息

}
