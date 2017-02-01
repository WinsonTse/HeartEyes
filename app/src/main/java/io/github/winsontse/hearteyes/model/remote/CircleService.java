package io.github.winsontse.hearteyes.model.remote;

import io.github.winsontse.hearteyes.model.entity.account.UserEntity;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by winson on 2017/1/29.
 */

public interface CircleService {
    String CIRCLE = "/classes/circle/";

    @GET(CIRCLE)
    Observable<UserEntity> circles();
}
