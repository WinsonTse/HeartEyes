package io.github.winsontse.hearteyes.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.util.LogUtil;
import io.github.winsontse.hearteyes.util.UIUtil;

/**
 * Created by winson on 16/6/30.
 */

public class MoreTextLayout extends LinearLayout {

    private TextView tvShort;
    private TextView tvLong;
    private TextView tvClick;

    private float textSize;
    private int textColor = Color.BLACK;
    private int clickTextColor = Color.BLUE;
    private int maxLines = 5;
    private String expandingStr = "";
    private String collaspingStr = "";

    private String currentText = "";

    public MoreTextLayout(Context context) {
        super(context);
        init(context, null);
    }

    public MoreTextLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MoreTextLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MoreTextLayout);
            textSize = a.getDimension(R.styleable.MoreTextLayout_mtl_text_size, UIUtil.spToPx(context, 14));
            textColor = a.getColor(R.styleable.MoreTextLayout_mtl_text_color, textColor);
            clickTextColor = a.getColor(R.styleable.MoreTextLayout_mtl_click_text_color, clickTextColor);
            maxLines = a.getInt(R.styleable.MoreTextLayout_mtl_max_lines, maxLines);
            expandingStr = a.getString(R.styleable.MoreTextLayout_mtl_expanding_text);
            collaspingStr = a.getString(R.styleable.MoreTextLayout_mtl_collasping_text);
            a.recycle();
        }

        if (TextUtils.isEmpty(expandingStr)) {
            expandingStr = context.getString(R.string.collasp);
        }

        if (TextUtils.isEmpty(collaspingStr)) {
            collaspingStr = context.getString(R.string.expanding_content);
        }

        tvShort = new TextView(context);
        tvShort.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        tvShort.setTextColor(textColor);
        tvShort.setMaxLines(maxLines);
        addView(tvShort);

        tvLong = new TextView(context);
        tvLong.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        tvLong.setTextColor(textColor);
        addView(tvLong);

        tvClick = new TextView(context);
        tvClick.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        tvClick.setTextColor(clickTextColor);
        tvClick.setText(collaspingStr);
        addView(tvClick);

        tvLong.setVisibility(GONE);
        tvClick.setVisibility(INVISIBLE);

        tvClick.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvShort.getVisibility() == VISIBLE) {
                    tvShort.setVisibility(GONE);
                    tvLong.setVisibility(VISIBLE);
                    tvClick.setText(expandingStr);
                    tvLong.setText(currentText);

                } else {
                    tvLong.setVisibility(GONE);
                    tvShort.setVisibility(VISIBLE);
                    tvClick.setText(collaspingStr);
                }
            }
        });
    }

    public void setText(final String text) {
        tvShort.setVisibility(VISIBLE);
        tvLong.setVisibility(GONE);
        tvClick.setVisibility(INVISIBLE);
        tvClick.setText(collaspingStr);
        LogUtil.e("是否一样:" + TextUtils.equals(text, tvShort.getText().toString()));
        currentText = text;
        tvShort.setText(text);
        tvShort.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                tvClick.setVisibility(tvShort.getLineCount() > maxLines ? VISIBLE : INVISIBLE);
                tvShort.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });

    }
}
