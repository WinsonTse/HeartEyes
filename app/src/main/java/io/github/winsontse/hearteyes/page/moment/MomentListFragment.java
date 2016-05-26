package io.github.winsontse.hearteyes.page.moment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.page.moment.component.DaggerMomentListComponent;
import io.github.winsontse.hearteyes.page.moment.contract.MomentListContract;
import io.github.winsontse.hearteyes.page.moment.module.MomentListModule;
import io.github.winsontse.hearteyes.page.moment.presenter.MomentListPresenter;
import io.github.winsontse.hearteyes.page.base.BaseFragment;
import io.github.winsontse.hearteyes.R;

public class MomentListFragment extends BaseFragment implements MomentListContract.View {

    @Inject
    MomentListPresenter presenter;

    public static MomentListFragment newInstance() {
        Bundle args = new Bundle();
        MomentListFragment fragment = new MomentListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_moment_list, container, false);
        return rootView;
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerMomentListComponent.builder()
                .appComponent(appComponent)
                .momentListModule(new MomentListModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

}