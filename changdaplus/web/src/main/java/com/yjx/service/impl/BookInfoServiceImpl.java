package com.yjx.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yjx.dao.IBookInfoDao;
import com.yjx.pojo.BookInfo;
import com.yjx.service.IBookInfoService;

@Service
@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
public class BookInfoServiceImpl implements IBookInfoService {

	@Autowired
	IBookInfoDao bookInfoDao;
	
	//添加书本信息
	public int addBookInfo(BookInfo bookInfo) {
		
		return bookInfoDao.addBookInfo(bookInfo);
	}

	//修改书本信息
	public int updateBookInfo(BookInfo bookInfo) {
		return bookInfoDao.updateBookInfo(bookInfo);
	}

	//查询书本信息
	public BookInfo getBookInfo(String no) {
		// TODO Auto-generated method stub
		return bookInfoDao.getBookInfo(no);
	}

	@Override
	public List<BookInfo> getIsbnData(String isbn) {
		// TODO Auto-generated method stub
		return bookInfoDao.getIsbnData(isbn);
	}

}
