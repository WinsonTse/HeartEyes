package io.github.winsontse.hearteyes.app;

import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVOSCloud;

import io.github.winsontse.hearteyes.util.LogUtil;
import io.github.winsontse.hearteyes.util.ScreenUtil;
import io.github.winsontse.hearteyes.util.constant.SecretConstant;

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

    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
