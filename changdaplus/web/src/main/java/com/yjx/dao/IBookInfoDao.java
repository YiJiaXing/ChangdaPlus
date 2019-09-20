package com.yjx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.yjx.pojo.BookInfo;

@Mapper
public interface IBookInfoDao {
	// 添加书本信息
	@Insert("insert into bookinfo values(#{no},#{isbn},#{nodata},#{isbndata},#{bookName},#{author},#{count});")
	public int addBookInfo(BookInfo bookInfo);

	// 修改书本信息
	@Update("update bookinfo set isbndata=#{isbndata} where isbn=#{isbn};")
	public int updateBookInfo(BookInfo bookInfo);

	// 查询书本信息
	@Select("select *from bookinfo where no=#{no};")
	public BookInfo getBookInfo(String no);

	// 查询isbndata
	@Select("select *from bookinfo where isbn=#{isbn};")
	public List<BookInfo> getIsbnData(String isbn);


}
