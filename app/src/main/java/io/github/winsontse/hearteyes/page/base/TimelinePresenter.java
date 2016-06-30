package io.github.winsontse.hearteyes.page.base;

import java.util.List;

import rx.Observable;

/**
 * Created by winson on 16/6/29.
 */

public interface TimelinePresenter<T> extends BasePresenter {

    void refresh();

    void loadMore();

    Observable<List<T>> requestRemoteData(int skipCount, int limitCount);

}
