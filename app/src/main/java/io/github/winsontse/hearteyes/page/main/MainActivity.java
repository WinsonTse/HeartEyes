package io.github.winsontse.hearteyes.page.main;

import android.os.Bundle;

import javax.inject.Inject;

import butterknife.ButterKnife;
import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.page.account.LoginActivity;
import io.github.winsontse.hearteyes.page.base.ActivityComponent;
import io.github.winsontse.hearteyes.page.base.BaseActivity;
import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.page.main.component.DaggerMainComponent;
import io.github.winsontse.hearteyes.page.main.contract.MainContract;
import io.github.winsontse.hearteyes.page.main.module.MainModule;
import io.github.winsontse.hearteyes.page.main.presenter.MainPresenter;

public class MainActivity extends BaseActivity implements MainContract.View {
    @Inject
    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        LoginActivity.goToLoginPage(this);
    }

    @Override
    protected void setupComponent(ActivityComponent activityComponent) {
        DaggerMainComponent.builder()
                .activityComponent(getActivityComponent())
                .mainModule(new MainModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

}
