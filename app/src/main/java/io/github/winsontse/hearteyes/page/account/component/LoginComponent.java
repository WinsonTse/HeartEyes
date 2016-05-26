package io.github.winsontse.hearteyes.page.account.component;

import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.page.account.LoginFragment;
import io.github.winsontse.hearteyes.page.account.module.LoginModule;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(dependencies = {AppComponent.class}, modules = {LoginModule.class})
public interface LoginComponent {

    void inject(LoginFragment fragment);


}

