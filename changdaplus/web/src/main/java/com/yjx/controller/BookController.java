package com.yjx.controller;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yjx.book.IBook;
import com.yjx.book.impl.BookImpl;
import com.yjx.pojo.Book;
import com.yjx.pojo.BookInfo;
import com.yjx.service.IBookInfoService;
import com.yjx.service.IBookService;
import com.yjx.util.Util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@ResponseBody
//@RequestMapping("/Loginweb")
public class BookController {
	// private static Logger log=LoggerFactory.getLogger(BookController.class);

	@Autowired
	private IBookService bookService; // 历史，收藏接口

	@Autowired
	private IBookInfoService bookInfoService; // 详细信息

	// 查询书籍
	@RequestMapping(value = "/SelectBookServlet", method = RequestMethod.GET)
	public JSONObject SelectBook(@RequestParam("code") String code, @RequestParam("bookName") String bookName,
			@RequestParam("page") String page) {
		Util util = new Util();
		IBook book = new BookImpl(); // 查询书籍的接口
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		Book bookRecord = new Book(); // 查书的历史记录以及收藏存放的对象

		if (bookName != null && page != null && code != null) {
			// System.out.println(bookName);
			String openid=util.getWeChatID(code).getString("openid");
			//String openid = code;

			json = book.getBook(bookName, page);

			bookRecord = bookService.getBook(openid);

			if (bookRecord != null) // 结果不为空，说明已经绑定了openid
			{
				if (bookRecord.getHistory() != null) {
					array = JSONArray.fromObject(bookRecord.getHistory());
				}
			} else // 没有绑定openid，首先进行绑定
			{
				bookService.addOpenid(openid); // 绑定openid
			}

			for (int i = 0; i < array.size(); i++) {
				if (array.get(i).equals(bookName)) // 查找array中是否存在与这次查询的书籍的书名
				{
					array.remove(i); // 存在就移除
				}
			}
			if (array.size() == 10) // 历史记录如果为存放最大条数10
			{
				array.remove(array.size() - 1); // 移除末尾的一条
				array.add(0, bookName); // 添加这次查询的书籍名称
				bookRecord.setId(openid);
				bookRecord.setHistory(array.toString());
				bookService.updateHistory(bookRecord); // 更新数据库
			} else {
				array.add(0, bookName);
				bookRecord.setId(openid);
				bookRecord.setHistory(array.toString());
				bookService.updateHistory(bookRecord);
			}
		}

		if (json.isEmpty()) {
			json.put("state", "false");
		} else {
			json.put("state", "true");
		}
		return json;
	}

	// 查询书的详细信息
	@RequestMapping(value = "/BookInfoServlet1", method = RequestMethod.GET)
	public JSONObject SelectBookInfo(@RequestParam("no") String no, @RequestParam("isbn") String isbn) {
		BookInfo bookInfo = new BookInfo(); // 书本信息对象
		IBook book = new BookImpl(); // 查询书本详细信息
		JSONObject json = new JSONObject();
		JSONObject json1 = new JSONObject();
		JSONArray array = new JSONArray();

		if (no != null && isbn != null) {
			isbn = isbn.replace("-", ""); // 去除isbn中的-字符

			BookInfo bookInfo1 = bookInfoService.getBookInfo(no);
			array = book.getCollectionInfo(no); // 获得馆藏信息
			if (bookInfo1 != null) {
				if (isbn.equals("")) {
					// 图书馆信息
					json = new JSONObject().fromObject(bookInfo1.getNodata());
					json.put("collection", array);
				} else {
					// 豆瓣图书信息
					json = new JSONObject().fromObject(bookInfo1.getIsbndata());
					json.put("collection", array);
				}
			} else {
				json1 = book.getBookInfo(no);
				bookInfo.setNo(no);
				bookInfo.setIsbn(isbn);
				bookInfo.setNodata(json1.toString());
				bookInfo.setBookName(json1.getString("bookName"));
				bookInfo.setCount(array.size());
				if (isbn.equals("")) {
					json.put("image",
							"https://img3.doubanio.com/f/shire/5522dd1f5b742d1e1394a17f44d590646b63871d/pics/book-default-lpic.gif");
					json.put("bookinfo", json1);
					json.put("collection", array);
				} else {
					bookInfo.setIsbndata(book.getDouBanBookInfo(isbn).toString());
					json = JSONObject.fromObject(bookInfo.getIsbndata());
					json.put("collection", array);
				}

				// 将数据放入数据库
				bookInfoService.addBookInfo(bookInfo);

			}

			json.put("state", "true");
		} else {
			json.put("state", "false");
		}

		return json;
	}

