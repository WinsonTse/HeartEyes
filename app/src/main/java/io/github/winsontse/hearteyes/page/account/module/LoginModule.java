package io.github.winsontse.hearteyes.page.account.module;

import dagger.Module;
import dagger.Provides;
import io.github.winsontse.hearteyes.page.account.contract.LoginContract;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;


@Module
public class LoginModule {
    private LoginContract.View view;

    public LoginModule(LoginContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    LoginContract.View provideLoginView() {
        return view;
    }
}
