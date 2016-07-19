package io.github.winsontse.hearteyes.page.map.module;

import dagger.Module;
import dagger.Provides;
import io.github.winsontse.hearteyes.page.map.contract.AddressContract;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;


@Module
public class AddressModule {
    private AddressContract.View view;

    public AddressModule(AddressContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    AddressContract.View provideShowLocationView() {
        return view;
    }
}
