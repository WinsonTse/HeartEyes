package io.github.winsontse.hearteyes.app;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hao.xie on 16/5/9.
 */
@Module
public class AppModule {
    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    public Context provideContext() {
        return context;
    }
}
