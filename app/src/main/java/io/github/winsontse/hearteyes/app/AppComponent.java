package io.github.winsontse.hearteyes.app;

import android.content.Context;

import javax.inject.Qualifier;
import javax.inject.Singleton;

import dagger.Component;
import io.github.winsontse.hearteyes.data.remote.WeiboApi;
import io.github.winsontse.hearteyes.util.scope.ApplicationScope;

/**
 * Created by hao.xie on 16/5/9.
 */

@ApplicationScope
@Component(modules = {AppModule.class})
public interface AppComponent {

    Context getApplicationContext();

    WeiboApi getWeiboApi();

}
