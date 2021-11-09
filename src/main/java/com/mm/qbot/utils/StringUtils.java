package com.mm.qbot.utils;

public class StringUtils {

    public  static  String omitString(String str,int num){

        if (str.length()>num){
            str=str.substring(0,num)+"…………";
        }
            return str;
    }
}
