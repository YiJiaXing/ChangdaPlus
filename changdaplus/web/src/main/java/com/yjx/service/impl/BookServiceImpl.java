package com.yjx.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yjx.dao.IBookDao;
import com.yjx.pojo.Book;
import com.yjx.service.IBookService;

import net.sf.json.JSONArray;

@Service
@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
public class BookServiceImpl implements IBookService {

	@Autowired
	IBookDao bookDao;
	
	
	//查询历史记录
//	public JSONArray getHistory(String id) {
//		JSONArray array=new JSONArray().fromObject(bookDao.getHistory(id));
//		return array;
//	}

	//查询收藏记录
//	public JSONArray getCollect(String id) {
//		JSONArray array=new JSONArray().fromObject(bookDao.getCollect(id));
//		return array;
//	}

	//绑定openid
	public int addOpenid(String id) {
		return bookDao.addOpenid(id);
	}

	//添加历史记录
	public int updateHistory(Book book) {
		return bookDao.updateHistory(book);
	}

	//添加收藏记录
	public int updateCollect(Book book) {
		return bookDao.updateCollect(book);
	}

	public Book getBook(String id) {
		return bookDao.getBook(id);
	}

}
