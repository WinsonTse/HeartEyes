package io.github.winsontse.hearteyes.page.moment.component;

import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.page.moment.MomentListFragment;
import io.github.winsontse.hearteyes.page.moment.module.MomentListModule;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(dependencies = {AppComponent.class}, modules = {MomentListModule.class})
public interface MomentListComponent {

    void inject(MomentListFragment fragment);

}

