package io.github.winsontse.hearteyes.page.main;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.page.base.BasePresenterImpl;

/**
 * Created by hao.xie on 16/5/10.
 */
public class MainActivityPresenter extends BasePresenterImpl implements MainActivityContract.Presenter {
    private MainActivityContract.View view;

    @Inject
    public MainActivityPresenter(MainActivityContract.View view) {
        this.view = view;
    }

    public void init() {

    }

}
