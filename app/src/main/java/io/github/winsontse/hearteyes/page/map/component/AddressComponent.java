package io.github.winsontse.hearteyes.page.map.component;

import io.github.winsontse.hearteyes.page.map.AddressFragment;
import io.github.winsontse.hearteyes.page.map.module.AddressModule;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(dependencies = {AppComponent.class}, modules = {AddressModule.class})
public interface AddressComponent {

    void inject(AddressFragment fragment);

}

