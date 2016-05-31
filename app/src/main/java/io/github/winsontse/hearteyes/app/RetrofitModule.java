package io.github.winsontse.hearteyes.app;


import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.winsontse.hearteyes.data.remote.WeiboApi;
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

    @Provides
    Retrofit provideRetrofit() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder.addInterceptor(httpLoggingInterceptor);

        return new Retrofit.Builder()
                .client(new OkHttpClient.Builder().build())
                .baseUrl(WeiboApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    WeiboApi provideWeiboApi(Retrofit retrofit) {
        return retrofit.create(WeiboApi.class);
    }
}
