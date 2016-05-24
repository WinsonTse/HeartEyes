package io.github.winsontse.hearteyes.page.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.page.account.component.DaggerAccountComponent;
import io.github.winsontse.hearteyes.page.account.contract.AccountContract;
import io.github.winsontse.hearteyes.page.account.module.AccountModule;
import io.github.winsontse.hearteyes.page.account.presenter.AccountPresenter;
import io.github.winsontse.hearteyes.page.base.ActivityComponent;
import io.github.winsontse.hearteyes.page.base.BaseFragment;
import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.R;

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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);
        return rootView;
    }

    @Override
    protected void setupComponent(ActivityComponent activityComponent) {
        DaggerAccountComponent.builder()
                .activityComponent(activityComponent)
                .accountModule(new AccountModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

}