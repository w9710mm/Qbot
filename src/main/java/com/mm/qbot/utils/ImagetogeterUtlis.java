package com.mm.qbot.utils;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.utils
 * @Description: 合成图片
 * @date 2021/11/3 23:21
 */
@Slf4j
public class ImagetogeterUtlis {
    public static BufferedImage modifyImagetogeter(BufferedImage[] image) {
        //创建一个304*304的图片
        BufferedImage tag = new BufferedImage(304,304,BufferedImage.TYPE_INT_RGB);
        try {
            Graphics2D graphics = tag.createGraphics();
            //设置颜色为218，223，224
            graphics.setColor(new Color(218,223,224));
            //填充颜色
            graphics.fillRect(0, 0, 304, 304);
            int count = image.length;
            //根据不同的合成图片数量设置图片放置位置
            if(count == 1){
                int startX = 108;
                int startY = 108;
                BufferedImage b = image[0];
                graphics.drawImage(b, startX, startY, b.getWidth(), b.getHeight(), null);
            }else if(count == 2){
                int startX = 60;
                int startY = 108;
                BufferedImage b1 = image[0];
                graphics.drawImage(b1, startX, startY, b1.getWidth(), b1.getHeight(), null);
                BufferedImage b2 = image[1];
                startX = startX + b1.getWidth()+8;
                graphics.drawImage(b2, startX, startY, b2.getWidth(), b2.getHeight(), null);
            }else if(count == 3){
                int startX = 108;
                int startY = 60;
                BufferedImage b1 = image[0];
                graphics.drawImage(b1, startX, startY, b1.getWidth(), b1.getHeight(), null);
                BufferedImage b2 = image[1];
                startX = 60;
                startY = 60 + b1.getHeight() + 8;
                graphics.drawImage(b2, startX, startY, b2.getWidth(), b2.getHeight(), null);
                BufferedImage b3 = image[2];
                startX = startX + b2.getWidth()+8;
                graphics.drawImage(b3, startX, startY, b3.getWidth(), b3.getHeight(), null);
            }else if(count == 4){
                int startX = 60;
                int startY = 60;
                BufferedImage b1 = image[0];
                graphics.drawImage(b1, startX, startY, b1.getWidth(), b1.getHeight(), null);
                BufferedImage b2 = image[1];
                startX = 60 + b1.getWidth() + 8;
                graphics.drawImage(b2, startX, startY, b2.getWidth(), b2.getHeight(), null);
                BufferedImage b3 = image[2];
                startX = 60;
                startY = 60 + b2.getHeight() + 8;
                graphics.drawImage(b3, startX, startY, b3.getWidth(), b3.getHeight(), null);
                BufferedImage b4 = image[3];
                startX = 60 + b3.getWidth() + 8;
                graphics.drawImage(b4, startX, startY, b4.getWidth(), b4.getHeight(), null);
            }else if(count == 5){
                int startX = 60;
                int startY = 60;
                BufferedImage b1 = image[0];
                graphics.drawImage(b1, startX, startY, b1.getWidth(), b1.getHeight(), null);
                BufferedImage b2 = image[1];
                startX = startX + b1.getWidth()+8;
                graphics.drawImage(b2, startX, startY, b2.getWidth(), b2.getHeight(), null);
                startX = 12;
                startY = 12 + startY + b2.getHeight();
                for(int i = 2;i<count;i++){
                    BufferedImage b = image[i];
                    graphics.drawImage(b, startX, startY, b.getWidth(), b.getHeight(), null);
                    startX = startX + b.getWidth() + 8;
                }
            }else if(count == 6){
                int startX = 12;
                int startY = 60;
                for(int i = 0;i<count;i++){
                    BufferedImage b = image[i];
                    graphics.drawImage(b, startX, startY, b.getWidth(), b.getHeight(), null);
                    startX = startX + b.getWidth() + 8;
                    if((i+1)%3 == 0){
                        startY = startY + b.getHeight() + 8;
                        startX = 12;
                    }
                }
            }else if(count == 7){
                int startX = 108;
                int startY = 12;
                BufferedImage b = image[0];
                graphics.drawImage(b, startX, startY, b.getWidth(), b.getHeight(), null);
                startX = 12;
                startY = startY + 8 + b.getHeight();
                for(int i = 1;i<count;i++){
                    b = image[i];
                    graphics.drawImage(b, startX, startY, b.getWidth(), b.getHeight(), null);
                    startX = startX + b.getWidth() + 8;
                    if(i%3 == 0){
                        startY = startY + b.getHeight() + 8;
                        startX = 12;
                    }
                }
            }else if(count == 8){
                int startX = 60;
                int startY = 12;
                BufferedImage b1 = image[0];
                graphics.drawImage(b1, startX, startY, b1.getWidth(), b1.getHeight(), null);
                BufferedImage b2 = image[1];
                startX = startX + b1.getWidth()+8;
                graphics.drawImage(b2, startX, startY, b2.getWidth(), b2.getHeight(), null);
                startX = 12;
                startY = 12 + b2.getHeight() + 8;
                for(int i = 2;i<count;i++){
                    BufferedImage b = image[i];
                    graphics.drawImage(b, startX, startY, b.getWidth(), b.getHeight(), null);
                    startX = startX + b.getWidth() + 8;
                    if(i == 4){
                        startY = startY + b.getHeight() + 8;
                        startX = 12;
                    }
                }
            }else if(count == 9){
                int startX = 12;
                int startY = 12;
                for(int i = 0;i<count;i++){
                    BufferedImage b = image[i];
                    graphics.drawImage(b, startX, startY, b.getWidth(), b.getHeight(), null);
                    startX = startX + b.getWidth() + 8;
                    if((i+1)%3 == 0){
                        startY = startY + b.getHeight() + 8;
                        startX = 12;
                    }
                }
            }
            graphics.dispose();
        } catch (Exception e) {
//            logger.error("推送同比压缩图片出错{}",e);
        }

        return tag;
    }

    public static BufferedImage loadImageLocal(String imgName) {
        try {
            return ImageIO.read(new File(imgName));
//        	return ImageIO.read(new URL(imgName));
        } catch (IOException e) {
//            logger.error("推送同比压缩图片出错{}",e);
        }
        return null;
    }

    public static void writeImageLocal(String newImage, BufferedImage img) {
        if (newImage != null && img != null) {
            try {
                File outputfile = new File(newImage);
                ImageIO.write(img, "jpg", outputfile);
            } catch (IOException e) {
//                logger.error("推送同比压缩图片出错{}",e);
            }
        }
    }




    public static BufferedImage zoom2(int width,int height,BufferedImage sourceImage) throws Exception {

        if( sourceImage == null ){
            return sourceImage;
        }
        // 计算x轴y轴缩放比例--如需等比例缩放，在调用之前确保參数width和height是等比例变化的
        double ratiox  = (Integer.valueOf(width)).doubleValue()/ sourceImage.getWidth();
        double ratioy  = (Integer.valueOf(height)).doubleValue()/ sourceImage.getHeight();
        AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratiox, ratioy), null);
        BufferedImage bufImg = op.filter(sourceImage, null);
        return bufImg;
    }

}
