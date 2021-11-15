package com.mm.qbot.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.bean
 * @Description:
 * @date 2021/11/15 21:40
 */
@Data

public class SchedulerList {
    private final List<SchedulerDTO> schedulerDTOList=new ArrayList<>();

    private static class  SchedulerListInstance{
        //        private static final LevelDB levelDB=LevelDB.getInstance();
//        private static BilibiliPushMap Instance;
//        static{
//            Instance= (BilibiliPushMap) levelDB.get("BilibiliPushMap");;
//            if (Instance==null){
//                Instance=new BilibiliPushMap();
//                levelDB.put("BilibiliPushMap",Instance);
//            }
//
//        }
        private static final SchedulerList Instance=new SchedulerList();
    }
    private SchedulerList(){}

    public  static SchedulerList getInstance(){
        return SchedulerList.SchedulerListInstance.Instance;
    }
}
