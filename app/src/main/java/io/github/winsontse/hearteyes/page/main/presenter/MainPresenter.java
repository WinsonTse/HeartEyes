package io.github.winsontse.hearteyes.page.main.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.model.entity.account.UserEntity;
import io.github.winsontse.hearteyes.model.entity.circle.CircleEntity;
import io.github.winsontse.hearteyes.model.entity.leancloud.CircleContract;
import io.github.winsontse.hearteyes.model.entity.leancloud.UserContract;
import io.github.winsontse.hearteyes.model.local.IAccountManager;
import io.github.winsontse.hearteyes.page.base.BasePresenterImpl;
import io.github.winsontse.hearteyes.page.main.contract.MainContract;
import io.github.winsontse.hearteyes.util.GsonUtil;
import io.github.winsontse.hearteyes.util.HeartEyesSubscriber;
import io.github.winsontse.hearteyes.util.rxbus.event.LoginOrLogoutEvent;
import io.github.winsontse.hearteyes.util.rxbus.event.PushEvent;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

public class MainPresenter extends BasePresenterImpl implements MainContract.Presenter {
    private MainContract.View view;
    private final IAccountManager accountManager;

    @Inject
    public MainPresenter(final MainContract.View view, IAccountManager accountManager) {
        this.view = view;
        this.accountManager = accountManager;
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
        if (accountManager.isValidUser()) {
            view.goToMomentListPage();
            return;
        }

        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser == null) {
            view.goToLoginPage();
            return;
        }
        getCircleAndFriend(currentUser);
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
    public void getCircleAndFriend(final AVUser currentUser) {
        rxLifeAndSchedule(Observable.just(currentUser).flatMap(new Func1<AVUser, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(final AVUser currentUser) {
                return Observable.create(new Observable.OnSubscribe<Boolean>() {
                    @Override
                    public void call(Subscriber<? super Boolean> subscriber) {
                        AVQuery<AVObject> meQuery = AVQuery.or(Arrays.asList(
                                new AVQuery<>(CircleContract.KEY)
                                        .whereEqualTo(CircleContract.CREATOR, currentUser),
                                new AVQuery<>(CircleContract.KEY).whereEqualTo(CircleContract.INVITEE, currentUser)));
                        try {
                            meQuery.include(CircleContract.CREATOR)
                                    .include(CircleContract.INVITEE);
                            List<AVObject> avObjects = meQuery.find();
                            if (avObjects == null || avObjects.size() != 1) {
                                subscriber.onNext(false);
                                subscriber.onCompleted();
                                return;
                            }
                            CircleEntity circleEntity = GsonUtil.getInstance().fromJson(avObjects.get(0).toJSONObject().toString(), CircleEntity.class);
                            UserEntity me;
                            UserEntity other;
                            if (TextUtils.equals(circleEntity.getCreator().getObjectId(), currentUser.getObjectId())) {
                                me = circleEntity.getCreator();
                                other = circleEntity.getInvitee();
                            } else {
                                other = circleEntity.getCreator();
                                me = circleEntity.getInvitee();
                            }
                            subscriber.onNext(accountManager.saveUserAndCircleSync(me, other, circleEntity));
                            subscriber.onCompleted();

                        } catch (AVException e) {
                            subscriber.onError(e);
                        }
                    }
                });
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
