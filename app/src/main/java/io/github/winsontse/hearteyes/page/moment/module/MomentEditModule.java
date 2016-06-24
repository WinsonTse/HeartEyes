package io.github.winsontse.hearteyes.page.moment.module;

import dagger.Module;
import dagger.Provides;
import io.github.winsontse.hearteyes.page.moment.contract.MomentEditContract;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;


@Module
public class MomentEditModule {
    private MomentEditContract.View view;

    public MomentEditModule(MomentEditContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    MomentEditContract.View provideMomentEditView() {
        return view;
    }
}
