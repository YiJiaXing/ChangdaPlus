package com.yjx.changdaplus.processor;

import com.alibaba.fastjson.JSONObject;
import com.yjx.changdaplus.util.StudentUtil;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @program: changda-plus
 * @description:学生
 * @author: YJX
 * @create: 2020-08-29 10:34
 **/
@Component
public class StudentProcessor implements PageProcessor {
    private Site site;
    private JSONObject dataJson;
    private String data;

    public JSONObject getDataJson() {
        return dataJson;
    }

    public String getData() {
        return data;
    }

    @Override
    public void process(Page page) {
        StudentUtil studentUtil = new StudentUtil();
        String url=page.getUrl().toString();
        if (url.contains("stdDetail")) {    //判断是否是获取基本信息
            dataJson = studentUtil.getStudentInfo(page);
        } else if (url.contains("person")) {    //判断是否是获取成绩信息
            dataJson = studentUtil.getScore(page);
        } else if (url.contains("name")) {  //获取 SemesterId
            data = studentUtil.getSemesterId(page);
        } else if (url.contains("innerIndex")) {    //课程信息
            dataJson = studentUtil.getCourseInfo(page);
        } else if (url.contains("examTable")) {
            dataJson = studentUtil.getExamInfo(page);
        } else if (url.contains("courseTableForStd")) {
            data = studentUtil.getIds(page);
        }
    }

    public void setSite(Site site) {
        this.site=site;
    }

    @Override
    public Site getSite() {
        return site;
    }
}
