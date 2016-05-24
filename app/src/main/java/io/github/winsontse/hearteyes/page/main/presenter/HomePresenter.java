package io.github.winsontse.hearteyes.page.main.presenter;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.page.main.contract.HomeContract;
import io.github.winsontse.hearteyes.page.base.BasePresenterImpl;

public class HomePresenter extends BasePresenterImpl implements HomeContract.Presenter {
    private HomeContract.View view;

    @Inject
    public HomePresenter(HomeContract.View view) {
        this.view = view;
    }
}
