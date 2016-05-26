package io.github.winsontse.hearteyes.page.account.presenter;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.page.account.contract.AssosiationContract;
import io.github.winsontse.hearteyes.page.base.BasePresenterImpl;

public class AssosiationPresenter extends BasePresenterImpl implements AssosiationContract.Presenter {
    private AssosiationContract.View view;

    @Inject
    public AssosiationPresenter(AssosiationContract.View view) {
        this.view = view;
    }
}
