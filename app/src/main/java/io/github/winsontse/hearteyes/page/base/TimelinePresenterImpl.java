package io.github.winsontse.hearteyes.page.base;

import java.util.List;

import io.github.winsontse.hearteyes.util.HeartEyesSubscriber;
import io.github.winsontse.hearteyes.util.LogUtil;
import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by winson on 16/6/29.
 */

public abstract class TimelinePresenterImpl<T> extends BasePresenterImpl implements TimelinePresenter<T> {
    private int currentPage = 0;
    private static final int PER_PAGE_SIZE = 20;
    private TimelineView<T> timelineView;

    public TimelinePresenterImpl(TimelineView<T> timelineView) {
        this.timelineView = timelineView;
    }

    public void loadMore() {
        clearSubscribe();
        addSubscription(requestRemoteData(currentPage * PER_PAGE_SIZE, PER_PAGE_SIZE), new HeartEyesSubscriber<List<T>>(timelineView) {
            @Override
            public void handleError(Throwable e) {
                timelineView.setLoadingCompleted();
                LogUtil.e("请求出错");
            }

            @Override
            public void onNext(List<T> data) {
                LogUtil.e("请求结果:" + data.size());

                timelineView.onLoadMoreCompleted(data);

                if (data.size() < PER_PAGE_SIZE) {
                    timelineView.setLoadMoreEnable(false);
                } else {
                    timelineView.setLoadMoreEnable(true);
                    currentPage++;
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                LogUtil.e("开始请求");
                timelineView.showLoadingView();
            }
        });
    }

    public void refresh() {
        clearSubscribe();
        addSubscription(requestRemoteData(0, PER_PAGE_SIZE), new HeartEyesSubscriber<List<T>>(timelineView) {
            @Override
            public void handleError(Throwable e) {
                timelineView.setLoadingCompleted();
                LogUtil.e("请求出错");
            }

            @Override
            public void onNext(List<T> data) {
                LogUtil.e("请求结果:" + data.size());
                currentPage = 0;
                timelineView.onRefreshCompleted(data);

                if (data.size() < PER_PAGE_SIZE) {
                    timelineView.setLoadMoreEnable(false);
                } else {
                    timelineView.setLoadMoreEnable(true);
                    currentPage++;
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                LogUtil.e("开始请求");
            }
        });
    }

}
