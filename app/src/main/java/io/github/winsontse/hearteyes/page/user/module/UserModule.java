package io.github.winsontse.hearteyes.page.user.module;

import dagger.Module;
import dagger.Provides;
import io.github.winsontse.hearteyes.page.user.contract.UserContract;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;


@Module
public class UserModule {
    private UserContract.View view;

    public UserModule(UserContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    UserContract.View provideUserView() {
        return view;
    }
}
