package io.github.winsontse.hearteyes.page.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.page.adapter.MainPagerAdapter;
import io.github.winsontse.hearteyes.page.base.BaseFragment;
import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.page.main.component.DaggerHomeComponent;
import io.github.winsontse.hearteyes.page.main.contract.HomeContract;
import io.github.winsontse.hearteyes.page.main.module.HomeModule;
import io.github.winsontse.hearteyes.page.main.presenter.HomePresenter;

public class HomeFragment extends BaseFragment implements HomeContract.View {

    @Inject
    HomePresenter presenter;
    public static final int PAGE_MOMENT = 0;
    public static final int PAGE_TODO = 1;
    public static final int PAGE_ACCOUNT = 2;
    @BindView(R.id.home_view_pager)
    ViewPager homeViewPager;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);
        homeViewPager.setAdapter(new MainPagerAdapter(getChildFragmentManager()));

        return rootView;
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerHomeComponent.builder()
                .appComponent(appComponent)
                .homeModule(new HomeModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

}