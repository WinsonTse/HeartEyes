package io.github.winsontse.hearteyes.page.account.component;

import io.github.winsontse.hearteyes.page.account.LoginActivity;
import io.github.winsontse.hearteyes.page.account.module.LoginModule;
import io.github.winsontse.hearteyes.page.base.ActivityComponent;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(dependencies = {ActivityComponent.class}, modules = {LoginModule.class})
public interface LoginComponent {

    void inject(LoginActivity activity);

}

