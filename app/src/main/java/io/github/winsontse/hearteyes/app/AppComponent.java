package io.github.winsontse.hearteyes.app;

import android.content.Context;

import javax.inject.Qualifier;
import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by hao.xie on 16/5/9.
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(HeartEyesApplication appApplication);
    Context getApplicationContext();
}
