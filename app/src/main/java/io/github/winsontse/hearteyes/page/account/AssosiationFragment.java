package io.github.winsontse.hearteyes.page.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.page.account.component.DaggerAssosiationComponent;
import io.github.winsontse.hearteyes.page.account.contract.AssosiationContract;
import io.github.winsontse.hearteyes.page.account.module.AssosiationModule;
import io.github.winsontse.hearteyes.page.account.presenter.AssosiationPresenter;
import io.github.winsontse.hearteyes.page.base.BaseFragment;
import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.R;

public class AssosiationFragment extends BaseFragment implements AssosiationContract.View {

    @Inject
    AssosiationPresenter presenter;

    public static AssosiationFragment newInstance() {
        Bundle args = new Bundle();
        AssosiationFragment fragment = new AssosiationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_assosiation, container, false);
        return rootView;
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerAssosiationComponent.builder()
                .appComponent(appComponent)
                .assosiationModule(new AssosiationModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

}