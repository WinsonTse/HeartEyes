package io.github.winsontse.hearteyes.page.main.component;

import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.page.main.HomeFragment;
import io.github.winsontse.hearteyes.page.main.module.HomeModule;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(dependencies = {AppComponent.class}, modules = {HomeModule.class})
public interface HomeComponent {

    void inject(HomeFragment fragment);

}

