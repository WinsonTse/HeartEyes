package io.github.winsontse.hearteyes.page.account.component;

import io.github.winsontse.hearteyes.page.account.AssosiationFragment;
import io.github.winsontse.hearteyes.page.account.module.AssosiationModule;
import io.github.winsontse.hearteyes.page.base.ActivityComponent;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(dependencies = {ActivityComponent.class}, modules = {AssosiationModule.class})
public interface AssosiationComponent extends ActivityComponent {

    void inject(AssosiationFragment fragment);

}

