package io.github.winsontse.hearteyes.page.base;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.DialogTitle;
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

    void toggleKeyboard(Activity activity, View contentView);

    void hideKeyboard();

    void showDialog(String title, String msg, String okTitle, DialogInterface.OnClickListener onOkClickListener);

    void showDialog(String title, String msg, String okTitle, DialogInterface.OnClickListener onOkClickListener, String cancelTitle, DialogInterface.OnClickListener onCancelClickListener);

    void showDatePicker(int year, int month, int day, DatePickerDialog.OnDateSetListener onDateSetListener);

    String getStringById(int stringId);

    String[] getStringArrayById(int arrayId);

    Drawable getDrawableById(int drawableId);

    int getColorById(int colorId);

    void setStatusBarViewVisible(boolean isVisible);

}
