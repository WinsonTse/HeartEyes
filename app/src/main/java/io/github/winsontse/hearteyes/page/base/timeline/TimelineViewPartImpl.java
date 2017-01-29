package io.github.winsontse.hearteyes.page.base.timeline;

import android.animation.Animator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.List;

import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.page.adapter.base.BaseRecyclerAdapter;
import io.github.winsontse.hearteyes.page.adapter.base.OnRecyclerViewScrollListener;
import io.github.winsontse.hearteyes.page.base.BaseView;
import io.github.winsontse.hearteyes.util.AnimatorUtil;

/**
 * Created by winson on 2016/11/3.
 */

public class TimelineViewPartImpl<T> implements TimelineViewPart<T> {

    private BaseView baseView;
    private SwipeRefreshLayout srl;
    private RecyclerView rv;
    private TimelinePresenter timelinePresenter;
    private LinearLayoutManager layoutManager;
    private BaseRecyclerAdapter<T> adapter;
    private View vEmpty;
    private View vLoading;
    private OnRecyclerViewScrollListener onRecyclerViewScrollListener;

    private boolean isLoading = false;
    private boolean loadMoreEnable = true;

    public TimelineViewPartImpl(BaseView baseView) {
        this.baseView = baseView;
    }

    @Override
    public void installTimelineView(final RecyclerView rv, final LinearLayoutManager layoutManager, final BaseRecyclerAdapter<T> adapter, final SwipeRefreshLayout srl, final View vEmpty, final View vLoading) {
        timelinePresenter = (TimelinePresenter) baseView.getPresenter();
        this.srl = srl;
        this.rv = rv;
        this.vEmpty = vEmpty;
        this.vLoading = vLoading;
        this.adapter = adapter;
        this.layoutManager = layoutManager;

        if (srl != null) {
            srl.setColorSchemeColors(baseView.getColorById(R.color.md_pink_500));
            srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    isLoading = true;
                    timelinePresenter.refresh();
                    onRefreshStart();
                }
            });
        }
        if (rv != null) {
            rv.setAdapter(adapter);
            rv.setLayoutManager(layoutManager);
            rv.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (onRecyclerViewScrollListener != null) {
                        onRecyclerViewScrollListener.onScrolled(recyclerView, dx, dy);
                    }

                    if (adapter.getItemCount() == 0) {
                        return;
                    }

                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                    if (loadMoreEnable && !isLoading && adapter.getItemCount() - adapter.getHeaderCount() > 0 && lastVisibleItemPosition == adapter.getItemCount() - 1) {
                        isLoading = true;
                        timelinePresenter.loadMore();
                    }
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if (onRecyclerViewScrollListener != null) {
                        onRecyclerViewScrollListener.onScrollStateChanged(recyclerView, newState);
                    }

                }
            });
            rv.setItemAnimator(new DefaultItemAnimator());
        }

        timelinePresenter.refresh();
    }


    @Override
    public void onRefreshStart() {

    }

    @Override
    public void onLoadMoreStart() {

    }

    @Override
    public void onRefreshCompleted(List<T> data) {
        if (adapter != null) {
            adapter.setItems(data);
        }
        onRequestCompleted();

        if (vEmpty != null && vEmpty.getVisibility() == View.VISIBLE) {
            vEmpty.animate()
                    .alpha(0)
                    .setDuration(AnimatorUtil.ANIMATOR_TIME)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            rv.setAlpha(0);
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            ViewParent parent = vEmpty.getParent();
                            if (parent != null) {
                                ViewGroup viewGroup = (ViewGroup) parent;
                                viewGroup.removeView(vEmpty);
                            }
                            vEmpty.setVisibility(View.GONE);
                            rv.animate().alpha(1).setDuration(AnimatorUtil.ANIMATOR_TIME).start();
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    }).start();
        }
    }

    @Override
    public void onLoadMoreCompleted(List<T> data) {
        if (adapter != null) {
            adapter.addItems(data);
        }
        onRequestCompleted();
    }

    @Override
    public void onRequestCompleted() {
        isLoading = false;
        hideRefreshView();
        hideLoadMoreView();
    }

    @Override
    public void setLoadMoreEnable(boolean enable) {
        loadMoreEnable = enable;
    }

    @Override
    public void showLoadMoreView() {
        if (vLoading != null) {
            vLoading.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoadMoreView() {
        if (vLoading != null) {
            vLoading.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showRefreshView() {
        if (srl != null && (vEmpty == null || vEmpty.getVisibility() == View.GONE)) {
            srl.setRefreshing(true);
        }
    }

    @Override
    public void hideRefreshView() {
        if (srl != null && srl.isRefreshing()) {
            srl.setRefreshing(false);
        }
    }

    @Override
    public void replaceItem(int position, T t) {
        adapter.replaceItem(position, t);

    }

    @Override
    public void updateItem(int position) {
        adapter.notifyItemChanged(position + adapter.getHeaderCount());

    }

    @Override
    public void addOnRecyclerViewScrollListener(OnRecyclerViewScrollListener onRecyclerViewScrollListener) {
        this.onRecyclerViewScrollListener = onRecyclerViewScrollListener;
    }

}
