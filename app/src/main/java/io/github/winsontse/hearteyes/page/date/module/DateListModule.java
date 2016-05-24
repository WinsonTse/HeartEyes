package io.github.winsontse.hearteyes.page.date.module;

import dagger.Module;
import dagger.Provides;
import io.github.winsontse.hearteyes.page.date.contract.DateListContract;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;


@Module
public class DateListModule {
    private DateListContract.View view;

    public DateListModule(DateListContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    DateListContract.View provideDateListView() {
        return view;
    }
}
