package io.github.winsontse.hearteyes.page.moment.component;

import io.github.winsontse.hearteyes.page.moment.MomentEditFragment;
import io.github.winsontse.hearteyes.page.moment.module.MomentEditModule;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(dependencies = {AppComponent.class}, modules = {MomentEditModule.class})
public interface MomentEditComponent {

    void inject(MomentEditFragment fragment);

}

