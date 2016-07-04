package io.github.winsontse.hearteyes.page.main.presenter;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.data.model.leancloud.CircleMemberContract;
import io.github.winsontse.hearteyes.util.LogUtil;
import io.github.winsontse.hearteyes.util.rxbus.event.MomentEvent;
import io.github.winsontse.hearteyes.util.rxbus.event.PushEvent;
import io.github.winsontse.hearteyes.data.model.leancloud.UserContract;
import io.github.winsontse.hearteyes.page.main.contract.MainContract;
import io.github.winsontse.hearteyes.page.base.BasePresenterImpl;
import io.github.winsontse.hearteyes.util.HeartEyesSubscriber;
import io.github.winsontse.hearteyes.util.RxUtil;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

public class MainPresenter extends BasePresenterImpl implements MainContract.Presenter {
    private MainContract.View view;

    @Inject
    public MainPresenter(final MainContract.View view) {
        this.view = view;

        registerEventReceiver(PushEvent.class, new Action1<PushEvent>() {
            @Override
            public void call(PushEvent pushEvent) {

                int type = pushEvent.getType();
                if (type == PushEvent.RESTART_INIT_PAGE) {
                    view.initPage();
                } else if (type == PushEvent.RESTART_AND_NOTIFY_FRIEND) {
                    view.initPage();
                    sendPushMessageToFriend(new PushEvent(PushEvent.RESTART_INIT_PAGE, AVUser.getCurrentUser().getString(UserContract.NICKNAME), "", ""));
                }
            }
        });
    }

    public void validateUserStatus() {
        Log.d("winson", "验证用户");

        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser == null || TextUtils.isEmpty(currentUser.getString(UserContract.NICKNAME))) {
            view.goToLoginPage();
        } else if (currentUser.getAVUser(UserContract.FRIEND) != null && !TextUtils.isEmpty(currentUser.getString(UserContract.CIRCLE_ID))) {
            view.goToMomentListPage();
        } else {
            getCircleAndFriend(currentUser);
        }
    }

    @Override
    public void handleNewPageEvent(int openType) {
        switch (openType) {

            default:
                break;
        }
    }

    @Override
    public void updateInstallationId() {
        AVInstallation.getCurrentInstallation()
                .saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        AVUser currentUser = AVUser.getCurrentUser();
                        if (currentUser != null) {
                            String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
                            Log.d("winson", installationId);
                            currentUser.put(UserContract.INSTALLATION_ID, installationId);
                            currentUser.saveInBackground();

                        }
                    }
                });

    }

    @Override
    public void handleActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void getCircleAndFriend(final AVUser currentUser) {
        view.showProgressDialog(false, view.getStringById(R.string.tips_getting_your_friend));
        addSubscription(Observable.create(new Observable.OnSubscribe<Boolean>() {
                    @Override
                    public void call(Subscriber<? super Boolean> subscriber) {
                        AVQuery<AVObject> memberQuery = new AVQuery<>(CircleMemberContract.KEY);
                        memberQuery.whereEqualTo(CircleMemberContract.MEMBER, currentUser);
                        try {
                            AVObject circleMember = memberQuery.getFirst();
                            if (circleMember == null) {
                                subscriber.onNext(false);
                            } else {
                                currentUser.put(UserContract.FRIEND, circleMember.getAVObject(CircleMemberContract.CREATOR));
                                currentUser.put(UserContract.CIRCLE_ID, circleMember.getString(CircleMemberContract.CIRCLE_ID));
                                currentUser.save();
                                subscriber.onNext(true);
                            }

                        } catch (AVException e) {
                            e.printStackTrace();
                            subscriber.onError(e);
                        }
                    }
                }).compose(RxUtil.rxSchedulerHelper(Boolean.class))
                        .subscribe(new HeartEyesSubscriber<Boolean>(view) {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void handleError(Throwable e) {
                                view.hideProgressDialog();
                            }

                            @Override
                            public void onNext(Boolean isGoToHome) {
                                view.hideProgressDialog();
                                if (isGoToHome) {
                                    view.goToMomentListPage();
                                } else {
                                    view.showToast(view.getStringById(R.string.error_have_no_friend));
                                    view.goToAssosiationPage();
                                }
                            }
                        })
        );
    }


}
