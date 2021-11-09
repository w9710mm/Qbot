package com.mm.qbot;

import clive.hua.app.simpleImageTool.SimpleImageTool;
import clive.hua.app.simpleImageTool.exception.MyImageException;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
        urls.add("2.jpg");
        urls.add("3.jpg");
        urls.add("4.jpg");
    urls.add("12.jpg");
    urls.add("53.jpg");

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

        graphics.setBackground(Color.white);
        int line=-1;
        int ge=0;
        for (String url:urls) {
            if (ge%3==0){
                line++;
            }
           SimpleImageTool.of(url).size(500,500).toFile(new File(url));
            BufferedImage bufferedImage=ImageIO.read(new File(url));
            graphics.drawImage(bufferedImage,(ge%3)*500,line*500,bufferedImage.getWidth(),bufferedImage.getHeight(),null);
            ge++;

        }
        graphics.dispose();

        ImageIO.write(image,"png", new File("out.jpg"));


    }
}
