package io.github.winsontse.hearteyes.page.main.module;

import dagger.Module;
import dagger.Provides;
import io.github.winsontse.hearteyes.page.main.contract.HomeContract;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;


@Module
public class HomeModule {
    private HomeContract.View view;

    public HomeModule(HomeContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    HomeContract.View provideHomeView() {
        return view;
    }
}
