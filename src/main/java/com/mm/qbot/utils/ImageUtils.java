package com.mm.qbot.utils;

import clive.hua.app.simpleImageTool.SimpleImageTool;
import com.sun.media.jai.codecimpl.JPEGCodec;
import com.sun.media.jai.codecimpl.JPEGImageEncoder;
import lombok.extern.slf4j.Slf4j;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.utils
 * @Description: 合成图片
 * @date 2021/11/3 23:21
 */
@Slf4j
public class ImageUtils {


    /**
     * 调整图像大小，并且补白
     * @param bi
     * @param height
     * @param width
     * @param bb
     * @return
     */
    public static BufferedImage resize(BufferedImage bi, int height, int width, boolean bb){
        double ratio;
        //计算比例
        double xRatio=(double)width/ bi.getWidth();
        double yRatio= (double)height/ bi.getHeight();
        ratio = Math.min(yRatio, xRatio);

            AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), AffineTransformOp.TYPE_BILINEAR);
            bi = op.filter(bi, null);

        if (bb) {
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.setColor(Color.white);
            g.fillRect(0, 0, width, height);
            g.drawImage(bi, (width - bi.getWidth(null)) / 2, (height - bi.getHeight(null)) / 2, bi.getWidth(), bi.getHeight(), Color.white, null);

            g.dispose();
            bi = image;
        }
        return bi;
    }

    public static String syntheticImage(List<String> urls){
        try {

            String url= UUID.randomUUID().toString().replace("-","");
            double v = (double) urls.size() / 3;
            int height= (int) (500*Math.ceil(v));

            int width;
            if (urls.size()>=3){
                width=1500;
            }else {
                width=1000;
            }
            BufferedImage image  =new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D graphics = image.createGraphics();

            graphics.setColor(Color.white);
            int line=-1;
            int ge=0;
            for (String netUrl:urls) {
                if (ge%3==0){
                    line++;
                }
                URL pic=new URL(netUrl);
                BufferedImage bufferedImage= ImageIO.read(pic);
                BufferedImage resize = resize(bufferedImage, 500, 500, true);
                graphics.drawImage(resize,(ge%3)*500,line*500,resize.getWidth(),resize.getHeight(),null);
                ge++;

            }
            graphics.dispose();
            url=System.getProperty("user.dir")+"\\"+url+".png";
//            FileOutputStream fos   =   new   FileOutputStream("img.jpg");
//            JPEGImageEncoder encoder   =   JPEGCodec.createJPEGEncoder(fos);
            ImageIO.write(image, "PNG", new File(url));

//            ImageIO.write(image,"jpeg", new File(System.getProperty("user.dir")+"\\"+url));
            return url;
        } catch (IOException e) {

            e.printStackTrace();
            return  null;
        }

    }



}
