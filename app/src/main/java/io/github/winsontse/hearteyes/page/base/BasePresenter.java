package io.github.winsontse.hearteyes.page.base;

import rx.Subscription;

/**
 * Created by hao.xie on 16/5/10.
 */
public interface BasePresenter<T extends BaseView> {

    void addSubscription(Subscription subscription);

    void unsubscribe();
}
