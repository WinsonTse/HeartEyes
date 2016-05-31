package io.github.winsontse.hearteyes.page.base;

import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVUser;

import io.github.winsontse.hearteyes.data.model.leancloud.PushMessage;
import io.github.winsontse.hearteyes.util.rxbus.event.base.BaseEvent;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by hao.xie on 16/5/10.
 */
public interface BasePresenter {

    void addSubscription(Subscription subscription);

    <T> void addSubscription(Observable<T> observable, Subscriber<T> subscriber);

    void unsubscribe();

    void sendPushMessage(AVUser avUser, PushMessage msg);

    <T extends BaseEvent> void registerEventReceiver(Class<T> cls, Action1<T> action1);
}
