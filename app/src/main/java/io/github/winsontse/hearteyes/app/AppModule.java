package io.github.winsontse.hearteyes.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import dagger.Module;
import dagger.Provides;
import io.github.winsontse.hearteyes.util.constant.SecretConstant;
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


    @Provides
    @ApplicationScope
    public Resources provideResources() {
        return context.getResources();
    }

    @Provides
    @ApplicationScope
    public SharedPreferences provideSharedPreferences() {
        return context.getSharedPreferences(SecretConstant.SP_NAME, Context.MODE_PRIVATE);
    }
}
