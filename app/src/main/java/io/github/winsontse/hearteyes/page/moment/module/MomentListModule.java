package io.github.winsontse.hearteyes.page.moment.module;

import dagger.Module;
import dagger.Provides;
import io.github.winsontse.hearteyes.page.moment.contract.MomentListContract;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;


@Module
public class MomentListModule {
    private MomentListContract.View view;

    public MomentListModule(MomentListContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    MomentListContract.View provideMomentListView() {
        return view;
    }
}
