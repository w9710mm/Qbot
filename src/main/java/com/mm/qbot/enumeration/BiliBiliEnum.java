package com.mm.qbot.enumeration;

public enum BiliBiliEnum  implements BaseEnum{


    FORWARD(1, "转发动态"),
    PICTURE(2, "图片动态"),
    WORD(4,"文字动态"),
    VIDEO(8,"视频投稿"),
    ARTICLE(6,"文章动态"),
    LIVE(4308,"直播动态");



    private Integer id;
    private String value;

    public static String getType(Integer id) {
        BiliBiliEnum[] carTypeEnums = values();
        for (BiliBiliEnum carTypeEnum : carTypeEnums) {
            if (carTypeEnum.getId().equals(id)) {
                return carTypeEnum.getValue();
            }
        }
        return null;
    }





    BiliBiliEnum(Integer id, String value) {
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
