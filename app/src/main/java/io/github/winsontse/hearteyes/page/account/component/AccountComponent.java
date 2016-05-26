package io.github.winsontse.hearteyes.page.account.component;

import io.github.winsontse.hearteyes.page.account.AccountFragment;
import io.github.winsontse.hearteyes.page.account.module.AccountModule;
import io.github.winsontse.hearteyes.page.base.ActivityComponent;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(dependencies = {ActivityComponent.class}, modules = {AccountModule.class})
public interface AccountComponent extends ActivityComponent {

    void inject(AccountFragment fragment);

}

