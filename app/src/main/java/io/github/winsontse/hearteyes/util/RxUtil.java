package io.github.winsontse.hearteyes.util;

import io.github.winsontse.hearteyes.page.base.BasePresenterImpl;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * Created by winson on 16/5/28.
 */
public class RxUtil {
    public static <T> Observable.Transformer<T, T> rxSchedulerHelper() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> Observable.Transformer<T, T> bindUntilEvent(BehaviorSubject<Integer> lifsubject, @BasePresenterImpl.RxLife final Integer bindEvent) {
        final Observable<Integer> observable = lifsubject.takeFirst(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer event) {
                return event.equals(bindEvent);
            }
        });

        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> sourceOb) {
                return sourceOb.takeUntil(observable);
            }
        };
    }
}
