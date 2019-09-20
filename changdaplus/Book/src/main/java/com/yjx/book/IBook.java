package com.yjx.book;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface IBook {
	
	public JSONObject getBook(String bookName,String page);	//��ѯ�鼮
	
	public JSONObject getBookInfo(String no);	//��������ϸ��Ϣ
	
	public JSONObject getDouBanBookInfo(String isbn);	//�Ӷ����������ϸ��Ϣ
	
	public JSONArray getCollectionInfo(String no);	//��ùݲ���Ϣ

}
