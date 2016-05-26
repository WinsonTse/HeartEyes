package io.github.winsontse.hearteyes.page.user.component;

import io.github.winsontse.hearteyes.page.user.UserFragment;
import io.github.winsontse.hearteyes.page.user.module.UserModule;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(dependencies = {AppComponent.class}, modules = {UserModule.class})
public interface UserComponent {

    void inject(UserFragment fragment);

}

