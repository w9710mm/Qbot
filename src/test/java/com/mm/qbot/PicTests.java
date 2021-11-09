package com.mm.qbot;

import clive.hua.app.simpleImageTool.SimpleImageTool;
import clive.hua.app.simpleImageTool.exception.MyImageException;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PicTests {

    @Test
   public void make(){
        try {
            SimpleImageTool.of("3.jpg")
                    .size(500,500)
                    .toFile(new File("4.jpg")); //gif png tiff jpg等多种格式
        } catch (MyImageException e) {
            e.printStackTrace();
        }

    }

@Test
    public void overlapImage() throws MyImageException, IOException {
        List<String> urls=new ArrayList<>();
//        urls.add("src/main/resources/testPic/1 (1).webp");
        urls.add("https://i0.hdslb.com/bfs/album/8320e8d3c88536f6320da42ee54c95b34a4b4ecd.jpg@500w_500h");
        urls.add("https://i0.hdslb.com/bfs/album/df576451704c0bc2f0f9c335c4aa8444e30d6d94.jpg@500w_500h");
    urls.add("https://i0.hdslb.com/bfs/album/90f7acfe55984e0951820ba73e0eae8c0452928f.png@500w_500h");
    urls.add("https://i0.hdslb.com/bfs/album/9ff93e121b5bea138547fdee98cc5c06ed06ce1b.jpg@500w_500h");

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
        for (String url:urls) {
            if (ge%3==0){
                line++;
            }
            URL pic=new URL(url);
//           SimpleImageTool.of(url).size(500,500).toFile(new File(url));
            BufferedImage bufferedImage=ImageIO.read(pic);
            graphics.drawImage(bufferedImage,(ge%3)*500,line*500,bufferedImage.getWidth(),bufferedImage.getHeight(),null);
            ge++;

        }
        graphics.dispose();

        ImageIO.write(image,"png", new File("out.jpg"));


    }

    public static void resize(BufferedImage bufferedImage, int height, int width, boolean bb){
            double ratio =0;
            Image image=bufferedImage.getScaledInstance(width,height,BufferedImage.SCALE_SMOOTH);
            if (bufferedImage.getHeight()>bufferedImage.getWidth()){
                ratio= (double) height /(double) bufferedImage.getHeight();
            }else {
                ratio=(double) width /(double) bufferedImage.getWidth();
            }
        AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
        bufferedImage = op.filter(bufferedImage, null);

        if (bb){
            BufferedImage bi=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D=bi.createGraphics();
            graphics2D.setColor(Color.white);
            graphics2D.fillRect(0,0,width,height);
            if (width==image.getWidth(null)){
                graphics2D.drawImage(image,0,(height-image.getHeight(null))/2,image.getWidth(null), image.getHeight(null), Color.white, null);
            }else {

                graphics2D.drawImage(image, (width - image.getWidth(null)) / 2, 0, image.getWidth(null), image.getHeight(null), Color.white, null);
            }
            graphics2D.dispose();
            bi= (BufferedImage) image;
        }
    }
}
