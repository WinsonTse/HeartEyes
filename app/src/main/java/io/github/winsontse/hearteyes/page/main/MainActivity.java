package io.github.winsontse.hearteyes.page.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.widget.FrameLayout;

import com.avos.avoscloud.PushService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.page.account.AccountFragment;
import io.github.winsontse.hearteyes.page.account.AssociationFragment;
import io.github.winsontse.hearteyes.page.account.LoginFragment;
import io.github.winsontse.hearteyes.page.base.BaseActivity;
import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.page.main.component.DaggerMainComponent;
import io.github.winsontse.hearteyes.page.main.contract.MainContract;
import io.github.winsontse.hearteyes.page.main.module.MainModule;
import io.github.winsontse.hearteyes.page.main.presenter.MainPresenter;
import io.github.winsontse.hearteyes.page.moment.MomentListFragment;
import io.github.winsontse.hearteyes.util.AnimatorUtil;
import io.github.winsontse.hearteyes.util.UIUtil;
import io.github.winsontse.hearteyes.util.constant.Extra;
import io.github.winsontse.hearteyes.util.constant.SecretConstant;
import io.github.winsontse.hearteyes.util.rxbus.event.PushEvent;
import io.github.winsontse.hearteyes.widget.BottomBar;

public class MainActivity extends BaseActivity implements MainContract.View, FragmentManager.OnBackStackChangedListener {
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

        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    /**
     * Called whenever the contents of the back stack change.
     */
    @Override
    public void onBackStackChanged() {
        List<Fragment> backStackFragments = getSupportFragmentManager().getFragments();
        List<Fragment> fragments = new ArrayList<>();
        for (Fragment fragment : backStackFragments) {
            if (fragment != null) {
                fragments.add(fragment);
            }
        }
        if (fragments.size() == 1 && TextUtils.equals(fragments.get(0).getTag(), MomentListFragment.class.getSimpleName())) {
            showBottomBar();
        } else {
            AnimatorUtil.translationToHideBottomBar(bottomBar).start();
        }
    }

    private void showBottomBar() {
        AnimatorUtil.translationToCorrect(bottomBar).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportFragmentManager().removeOnBackStackChangedListener(this);
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

        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            showBottomBar();
        }


//        Fragment loginFragment = findFragmentByClass(LoginFragment.class);
//        if(loginFragment != null) {
//            getSupportFragmentManager().beginTransaction().remove(loginFragment).commitAllowingStateLoss();
//        }
//
//        Fragment assosiationFragment = findFragmentByClass(AssociationFragment.class);
//        if(assosiationFragment != null) {
//            getSupportFragmentManager().beginTransaction().remove(assosiationFragment).commitAllowingStateLoss();
//        }
//        bottomBar.setVisibility(View.VISIBLE);
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