	@RequestMapping(value = "/BookInfoServlet", method = RequestMethod.GET)
	public JSONObject SelectBookInfo1(@RequestParam("no") String no) {
		IBook book = new BookImpl(); // 查询书本详细信息
		JSONObject json = new JSONObject();
		JSONObject json1 = new JSONObject();
		JSONArray array = new JSONArray();

		if (no != null) {
			if (!no.equals("")) {
				// 获得馆藏信息
				array = book.getCollectionInfo(no);

				BookInfo bookInfo1 = bookInfoService.getBookInfo(no);
				if (bookInfo1 != null) {
					json1 = JSONObject.fromObject(bookInfo1.getNodata());
				} else {
					// 获得书详细信息
					json1 = book.getBookInfo(no);
					String isbn=json1.getString("isbn").toString();
					isbn = isbn.replace("-", ""); // 去除isbn中的-字符
					BookInfo bookInfo = new BookInfo(no, isbn, json1.toString(),
					json1.getString("bookName").toString(), json1.getString("author").toString(), array.size());
					bookInfo.setIsbndata("");
					bookInfoService.addBookInfo(bookInfo);
				}
				// 书的图片
				json.put("image",
						"https://img3.doubanio.com/f/shire/5522dd1f5b742d1e1394a17f44d590646b63871d/pics/book-default-lpic.gif");
				// 从图书馆获取书的详细信息
				json.put("bookinfo", json1);
				// 将馆藏信息放入json
				json.put("collection", array);
			}
		}

		if (json.isEmpty()) {
			json.put("state", "false");
		} else {
			json.put("state", "true");
		}

		return json;
	}

	@RequestMapping(value = "/DouBanServlet", method = RequestMethod.GET)
	public JSONObject SelectBookInfo2(@RequestParam("isbn") String isbn) {
		IBook book = new BookImpl(); // 查询书本详细信息
		JSONObject json = new JSONObject();
		
		if (isbn != null && !isbn.equals("") && !isbn.equals("0")) {
			isbn = isbn.replace("-", ""); // 去除isbn中的-字符
			List<BookInfo> l=new ArrayList<>();
			l=bookInfoService.getIsbnData(isbn);
			BookInfo bookInfo1=new BookInfo();
			
			int i=0;
			for(BookInfo b:l)
			{
				i++;
				if(!b.getIsbndata().equals(""))
				{
					bookInfo1=b;
					break;
				}
				else
				{
					if(i==l.size())
					{
						bookInfo1=b;
					}
				}
			}
			
			
			if(bookInfo1!=null)
			{
				if(!bookInfo1.getIsbndata().equals(""))
				{
					json=JSONObject.fromObject(bookInfo1.getIsbndata());
				}
				else
				{
					json = book.getDouBanBookInfo(isbn);
					BookInfo bookInfo=new BookInfo();
					bookInfo.setIsbn(isbn);
					bookInfo.setIsbndata(json.toString());
					bookInfoService.updateBookInfo(bookInfo);
				}
			}
			else
			{
				json = book.getDouBanBookInfo(isbn);
			}
		}

		if (json.isEmpty()) {
			json.put("state", "false");
		} else {
			json.put("state", "true");
		}

		return json;

	}

