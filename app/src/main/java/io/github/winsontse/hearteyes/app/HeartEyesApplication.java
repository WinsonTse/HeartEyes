package io.github.winsontse.hearteyes.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by hao.xie on 16/5/9.
 */
public class HeartEyesApplication extends Application {
    AppComponent appComponent;

    public static HeartEyesApplication get(Context context) {
        return (HeartEyesApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
