package com.yjx.book.impl;

import com.yjx.book.IBook;
import com.yjx.util.BookUtil;
import com.yjx.util.DouBanUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class BookImpl implements IBook {

	//查询书籍
	public JSONObject getBook(String bookName, String page) {
		BookUtil bookUtil=new BookUtil();
		JSONObject json=new JSONObject();
		try {
			json=bookUtil.getBook(bookName, page);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;		
	}

	//获得书本的详细信息
	public JSONObject getBookInfo(String no) {
		BookUtil bookUtil=new BookUtil();
		JSONObject json=new JSONObject();
		try {
			json=bookUtil.getBookInfo(no);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;	
	}

	//获得豆瓣图书信息
	public JSONObject getDouBanBookInfo(String isbn) {
		DouBanUtil douBanUtil=new DouBanUtil();
		JSONObject json=new JSONObject();
		try {
			json=douBanUtil.getBouBanBookInfo(isbn);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;	
	}

	//获得馆藏信息
	public JSONArray getCollectionInfo(String no) {
		BookUtil bookUtil=new BookUtil();
		JSONArray array=new JSONArray();
		try {
			array=bookUtil.getCollectionInfo(no);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return array;	
	}

}
