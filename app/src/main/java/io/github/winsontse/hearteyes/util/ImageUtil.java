package io.github.winsontse.hearteyes.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by winson on 16/6/30.
 */

public class ImageUtil {
    public static final String JPG = "jpg";
    public static final String GIF = "gif";

    public static final int MAX_IMAGE_SIZE = 700 * 1024;
    public static final int REQ_WIDTH = 1000;
    public static final int REQ_HEIGHT = 1000;

    public static String getNameSuffix(String mineType) {
        String mimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType(mineType);
        return TextUtils.isEmpty(mineType) ? ".jpg" : "." + mimeType;
    }

    public static byte[] compressImageFile(String path) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateInSampleSize(options, REQ_WIDTH, REQ_HEIGHT);
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        LogUtil.e("分辨率:" + bitmap.getWidth() + "*" + bitmap.getHeight());
        LogUtil.e("bitmap占用:" + bitmap.getByteCount() + "  ");

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
        LogUtil.e("out占用:" + out.toByteArray().length + "  质量:" + quality);

        while (out.toByteArray().length > MAX_IMAGE_SIZE) {
            out.reset();
            quality -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
            LogUtil.e("out占用:" + out.toByteArray().length + "  质量:" + quality);
        }

        out.flush();
        out.close();

        return out.toByteArray();
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

}
