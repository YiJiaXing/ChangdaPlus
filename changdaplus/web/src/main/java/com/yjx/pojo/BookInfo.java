package com.yjx.pojo;

//书本基本信息
public class BookInfo {
	private String no;		//书本的编号
	private String isbn;	//书本的isbn
	private String nodata;	//图书馆书本信息
	private String isbndata;	//豆瓣图书信息
	private String bookName;	//书名
	private String author;	//作者
	private int count;	//馆藏数量
	
	public BookInfo() {}
	
	public BookInfo(String no, String isbn, String nodata, String bookName, String author, int count) {
		this.no = no;
		this.isbn = isbn;
		this.nodata = nodata;
		this.bookName = bookName;
		this.author = author;
		this.count = count;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getNodata() {
		return nodata;
	}
	public void setNodata(String nodata) {
		this.nodata = nodata;
	}
	public String getIsbndata() {
		return isbndata;
	}
	public void setIsbndata(String isbndata) {
		this.isbndata = isbndata;
	}
	

}
