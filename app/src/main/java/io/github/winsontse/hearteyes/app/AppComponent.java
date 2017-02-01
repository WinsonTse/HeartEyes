package io.github.winsontse.hearteyes.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import dagger.Component;
import io.github.winsontse.hearteyes.model.local.IAccountManager;
import io.github.winsontse.hearteyes.model.remote.CircleService;
import io.github.winsontse.hearteyes.model.remote.UserService;
import io.github.winsontse.hearteyes.model.remote.WeiboService;
import io.github.winsontse.hearteyes.util.scope.ApplicationScope;

/**
 * Created by hao.xie on 16/5/9.
 */

@ApplicationScope
@Component(modules = {AppModule.class})
public interface AppComponent {

    Context getApplicationContext();

    WeiboService getWeiboService();

    UserService getUserService();

    CircleService getCircleService();

    LeanCloudManager getLeanCloudManager();

    Resources getResources();

    SharedPreferences getSharedPreferences();

    IAccountManager getAccountManager();
}
