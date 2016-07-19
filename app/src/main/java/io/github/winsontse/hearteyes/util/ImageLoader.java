package io.github.winsontse.hearteyes.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;

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

    public void displayAvatar(Context context, String url, ImageView iv) {
        Glide.with(context).load(url).placeholder(R.color.md_pink_200).dontAnimate().into(iv);
    }

    public void displayImage(Context context, String url, int placeHolder, ImageView iv) {
        Glide.with(context).load(url).placeholder(placeHolder).into(iv);
    }

    public void displayImage(Context context, String url, int placeHolder, ImageView iv, Request request) {
        Glide.with(context).load(url).placeholder(placeHolder).into(iv).setRequest(request);
    }


    public void displayImage(Context context, String url, ImageView iv) {
        displayImage(context, url, R.color.md_pink_200, iv);
    }

    public void displayImage(Context context, File file, int placeHolder, ImageView iv) {
        Glide.with(context).load(file).placeholder(placeHolder).into(iv);
    }

    public void displayImage(Context context, File file, ImageView iv) {
        Glide.with(context).load(file).placeholder(R.color.md_pink_200).into(iv);
    }
}
