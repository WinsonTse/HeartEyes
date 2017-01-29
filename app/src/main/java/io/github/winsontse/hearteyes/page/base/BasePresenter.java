package io.github.winsontse.hearteyes.page.base;

import com.avos.avoscloud.AVUser;

import io.github.winsontse.hearteyes.util.rxbus.event.LoginOrLogoutEvent;
import io.github.winsontse.hearteyes.util.rxbus.event.PushEvent;
import io.github.winsontse.hearteyes.util.rxbus.event.base.BaseEvent;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by hao.xie on 16/5/10.
 */
public interface BasePresenter {

    void onAttach();

    void onDetach();

    <T> Observable<T> rxLife(Observable<T> observable);

    <T> Observable<T> rxSchedule(Observable<T> observable);

    <T> Observable<T> rxLifeAndSchedule(Observable<T> observable);

    void addSubscription(Subscription subscription);

    <T> void addSubscription(Observable<T> observable, Subscriber<T> subscriber);

    void unsubscribe();

    void clearSubscribe();

    void sendPushMessage(AVUser avUser, PushEvent msg);

    void sendPushMessageToFriend(PushEvent msg);

    void getMyFriend(Subscriber<AVUser> subscriber);

    AVUser getCurrentUser();

    <T> void receiveEvent(Class<T> cls, Action1<T> action1);

    void receiveLoginOrLogoutEvent(Action1<LoginOrLogoutEvent> action1);
}
