package com.mm.qbot.enumeration;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.enumeration
 * @Description:
 * @date 2021/11/12 13:53
 */
public enum PathEnum {

    DYNAMIC_IMAGE_FILE(0, String.format("%s\\DynamicPic",System.getProperty("user.dir"))),
    ASOUL_PAPER_IMAGE_FILE(1, String.format("%s\\PaperPic",System.getProperty("user.dir"))),
    ASOUL_PAPER_TEXT_FILE(2,String.format("%s\\PaperText",System.getProperty("user.dir"))),
    WEIBO_IMAGE_FILE(3,String.format("%s\\WeiboImage",System.getProperty("user.dir")));
    private Integer id;
    private String value;

    PathEnum(int id, String value) {
        this.id = id;
        this.value = value;
    }
    public static String getType(Integer id) {
        PathEnum[] carTypeEnums = values();
        for (PathEnum carTypeEnum : carTypeEnums) {
            if (carTypeEnum.getId().equals(id)) {
                return carTypeEnum.getValue();
            }
        }
        return null;
    }


    public Integer getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
