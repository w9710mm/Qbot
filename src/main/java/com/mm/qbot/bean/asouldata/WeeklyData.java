package com.mm.qbot.bean.asouldata;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * weekly_data
 * @author 
 */

@Data
public class WeeklyData implements Serializable {

    private Integer id;

    private Integer uid;

    private String username;

    /**
     * b站粉丝
     */
    private Integer bFol;

    /**
     * b站总播放量
     */
    private Integer bPlay;

    /**
     * 抖音点赞数
     */
    private Integer dFav;

    /**
     * 抖音粉丝
     */
    private Integer dFol;

    /**
     * b站视频总数
     */
    private Integer bVideoNum;

    /**
     * 抖音视频总数
     */
    private Integer dVideoNum;

    /**
     * b站阅读量
     */
    private Integer bRead;

    /**
     * b站文章数
     */
    private Integer bArticleNum;

    /**
     * b站音频数
     */
    private Integer bAlbumNum;

    /**
     * 微博粉丝数
     */
    private Integer wFol;

    /**
     * 微博数
     */
    private Integer wWeibo;

    /**
     * b站点赞数
     */
    private Integer bLikes;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}