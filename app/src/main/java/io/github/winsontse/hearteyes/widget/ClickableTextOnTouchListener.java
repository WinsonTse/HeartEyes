/*
 * Copyright (c) 2016 Copyright Istuary Innovation Group 2016.
 * Unauthorized copying of this file, via any medium is strictly prohibited proprietary and
 *  confidential.
 * Created on 16-6-14 下午3:49
 * ProjectName: ironhide-android ; ModuleName: IronHideProject ; ClassName: ClickableTextOnTouchListener.
 * Author: winson; Last Modified: 16-6-14 下午3:49.
 *  This file is originally created by winson.
 */

package io.github.winsontse.hearteyes.widget;

import android.graphics.Color;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


public class ClickableTextOnTouchListener implements View.OnTouchListener {
    private boolean find = false;

    private int color;

    public ClickableTextOnTouchListener(int color) {
//        this.color = color;
        this.color = Color.TRANSPARENT;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        Layout layout = ((TextView) v).getLayout();

        if (layout == null)
            return false;

        int x = (int) event.getX();
        int y = (int) event.getY();

        int line = layout.getLineForVertical(y);
        int offset = layout.getOffsetForHorizontal(line, x);

        TextView tv = (TextView) v;
        SpannableString value = SpannableString.valueOf(tv.getText());

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                ClickableURLSpan[] urlSpans = value.getSpans(0, value.length(), ClickableURLSpan.class);
                int findStart = 0;
                int findEnd = 0;
                for (ClickableURLSpan urlSpan : urlSpans) {
                    int start = value.getSpanStart(urlSpan);
                    int end = value.getSpanEnd(urlSpan);
                    if (start <= offset && offset <= end) {
                        find = true;
                        findStart = start;
                        findEnd = end;

                        break;
                    }
                }

                float lineWidth = layout.getLineWidth(line);

                find &= (lineWidth >= x);

                if (find) {
                    LongClickableLinkMovementMethod.getInstance().onTouchEvent(tv, value, event);
                    BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(color);
                    value.setSpan(backgroundColorSpan, findStart, findEnd, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    // Android has a bug, sometime TextView wont change its value
                    // when you modify SpannableString,
                    // so you must setText again, test on Android 4.3 Nexus4
                    tv.setText(value);
                }

                return find;
            case MotionEvent.ACTION_MOVE:
                if (find) {
                    LongClickableLinkMovementMethod.getInstance().onTouchEvent(tv, value, event);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (find) {
                    LongClickableLinkMovementMethod.getInstance().onTouchEvent(tv, value, event);
                    LongClickableLinkMovementMethod.getInstance().removeLongClickCallback();
                }
                BackgroundColorSpan[] backgroundColorSpans = value.getSpans(0, value.length(), BackgroundColorSpan.class);
                for (BackgroundColorSpan backgroundColorSpan : backgroundColorSpans) {
                    value.removeSpan(backgroundColorSpan);
                }
//                tv.setText(value);
                find = false;
                break;
        }

        return false;

    }
}
