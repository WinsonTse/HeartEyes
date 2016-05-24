package io.github.winsontse.hearteyes.page.main;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.page.account.LoginActivity;
import io.github.winsontse.hearteyes.page.adapter.MainPagerAdapter;
import io.github.winsontse.hearteyes.page.base.ActivityComponent;
import io.github.winsontse.hearteyes.page.base.BaseActivity;
import io.github.winsontse.hearteyes.page.base.BasePresenter;

public class MainActivity extends BaseActivity implements MainActivityContract.View {
    @Inject
    MainActivityPresenter presenter;

    public static final int PAGE_MOMENT = 0;
    public static final int PAGE_TODO = 1;
    public static final int PAGE_ACCOUNT = 2;
    @BindView(R.id.main_view_pager)
    ViewPager mainViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mainViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        LoginActivity.goToLoginPage(this);
    }

    @Override
    protected void setupComponent(ActivityComponent activityComponent) {
        DaggerMainActivityComponent.builder()
                .activityComponent(getActivityComponent())
                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

}
