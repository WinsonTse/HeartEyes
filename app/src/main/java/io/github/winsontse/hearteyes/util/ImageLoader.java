package io.github.winsontse.hearteyes.util;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.io.File;

import io.github.winsontse.hearteyes.R;

/**
 * Created by winson on 16/6/20.
 */
public class ImageLoader {
    private static ImageLoader instance;

    private ImageLoader() {
    }

    public static ImageLoader getInstance() {
        if (instance == null) {
            synchronized (ImageLoader.class) {
                if (instance == null) {
                    instance = new ImageLoader();
                }
            }
        }

        return instance;
    }

    public void displayImage(Context context, String url, int placeHolder, ImageView iv) {
        getRequestManager(context).load(url).placeholder(placeHolder).into(iv);
    }

    public void displayImage(Context context, String url, ImageView iv) {
        displayImage(context, url, R.color.material_pink_200, iv);
    }

    public void displayImage(Context context, File file, int placeHolder, ImageView iv) {
        getRequestManager(context).load(file).placeholder(placeHolder).into(iv);
    }

    public void displayImage(Context context, File file, ImageView iv) {
        displayImage(context, file, R.color.material_pink_200, iv);
    }

    private RequestManager getRequestManager(Context context) {
        RequestManager requestManager;
        if (context instanceof Activity) {
            requestManager = Glide.with((Activity) context);
        } else {
            requestManager = Glide.with(context);
        }
        return requestManager;
    }

    public void displayImage(Fragment fragment, String url, ImageView iv) {
        Glide.with(fragment).load(url).into(iv);
    }
}
