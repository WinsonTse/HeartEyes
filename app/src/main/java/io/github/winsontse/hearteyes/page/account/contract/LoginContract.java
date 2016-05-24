package io.github.winsontse.hearteyes.page.account.contract;

import com.sina.weibo.sdk.auth.sso.SsoHandler;

import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.page.base.BaseView;
import io.github.winsontse.hearteyes.util.AnimatorUtil;

public interface LoginContract {

    interface View extends BaseView {
        void setLoadingState();
        void showEnterFab();
    }

    interface Presenter extends BasePresenter {

        void authirize(SsoHandler ssoHandler);

    }

}
