package io.github.winsontse.hearteyes.app;

import android.content.Context;

import com.avos.avoscloud.AVPush;

import dagger.Module;
import dagger.Provides;
import io.github.winsontse.hearteyes.util.scope.ApplicationScope;

/**
 * Created by hao.xie on 16/5/9.
 */
@Module(includes = {RetrofitModule.class})
public class AppModule {
    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @ApplicationScope
    public Context provideContext() {
        return context;
    }
}
