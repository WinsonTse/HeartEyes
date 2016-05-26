package io.github.winsontse.hearteyes.page.account.component;

import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.page.account.AssosiationFragment;
import io.github.winsontse.hearteyes.page.account.module.AssosiationModule;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(dependencies = {AppComponent.class}, modules = {AssosiationModule.class})
public interface AssosiationComponent {

    void inject(AssosiationFragment fragment);

}

