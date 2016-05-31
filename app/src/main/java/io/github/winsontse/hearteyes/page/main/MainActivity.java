package io.github.winsontse.hearteyes.page.main;

import android.content.Intent;
import android.os.Bundle;

import com.avos.avoscloud.PushService;

import javax.inject.Inject;

import butterknife.ButterKnife;
import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.page.account.AssociationFragment;
import io.github.winsontse.hearteyes.page.account.LoginFragment;
import io.github.winsontse.hearteyes.page.base.BaseActivity;
import io.github.winsontse.hearteyes.page.base.BaseFragment;
import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.page.main.component.DaggerMainComponent;
import io.github.winsontse.hearteyes.page.main.contract.MainContract;
import io.github.winsontse.hearteyes.page.main.module.MainModule;
import io.github.winsontse.hearteyes.page.main.presenter.MainPresenter;
import io.github.winsontse.hearteyes.util.constant.SecretConstant;

public class MainActivity extends BaseActivity implements MainContract.View {
    @Inject
    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initPage();


    }

    public void initPage() {
        presenter.init();
        PushService.setDefaultPushCallback(this, MainActivity.class);
        PushService.subscribe(this, SecretConstant.PUSH_CHANNEL_PRIVATE, MainActivity.class);
        presenter.updateInstallationId();

    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerMainComponent.builder()
                .appComponent(appComponent)
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


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
//                moveTaskToBack(true);
//                return true;
//            } else {
//                getSupportFragmentManager().popBackStack();
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public void goToHomePage() {
        if (findFragmentByClass(HomeFragment.class) == null) {
            replaceFragment(HomeFragment.newInstance(), false);
        }

    }

    @Override
    public void goToLoginPage() {
        if (findFragmentByClass(LoginFragment.class) == null) {
            replaceFragment(LoginFragment.newInstance(), false);
        }
    }

    @Override
    public void goToAssosiationPage() {
        if (findFragmentByClass(AssociationFragment.class) == null) {
            replaceFragment(AssociationFragment.newInstance(), false);
        }
    }

    @Override
    public void finish() {
        moveTaskToBack(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        LoginFragment loginFragment = (LoginFragment) findFragmentByClass(LoginFragment.class);
        if (loginFragment != null) {
            loginFragment.onActivityResult(requestCode, resultCode, data);
        }
        presenter.handleActivityResult(requestCode, resultCode, data);
    }
}
