package io.github.winsontse.hearteyes.page.account.component;

import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.page.account.AccountFragment;
import io.github.winsontse.hearteyes.page.account.module.AccountModule;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(dependencies = {AppComponent.class}, modules = {AccountModule.class})
public interface AccountComponent {

    void inject(AccountFragment fragment);

}

