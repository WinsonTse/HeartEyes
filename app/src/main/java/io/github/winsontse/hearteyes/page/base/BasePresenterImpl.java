package io.github.winsontse.hearteyes.page.base;

import android.support.annotation.IntDef;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.github.winsontse.hearteyes.model.entity.leancloud.UserContract;
import io.github.winsontse.hearteyes.util.HeartEyesSubscriber;
import io.github.winsontse.hearteyes.util.RxUtil;
import io.github.winsontse.hearteyes.util.constant.SecretConstant;
import io.github.winsontse.hearteyes.util.rxbus.RxBus;
import io.github.winsontse.hearteyes.util.rxbus.event.LoginOrLogoutEvent;
import io.github.winsontse.hearteyes.util.rxbus.event.PushEvent;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hao.xie on 16/5/12.
 */
public class BasePresenterImpl implements BasePresenter {
    private CompositeSubscription compositeSubscription;
    public static final int CREATE = 1;
    public static final int DESTROY = 2;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CREATE, DESTROY})
    public @interface RxLife {

    }

    private final BehaviorSubject<Integer> lifeSubject = BehaviorSubject.create();


    @Override
    public void onAttach() {
        lifeSubject.onNext(CREATE);
    }

    @Override
    public void onDetach() {
        lifeSubject.onNext(DESTROY);
    }

    @Override
    public <T> Observable<T> rxLife(Observable<T> observable) {
        return observable.compose(RxUtil.<T>bindUntilEvent(lifeSubject, DESTROY));
    }

    @Override
    public <T> Observable<T> rxSchedule(Observable<T> observable) {
        return observable.compose(RxUtil.<T>rxSchedulerHelper());
    }

    @Override
    public <T> Observable<T> rxLifeAndSchedule(Observable<T> observable) {
        return rxLife(rxSchedule(observable));
    }

    @Override
    public void addSubscription(Subscription subscription) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);
    }

    @Override
    public <T> void addSubscription(Observable<T> observable, Subscriber<T> subscriber) {
        addSubscription(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber)
        );
    }

    @Override
    public void unsubscribe() {
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public void clearSubscribe() {
        if (compositeSubscription != null) {
            compositeSubscription.clear();
        }
    }

    @Override
    public void sendPushMessage(AVUser avUser, final PushEvent msg) {
        if (avUser == null || msg == null) {
            return;
        }
        rxLifeAndSchedule(Observable.just(avUser)
                .map(new Func1<AVUser, String>() {
                    @Override
                    public String call(AVUser avUser) {
                        try {
                            String installationId = avUser.getString(UserContract.INSTALLATION_ID);
                            if (TextUtils.isEmpty(installationId)) {
                                avUser.fetch();
                                installationId = avUser.getString(UserContract.INSTALLATION_ID);
                            }
                            return installationId;
                        } catch (AVException e) {
                            Log.d("winson", "出错:" + e.getMessage());
                            e.printStackTrace();
                            return null;
                        }
                    }
                }).filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return !TextUtils.isEmpty(s);
                    }
                }).map(new Func1<String, AVPush>() {
                    @Override
                    public AVPush call(String installationId) {
                        AVPush push = new AVPush();
                        AVQuery<AVInstallation> query = AVInstallation.getQuery();
                        query.whereEqualTo(UserContract.INSTALLATION_ID, installationId);
                        push.setQuery(query);
                        push.setChannel(SecretConstant.PUSH_CHANNEL_PRIVATE);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("action", SecretConstant.PUSH_ACTION);
                        jsonObject.put("type", msg.getType());
                        if (!TextUtils.isEmpty(msg.getAlert())) {
                            jsonObject.put("alert", msg.getAlert());
                        }

                        if (!TextUtils.isEmpty(msg.getFrom())) {
                            jsonObject.put("from", msg.getFrom());
                        }

                        if (!TextUtils.isEmpty(msg.getContent())) {
                            jsonObject.put("content", msg.getContent());
                        }
                        push.setData(jsonObject);
                        return push;
                    }
                })).subscribe(new HeartEyesSubscriber<AVPush>() {
            @Override
            public void handleError(Throwable e) {

            }

            @Override
            public void onNext(AVPush avPush) {

            }
        });
    }

    @Override
    public void sendPushMessageToFriend(final PushEvent msg) {
        getMyFriend(new Subscriber<AVUser>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(AVUser avUser) {
                sendPushMessage(avUser, msg);
            }
        });
    }

    @Override
    public void getMyFriend(Subscriber<AVUser> subscriber) {
        addSubscription(Observable.create(new Observable.OnSubscribe<AVUser>() {
            @Override
            public void call(Subscriber<? super AVUser> subscriber) {
//                subscriber.onNext(AVUser.getCurrentUser().getAVUser(UserContract));
            }
        }), subscriber);

    }

    @Override
    public AVUser getCurrentUser() {
        return AVUser.getCurrentUser();
    }

    @Override
    public <T> void receiveEvent(Class<T> cls, Action1<T> action1) {
        rxLifeAndSchedule(RxBus.getInstance().toObserverable(cls).compose(RxUtil.<T>rxSchedulerHelper())).subscribe(action1);
    }

    @Override
    public void receiveLoginOrLogoutEvent(Action1<LoginOrLogoutEvent> action1) {
        receiveEvent(LoginOrLogoutEvent.class, action1);
    }


}
