package io.github.winsontse.hearteyes.page.main;

import dagger.Component;
import io.github.winsontse.hearteyes.page.base.ActivityComponent;
import io.github.winsontse.hearteyes.util.scope.ActivityScope;

/**
 * Created by hao.xie on 16/5/10.
 */
@ActivityScope
@Component(modules = {MainActivityModule.class}, dependencies = {ActivityComponent.class})
public interface MainActivityComponent {
    void inject(MainActivity mainActivity);
}
