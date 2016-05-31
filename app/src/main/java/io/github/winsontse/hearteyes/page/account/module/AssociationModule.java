package io.github.winsontse.hearteyes.page.account.module;

import dagger.Module;
import dagger.Provides;
import io.github.winsontse.hearteyes.page.account.contract.AssociationContract;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;


@Module
public class AssociationModule {
    private AssociationContract.View view;

    public AssociationModule(AssociationContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    AssociationContract.View provideAssosiationView() {
        return view;
    }
}
