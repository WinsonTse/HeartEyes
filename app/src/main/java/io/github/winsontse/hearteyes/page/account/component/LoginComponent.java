package io.github.winsontse.hearteyes.page.account.component;

import dagger.Component;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.page.account.LoginActivity;
import io.github.winsontse.hearteyes.page.account.module.LoginModule;
import io.github.winsontse.hearteyes.util.scope.ActivityScope;

@ActivityScope
@Component(dependencies = {AppComponent.class}, modules = {LoginModule.class})
public interface LoginComponent {

    void inject(LoginActivity activity);


}

