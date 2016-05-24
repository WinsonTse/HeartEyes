package io.github.winsontse.hearteyes.page.account.presenter;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.page.account.contract.AccountContract;
import io.github.winsontse.hearteyes.page.base.BasePresenterImpl;

public class AccountPresenter extends BasePresenterImpl implements AccountContract.Presenter {
    private AccountContract.View view;

    @Inject
    public AccountPresenter(AccountContract.View view) {
        this.view = view;
    }
}
