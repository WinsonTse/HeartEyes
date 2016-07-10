package io.github.winsontse.hearteyes.page.base;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.avos.avoscloud.AVObject;

import java.util.List;

import io.github.winsontse.hearteyes.page.adapter.base.BaseRecyclerAdapter;

/**
 * Created by winson on 16/6/29.
 */

public interface TimelineView<T> extends BaseView {

    void onRefreshCompleted(List<T> data);

    void onLoadMoreCompleted(List<T> data);

    void setLoadingCompleted();

    void setLoadMoreEnable(boolean enable);

    void showLoadingView();

    void hideLoadingView();

    void replaceItem(int position, T t);

    void updateItem(int position);

    void showRefreshView();
}
