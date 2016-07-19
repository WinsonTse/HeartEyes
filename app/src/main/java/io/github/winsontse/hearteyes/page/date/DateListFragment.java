package io.github.winsontse.hearteyes.page.date;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.page.base.BaseFragment;
import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.page.date.component.DaggerDateListComponent;
import io.github.winsontse.hearteyes.page.date.contract.DateListContract;
import io.github.winsontse.hearteyes.page.date.module.DateListModule;
import io.github.winsontse.hearteyes.page.date.presenter.DateListPresenter;

public class DateListFragment extends BaseFragment implements DateListContract.View {

    @Inject
    DateListPresenter presenter;

    public static DateListFragment newInstance() {
        Bundle args = new Bundle();
        DateListFragment fragment = new DateListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void initView(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_date_list;
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerDateListComponent.builder()
                .appComponent(appComponent)
                .dateListModule(new DateListModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

}