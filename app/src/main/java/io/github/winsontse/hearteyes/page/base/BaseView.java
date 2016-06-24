package io.github.winsontse.hearteyes.page.base;

import android.graphics.drawable.Drawable;
import android.view.View;

import rx.Observable;

/**
 * Created by hao.xie on 16/5/10.
 */
public interface BaseView {

    void closePage();

    void showProgressDialog(boolean cancelable, String msg);

    void hideProgressDialog();

    void showToast(String msg);

    void showKeyboard(View view);

    void hideKeyboard();

    String getStringById(int stringId);

    String[] getStringArrayById(int arrayId);

    Drawable getDrawableById(int drawableId);

    int getColorById(int colorId);

}
