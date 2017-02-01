package io.github.winsontse.hearteyes.page.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import javax.inject.Inject;

import butterknife.BindView;
import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.page.account.component.DaggerLoginComponent;
import io.github.winsontse.hearteyes.page.account.contract.LoginContract;
import io.github.winsontse.hearteyes.page.account.module.LoginModule;
import io.github.winsontse.hearteyes.page.account.presenter.LoginPresenter;
import io.github.winsontse.hearteyes.page.base.BaseActivity;
import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.util.AnimatorUtil;
import io.github.winsontse.hearteyes.util.constant.SecretConstant;

public class LoginActivity extends BaseActivity implements LoginContract.View {

    @Inject
    LoginPresenter presenter;
    @BindView(R.id.fab_enter)
    FloatingActionButton fabEnter;
    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar progressBar;

    private SsoHandler ssoHandler;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        ssoHandler = new SsoHandler(this, new AuthInfo(this, SecretConstant.WEIBO_APP_KEY, SecretConstant.WEIBO_REDIRECT_URL, SecretConstant.WEIBO_SCOPE));
        fabEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLoadingStatus();
            }
        });
        showEnterFab();
    }


    public void setLoadingStatus() {
        progressBar.show();
        fabEnter.hide(new FloatingActionButton.OnVisibilityChangedListener() {

            @Override
            public void onHidden(FloatingActionButton fab) {
                super.onHidden(fab);
                presenter.authirize(ssoHandler);

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerLoginComponent.builder()
                .appComponent(appComponent)
                .loginModule(new LoginModule(this))
                .build()
                .inject(this);
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }


    @Override
    public void showEnterFab() {
        fabEnter.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.hide();
                fabEnter.show();
            }
        }, AnimatorUtil.ANIMATOR_TIME);

    }
}