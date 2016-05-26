package io.github.winsontse.hearteyes.page.account.module;

import dagger.Module;
import dagger.Provides;
import io.github.winsontse.hearteyes.page.account.contract.AssosiationContract;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;


@Module
public class AssosiationModule {
    private AssosiationContract.View view;

    public AssosiationModule(AssosiationContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    AssosiationContract.View provideAssosiationView() {
        return view;
    }
}
