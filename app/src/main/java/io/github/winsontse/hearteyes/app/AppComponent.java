package io.github.winsontse.hearteyes.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import dagger.Component;
import io.github.winsontse.hearteyes.model.remote.WeiboService;
import io.github.winsontse.hearteyes.util.scope.ApplicationScope;

/**
 * Created by hao.xie on 16/5/9.
 */

@ApplicationScope
@Component(modules = {AppModule.class})
public interface AppComponent {

    Context getApplicationContext();

    WeiboService getWeiboApi();

    LeanCloudManager getLeanCloudManager();

    Resources getResources();

    SharedPreferences getSharedPreferences();
}
