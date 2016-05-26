package io.github.winsontse.hearteyes.page.account.component;

import io.github.winsontse.hearteyes.app.ApiServiceModule;
import io.github.winsontse.hearteyes.app.RetrofitModule;
import io.github.winsontse.hearteyes.data.remote.WeiboApi;
import io.github.winsontse.hearteyes.page.account.LoginFragment;
import io.github.winsontse.hearteyes.page.account.module.LoginModule;
import io.github.winsontse.hearteyes.page.base.ActivityComponent;
import io.github.winsontse.hearteyes.util.scope.ActivityScope;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(dependencies = {ActivityComponent.class}, modules = {LoginModule.class})
public interface LoginComponent extends ActivityComponent{

    void inject(LoginFragment fragment);


}

