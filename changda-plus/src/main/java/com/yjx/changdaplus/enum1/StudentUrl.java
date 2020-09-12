package com.yjx.changdaplus.enum1;

/**
 * @program: changda-plus
 * @description:
 * @author: YJX
 * @create: 2020-09-01 14:00
 **/
public enum StudentUrl {
    STUDENTINFO("http://jwc3.yangtzeu.edu.cn/eams/stdDetail!innerIndex.action?projectId="),
    STUDENTSCORE("http://221.233.24.23/eams/teach/grade/course/person!historyCourseGrade.action?projectType=MAJOR"),
    SEMESTERID("http://jwc3.yangtzeu.edu.cn/eams/stdExamTable!innerIndex.action?project.id="),
    COURSEINFO("http://jwc3.yangtzeu.edu.cn/eams/stdExamTable!innerIndex.action?project.id="),
    EXAMINFO("http://jwc3.yangtzeu.edu.cn/eams/stdExamTable!examTable.action?examBatch.id="),
    STUDENTIDS("http://jwc3.yangtzeu.edu.cn/eams/courseTableForStd.action");
    private String url;

    public String getUrl() {
        return url;
    }

    StudentUrl(String url) {
        this.url = url;
    }
}
