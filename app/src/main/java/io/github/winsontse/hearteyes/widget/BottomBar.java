package io.github.winsontse.hearteyes.widget;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.github.winsontse.hearteyes.R;

/**
 * Created by winson on 16/6/5.
 */
public class BottomBar extends TabLayout {
    private static final float ALPHA_SELECTED = 1.0f;
    private static final float ALPHA_UNSELECTED = 0.5f;
    private static final int TEXT_SIZE_UNSELECTED = 12;
    private static final int TEXT_SIZE_SELECTED = 14;
    private LayoutInflater inflater;

    public BottomBar(Context context) {
        super(context);
        init(context);
    }

    public BottomBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        inflater = LayoutInflater.from(context);
    }


    public void setTitlesAndIcons(List<String> titles, List<Integer> icons) {
        for (int i = 0; i < titles.size(); i++) {
            TabLayout.Tab tab = newTab();
            View customView = inflater.inflate(R.layout.item_tab, this, false);
            ImageView iv = (ImageView) customView.findViewById(R.id.iv);
            TextView tv = (TextView) customView.findViewById(R.id.tv);
            tab.setCustomView(customView);
            iv.setImageResource(icons.get(i));
            tv.setText(titles.get(i));
            if(i != 0) {
                iv.setAlpha(ALPHA_UNSELECTED);
                tv.setAlpha(ALPHA_UNSELECTED);
            }
            else {
                tv.setTextSize(TEXT_SIZE_SELECTED);
            }

            addTab(tab);
        }
        setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setTabAppearance(tab, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                setTabAppearance(tab, false);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setTabAppearance(Tab tab, boolean isSelected) {
        View customView = tab.getCustomView();
        if(customView!= null) {
            ImageView iv = (ImageView) customView.findViewById(R.id.iv);
            TextView tv = (TextView) customView.findViewById(R.id.tv);
            iv.setAlpha(isSelected ? ALPHA_SELECTED : ALPHA_UNSELECTED);
            tv.setAlpha(isSelected ? ALPHA_SELECTED : ALPHA_UNSELECTED);
            tv.setTextSize(isSelected ? TEXT_SIZE_SELECTED : TEXT_SIZE_UNSELECTED);
        }
    }


}
