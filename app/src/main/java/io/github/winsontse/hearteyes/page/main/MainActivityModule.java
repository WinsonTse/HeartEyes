package io.github.winsontse.hearteyes.page.main;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hao.xie on 16/5/10.
 */
@Module
public class MainActivityModule {
    private final MainActivityContract.View view;

    public MainActivityModule(MainActivityContract.View view) {
        this.view = view;
    }

    @Provides
    MainActivityContract.View provideMainActivityContractView() {
        return view;
    }
}
