package io.github.winsontse.hearteyes.page.user.presenter;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.page.user.contract.UserContract;
import io.github.winsontse.hearteyes.page.base.BasePresenterImpl;

public class UserPresenter extends BasePresenterImpl implements UserContract.Presenter {
    private UserContract.View view;

    @Inject
    public UserPresenter(UserContract.View view) {
        this.view = view;
    }
}
