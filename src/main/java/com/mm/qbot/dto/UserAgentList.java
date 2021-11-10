package com.mm.qbot.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.dto
 * @Description:
 * @date 2021/11/10 9:20
 */
@Data
public class UserAgentList {
   private final List<String> uaList=new ArrayList<>();

    private static class  UserAgentListpInstance{
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
        private static final UserAgentList Instance=new UserAgentList();
    }
    private UserAgentList(){}

    public  static UserAgentList getInstance(){
        return UserAgentListpInstance.Instance;
    }
}
