package io.github.winsontse.hearteyes.page.date.component;

import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.page.date.DateListFragment;
import io.github.winsontse.hearteyes.page.date.module.DateListModule;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(dependencies = {AppComponent.class}, modules = {DateListModule.class})
public interface DateListComponent {

    void inject(DateListFragment fragment);

}

