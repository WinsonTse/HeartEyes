package io.github.winsontse.hearteyes.page.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;

import com.avos.avoscloud.PushService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.page.account.AssociationFragment;
import io.github.winsontse.hearteyes.page.account.LoginFragment;
import io.github.winsontse.hearteyes.page.base.BaseActivity;
import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.page.main.component.DaggerMainComponent;
import io.github.winsontse.hearteyes.page.main.contract.MainContract;
import io.github.winsontse.hearteyes.page.main.module.MainModule;
import io.github.winsontse.hearteyes.page.main.presenter.MainPresenter;
import io.github.winsontse.hearteyes.page.moment.MomentListFragment;
import io.github.winsontse.hearteyes.util.constant.Extra;
import io.github.winsontse.hearteyes.util.constant.SecretConstant;
import io.github.winsontse.hearteyes.widget.BottomBar;

public class MainActivity extends BaseActivity implements MainContract.View {
    @Inject
    MainPresenter presenter;
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;
    @BindView(R.id.bottom_bar)
    BottomBar bottomBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initBottomBar();
        initPage();
        openNewPage(getIntent().getIntExtra(Extra.TYPE_NEW_PAGE, 0));

    }

    private void initBottomBar() {
        List<String> titles = new ArrayList<>();
        titles.add("动态");
        titles.add("备忘");
        titles.add("我");

        List<Integer> icons = new ArrayList<>();
        icons.add(R.drawable.ic_moment);
        icons.add(R.drawable.ic_av_timer);
        icons.add(R.drawable.ic_face);

        bottomBar.setTitlesAndIcons(titles, icons);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        openNewPage(intent.getIntExtra(Extra.TYPE_NEW_PAGE, 0));
    }

    public void initPage() {
        presenter.validateUserStatus();
        PushService.setDefaultPushCallback(this, MainActivity.class);
        PushService.subscribe(this, SecretConstant.PUSH_CHANNEL_PRIVATE, MainActivity.class);
        presenter.updateInstallationId();

    }

    private void openNewPage(int intExtra) {
        presenter.handleNewPageEvent(intExtra);
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


    public void addFragment(Fragment ragment, boolean isAddToBackStack) {
        addFragment(R.id.fragment_container, ragment, isAddToBackStack);
    }

    public void replaceFragment(Fragment fragment, boolean isAddToBackStack) {
        replaceFragment(R.id.fragment_container, fragment, isAddToBackStack);
    }


    @Override
    public void goToMomentListPage() {
        Fragment fragment = findFragmentByClass(MomentListFragment.class);
        if (fragment == null) {
            replaceFragment(MomentListFragment.newInstance(), false);
        }
        bottomBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void goToLoginPage() {
        Fragment fragment = findFragmentByClass(LoginFragment.class);
        if (fragment == null) {
            replaceFragment(LoginFragment.newInstance(), false);
        }
    }

    @Override
    public void goToAssosiationPage() {
        Fragment fragment = findFragmentByClass(AssociationFragment.class);
        if (fragment == null) {
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
