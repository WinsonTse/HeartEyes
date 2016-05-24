package io.github.winsontse.hearteyes.page.base;

import android.graphics.drawable.Drawable;

import rx.Observable;

/**
 * Created by hao.xie on 16/5/10.
 */
public interface BaseView {
    void showProgressDialog(boolean cancelable, String msg);

    void hideProgressDialog();

    void showToast(String msg);

    String getStringById(int stringId);

    String[] getStringArrayById(int arrayId);

    Drawable getDrawableById(int drawableId);

    int getColorById(int colorId);

}
