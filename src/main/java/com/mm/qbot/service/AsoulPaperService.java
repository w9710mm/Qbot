package com.mm.qbot.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mm.qbot.bean.asouldata.DynamicDTO;
import com.mm.qbot.bean.asouldata.VideoDTO;
import com.mm.qbot.dao.mapper.WeeklyDataMapper;
import com.mm.qbot.bean.asouldata.WeeklyData;
import com.mm.qbot.bean.pushMap.User;
import com.mm.qbot.enumeration.BiliBiliEnum;
import com.mm.qbot.enumeration.NeedTopEnum;
import com.mm.qbot.enumeration.PathEnum;
import com.mm.qbot.utils.BilibiliApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.mm.qbot.utils.TimeUtils.getLastWeekMondayEnd;
import static com.mm.qbot.utils.TimeUtils.getLastWeekMondayStart;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.service
 * @Description:
 * @date 2021/11/10 15:46
 */
@Service
public class AsoulPaperService extends ServiceImpl<WeeklyDataMapper, WeeklyData> {

    @Autowired
    private WeeklyDataMapper weeklyDataMapper;

    public String weeklyData(List<User> userList) {

        Date lastWeekMondayStart = getLastWeekMondayStart();
        Date lastWeekMondayEnd = getLastWeekMondayEnd();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy_MM_dd HH_mm_ss");

        String url=String.format("%s\\%s.txt", PathEnum.getType(PathEnum.ASOUL_PAPER_TEXT_FILE.getId()),simpleDateFormat.format(lastWeekMondayStart),simpleDateFormat.format(lastWeekMondayEnd));
        try {
            File f = new File(url);
            if (!f.exists()){
                f.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(f);
            OutputStreamWriter dos = new OutputStreamWriter(fos);
            StringBuilder data=new StringBuilder();
            data.append("数据统计时间段：").append(simpleDateFormat.format(lastWeekMondayStart)).append("->")
                    .append(simpleDateFormat.format(lastWeekMondayEnd)).append("\n\n\n");
            for (User user:userList) {
                LambdaQueryWrapper<WeeklyData> wrapper = new LambdaQueryWrapper<>();


                data.append(user.getUname()).append(":\n");

                wrapper.eq(WeeklyData::getDVideoNum,user.getUid());
                wrapper.ge(WeeklyData::getCreateTime,lastWeekMondayStart);
                wrapper.last("limit 1");
                WeeklyData weeklyDataStart = weeklyDataMapper.selectOne(wrapper);
                wrapper=new LambdaQueryWrapper<>();
                wrapper.eq(WeeklyData::getDVideoNum,user.getUid());
                wrapper.ge(WeeklyData::getCreateTime,lastWeekMondayStart);
                wrapper.last("limit 1");
                WeeklyData weeklyDataEnd = weeklyDataMapper.selectOne(wrapper);
                if (weeklyDataEnd==null||weeklyDataStart==null){
                    return null;
                }
                data.append("涨粉：").append(weeklyDataEnd.getBFol()-weeklyDataStart.getBFol()).append("\n");

                JSONObject spaceVideo = BilibiliApi.getSpaceVideo(user.getUid());
                if (spaceVideo.getInteger("code")!=0){
                    return null;
                }
                JSONArray jsonArray = spaceVideo.getJSONObject("data").getJSONObject("list").getJSONArray("vlist");
                List<VideoDTO> weeklyVideo = getWeeklyVideo(jsonArray, user, lastWeekMondayStart, lastWeekMondayEnd);
                if (weeklyVideo.size()!=0) {
                    data.append("本周共发布(").append(weeklyVideo.size()).append(")条视频，其中播放量最高的是(")
                            .append(weeklyVideo.get(0).getTitle()).append(")\n")
                            .append(weeklyVideo.get(0).getUrl()).append("\n");
                }else {
                    data.append("本周共发布（0）条视频\n");
                }

                data.append("本周的视频播放总增量为：").append(weeklyDataEnd.getBPlay()-weeklyDataStart.getBPlay())
                        .append("\n");

                JSONObject spaceDynamic = BilibiliApi.getSpaceDynamic(Long.valueOf(user.getUid()),0L, NeedTopEnum.NOT_NEED);
                if (spaceDynamic.getInteger("code")!=0){
                    return null;
                }

                JSONArray cards=spaceDynamic.getJSONObject("data").getJSONArray("cards");
                Long next=spaceDynamic.getJSONObject("data").getLong("next_offset");
                while (true){
                    JSONObject lastCard =(JSONObject) cards.get(cards.size() - 1);
                    if (lastCard.getJSONObject("desc").getLong("timestamp")<lastWeekMondayStart.getTime()){
                        break;
                    }
                    JSONObject sd = BilibiliApi.getSpaceDynamic(Long.valueOf(user.getUid()),next, NeedTopEnum.NOT_NEED);
                    next=sd.getJSONObject("data").getLong("next_offset");
                    cards.addAll(sd.getJSONObject("data").getJSONArray("cards"));
                }

                List<DynamicDTO> weeklyDynamic = getWeeklyDynamic(cards, lastWeekMondayStart, lastWeekMondayEnd);
                if (weeklyDynamic.size()!=0) {
                    data.append("本周共发布(").append(weeklyDynamic.size()).append("）条动态，其中评论数最高为（")
                            .append(weeklyDynamic.get(0).getContent()).append(")\n")
                            .append(weeklyDynamic.get(0).getUrl()).append("\n");
                }else {
                    data.append("本周共发布（0）条视频\n");
                }
                data.append("_______________________________________");

//                data.append()

            }
            dos.write(data.toString());
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            url=null;
        }
        return url;
    }


    private List<VideoDTO> getWeeklyVideo(JSONArray cards,User user,Date lastWeekMondayStart,Date lastWeekMondayEnd){
        List<VideoDTO> videoDTOList=new ArrayList<>();
        for (Object o:cards) {
           JSONObject card =(JSONObject)o;
            Long created = card.getLong("created");
            if (created>lastWeekMondayEnd.getTime()) {
                continue;
            }
            if (created<lastWeekMondayStart.getTime()){
                break;
            }
            if (user!=null){
                if (!user.getUid().equals(card.getString("mid"))){
                    continue;
                }

            }
            VideoDTO dto=new VideoDTO();
            dto.setAuthor(card.getString("author"));
            dto.setBvid(card.getString("bvid"));
            dto.setCreated(card.getString("created"));
            dto.setMid(card.getString("mid"));
            dto.setPlay(card.getLong("play"));
            dto.setTitle(card.getString("title"));
            dto.setUrl("https://www.bilibili.com/video/"+card.getString("bvid"));
            videoDTOList.add(dto);

        }
        videoDTOList.sort(Comparator.comparingLong(VideoDTO::getPlay).reversed());

        return videoDTOList;
    }

    private List<DynamicDTO> getWeeklyDynamic(JSONArray cards, Date lastWeekMondayStart, Date lastWeekMondayEnd){

        List<DynamicDTO> dynamicDTOS=new ArrayList<>();
        for (Object o:cards) {
            JSONObject card =(JSONObject)o;
            Long created = card.getJSONObject("desc").getLong("timestamp");
            if (created>lastWeekMondayEnd.getTime()) {
                continue;
            }
            if (created<lastWeekMondayStart.getTime()){
                break;
            }
            JSONObject desc=card.getJSONObject("desc");
            int type=desc.getInteger("type");
            if (type!=BiliBiliEnum.WORD.getId()||type!=BiliBiliEnum.PICTURE.getId()){
                continue;
            }

            JSONObject c=JSONObject.parseObject(card.getString("card"));
            DynamicDTO dynamicDTO=new DynamicDTO();
            JSONObject item=c.getJSONObject("item");

            if (item.getString("description")!=null){
                dynamicDTO.setContent(item.getString("description"));
            }else {
                dynamicDTO.setContent(item.getString("content"));
            }
            dynamicDTO.setCommentNum(desc.getInteger("comment"));
            dynamicDTO.setMid(desc.getJSONObject("user_profile").getJSONObject("info").getString("uid"));
            dynamicDTO.setCreated(desc.getString("timestamp"));
            dynamicDTO.setDid(desc.getString("dynamic_id_str"));
            dynamicDTO.setUrl("https://t.bilibili.com/"+desc.getString("dynamic_id_str"));
            dynamicDTOS.add(dynamicDTO);
        }
        dynamicDTOS.sort(Comparator.comparingInt(DynamicDTO::getCommentNum).reversed());





        return dynamicDTOS;
    }

}
