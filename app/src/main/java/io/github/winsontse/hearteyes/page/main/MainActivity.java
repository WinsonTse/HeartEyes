package io.github.winsontse.hearteyes.page.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.avos.avoscloud.PushService;
import com.bumptech.glide.manager.SupportRequestManagerFragment;

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
import io.github.winsontse.hearteyes.page.todo.TodoListFragment;
import io.github.winsontse.hearteyes.page.user.UserFragment;
import io.github.winsontse.hearteyes.util.AnimatorUtil;
import io.github.winsontse.hearteyes.util.ScreenUtil;
import io.github.winsontse.hearteyes.util.constant.Extra;
import io.github.winsontse.hearteyes.util.constant.SecretConstant;
import io.github.winsontse.hearteyes.widget.BottomBar;

public class MainActivity extends BaseActivity implements MainContract.View,
        FragmentManager.OnBackStackChangedListener,
        BottomBar.OnTabChangeListener {
    @Inject
    MainPresenter presenter;
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;
    @BindView(R.id.bottom_bar)
    BottomBar bottomBar;
    @BindView(R.id.main_content_container)
    LinearLayout mainContentContainer;
    @BindView(R.id.dl)
    DrawerLayout dl;
    @BindView(R.id.v_status)
    View vStatus;
    @BindView(R.id.bottom_container)
    LinearLayout bottomContainer;

    private boolean isShowStatusView = true;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        ButterKnife.bind(this);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        initStatusBar();
        initBottomBar();
        initPage();
        openNewPage(getIntent().getIntExtra(Extra.TYPE_NEW_PAGE, 0));

        fragmentManager.addOnBackStackChangedListener(this);

        //重建时会先恢复fragment再恢复activity
        setStatusBarViewVisible(isShowStatusView);
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            vStatus.getLayoutParams().height = ScreenUtil.statusBarHeight;
            vStatus.requestLayout();
        }
    }

    @Override
    public void setStatusBarViewVisible(boolean isVisible) {
        isShowStatusView = isVisible;
        if (vStatus != null) {
            vStatus.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Called whenever the contents of the back stack change.
     */
    @Override
    public void onBackStackChanged() {
        List<Fragment> backStackFragments = fragmentManager.getFragments();
        List<Fragment> fragments = new ArrayList<>();
        for (Fragment fragment : backStackFragments) {
            if (fragment != null && !(fragment instanceof SupportRequestManagerFragment)) {
                fragments.add(fragment);
            }
        }
        if (fragmentManager.getBackStackEntryCount() == 0 && bottomBar.isTabTag(fragments.get(0).getTag())) {
            showBottomBar();
        } else {
            AnimatorUtil.translationToHideBottomBar(bottomContainer).start();
        }
    }

    private void showBottomBar() {
        AnimatorUtil.translationToCorrect(bottomContainer).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bottomBar.setOnTabChangeListener(null);
        fragmentManager.removeOnBackStackChangedListener(this);
    }

    private void initBottomBar() {

        List<BottomBar.Item> items = new ArrayList<>();
        items.add(new BottomBar.Item(R.string.title_moment, R.drawable.ic_moment, MomentListFragment.class.getName()));
        items.add(new BottomBar.Item(R.string.title_todo, R.drawable.ic_av_timer, TodoListFragment.class.getName()));
        items.add(new BottomBar.Item(R.string.title_me, R.drawable.ic_face, UserFragment.class.getName()));

        bottomBar.setItems(items);
        bottomBar.setOnTabChangeListener(this);
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


    public void openPage(Fragment oldFragment, Fragment newFragment, boolean isAddToBackStack) {
        String tag = newFragment.getClass().getName();
        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .hide(oldFragment)
                .add(R.id.fragment_container, newFragment, tag);
        if (isAddToBackStack) {
            transaction.addToBackStack(null);

        }
        transaction.commitAllowingStateLoss();
    }

    public void replacePage(Fragment fragment, boolean isAddToBackStack) {
        String tag = fragment.getClass().getName();
        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, tag);

        if (isAddToBackStack) {
            transaction.addToBackStack(tag);

        }
        transaction.commitAllowingStateLoss();
    }

    public Fragment findFragmentByClass(Class cls) {
        return fragmentManager.findFragmentByTag(cls.getName());
    }


    @Override
    public void goToMomentListPage() {
        Fragment fragment = findFragmentByClass(MomentListFragment.class);
        if (fragment == null) {
            replacePage(MomentListFragment.newInstance(), false);
        }

        if (fragmentManager.getBackStackEntryCount() == 0) {
            showBottomBar();
        }


//        Fragment loginFragment = findFragmentByClass(LoginFragment.class);
//        if(loginFragment != null) {
//            fragmentManager.beginTransaction().remove(loginFragment).commitAllowingStateLoss();
//        }
//
//        Fragment assosiationFragment = findFragmentByClass(AssociationFragment.class);
//        if(assosiationFragment != null) {
//            fragmentManager.beginTransaction().remove(assosiationFragment).commitAllowingStateLoss();
//        }
//        bottomBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void goToLoginPage() {
        Fragment fragment = findFragmentByClass(LoginFragment.class);
        if (fragment == null) {
            replacePage(LoginFragment.newInstance(), false);
        }
    }

    @Override
    public void goToAssosiationPage() {
        Fragment fragment = findFragmentByClass(AssociationFragment.class);
        if (fragment == null) {
            replacePage(AssociationFragment.newInstance(), false);
        }
    }

    @Override
    public void finish() {
        super.finish();
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

    @Override
    public void onTabChanged(BottomBar.Item item) {
        Fragment newFragment;
        boolean isExistNewFragment = true;
        String currentTag = item.getFragmentTag();
        newFragment = fragmentManager.findFragmentByTag(currentTag);
        if (newFragment == null) {
            newFragment = newTabFragmentInstance(currentTag);
            isExistNewFragment = false;
        }

        if (newFragment == null) {
            return;
        }

        List<Fragment> fragments = fragmentManager.getFragments();
        List<Fragment> oldFragments = new ArrayList<>();
        for (Fragment fragment : fragments) {
            if (fragment != null && !TextUtils.equals(currentTag, fragment.getTag())
                    && bottomBar.isTabTag(fragment.getTag())) {
                oldFragments.add(fragment);
            }
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (Fragment oldFragment : oldFragments) {
            transaction.hide(oldFragment);
        }
        if (isExistNewFragment) {
            transaction.show(newFragment);
        } else {
            transaction.add(R.id.fragment_container, newFragment, currentTag);
        }
        transaction.commitAllowingStateLoss();
    }

    private Fragment newTabFragmentInstance(String tag) {
        if (TextUtils.equals(tag, MomentListFragment.class.getName())) {
            return MomentListFragment.newInstance();
        } else if (TextUtils.equals(tag, TodoListFragment.class.getName())) {
            return TodoListFragment.newInstance();
        } else if (TextUtils.equals(tag, UserFragment.class.getName())) {
            return UserFragment.newInstance();
        } else {
            return null;
        }
    }
}
