package io.github.winsontse.hearteyes.page.base.timeline;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import io.github.winsontse.hearteyes.page.adapter.base.BaseRecyclerAdapter;
import io.github.winsontse.hearteyes.page.adapter.base.OnRecyclerViewScrollListener;

/**
 * Created by winson on 16/6/29.
 */

public interface TimelineViewPart<T> {

    void installTimelineView(final RecyclerView rv, final LinearLayoutManager layoutManager, final BaseRecyclerAdapter<T> adapter, final SwipeRefreshLayout srl, final View vEmpty, final View vLoading);

    void onRefreshStart();

    void onLoadMoreStart();

    void onRefreshCompleted(List<T> data);

    void onLoadMoreCompleted(List<T> data);

    void onRequestCompleted();

    void setLoadMoreEnable(boolean enable);

    void showRefreshView();

    void hideRefreshView();

    void showLoadMoreView();

    void hideLoadMoreView();

    void replaceItem(int position, T t);

    void updateItem(int position);

    void addOnRecyclerViewScrollListener(OnRecyclerViewScrollListener onRecyclerViewScrollListener);
}
