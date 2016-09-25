package io.github.winsontse.hearteyes.page.base;

import java.util.List;

import io.github.winsontse.hearteyes.util.HeartEyesSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by winson on 16/6/29.
 */

public abstract class TimelinePresenterImpl<T> extends BasePresenterImpl implements TimelinePresenter<T> {
    private int currentPage = 0;
    private static final int PER_PAGE_SIZE = 20;
    private TimelineView<T> timelineView;
    private Subscription listSubscribe;


    public TimelinePresenterImpl(TimelineView<T> timelineView) {
        this.timelineView = timelineView;
    }

    public void loadMore() {
        if (listSubscribe != null) {
            listSubscribe.unsubscribe();
        }
        listSubscribe = requestRemoteData(currentPage * PER_PAGE_SIZE, PER_PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new HeartEyesSubscriber<List<T>>(timelineView) {
                            @Override
                            public void handleError(Throwable e) {
                                timelineView.setLoadingCompleted();
                            }

                            @Override
                            public void onNext(List<T> data) {

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
                                timelineView.showLoadingView();
                            }
                        });
        addSubscription(listSubscribe);
    }

    public void refresh() {
        if (listSubscribe != null) {
            listSubscribe.unsubscribe();
        }
        listSubscribe = requestRemoteData(0, PER_PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new HeartEyesSubscriber<List<T>>(timelineView) {
                            @Override
                            public void handleError(Throwable e) {
                                timelineView.setLoadingCompleted();
                            }

                            @Override
                            public void onNext(List<T> data) {
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
                                timelineView.showRefreshView();
                            }
                        });
        addSubscription(listSubscribe);
    }
}
