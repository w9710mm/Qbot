package com.mm.qbot.utils;

public class StringUtils {

    public  static  String omitString(String str){

        if (str.length()>40){
            str=str.substring(0,40)+"…………";
        }
            return str;
    }
}
