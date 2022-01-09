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
import com.mm.qbot.utils.ChromeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
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

    private final SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy_MM_dd HH_mm_ss");

    private final SimpleDateFormat simpleDateFormat2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static HashMap<String, Color> colorMap;

    static {
        colorMap=new HashMap<>();
        colorMap.put("672346917",new Color(155,201,226));
        colorMap.put("672353429",new Color(189,125,116));
        colorMap.put("351609538",new Color(184,166,217));
        colorMap.put("672328094",new Color(247,153,176));
        colorMap.put("672342685",new Color(87,102,144));
        colorMap.put("703007996",new Color(252,150,110));

    }


    public String weeklyData(List<User> userList) {

        Date lastWeekMondayStart = getLastWeekMondayStart();
        Date lastWeekMondayEnd = getLastWeekMondayEnd();

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

                wrapper.eq(WeeklyData::getUid,user.getUid());

                wrapper.ge(WeeklyData::getCreateTime,simpleDateFormat2.format(lastWeekMondayStart));

                wrapper.last("limit 1");
                WeeklyData weeklyDataStart = weeklyDataMapper.selectOne(wrapper);
                wrapper=new LambdaQueryWrapper<>();
                wrapper.eq(WeeklyData::getUid,user.getUid());
                wrapper.ge(WeeklyData::getCreateTime,simpleDateFormat2.format(lastWeekMondayEnd));
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
                    if (lastCard.getJSONObject("desc").getLong("timestamp")<lastWeekMondayStart.getTime()/1000){
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
                    data.append("本周共发布（0）条评论\n");
                }
                data.append("_______________________________________\n");

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


    public String weeklyImage(User user){

        Date lastWeekMondayStart = getLastWeekMondayStart();
        Date lastWeekMondayEnd = getLastWeekMondayEnd();
        LambdaQueryWrapper<WeeklyData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WeeklyData::getUid,user.getUid());
        wrapper.ge(WeeklyData::getCreateTime,simpleDateFormat2.format(lastWeekMondayStart));
        wrapper.last("limit 1");
        WeeklyData weeklyDataStart = weeklyDataMapper.selectOne(wrapper);
        wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(WeeklyData::getUid,user.getUid());
        wrapper.ge(WeeklyData::getCreateTime,simpleDateFormat2.format(lastWeekMondayEnd));
        wrapper.last("limit 1");
        WeeklyData weeklyDataEnd = weeklyDataMapper.selectOne(wrapper);
        JSONObject spaceDynamic = BilibiliApi.getSpaceDynamic(Long.valueOf(user.getUid()),0L, NeedTopEnum.NOT_NEED);
        if (spaceDynamic.getInteger("code")!=0){
            return null;
        }
        JSONArray cards=spaceDynamic.getJSONObject("data").getJSONArray("cards");
        Long next=spaceDynamic.getJSONObject("data").getLong("next_offset");
        while (true){
            JSONObject lastCard =(JSONObject) cards.get(cards.size() - 1);
            if (lastCard.getJSONObject("desc").getLong("timestamp")<lastWeekMondayStart.getTime()/1000){
                break;
            }
            JSONObject sd = BilibiliApi.getSpaceDynamic(Long.valueOf(user.getUid()),next, NeedTopEnum.NOT_NEED);
            next=sd.getJSONObject("data").getLong("next_offset");
            cards.addAll(sd.getJSONObject("data").getJSONArray("cards"));
        }
        List<DynamicDTO> weeklyDynamic = getWeeklyDynamic(cards, lastWeekMondayStart, lastWeekMondayEnd);
        String url=String.format("%s\\%s.png",PathEnum.getType(PathEnum.ASOUL_PAPER_IMAGE_FILE.getId()),weeklyDynamic.get(0).getDid());
        try {
        BufferedImage dynamicImage = ChromeUtils.getDynamicImage(weeklyDynamic.get(0).getDid());

        File template=new File(String.format("%s\\%s.png",PathEnum.getType(PathEnum.TEMPLATE.getId()),user.getUid()));
        File front=new File(String.format("%s\\YouSheBiaoTiHei-2.ttf",PathEnum.getType(PathEnum.TEMPLATE.getId())));

       Font f= Font.createFont(0,front);
//        font.

            BufferedImage bufferedImage= ImageIO.read(template);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setPaint(colorMap.get(user.getUid()));

        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        if ("703007996".equals(user.getUid())){
            Font font = f.deriveFont(39L);
            graphics.setFont(font);
            graphics.drawString(String.valueOf(weeklyDataEnd.getBFol()-weeklyDataStart.getBFol()),237,80);
            graphics.drawString(String.valueOf(weeklyDataEnd.getBPlay()-weeklyDataStart.getBPlay()),825,74);
//            graphics.drawString(String.valueOf(weeklyDataEnd.getDFol()-weeklyDataStart.getDFol()),3330,850);
//            graphics.drawString(String.valueOf(weeklyDataEnd.getDFav()-weeklyDataStart.getDFol()),4170,850);


        }else {
            Font font = f.deriveFont(55L);
            graphics.setFont(font);
            graphics.drawString(String.valueOf(weeklyDataEnd.getBFol()-weeklyDataStart.getBFol()),840,120);
            graphics.drawString(String.valueOf(weeklyDataEnd.getBPlay()-weeklyDataStart.getBPlay()),890,177);
//            graphics.drawString(String.valueOf(weeklyDataEnd.getDFol()-weeklyDataStart.getDFol()),3820,730);
//            graphics.drawString(String.valueOf(weeklyDataEnd.getDFav()-weeklyDataStart.getDFol()),3820,900);

        }

          float ratio = Math.min(1150L / (float)dynamicImage.getWidth(), 450L / (float)dynamicImage.getHeight());
            AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), AffineTransformOp.TYPE_BILINEAR);
            dynamicImage = op.filter(dynamicImage, null);


            graphics.drawImage(dynamicImage,550-dynamicImage.getWidth()/2,600-dynamicImage.getHeight()/2,null);

            graphics.dispose();



            ImageIO.write(bufferedImage, "PNG", new File(url));


        } catch (FontFormatException | IOException e) {
        e.printStackTrace();
        return null;
    }

    return url;
    }
    private List<VideoDTO> getWeeklyVideo(JSONArray cards,User user,Date lastWeekMondayStart,Date lastWeekMondayEnd){
        List<VideoDTO> videoDTOList=new ArrayList<>();
        for (Object o:cards) {
           JSONObject card =(JSONObject)o;
            Long created = card.getLong("created");
            if (created>lastWeekMondayEnd.getTime()/1000) {
                continue;
            }
            if (created<lastWeekMondayStart.getTime()/1000){
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
            if (created>lastWeekMondayEnd.getTime()/1000) {
                continue;
            }
            if (created<lastWeekMondayStart.getTime()/1000){
                break;
            }
            JSONObject desc=card.getJSONObject("desc");
            int type=desc.getInteger("type");
            if (type!=BiliBiliEnum.WORD.getId()&&type!=BiliBiliEnum.PICTURE.getId()){
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
