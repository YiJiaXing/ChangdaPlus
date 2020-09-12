package com.yjx.changdaplus.enum1;

/**
 * @program: changda-plus
 * @description:
 * @author: YJX
 * @create: 2020-09-01 09:54
 **/
public enum OpenId {
    CODEERROR("00","传入code不合格"),
    ERROR("01","获取id失败"),
    SUCCESS("11","获取id成功");

    private String code;
    private String msg;

    OpenId(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
