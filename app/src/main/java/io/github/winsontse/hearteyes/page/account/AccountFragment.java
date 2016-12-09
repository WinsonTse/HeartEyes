package io.github.winsontse.hearteyes.page.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.page.account.component.DaggerAccountComponent;
import io.github.winsontse.hearteyes.page.account.contract.AccountContract;
import io.github.winsontse.hearteyes.page.account.module.AccountModule;
import io.github.winsontse.hearteyes.page.account.presenter.AccountPresenter;
import io.github.winsontse.hearteyes.page.base.BaseFragment;
import io.github.winsontse.hearteyes.page.base.BasePresenter;

public class AccountFragment extends BaseFragment implements AccountContract.View {

    @Inject
    AccountPresenter presenter;

    public static AccountFragment newInstance() {
        Bundle args = new Bundle();
        AccountFragment fragment = new AccountFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_account;
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder()
                .appComponent(appComponent)
                .accountModule(new AccountModule(this))
                .build()
                .inject(this);
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

}