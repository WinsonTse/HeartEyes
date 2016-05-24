package io.github.winsontse.hearteyes.page.account.presenter;

import android.os.Bundle;

import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.page.account.contract.LoginContract;
import io.github.winsontse.hearteyes.page.base.BasePresenterImpl;
import io.github.winsontse.hearteyes.util.AnimatorUtil;

public class LoginPresenter extends BasePresenterImpl implements LoginContract.Presenter {
    private LoginContract.View view;

    @Inject
    public LoginPresenter(LoginContract.View view) {
        this.view = view;
    }


    @Override
    public void authirize(final SsoHandler ssoHandler) {
        initAuthListener(ssoHandler);
    }

    private void initAuthListener(SsoHandler ssoHandler) {
        ssoHandler.authorize(new WeiboAuthListener() {
            @Override
            public void onComplete(Bundle bundle) {
                view.showToast("登录成功");
            }

            @Override
            public void onWeiboException(WeiboException e) {
                view.showToast(e.getMessage());
                view.showEnterFab();
            }

            @Override
            public void onCancel() {
                view.showEnterFab();
            }
        });
    }
}
