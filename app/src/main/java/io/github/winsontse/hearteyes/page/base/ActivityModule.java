package io.github.winsontse.hearteyes.page.base;

import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hao.xie on 16/5/10.
 */
@Module
public class ActivityModule {
    private AppCompatActivity appCompatActivity;

    public ActivityModule(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    @Provides
    public AppCompatActivity provideAppCompatActivity() {
        return appCompatActivity;
    }
}
