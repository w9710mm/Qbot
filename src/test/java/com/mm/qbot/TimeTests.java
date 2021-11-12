package com.mm.qbot;

import com.mm.qbot.utils.TimeUtils;
import org.junit.Test;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot
 * @Description:
 * @date 2021/11/12 12:59
 */
public class TimeTests {
    @Test
    public void test(){
        Date lastWeekMondayStart = TimeUtils.getLastWeekMondayStart();
        Date lastWeekMondayEnd = TimeUtils.getLastWeekMondayEnd();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        System.out.println( simpleDateFormat.format(lastWeekMondayEnd));
        System.out.println(simpleDateFormat.format(lastWeekMondayStart));
    }
    @Test
    public void test1() throws IOException {
        File f=new File("G:\\aa\\b.txt");
        if (!f.exists()){
            f.mkdirs();
        }
        FileOutputStream fos1= null;
        try {
            fos1 = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        OutputStreamWriter dos1=new OutputStreamWriter(fos1);
        dos1.write("d 好");
        dos1.close();
    }

}
