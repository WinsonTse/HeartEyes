package io.github.winsontse.hearteyes.model.remote;

import io.github.winsontse.hearteyes.model.entity.weibo.WeiboUser;
import io.github.winsontse.hearteyes.util.constant.Request;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by winson on 16/5/25.
 */
public interface WeiboService {
    String BASE_URL = "https://api.weibo.com/2/";

    @GET(BASE_URL + "users/show.json")
    Observable<WeiboUser> getUserByUid(@Query(Request.ACCESS_TOKEN) String token, @Query(Request.UID) String uid);

}
