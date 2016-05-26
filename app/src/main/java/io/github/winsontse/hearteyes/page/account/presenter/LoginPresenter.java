package io.github.winsontse.hearteyes.page.account.presenter;

import android.os.Bundle;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.data.model.weibo.WeiboUser;
import io.github.winsontse.hearteyes.data.remote.WeiboApi;
import io.github.winsontse.hearteyes.page.account.contract.LoginContract;
import io.github.winsontse.hearteyes.page.base.BasePresenterImpl;
import io.github.winsontse.hearteyes.util.AnimatorUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class LoginPresenter extends BasePresenterImpl implements LoginContract.Presenter {
    private LoginContract.View view;
    private WeiboApi weiboApi;

    @Inject
    public LoginPresenter(LoginContract.View view, WeiboApi weiboApi) {
        this.view = view;
        this.weiboApi = weiboApi;
    }


    @Override
    public void authirize(final SsoHandler ssoHandler) {
        initAuthListener(ssoHandler);
    }

    private void initAuthListener(SsoHandler ssoHandler) {
        ssoHandler.authorize(new WeiboAuthListener() {
            @Override
            public void onComplete(Bundle bundle) {
                handleWeiboToken(bundle);
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

    private void handleWeiboToken(Bundle bundle) {
        final Oauth2AccessToken token = Oauth2AccessToken.parseAccessToken(bundle);
        AVUser.AVThirdPartyUserAuth userAuth = new AVUser.AVThirdPartyUserAuth(token.getToken(), token.getExpiresTime() + "", "weibo", token.getUid());//此处snsType 可以是"qq","weibo"等字符串
        AVUser.loginWithAuthData(userAuth, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e != null) {
                    view.showToast(e.getMessage());
                } else {
                    weiboApi.getUserByUid(token.getToken(), token.getUid())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<WeiboUser>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.d("winson", "error:" + e.getMessage());
                                }

                                @Override
                                public void onNext(WeiboUser weiboUser) {
                                    Log.d("winson", weiboUser.toString());
                                }
                            });
                }
            }
        });
    }
}
