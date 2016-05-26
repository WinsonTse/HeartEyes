package io.github.winsontse.hearteyes.page.main;

import android.os.Bundle;
import android.view.KeyEvent;

import javax.inject.Inject;

import butterknife.ButterKnife;
import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.page.account.LoginFragment;
import io.github.winsontse.hearteyes.page.base.ActivityComponent;
import io.github.winsontse.hearteyes.page.base.BaseActivity;
import io.github.winsontse.hearteyes.page.base.BaseFragment;
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


        presenter.init();
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


    public void addFragment(BaseFragment baseFragment, boolean isAddToBackStack) {
        addFragment(R.id.fragment_container, baseFragment, isAddToBackStack);
    }

    public void replaceFragment(BaseFragment baseFragment, boolean isAddToBackStack) {
        replaceFragment(R.id.fragment_container, baseFragment, isAddToBackStack);
    }


    /**
     * Take care of calling onBackPressed() for pre-Eclair platforms.
     *
     * @param keyCode
     * @param event
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                moveTaskToBack(true);
                return true;
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void goToHomePage() {
        if (findFragmentByClass(HomeFragment.class)) {
            replaceFragment(HomeFragment.newInstance(), false);
        }

    }

    @Override
    public void goToLoginPage() {
        if (findFragmentByClass(LoginFragment.class)) {
            replaceFragment(LoginFragment.newInstance(), false);
        }
    }
}
