package io.github.winsontse.hearteyes.app;

import android.app.Application;
import android.content.Context;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import io.github.winsontse.hearteyes.util.ScreenUtil;

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
        init();
    }

    private void init() {
        appComponent.getLeanCloudManager().init();
        ScreenUtil.init(this);
        FlowManager.init(new FlowConfig.Builder(this).build());


    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
