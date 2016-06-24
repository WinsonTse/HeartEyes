package io.github.winsontse.hearteyes.page.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.page.user.component.DaggerUserComponent;
import io.github.winsontse.hearteyes.page.user.contract.UserContract;
import io.github.winsontse.hearteyes.page.user.module.UserModule;
import io.github.winsontse.hearteyes.page.user.presenter.UserPresenter;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.page.base.BaseFragment;
import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.R;

public class UserFragment extends BaseFragment implements UserContract.View {

    @Inject
    UserPresenter presenter;

    public static UserFragment newInstance() {
        Bundle args = new Bundle();
        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);
        return rootView;
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerUserComponent.builder()
                .appComponent(appComponent)
                .userModule(new UserModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

}