package io.github.winsontse.hearteyes.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVSaveOption;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SendCallback;
import com.google.gson.Gson;

import org.json.JSONObject;

import io.github.winsontse.hearteyes.data.model.leancloud.UserContract;
import io.github.winsontse.hearteyes.page.main.MainActivity;
import io.github.winsontse.hearteyes.util.constant.SecretConstant;
import io.github.winsontse.hearteyes.util.rxbus.event.UidEvent;

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

        new AVPush().send();

        init();
    }

    private void init() {
        AVOSCloud.initialize(this, SecretConstant.LEANCLOUD_APP_ID, SecretConstant.LEANCLOUD_APP_KEY);

    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
