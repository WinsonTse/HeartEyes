package io.github.winsontse.hearteyes.page.main.component;

import io.github.winsontse.hearteyes.page.main.HomeFragment;
import io.github.winsontse.hearteyes.page.main.module.HomeModule;
import io.github.winsontse.hearteyes.page.base.ActivityComponent;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(dependencies = {ActivityComponent.class}, modules = {HomeModule.class})
public interface HomeComponent extends ActivityComponent {

    void inject(HomeFragment fragment);

}

