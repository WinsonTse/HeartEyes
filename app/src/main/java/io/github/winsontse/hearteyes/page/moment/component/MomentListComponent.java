package io.github.winsontse.hearteyes.page.moment.component;

import io.github.winsontse.hearteyes.page.moment.MomentListFragment;
import io.github.winsontse.hearteyes.page.moment.module.MomentListModule;
import io.github.winsontse.hearteyes.page.base.ActivityComponent;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(dependencies = {ActivityComponent.class}, modules = {MomentListModule.class})
public interface MomentListComponent extends ActivityComponent {

    void inject(MomentListFragment fragment);

}

