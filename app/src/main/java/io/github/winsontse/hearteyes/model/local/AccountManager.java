package io.github.winsontse.hearteyes.model.local;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import io.github.winsontse.hearteyes.model.entity.account.UserEntity;
import io.github.winsontse.hearteyes.model.entity.account.UserEntity_Table;
import io.github.winsontse.hearteyes.model.entity.circle.CircleEntity;
import io.github.winsontse.hearteyes.model.entity.circle.CircleEntity_Table;
import io.github.winsontse.hearteyes.model.local.base.BaseDBManager;
import io.github.winsontse.hearteyes.util.HeartEyesSubscriber;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by winson on 2017/2/1.
 */

public class AccountManager extends BaseDBManager<UserEntity> implements IAccountManager {

    private UserEntity loginUser;
    private UserEntity friend;
    private CircleEntity circle;


    public AccountManager() {
        super(UserEntity.class, UserEntity_Table.objectId);
        init();
    }

    private void init() {
        loginUser = loadSingleUserSync(UserEntity.USER_TYPE_LOGIN);
        friend = loadSingleUserSync(UserEntity.USER_TYPE_FRIEND);
        circle = loadMyCicleSync();
    }


    @Override
    public UserEntity loadSingleUserSync(@UserEntity.UserType int type) {
        return selectFrom().where(UserEntity_Table.type.eq(type)).querySingle();
    }

    @Override
    public Observable<UserEntity> loadSingleUserObservable(@UserEntity.UserType final int type) {
        return Observable.create(new Observable.OnSubscribe<UserEntity>() {
            @Override
            public void call(Subscriber<? super UserEntity> subscriber) {
                try {
                    subscriber.onNext(loadSingleUserSync(type));
                    subscriber.onCompleted();
                } catch (Throwable e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public UserEntity loadLoginUserSync() {
        return loadSingleUserSync(UserEntity.USER_TYPE_LOGIN);
    }

    @Override
    public Observable<UserEntity> loadLoginUserObservable() {
        return loadSingleUserObservable(UserEntity.USER_TYPE_LOGIN);
    }

    @Override
    public UserEntity loadFriendSync() {
        return loadSingleUserSync(UserEntity.USER_TYPE_FRIEND);
    }

    @Override
    public Observable<UserEntity> loadFriendObservable() {
        return loadSingleUserObservable(UserEntity.USER_TYPE_FRIEND);
    }

    @Override
    public CircleEntity loadMyCicleSync() {
        return SQLite.select().from(CircleEntity.class).where(CircleEntity_Table.type.eq(CircleEntity.CIRCLE_TYPE_MINE)).querySingle();
    }

    @Override
    public Observable<CircleEntity> loadMyCicleObservable() {
        return Observable.create(new Observable.OnSubscribe<CircleEntity>() {
            @Override
            public void call(Subscriber<? super CircleEntity> subscriber) {
                try {
                    subscriber.onNext(loadMyCicleSync());
                    subscriber.onCompleted();
                } catch (Throwable e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public void updateUser(@UserEntity.UserType final int type, final UserEntity userEntity) {
        if (userEntity == null) return;
        userEntity.setType(type);
        Observable
                .create(new Observable.OnSubscribe<UserEntity>() {
                    @Override
                    public void call(Subscriber<? super UserEntity> subscriber) {
                        boolean exists = userEntity.exists();
                        if (exists) {
                            userEntity.update();
                        } else {
                            userEntity.insert();
                        }
                        if (type == UserEntity.USER_TYPE_LOGIN) {
                            AccountManager.this.loginUser = userEntity;
                        } else if (type == UserEntity.USER_TYPE_FRIEND) {
                            AccountManager.this.friend = userEntity;
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HeartEyesSubscriber<UserEntity>() {
                    @Override
                    public void handleError(Throwable e) {

                    }

                    @Override
                    public void onNext(UserEntity result) {
                    }
                });
    }

    @Override
    public void updateCircle(final CircleEntity circleEntity) {
        if (circleEntity == null) return;
        Observable
                .create(new Observable.OnSubscribe<CircleEntity>() {
                    @Override
                    public void call(Subscriber<? super CircleEntity> subscriber) {
                        try {
                            circleEntity.setType(CircleEntity.CIRCLE_TYPE_MINE);
                            circleEntity.save();
                            AccountManager.this.circle = circleEntity;
                            subscriber.onNext(circleEntity);
                            subscriber.onCompleted();
                        } catch (Throwable e) {
                            subscriber.onError(e);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HeartEyesSubscriber<CircleEntity>() {
                    @Override
                    public void handleError(Throwable e) {

                    }

                    @Override
                    public void onNext(CircleEntity result) {

                    }
                });
    }

    @Override
    public Observable<Boolean> saveUserAndCircleObservable(final UserEntity loginUser, final UserEntity friend, final CircleEntity circleEntity) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    if (loginUser == null || friend == null || circleEntity == null) {
                        subscriber.onNext(false);
                    } else {
                        subscriber.onNext(saveUserAndCircleSync(loginUser, friend, circleEntity));
                    }
                    subscriber.onCompleted();
                } catch (Throwable e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public boolean saveUserAndCircleSync(UserEntity loginUser, UserEntity friend, CircleEntity circleEntity) {
        loginUser.setType(UserEntity.USER_TYPE_LOGIN);
        friend.setType(UserEntity.USER_TYPE_FRIEND);
        circleEntity.setType(CircleEntity.CIRCLE_TYPE_MINE);
        boolean save0 = loginUser.save();
        boolean save1 = friend.save();
        boolean save2 = circleEntity.save();
        boolean result = save0 && save1 && save2;
        if (result) {
            this.loginUser = loginUser;
            this.friend = friend;
            this.circle = circleEntity;
        }
        return result;
    }

    @Override
    public boolean isValidUser() {
        return loginUser != null && friend != null && circle != null;
    }

    @Override
    public UserEntity getLoginUser() {
        return loginUser;
    }

    @Override
    public UserEntity getFriend() {
        return friend;
    }

    @Override
    public CircleEntity getMyCircle() {
        return circle;
    }
}
