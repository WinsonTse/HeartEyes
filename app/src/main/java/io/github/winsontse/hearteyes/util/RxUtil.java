package io.github.winsontse.hearteyes.util;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by winson on 16/5/28.
 */
public class RxUtil {
    public static <T> Observable.Transformer<T, T> rxSchedulerHelper(Class<T> tClass) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