	// 查询历史记录
	@RequestMapping(value = "/BookHistoryServlet", method = RequestMethod.GET)
	public JSONObject GetHistory(@RequestParam("code") String code) {
		Util util = new Util();
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		Book bookRecord = new Book(); // 查书的历史记录以及收藏存放的对象

		if (code != null && !code.equals("")) {
			String openid=util.getWeChatID(code).getString("openid");
			//String openid = code;

			bookRecord = bookService.getBook(openid);
			// 判断是否绑定id
			if (bookRecord == null) {
				bookService.addOpenid(openid); // 未绑定就进行绑定
			} else {
				array = JSONArray.fromObject(bookRecord.getHistory());
			}
		}
		json.put("history", array);

		return json;
	}

	// 查询收藏记录
	@RequestMapping(value = "/BookCollectServlet", method = RequestMethod.GET)
	public JSONObject GetCollect(@RequestParam("code") String code, @RequestParam("n") String n,
			HttpServletRequest request) {
		Util util = new Util();
		IBook book = new BookImpl();
		JSONArray array = new JSONArray();
		JSONArray array1 = new JSONArray();
		JSONObject json = new JSONObject();
		Book bookRecord = new Book(); // 查书的历史记录以及收藏存放的对象
		if (code != null && n != null && !code.equals("") && !n.equals("")) {
			String openid=util.getWeChatID(code).getString("openid");
			//String openid = code;
			bookRecord = bookService.getBook(openid);
			if (bookRecord == null) {
				bookService.addOpenid(openid);
			}
			Book bookRecord1 = bookService.getBook(openid);

			if (n.equals("2")) {
				array = JSONArray.fromObject(bookRecord1.getCollect()); // 获得收藏记录
				for (int i = 0; i < array.size(); i++) {
					BookInfo bookInfo = bookInfoService.getBookInfo(array.getString(i));
					JSONObject temp = new JSONObject();
					if (bookInfo == null) {
						JSONObject jsonTemp=book.getBookInfo(array.getString(i));
						JSONArray arrayTemp = new JSONArray();
						arrayTemp = book.getCollectionInfo(array.getString(i));
						temp.put("no", array.getString(i));
						temp.put("isbn",jsonTemp.getString("isbn"));
						temp.put("bookName", jsonTemp.getString("bookName"));
						temp.put("author", jsonTemp.getString("author"));
						temp.put("Count", arrayTemp.size());
					} else {
						temp.put("no", bookInfo.getNo());
						temp.put("isbn", bookInfo.getIsbn());
						temp.put("bookName", bookInfo.getBookName());
						temp.put("author", bookInfo.getAuthor());
						temp.put("Count", bookInfo.getCount());
					}
					array1.add(temp);
				}
				json.put("book", array1);
			} else if (n.equals("3")) {
				String no = request.getParameter("no");
				array = JSONArray.fromObject(bookRecord1.getCollect()); // 获得收藏记录
				if(array.size()>0)
				{
					if(array.get(0).equals("null"))
					{
						array.remove(0);
					}
				}
				
				for (int i = 0; i < array.size(); i++) {
					if (array.get(i).equals(no)) {
						array.remove(i);
					}
				}
				if (array.size() == 10) {
					array.remove(array.size() - 1);
					array.add(no);
					bookRecord1.setCollect(array.toString());
					bookService.updateCollect(bookRecord1);
				} else {
					array.add(no);
					bookRecord1.setCollect(array.toString());
					bookService.updateCollect(bookRecord1);
				}
				json.put("state", "true");
			} else if (n.equals("4")) {
				String no = request.getParameter("no");
				array = JSONArray.fromObject(bookRecord1.getCollect()); // 获得收藏记录
				for (int i = 0; i < array.size(); i++) {
					if (array.get(i).equals(no)) {
						array.remove(i);
					}
				}
				bookRecord1.setCollect(array.toString());
				bookService.updateCollect(bookRecord1);
				json.put("state", "true");
			}
		}
		return json;
	}

}
