package io.github.winsontse.hearteyes.page.main.contract;

import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.page.base.BaseView;

public interface MainContract {

    interface View extends BaseView {
        void goToHomePage();
        void goToLoginPage();
    }

    interface Presenter extends BasePresenter {
        void init();
    }

}