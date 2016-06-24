package io.github.winsontse.hearteyes.page.base;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SendCallback;

import io.github.winsontse.hearteyes.util.rxbus.event.PushEvent;
import io.github.winsontse.hearteyes.data.model.leancloud.UserContract;
import io.github.winsontse.hearteyes.util.RxUtil;
import io.github.winsontse.hearteyes.util.constant.SecretConstant;
import io.github.winsontse.hearteyes.util.rxbus.RxBus;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
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
    public void sendPushMessage(AVUser avUser, final PushEvent msg) {
        if (avUser == null || msg == null) {
            return;
        }
        addSubscription(Observable.just(avUser)
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
                        })

                , new Subscriber<AVPush>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("winson", "出错:" + e.getMessage());
                    }

                    @Override
                    public void onNext(AVPush push) {
                        push.sendInBackground(new SendCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    Log.d("winson", "发送成功!" + "消息内容:" + msg.toString());
                                } else {
                                    Log.d("winson", "发送出错:" + e.getMessage() + "消息内容:" + msg.toString());

                                }
                            }
                        });
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
                subscriber.onNext(AVUser.getCurrentUser().getAVUser(UserContract.FRIEND));
            }
        }), subscriber);

    }

    @Override
    public AVUser getCurrentUser() {
        return AVUser.getCurrentUser();
    }

    @Override
    public <T> void registerEventReceiver(Class<T> cls, Action1<T> action1) {
        addSubscription(RxBus.getInstance().toObserverable(cls).compose(RxUtil.rxSchedulerHelper(cls)).subscribe(action1));
    }


}
