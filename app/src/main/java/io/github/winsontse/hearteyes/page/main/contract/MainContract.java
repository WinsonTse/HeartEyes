package io.github.winsontse.hearteyes.page.main.contract;

import com.avos.avoscloud.AVUser;

import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.page.base.BaseView;

public interface MainContract {

    interface View extends BaseView {

        void goToMomentListPage();

        void goToLoginPage();

        void goToAssosiationPage();

        void initPage();
    }

    interface Presenter extends BasePresenter {

        void validateUserStatus();

        void getCircleAndFriend(AVUser currentUser);

        void updateInstallationId();
    }


}
