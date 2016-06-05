package io.github.winsontse.hearteyes.page.main.contract;

import android.content.Intent;

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

        void handleNewPageEvent(int openType);

        void handleActivityResult(int requestCode, int resultCode, Intent data);

        void getCircleAndFriend(AVUser currentUser);

        void updateInstallationId();
    }



}
