package io.github.winsontse.hearteyes.app;

import dagger.Module;
import dagger.Provides;
import io.github.winsontse.hearteyes.data.remote.WeiboApi;
import io.github.winsontse.hearteyes.util.scope.ApplicationScope;
import retrofit2.Retrofit;

/**
 * Created by winson on 16/5/25.
 */
@Module
public class ApiServiceModule {

    @Provides
    WeiboApi provideWeiboApi(Retrofit retrofit) {
        return retrofit.create(WeiboApi.class);
    }
}
