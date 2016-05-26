package io.github.winsontse.hearteyes.page.main.presenter;

import android.text.TextUtils;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.page.main.contract.MainContract;
import io.github.winsontse.hearteyes.page.base.BasePresenterImpl;

public class MainPresenter extends BasePresenterImpl implements MainContract.Presenter {
    private MainContract.View view;

    @Inject
    public MainPresenter(MainContract.View view) {
        this.view = view;
    }

    public void init() {
        AVUser currentUser = AVUser.getCurrentUser();
        if(currentUser == null || TextUtils.isEmpty(currentUser.getString("nickname"))) {
            view.goToLoginPage();
        }
        else {
            view.goToHomePage();
        }
    }
}
