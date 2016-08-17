package io.github.winsontse.hearteyes.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Layout;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.util.LogUtil;

/**
 * Created by winson on 16/6/30.
 */

public class MoreTextView extends TextView {

    private boolean isShowingMoreText = false;
    private boolean hasMoreText = false;

    private int clickTextColor = Color.BLUE;
    private int maxLines = 5;
    private int currentLines = 0;
    private String expandingStr = "";
    private String collaspingStr = "";

    private String correctContent = "";
    private SuperSpannable shortContent;
    private SuperSpannable longContent;
    private ClickableSpan clickableSpan;
    private int measuredHeight;

    public MoreTextView(Context context) {
        super(context);

        init(context, null);
    }

    public MoreTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MoreTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MoreTextView);
            clickTextColor = a.getColor(R.styleable.MoreTextView_mtl_click_text_color, clickTextColor);
            maxLines = a.getInt(R.styleable.MoreTextView_mtl_max_lines, maxLines) + 1;
            expandingStr = a.getString(R.styleable.MoreTextView_mtl_expanding_text);
            collaspingStr = a.getString(R.styleable.MoreTextView_mtl_collasping_text);
            a.recycle();
        }

        if (TextUtils.isEmpty(expandingStr)) {
            expandingStr = context.getString(R.string.collasp);
        }

        if (TextUtils.isEmpty(collaspingStr)) {
            collaspingStr = context.getString(R.string.expanding_content);
        }
        setMaxLines(maxLines);
        clickableSpan = new ClickableURLSpan("", clickTextColor) {
            @Override
            public void onLongClick(View widget) {

            }

            @Override
            public void onClick(View view) {
                if (isShowingMoreText) {
                    if (measuredHeight == 0) {
                        measuredHeight = getMeasuredHeight();
                    }
                    getLayoutParams().height = measuredHeight;
                    requestLayout();
                    setText(shortContent);

                } else {
                    setMaxLines(Integer.MAX_VALUE);
                    if (longContent == null) {
                        longContent = new SuperSpannable();
                    }
                    if (longContent.length() == 0) {
                        longContent.append(correctContent);
                        longContent.append("\n");
                        longContent.append(expandingStr, clickableSpan);
                    }
                    getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    requestLayout();
                    setText(longContent);
                }
                isShowingMoreText = !isShowingMoreText;

            }
        };
        setOnTouchListener(new ClickableTextOnTouchListener(Color.TRANSPARENT));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setContent(final String content) {
        isShowingMoreText = false;
        hasMoreText = false;
        getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        requestLayout();
        this.correctContent = content;
        if (shortContent != null) {
            shortContent.clear();
        }
        if (longContent != null) {
            longContent.clear();
        }
        setMaxLines(maxLines);
        setText(correctContent);

        post(new Runnable() {
            @Override
            public void run() {
                currentLines = getLineCount();
                if (currentLines >= maxLines) {
                    Layout layout = getLayout();
                    int end = layout.getLineEnd(maxLines - 2);
                    String subString = correctContent.substring(0, end);
                    if (shortContent == null) {
                        shortContent = new SuperSpannable();
                    }
                    //行距可能会不同,所以限定一下
                    if (measuredHeight == 0) {
                        measuredHeight = getMeasuredHeight();
                    }
                    getLayoutParams().height = measuredHeight;
                    requestLayout();
                    shortContent.append(subString);
                    if (!subString.endsWith("\n")) {
                        shortContent.append("\n");
                    }
                    shortContent.append(collaspingStr, clickableSpan);
                    setText(shortContent);
                    hasMoreText = true;
                } else {
                    hasMoreText = false;
                }
            }
        });

    }
}
