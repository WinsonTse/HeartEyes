package io.github.winsontse.hearteyes.page.main.presenter;

import android.content.Intent;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.model.entity.leancloud.CircleMemberContract;
import io.github.winsontse.hearteyes.model.entity.leancloud.UserContract;
import io.github.winsontse.hearteyes.page.base.BasePresenterImpl;
import io.github.winsontse.hearteyes.page.main.contract.MainContract;
import io.github.winsontse.hearteyes.util.HeartEyesSubscriber;
import io.github.winsontse.hearteyes.util.rxbus.event.LoginOrLogoutEvent;
import io.github.winsontse.hearteyes.util.rxbus.event.PushEvent;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

public class MainPresenter extends BasePresenterImpl implements MainContract.Presenter {
    private MainContract.View view;

    @Inject
    public MainPresenter(final MainContract.View view) {
        this.view = view;

        receiveEvent(PushEvent.class, new Action1<PushEvent>() {
            @Override
            public void call(PushEvent pushEvent) {

//                int type = pushEvent.getType();
//                if (type == PushEvent.RESTART_INIT_PAGE) {
//                    view.initPage();
//                } else if (type == PushEvent.RESTART_AND_NOTIFY_FRIEND) {
//                    view.initPage();
                sendPushMessageToFriend(new PushEvent(PushEvent.RESTART_INIT_PAGE, AVUser.getCurrentUser().getString(UserContract.NICKNAME), "", ""));
//                }
            }
        });

        receiveLoginOrLogoutEvent(new Action1<LoginOrLogoutEvent>() {
            @Override
            public void call(LoginOrLogoutEvent loginOrLogoutEvent) {
                validateUserStatus();
            }
        });
    }

    public void validateUserStatus() {
        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser == null) {
            view.goToLoginPage();
            return;
        }
        
        getCircleAndFriend(currentUser);
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
        rxLifeAndSchedule(Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                AVQuery<AVObject> memberQuery = new AVQuery<>(CircleMemberContract.KEY);
                memberQuery.whereEqualTo(CircleMemberContract.USER, currentUser);
                try {
                    AVObject circleMember = memberQuery.getFirst();
                    if (circleMember == null) {
                        subscriber.onNext(false);
                        subscriber.onCompleted();
                    } else {
                        currentUser.save();
                        subscriber.onNext(true);
                        subscriber.onCompleted();
                    }

                } catch (AVException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        })).subscribe(new HeartEyesSubscriber<Boolean>(view) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onStart() {
                super.onStart();
                view.showProgressDialog(false, view.getStringById(R.string.tips_getting_your_friend));
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
        });

    }


}
