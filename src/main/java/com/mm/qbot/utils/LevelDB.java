package com.mm.qbot.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.iq80.leveldb.*;
import org.iq80.leveldb.impl.Iq80DBFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.utils
 * @Description: levelDb工具
 * @date 2021/10/30 19:41
 */@Slf4j

public class LevelDB {

    //@Value("${levelDB.folder}")
    private static final LevelDB levelDB=new LevelDB();
    private  static DB db;
    private  static final Charset  charset = StandardCharsets.UTF_8;
    static {
        try {
            String PATH  =  "/leveldb";
            File FILE = new File(PATH);
            DBFactory factory = new Iq80DBFactory();
            Options options = new Options();
            options.createIfMissing(true);
            db = factory.open(FILE, options);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private LevelDB(){}

    public static LevelDB getInstance(){
        return levelDB;
    }

    //基于fastjson的对象序列化
    public byte[] serializer(Object obj) {
        return JSON.toJSONBytes(obj, SerializerFeature.DisableCircularReferenceDetect);
    }

    //存放数据
    public  void put(String key, String val) {
        db.put(key.getBytes(charset), val.getBytes(charset));
    }



    public  void put(String key, Object val) {


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(val);
            db.put(key.getBytes(charset), baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //根据key获取数据
    public Object get(String key) {
        byte[] val = db.get(key.getBytes(charset));
        if (val!=null){
            try {
                ByteArrayInputStream bais = new ByteArrayInputStream(val);
                ObjectInputStream ois = new ObjectInputStream(bais);
                return ois.readObject();
            } catch (Exception e) {
                log.error("levelDB get error", e);
            }
        }

        return null;
    }

    //根据key删除数据
    public void delete(String key) {
        try {
            db.delete(key.getBytes(charset));
        } catch (Exception e) {
            log.error("levelDB delete error", e);
        }

    }

    //获取所有key
    public List<String> getKeys() {

        List<String> list = new ArrayList<>();
        DBIterator iterator = null;
        try {
            iterator = db.iterator();
            while (iterator.hasNext()) {
                Map.Entry<byte[], byte[]> item = iterator.next();
                String key = new String(item.getKey(), charset);
                list.add(key);
            }
        } catch (Exception e) {
            log.error("遍历发生异常", e);
        } finally {
            if (iterator != null) {
                try {
                    iterator.close();
                } catch (Exception e) {
                    log.error("遍历发生异常", e);
                }

            }
        }
        return list;
    }

    //spring销毁对象前关闭
    @PreDestroy
    public void closeDB() {
        if (db != null) {
            try {
                db.close();
            } catch (IOException e) {
                log.error("levelDB 关闭异常", e);
            }
        }
    }


}
