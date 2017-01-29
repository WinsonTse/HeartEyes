package io.github.winsontse.hearteyes.page.account.component;

import dagger.Component;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.page.account.AssociationActivity;
import io.github.winsontse.hearteyes.page.account.module.AssociationModule;
import io.github.winsontse.hearteyes.util.scope.ActivityScope;

@ActivityScope
@Component(dependencies = {AppComponent.class}, modules = {AssociationModule.class})
public interface AssociationComponent {

    void inject(AssociationActivity activity);

}

