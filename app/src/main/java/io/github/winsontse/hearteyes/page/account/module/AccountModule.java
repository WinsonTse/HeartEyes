package io.github.winsontse.hearteyes.page.account.module;

import dagger.Module;
import dagger.Provides;
import io.github.winsontse.hearteyes.page.account.contract.AccountContract;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;


@Module
public class AccountModule {
    private AccountContract.View view;

    public AccountModule(AccountContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    AccountContract.View provideAccountView() {
        return view;
    }
}
