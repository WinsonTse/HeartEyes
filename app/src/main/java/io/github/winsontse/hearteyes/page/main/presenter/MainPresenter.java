package io.github.winsontse.hearteyes.page.main.presenter;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.page.main.contract.MainContract;
import io.github.winsontse.hearteyes.page.base.BasePresenterImpl;

public class MainPresenter extends BasePresenterImpl implements MainContract.Presenter {
    private MainContract.View view;

    @Inject
    public MainPresenter(MainContract.View view) {
        this.view = view;
    }
}
