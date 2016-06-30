package io.github.winsontse.hearteyes.util;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import org.apache.http.entity.mime.MIME;

/**
 * Created by winson on 16/6/30.
 */

public class ImageUtil {
    public static String getNameSuffix(String mineType) {
        String mimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType(mineType);
        return TextUtils.isEmpty(mineType) ? ".jpg" : "." + mimeType;
    }
}
