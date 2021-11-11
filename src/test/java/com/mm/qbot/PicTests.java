package com.mm.qbot;

import clive.hua.app.simpleImageTool.SimpleImageTool;
import clive.hua.app.simpleImageTool.exception.MyImageException;
import com.mm.qbot.utils.ImageUtils;
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

    String s = ImageUtils.syntheticImage(urls);

    System.out.println(s);
    }
@Test
    public  void testForSize() {
    try {
        BufferedImage bufferedImage=ImageIO.read(new URL("https://i0.hdslb.com/bfs/album/c64ce92d5b928642ecb2751d532f82be9fbfc50f.jpg"));

        BufferedImage resize = ImageUtils.resize(bufferedImage, 5500, 5500, true);

        ImageIO.write(resize,"jpg", new File("out.jpg"));

    } catch (IOException e) {
        e.printStackTrace();
    }

}

    public static BufferedImage resize(BufferedImage bi, int height, int width, boolean bb){
        double ratio;
        //计算比例
        if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
            if (bi.getHeight() > bi.getWidth()) {
                ratio = (new Integer(height)).doubleValue() / bi.getHeight();
            } else {
                ratio = (new Integer(width)).doubleValue() / bi.getWidth();
            }
            AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
            bi = op.filter(bi, null);
        }
        if (bb) {
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.setColor(Color.white);
            g.fillRect(0, 0, width, height);
            if (height== bi.getHeight(null))
                g.drawImage(bi, (width - bi.getWidth(null)) / 2, 0, bi.getWidth(null), bi.getHeight(null), Color.white, null);

            else
                g.drawImage(bi, (width - bi.getWidth(null)) / 2, (height - bi.getHeight(null)) / 2, bi.getWidth(null), bi.getHeight(null), Color.white, null);


            g.dispose();
            bi = image;
        }
        return bi;
    }
}
