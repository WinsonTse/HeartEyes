package io.github.winsontse.hearteyes.page.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.avos.avoscloud.PushService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.page.account.AssociationActivity;
import io.github.winsontse.hearteyes.page.account.LoginActivity;
import io.github.winsontse.hearteyes.page.base.BaseActivity;
import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.page.main.component.DaggerMainComponent;
import io.github.winsontse.hearteyes.page.main.contract.MainContract;
import io.github.winsontse.hearteyes.page.main.module.MainModule;
import io.github.winsontse.hearteyes.page.main.presenter.MainPresenter;
import io.github.winsontse.hearteyes.page.moment.MomentListFragment;
import io.github.winsontse.hearteyes.page.todo.TodoListFragment;
import io.github.winsontse.hearteyes.page.user.UserFragment;
import io.github.winsontse.hearteyes.util.constant.SecretConstant;
import io.github.winsontse.hearteyes.widget.BottomBar;

public class MainActivity extends BaseActivity implements MainContract.View,
        BottomBar.OnTabChangeListener {
    @Inject
    MainPresenter presenter;
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;
    @BindView(R.id.bottom_bar)
    BottomBar bottomBar;
    @BindView(R.id.dl)
    DrawerLayout dl;
    @BindView(R.id.bottom_container)
    LinearLayout bottomContainer;

    private FragmentManager fragmentManager;

    @Override
    protected void initView(Bundle savedInstanceState) {
        fragmentManager = getSupportFragmentManager();
        initBottomBar();
        initPage();
    }

    public void initPage() {
        presenter.validateUserStatus();
        PushService.setDefaultPushCallback(this, MainActivity.class);
        PushService.subscribe(this, SecretConstant.PUSH_CHANNEL_PRIVATE, MainActivity.class);
        presenter.updateInstallationId();

    }


    @Override
    protected boolean isSupportLayoutFullScreen() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bottomBar.setOnTabChangeListener(null);
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
    public BasePresenter getPresenter() {
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
        LoginActivity.start(this);
    }

    @Override
    public void goToAssosiationPage() {
        AssociationActivity.start(this);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AssociationActivity.RESULT_CODE) {
            if (resultCode == RESULT_OK) {
                presenter.validateUserStatus();
            } else {
                finish();
            }
        }
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
