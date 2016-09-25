package io.github.winsontse.hearteyes.widget;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.winsontse.hearteyes.R;

/**
 * Created by winson on 16/6/5.
 */
public class BottomBar extends TabLayout implements TabLayout.OnTabSelectedListener {
    private static final float ALPHA_SELECTED = 1.0f;
    private static final float ALPHA_UNSELECTED = 0.5f;
    private static final int TEXT_SIZE_UNSELECTED = 12;
    private static final int TEXT_SIZE_SELECTED = 14;
    private LayoutInflater inflater;
    private OnTabChangeListener onTabChangeListener;
    private List<Item> items = new ArrayList<>();
    private List<String> fragmentTags = new ArrayList<>();

    public void setOnTabChangeListener(OnTabChangeListener onTabChangeListener) {
        this.onTabChangeListener = onTabChangeListener;
    }

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


    public void setItems(List<Item> items) {
        if (items == null || items.size() == 0) {
            return;
        }
        this.items.addAll(items);
        this.fragmentTags.clear();
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            TabLayout.Tab tab = newTab();
            View customView = inflater.inflate(R.layout.item_tab, this, false);
            ImageView iv = (ImageView) customView.findViewById(R.id.iv);
            TextView tv = (TextView) customView.findViewById(R.id.tv);
            tab.setCustomView(customView);
            iv.setImageResource(item.getIcon());
            tv.setText(item.getTitle());
            if (i != 0) {
                iv.setAlpha(ALPHA_UNSELECTED);
                tv.setAlpha(ALPHA_UNSELECTED);
            } else {
                tv.setTextSize(TEXT_SIZE_SELECTED);
            }

            addTab(tab);
            fragmentTags.add(item.getFragmentTag());
        }
        addOnTabSelectedListener(this);
    }

    public boolean isTabTag(String tag) {
        return fragmentTags.contains(tag);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeOnTabSelectedListener(this);
        onTabChangeListener = null;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        setTabAppearance(tab, true);
        if (onTabChangeListener != null) {
            onTabChangeListener.onTabChanged(items.get(tab.getPosition()));
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        setTabAppearance(tab, false);

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public interface OnTabChangeListener {
        void onTabChanged(Item tab);
    }

    private void setTabAppearance(Tab tab, boolean isSelected) {
        View customView = tab.getCustomView();
        if (customView != null) {
            ImageView iv = (ImageView) customView.findViewById(R.id.iv);
            TextView tv = (TextView) customView.findViewById(R.id.tv);
            iv.setAlpha(isSelected ? ALPHA_SELECTED : ALPHA_UNSELECTED);
            tv.setAlpha(isSelected ? ALPHA_SELECTED : ALPHA_UNSELECTED);
            tv.setTextSize(isSelected ? TEXT_SIZE_SELECTED : TEXT_SIZE_UNSELECTED);
        }
    }

    public static class Item {
        @StringRes
        private int title;
        @DrawableRes
        private int icon;
        private String fragmentTag;

        public Item(int title, int icon, String fragmentTag) {
            this.title = title;
            this.icon = icon;
            this.fragmentTag = fragmentTag;
        }

        public int getTitle() {
            return title;
        }

        public Item setTitle(int title) {
            this.title = title;
            return this;
        }

        public int getIcon() {
            return icon;
        }

        public Item setIcon(int icon) {
            this.icon = icon;
            return this;
        }

        public String getFragmentTag() {
            return fragmentTag;
        }

        public Item setFragmentTag(String fragmentTag) {
            this.fragmentTag = fragmentTag;
            return this;
        }
    }

}
