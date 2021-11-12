package com.mm.qbot.enumeration;

public enum BiliBiliEnum  implements BaseEnum{


    FORWARD(1, "转发"),
    PICTURE(2, "图片"),
    WORD(4,"文字"),
    VIDEO(8,"视频"),
    ARTICLE(64,"文章"),
    LIVE(4308,"直播");



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

        @Override
        public Integer getId() {
            return id;
        }

        @Override
        public String getValue() {
            return value;
        }


}
