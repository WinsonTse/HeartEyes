package io.github.winsontse.hearteyes.page.main.module;

import dagger.Module;
import dagger.Provides;
import io.github.winsontse.hearteyes.page.main.contract.MainContract;
import io.github.winsontse.hearteyes.util.scope.ActivityScope;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;


@Module
public class MainModule {
    private MainContract.View view;

    public MainModule(MainContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MainContract.View provideMainView() {
        return view;
    }
}
