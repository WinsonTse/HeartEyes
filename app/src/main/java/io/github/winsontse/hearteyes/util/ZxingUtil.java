package io.github.winsontse.hearteyes.util;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by winson on 16/5/26.
 */
public class ZxingUtil {

    private static Bitmap bitMatrix2Bitmap(BitMatrix matrix, int bgColor, int fgColor) {
        int w = matrix.getWidth();
        int h = matrix.getHeight();
        int[] rawData = new int[w * h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int color = bgColor;
                if (matrix.get(i, j)) {
                    color = fgColor;
                }
                rawData[i + (j * w)] = color;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        bitmap.setPixels(rawData, 0, w, 0, 0, w, h);
        return bitmap;
    }


    /**
     * 生成二维码
     *
     * @param content
     * @return
     */
    public static Bitmap generateQRCode(String content, int widthPix, int heightPix, int bgColor, int fgColor) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            //配置参数
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            //设置空白边距的宽度
            hints.put(EncodeHintType.MARGIN, 1); //default is 4

            BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, widthPix, heightPix, hints);
            return bitMatrix2Bitmap(matrix, bgColor, fgColor);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
}
