package io.github.winsontse.hearteyes.page.date;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.page.date.component.DaggerDateListComponent;
import io.github.winsontse.hearteyes.page.date.contract.DateListContract;
import io.github.winsontse.hearteyes.page.date.module.DateListModule;
import io.github.winsontse.hearteyes.page.date.presenter.DateListPresenter;
import io.github.winsontse.hearteyes.page.base.ActivityComponent;
import io.github.winsontse.hearteyes.page.base.BaseFragment;
import io.github.winsontse.hearteyes.R;

public class DateListFragment extends BaseFragment implements DateListContract.View {

    @Inject
    DateListPresenter presenter;

    public static DateListFragment newInstance() {
        Bundle args = new Bundle();
        DateListFragment fragment = new DateListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_date_list, container, false);
        return rootView;
    }

    @Override
    protected void setupComponent(ActivityComponent activityComponent) {
        DaggerDateListComponent.builder()
                .activityComponent(activityComponent)
                .dateListModule(new DateListModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

}