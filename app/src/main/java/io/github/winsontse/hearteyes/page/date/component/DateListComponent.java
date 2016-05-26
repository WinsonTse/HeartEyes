package io.github.winsontse.hearteyes.page.date.component;

import io.github.winsontse.hearteyes.page.date.DateListFragment;
import io.github.winsontse.hearteyes.page.date.module.DateListModule;
import io.github.winsontse.hearteyes.page.base.ActivityComponent;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(dependencies = {ActivityComponent.class}, modules = {DateListModule.class})
public interface DateListComponent extends ActivityComponent {

    void inject(DateListFragment fragment);

}

