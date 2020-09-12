package com.yjx.changdaplus.enum1;

/**
 * @program: changda-plus
 * @description:
 * @author: YJX
 * @create: 2020-09-02 21:18
 **/
public enum NewsUrl {
    XUEXIAO("http://www.yangtzeu.edu.cn"),JIAOWU("http://jwc.yangtzeu.edu.cn");

    private String url;

    public String getUrl() {
        return url;
    }

    NewsUrl(String url) {
        this.url = url;
    }
}
