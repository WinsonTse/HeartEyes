package io.github.winsontse.hearteyes.page.moment.presenter;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.page.moment.contract.MomentListContract;
import io.github.winsontse.hearteyes.page.base.BasePresenterImpl;

public class MomentListPresenter extends BasePresenterImpl implements MomentListContract.Presenter {
    private MomentListContract.View view;

    @Inject
    public MomentListPresenter(MomentListContract.View view) {
        this.view = view;
    }
}
