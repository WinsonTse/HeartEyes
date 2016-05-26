package io.github.winsontse.hearteyes.page.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.page.account.component.DaggerLoginComponent;
import io.github.winsontse.hearteyes.page.account.contract.LoginContract;
import io.github.winsontse.hearteyes.page.account.module.LoginModule;
import io.github.winsontse.hearteyes.page.account.presenter.LoginPresenter;
import io.github.winsontse.hearteyes.page.base.ActivityComponent;
import io.github.winsontse.hearteyes.page.base.BaseFragment;
import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.util.AnimatorUtil;
import io.github.winsontse.hearteyes.util.constant.SecretConstant;

public class LoginFragment extends BaseFragment implements LoginContract.View {

    @Inject
    LoginPresenter presenter;
    @BindView(R.id.fab_enter)
    FloatingActionButton fabEnter;
    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar progressBar;

    private SsoHandler ssoHandler;

    public static LoginFragment newInstance() {
        
        Bundle args = new Bundle();
        
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, rootView);
        ssoHandler = new SsoHandler(getActivity(), new AuthInfo(getActivity(), SecretConstant.WEIBO_APP_KEY, SecretConstant.WEIBO_REDIRECT_URL, SecretConstant.WEIBO_SCOPE));
        fabEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLoadingState();
            }
        });
        return rootView;
    }

    @Override
    protected void setupComponent(ActivityComponent activityComponent) {
        DaggerLoginComponent.builder()
                .activityComponent(activityComponent)
                .loginModule(new LoginModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected BasePresenter getPresenter() {
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
    public void setLoadingState() {
        progressBar.show();
        AnimatorUtil.hideFab(fabEnter, new AnimatorUtil.AnimatorCallback() {
            @Override
            public void onAnimatorEnd() {
                presenter.authirize(ssoHandler);
            }
        });
    }

    @Override
    public void showEnterFab() {
        fabEnter.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.hide();
                AnimatorUtil.showFab(fabEnter, null);
            }
        }, 1000);

    }
}