package io.github.winsontse.hearteyes.app;


import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import io.github.winsontse.hearteyes.model.remote.WeiboService;
import io.github.winsontse.hearteyes.util.scope.ApplicationScope;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by winson on 16/5/25.
 */
@Module
public class RetrofitModule {
    public static final int DEFAULT_TIMEOUT = 60;

    @ApplicationScope
    @Provides
    OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(httpLoggingInterceptor);
        return builder.build();
    }


    @ApplicationScope
    @Provides
    Retrofit provideRetrofit(OkHttpClient httpClient) {

        return new Retrofit.Builder()
                .client(httpClient)
                .baseUrl("http://www.baidu.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @ApplicationScope
    @Provides
    WeiboService provideWeiboService(Retrofit retrofit) {
        return retrofit.create(WeiboService.class);
    }
}
