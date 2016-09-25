package io.github.winsontse.hearteyes.page.base;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.ArrayList;
import java.util.List;

import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.page.adapter.base.BaseRecyclerAdapter;
import io.github.winsontse.hearteyes.page.adapter.base.OnRecyclerViewScrollListener;
import io.github.winsontse.hearteyes.page.adapter.diff.base.BaseListDiffCallback;
import io.github.winsontse.hearteyes.util.AnimatorUtil;

/**
 * Created by winson on 16/6/29.
 */

public abstract class TimelineFragment<T> extends BaseFragment implements TimelineView<T> {

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

    public void addOnRecyclerViewScrollListener(OnRecyclerViewScrollListener onRecyclerViewScrollListener) {
        this.onRecyclerViewScrollListener = onRecyclerViewScrollListener;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getPresenter() == null || !(getPresenter() instanceof TimelinePresenterImpl)) {
            return;
        }
        timelinePresenter = (TimelinePresenter) getPresenter();
        adapter = getBaseRecyclerAdapter();
        srl = getSwipeRefreshLayout();
        rv = getRecyclerView();
        vLoading = getLoadingViewContainer();
        vEmpty = getEmptyView();

        if (srl != null) {
            srl.setColorSchemeColors(getColorById(R.color.md_pink_500));
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


                    if (layoutManager == null) {
                        layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
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
    public void setLoadingCompleted() {
        isLoading = false;
        if (srl.isRefreshing()) {
            srl.setRefreshing(false);
        }
        hideLoadingView();
    }

    @Override
    public void onRefreshStart() {

    }

    @Override
    public void onRefreshCompleted(List<T> data) {
        if (adapter != null) {
            adapter.setItems(data);
//            rv.smoothScrollToPosition(0);
        }
        setLoadingCompleted();

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
            List<T> oldData = new ArrayList<>();
            oldData.addAll(adapter.getData());


//            if (getMomentListDiffCallback() != null) {
//                adapter.getData().addAll(data);
//
//                DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(getMomentListDiffCallback().oldData(oldData).newData(adapter.getData()), true);
//                diffResult.dispatchUpdatesTo(adapter);
//            }
//            else {
            adapter.addItems(data);
//            }
        }
        setLoadingCompleted();
    }

    public BaseListDiffCallback getMomentListDiffCallback() {
        return null;
    }

    @Override
    public void updateItem(int position) {
        adapter.notifyItemChanged(position + adapter.getHeaderCount());
    }

    @Override
    public void replaceItem(int position, T t) {
        adapter.replaceItem(position, t);
    }

    @Override
    public void setLoadMoreEnable(boolean enable) {
        loadMoreEnable = enable;
    }

    protected abstract SwipeRefreshLayout getSwipeRefreshLayout();

    protected abstract RecyclerView getRecyclerView();

    protected abstract BaseRecyclerAdapter<T> getBaseRecyclerAdapter();

    protected abstract View getLoadingViewContainer();

    protected abstract View getEmptyView();

    @Override
    public void showLoadingView() {
        if (vLoading != null) {
            vLoading.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoadingView() {
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
}
