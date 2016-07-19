package io.github.winsontse.hearteyes.page.map.presenter;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.page.map.contract.AddressContract;
import io.github.winsontse.hearteyes.page.base.BasePresenterImpl;

public class AddressPresenter extends BasePresenterImpl implements AddressContract.Presenter {
    private AddressContract.View view;

    @Inject
    public AddressPresenter(AddressContract.View view) {
        this.view = view;
    }
}
