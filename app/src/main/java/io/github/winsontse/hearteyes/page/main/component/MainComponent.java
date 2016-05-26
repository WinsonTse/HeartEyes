package io.github.winsontse.hearteyes.page.main.component;

import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.page.main.MainActivity;
import io.github.winsontse.hearteyes.page.main.module.MainModule;
import io.github.winsontse.hearteyes.util.scope.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(dependencies = {AppComponent.class}, modules = {MainModule.class})
public interface MainComponent {

    void inject(MainActivity activity);

}

