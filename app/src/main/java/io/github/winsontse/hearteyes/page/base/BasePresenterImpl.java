package io.github.winsontse.hearteyes.page.base;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hao.xie on 16/5/12.
 */
public class BasePresenterImpl implements BasePresenter {
    private CompositeSubscription compositeSubscription;

    @Override
    public void addSubscription(Subscription subscription) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);
    }

    @Override
    public void unsubscribe() {
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
    }
}
