/*
 * Copyright (c) 2016 Copyright Istuary Innovation Group 2016.
 * Unauthorized copying of this file, via any medium is strictly prohibited proprietary and
 *  confidential.
 * Created on 16-6-14 下午3:50
 * ProjectName: ironhide-android ; ModuleName: IronHideProject ; ClassName: ClickableURLSpan.
 * Author: winson; Last Modified: 16-6-14 下午3:50.
 *  This file is originally created by winson.
 */

package io.github.winsontse.hearteyes.widget;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

@SuppressLint("ParcelCreator")
public abstract class ClickableURLSpan extends ClickableSpan implements ParcelableSpan {
    public static final String URL_USER_ID = "user_id=";
    private final String mURL;
    private int color;

    public ClickableURLSpan(String mURL, int clikedColor) {
        this.mURL = mURL;
        this.color = clikedColor;
    }

    public ClickableURLSpan(Parcel src) {
        mURL = src.readString();
    }

    @Override
    public int getSpanTypeId() {
        return 11;
    }

    public int getSpanTypeIdInternal() {
        return getSpanTypeId();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mURL);
    }

    public void writeToParcelInternal(Parcel dest, int flags) {
        writeToParcel(dest, flags);
    }

    public String getURL() {
        return mURL;
    }

    public abstract void onLongClick(View widget);

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
        ds.setColor(color);
    }
}
