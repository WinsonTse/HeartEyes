package io.github.winsontse.hearteyes.page.account.component;

import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.page.account.AssociationFragment;
import io.github.winsontse.hearteyes.page.account.module.AssociationModule;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(dependencies = {AppComponent.class}, modules = {AssociationModule.class})
public interface AssociationComponent {

    void inject(AssociationFragment fragment);

}

