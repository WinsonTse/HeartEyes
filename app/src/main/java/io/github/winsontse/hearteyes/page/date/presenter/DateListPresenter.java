package io.github.winsontse.hearteyes.page.date.presenter;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.page.date.contract.DateListContract;
import io.github.winsontse.hearteyes.page.base.BasePresenterImpl;

public class DateListPresenter extends BasePresenterImpl implements DateListContract.Presenter {
    private DateListContract.View view;

    @Inject
    public DateListPresenter(DateListContract.View view) {
        this.view = view;
    }
}
