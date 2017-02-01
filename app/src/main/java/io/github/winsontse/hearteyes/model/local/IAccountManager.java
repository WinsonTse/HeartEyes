package io.github.winsontse.hearteyes.model.local;

import io.github.winsontse.hearteyes.model.entity.account.UserEntity;
import io.github.winsontse.hearteyes.model.entity.circle.CircleEntity;
import io.github.winsontse.hearteyes.model.local.base.IBaseDBManager;
import rx.Observable;

/**
 * Created by winson on 2017/2/1.
 */

public interface IAccountManager extends IBaseDBManager<UserEntity> {
    UserEntity loadSingleUserSync(@UserEntity.UserType int type);

    Observable<UserEntity> loadSingleUserObservable(@UserEntity.UserType int type);

    UserEntity loadLoginUserSync();

    Observable<UserEntity> loadLoginUserObservable();

    UserEntity loadFriendSync();

    Observable<UserEntity> loadFriendObservable();

    CircleEntity loadMyCicleSync();

    Observable<CircleEntity> loadMyCicleObservable();

    void updateUser(@UserEntity.UserType int type, UserEntity userEntity);

    void updateCircle(CircleEntity circleEntity);

    Observable<Boolean> saveUserAndCircleObservable(UserEntity loginUser, UserEntity friend, CircleEntity circleEntity);

    boolean saveUserAndCircleSync(UserEntity loginUser, UserEntity friend, CircleEntity circleEntity);

    boolean isValidUser();

    UserEntity getLoginUser();

    UserEntity getFriend();

    CircleEntity getMyCircle();
}
