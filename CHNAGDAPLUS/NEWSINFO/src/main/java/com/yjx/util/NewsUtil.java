package com.yjx.util;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class NewsUtil {

	//详细信息
	public JSONObject getMessage(String url) throws IOException {
		Connection con = Jsoup.connect(url);
		con.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
		Response rs = con.execute();
		Document d1 = Jsoup.parse(rs.body());
		JSONObject json = new JSONObject();
		for (int i = 0; i < d1.select("form").size(); i++) {
			if (d1.select("form").get(i).attr("name").equals("_newscontent_fromname")) {
				String text = d1.select("form").get(i).outerHtml();
				if (d1.select("form").get(i).select("#arc_next").size() > 0) {
					if (d1.select("form").get(i).select("ul").size() > 0) {
						d1.select("form").get(i).select("ul").get(d1.select("form").get(i).select("ul").size() - 1)
								.remove();
					}
					d1.select("form").get(i).select("#arc_next").remove();
					text = d1.select("form").get(i).outerHtml();
				}
				if (d1.select("form").get(i).select(".detail_news_footer").size() > 0) {
					d1.select("form").get(i).select(".detail_news_footer").remove();
					text = d1.select("form").get(i).outerHtml();
				}
				if (d1.select("form").get(i).select(".article-container").size() > 0) {
					text = d1.select("form").get(i).select(".article-title").get(0).outerHtml()
							+ d1.select("form").get(i).select(".article-info").get(0).outerHtml()
							+ d1.select("form").get(i).select(".article-container").get(0).outerHtml();
				}
				if (d1.select("form").get(i).select(".single-content").size() > 0) {
					text = d1.select("form").get(i).select(".title").get(0).outerHtml()
							+ d1.select("form").get(i).select(".single-content").get(0).outerHtml();
				}
				if (d1.select("form").get(i).select(".main_content").size() > 0) {
					d1.select("form").get(i).select(".main_content").get(0).select(".main_art").remove();
					if (d1.select("form").get(i).select(".main_content").get(0).select(".main_conDiv").size() > 0) {
						if (d1.select("form").get(i).select(".main_content").get(0).select(".main_conDiv").select("ul")
								.size() > 0) {
							d1.select("form").get(i).select(".main_content").get(0).select(".main_conDiv").select("ul")
									.get(d1.select("form").get(i).select(".main_content").get(0).select(".main_conDiv")
											.select("ul").size() - 1)
									.remove();
						}
					}
					text = d1.select("form").get(i).outerHtml();
				}
				json.put("message", text);
				break;
			}
		}
		return json;
	}

	//信息返回
	public JSONObject getSchoolMessage()
	{
		JSONObject json=new JSONObject();
		JSONArray array=new JSONArray();
		try {
			json=getMessage();
			array=getJaoWu();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONArray array2=json.getJSONArray("tongzhi");
		json.remove("tongzhi");
		for(int i=0;i<array2.size();i++)
		{
			array.add(array2.get(i));
		}
		json.put("JaoWu",array);
		
		return json;
	}
	
	// 学校信息
	private static JSONObject getMessage() throws IOException {
		String url = "http://www.yangtzeu.edu.cn/";
		Connection con = Jsoup.connect(url); // 创建连接
		Response rs = con.execute();
		Document d1 = Jsoup.parse(rs.body());

		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		JSONArray array1 = new JSONArray();
		JSONArray array2 = new JSONArray();
		JSONArray array3 = new JSONArray();
		// 获得头部新闻
		for (int i = 0; i < d1.select(".bd").get(0).select("ul").get(0).select("li").size(); i++) {
			JSONObject temp = new JSONObject();
			String s = d1.select(".bd").get(0).select("ul").get(0).select("li").get(i).attr("style");
			temp.element("image", "http://www.yangtzeu.edu.cn/" + s.substring(s.indexOf("url") + 4, s.indexOf(")")))
					.element("url", d1.select(".bd").get(0).select("ul").get(0).select("li").get(i).select("a").get(0)
							.attr("href"));
			array.add(temp);
		}

		// 学校新闻
		for (int i = 0; i < d1.select(".hd").get(0).select("ul").get(0).select("li").size(); i++) {
			JSONObject temp = new JSONObject();
			temp.element("title",
					d1.select(".hd").get(0).select("ul").get(0).select("li").get(i).select(".txt").get(0).select("a")
							.attr("title"))
					.element("image",
							"http://www.yangtzeu.edu.cn" + d1.select(".hd").get(0).select("ul").get(0).select("li")
									.get(i).select(".pic").get(0).select("a").get(0).select("img").attr("src"))
					.element("url", d1.select(".hd").get(0).select("ul").get(0).select("li").get(i).select(".txt")
							.get(0).select("a").attr("href"));
			array1.add(temp);
		}

		// 通知公告
		if (d1.select(".iac").get(0).select(".w1002").size() > 0) {
			for (int i = 0; i < d1.select(".iac").get(0).select(".w1002").get(0).select(".list").size(); i++) {
				if (d1.select(".iac").get(0).select(".w1002").get(0).select(".list").get(i).select("ul").size() > 0) {
					for (int j = 0; j < d1.select(".iac").get(0).select(".w1002").get(0).select(".list").get(i)
							.select("ul").get(0).select("li").size(); j++) {
						JSONObject temp = new JSONObject();
						temp.element("title",
								d1.select(".iac").get(0).select(".w1002").get(0).select(".list").get(i).select("ul")
										.get(0).select("li").get(j).select("a").get(0).attr("title"))
								.element("url", d1.select(".iac").get(0).select(".w1002").get(0).select(".list").get(i)
										.select("ul").get(0).select("li").get(j).select("a").get(0).attr("href"));
						array3.add(temp);
					}
				}
			}
		}

		// 学术动态
		for (int i = 0; i < d1.select(".fl").get(0).select("dl").size(); i++) {
			JSONObject temp = new JSONObject();
			temp.element("Date",
					d1.select(".fl").get(0).select("dl").get(i).select("dt").get(0).select("strong").get(0).ownText())
					.element("time",
							d1.select(".fl").get(0).select("dl").get(i).select("dt").get(0).select("span").get(0)
									.ownText())
					.element("title",
							d1.select(".fl").get(0).select("dl").get(i).select("dd").get(0).select("h4").get(0)
									.select("a").attr("title"))
					.element("url",
							d1.select(".fl").get(0).select("dl").get(i).select("dd").get(0).select("h4").get(0)
									.select("a").attr("href"))
					.element("rapporteur",
							d1.select(".fl").get(0).select("dl").get(i).select("dd").get(0).select("p").get(0).text())
					.element("place",
							d1.select(".fl").get(0).select("dl").get(i).select("dd").get(0).select("p").get(1).text());
			array2.add(temp);
		}

		json.element("message1", array).element("SchoolMessage", array1).element("ScienceMessage3", array2)
				.element("tongzhi", array3);
		return json;
	}

	// 教务处通知
	private static JSONArray getJaoWu() throws IOException {
		String url = "http://jwc.yangtzeu.edu.cn/jwxw/jwtz.htm";
		Connection con = Jsoup.connect(url);
		con.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
		Response rs = con.execute();
		Document d1 = Jsoup.parse(rs.body());
		JSONArray array = new JSONArray();

		for (int i = 0; i < d1.select("#list_r").get(0).select("ul").get(0).select("li").size(); i++) {
			JSONObject temp = new JSONObject();
			temp.put("title",
					d1.select("#list_r").get(0).select("ul").get(0).select("li").get(i).select("a").get(0).ownText());
			if (!d1.select("#list_r").get(0).select("ul").get(0).select("li").get(i).select("a").attr("href")
					.startsWith("http")) {
				String s = d1.select("#list_r").get(0).select("ul").get(0).select("li").get(i).select("a").attr("href");
				temp.put("url", "http://jwc.yangtzeu.edu.cn/" + s.substring(s.indexOf("../") + 3));
			} else {
				temp.put("url",
						d1.select("#list_r").get(0).select("ul").get(0).select("li").get(i).select("a").attr("href"));
			}
			array.add(temp);
		}
		return array;
	}

}
