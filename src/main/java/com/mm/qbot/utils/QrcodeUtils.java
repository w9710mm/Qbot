package com.mm.qbot.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.nio.file.Path;

/**
 * @author WWM
 * @version V0.0.1
 * @Package com.mm.qbot.utlis
 * @Description: 生成二维码的工具
 * @date 2021/10/21 12:16
 */
public class QrcodeUtils {
    public static void createQrcode(String content, Path file) throws Exception {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bm = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 100, 100);
            System.out.print(bm.toString());
            MatrixToImageWriter.writeToPath(bm, "png", file);
        } catch (WriterException e) {
            e.getStackTrace();
        }
    }

}
