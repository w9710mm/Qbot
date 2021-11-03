package com.mm.qbot.enumeration;

public enum NeedTopEnum {


    NEED(1, "需要"),
    NOT_NEED(0, "不需要");


    private Integer id;
    private String value;

    NeedTopEnum(int id, String value) {
    }


    public Integer getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
