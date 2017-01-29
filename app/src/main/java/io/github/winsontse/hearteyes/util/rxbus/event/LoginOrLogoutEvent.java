package io.github.winsontse.hearteyes.util.rxbus.event;

/**
 * Created by winson on 2016/12/9.
 */

public class LoginOrLogoutEvent {
    private boolean isLogin;

    public LoginOrLogoutEvent(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
