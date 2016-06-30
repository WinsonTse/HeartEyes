package io.github.winsontse.hearteyes.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.util.LogUtil;

/**
 * Created by winson on 16/6/30.
 */

public class MoreTextView extends TextView {

    private boolean isShowMore = false;

    private int clickTextColor = Color.BLUE;
    private int maxLength = 150;
    private String expandingStr = "";
    private String collaspingStr = "";

    private String correctContent = "";
    private ClickableSpan clickableSpan;
    private SuperSpannable shortContent;
    private SuperSpannable longContent;

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
            maxLength = a.getInt(R.styleable.MoreTextView_mtl_max_length, maxLength);
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
        final Resources resources = getContext().getResources();
        clickableSpan = new ClickableURLSpan("", resources.getColor(R.color.material_pink_500)) {
            @Override
            public void onLongClick(View widget) {

            }

            @Override
            public void onClick(View view) {
                LogUtil.d("点击事件");
                if (isShowMore) {
                    setText(shortContent);
                } else {
                    if(longContent == null) {
                        longContent = new SuperSpannable();
                    }
                    if(longContent.length() == 0) {
                        longContent.append(correctContent);
                        longContent.append("\n");
                        longContent.append(expandingStr, clickableSpan);
                    }
                    setText(longContent);

                }
                isShowMore = !isShowMore;
            }
        };
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        setOnTouchListener(new ClickableTextOnTouchListener(resources.getColor(R.color.material_pink_200)));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setContent(final String content) {
        isShowMore = false;
        this.correctContent = content;
        if (shortContent != null) {
            shortContent.clear();
        }
        if(longContent != null) {
            longContent.clear();
        }
        if (correctContent.length() <= maxLength) {
            setText(correctContent);
        } else {
            if (shortContent == null) {
                shortContent = new SuperSpannable();
            }
            shortContent.append(correctContent.subSequence(0, maxLength));
            shortContent.append("…");
            shortContent.append("\n");
            shortContent.append(collaspingStr, clickableSpan);
            LogUtil.e("最后length:" + shortContent.length());

            setText(shortContent);
        }


    }
}
