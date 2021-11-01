package com.mm.qbot.enumeration;

public enum RelationActionEnum {


    SUBSCRIBE(1, "关注"),
    UNSUBSCRIBE(2, "取消关注"),
    SUBSCRIBE_SECRETLY(4,"悄悄关注"),
    BLOCK(5,"拉黑"),
    UNBLOCK(6,"取消拉黑"),
    REMOVE_FANS(7,"移除粉丝");



    private Integer id;
    private String value;

    public static String getType(Integer id) {
        RelationActionEnum[] carTypeEnums = values();
        for (RelationActionEnum carTypeEnum : carTypeEnums) {
            if (carTypeEnum.getId().equals(id)) {
                return carTypeEnum.getValue();
            }
        }
        return null;
    }





    RelationActionEnum(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

}
