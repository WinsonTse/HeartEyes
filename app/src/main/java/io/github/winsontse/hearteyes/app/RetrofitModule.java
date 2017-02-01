package io.github.winsontse.hearteyes.app;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import io.github.winsontse.hearteyes.model.remote.CircleService;
import io.github.winsontse.hearteyes.model.remote.UserService;
import io.github.winsontse.hearteyes.model.remote.WeiboService;
import io.github.winsontse.hearteyes.util.scope.ApplicationScope;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
    public static final String BASE_URL = "https://api.leancloud.cn/1.1/";

    @ApplicationScope
    @Provides
    OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(httpLoggingInterceptor);
        builder.addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder requestBuilder = request.newBuilder();
                requestBuilder.addHeader("Content-Type", "application/json");
                requestBuilder.addHeader("X-LC-Id", "application/json");
                requestBuilder.addHeader("X-LC-Key", "application/json");

                return chain.proceed(requestBuilder.build());
            }
        });
        return builder.build();
    }


    @ApplicationScope
    @Provides
    Retrofit provideRetrofit(OkHttpClient httpClient) {

        return new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @ApplicationScope
    @Provides
    WeiboService provideWeiboService(Retrofit retrofit) {
        return retrofit.create(WeiboService.class);
    }

    @ApplicationScope
    @Provides
    UserService provideUserService(Retrofit retrofit) {
        return retrofit.create(UserService.class);
    }

    @ApplicationScope
    @Provides
    CircleService provideCircleService(Retrofit retrofit) {
        return retrofit.create(CircleService.class);
    }
}
