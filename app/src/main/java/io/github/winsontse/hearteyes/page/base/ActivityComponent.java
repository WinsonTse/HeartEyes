package io.github.winsontse.hearteyes.page.base;

import android.support.v7.app.AppCompatActivity;

import dagger.Component;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.data.remote.WeiboApi;
import io.github.winsontse.hearteyes.util.scope.BaseActivityScope;

/**
 * Created by hao.xie on 16/5/10.
 */
@Component(modules = {ActivityModule.class}, dependencies = {AppComponent.class})
@BaseActivityScope
public interface ActivityComponent extends AppComponent {
    AppCompatActivity getAppCompatActivity();

}
