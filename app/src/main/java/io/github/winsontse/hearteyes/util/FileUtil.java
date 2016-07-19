package io.github.winsontse.hearteyes.util;

import android.content.Context;
import android.os.Environment;
import android.support.v4.os.EnvironmentCompat;

import java.io.File;
import java.io.IOException;

/**
 * Created by winson on 16/7/10.
 */

public class FileUtil {

    private static File cacheDir;
    private static File externalCacheDir;
    private static File imageCacheDir;

    private static final String PATH_IMAGE_DIR = "images";
    private static final String PATH_HEART_EYES = "HeartEyes";

    public static File getCacheDir(Context context) {
        if (cacheDir == null) {
            cacheDir = context.getCacheDir();
        }
        return cacheDir;
    }

    public static File getExternalCacheDir(Context context) {
        if (externalCacheDir == null) {
            externalCacheDir = context.getExternalCacheDir();
        }
        return externalCacheDir;
    }

    public static File getExternalImageCacheDir(Context context) {
        if (imageCacheDir == null) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                imageCacheDir = new File(Environment.getExternalStorageDirectory() + File.separator + PATH_HEART_EYES);
            } else {
                imageCacheDir = new File(getExternalCacheDir(context).getAbsolutePath() + File.separator + PATH_IMAGE_DIR);
            }
            if (!imageCacheDir.exists()) {
                if (!imageCacheDir.mkdirs()) {
                    return null;
                }
            }
        }
        return imageCacheDir;
    }

    public static File createTempImageFile(Context context, String fileName) {
        return new File(getExternalImageCacheDir(context), fileName);
    }
}
