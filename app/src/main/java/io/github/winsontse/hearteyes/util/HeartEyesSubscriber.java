package io.github.winsontse.hearteyes.util;

import android.util.Log;

import com.avos.avoscloud.AVException;

import io.github.winsontse.hearteyes.page.base.BaseView;
import rx.Subscriber;

/**
 * Created by winson on 16/5/28.
 */
public abstract class HeartEyesSubscriber<T> extends Subscriber<T> {

    private BaseView view;

    public HeartEyesSubscriber(BaseView view) {
        this.view = view;
    }

    public HeartEyesSubscriber() {
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public final void onError(Throwable e) {
        LogUtil.e("RxJava错误信息:" + e);
        if (e instanceof AVException) {
            AVException avException = (AVException) e;
            if (view != null && isShowToast()) {
                view.showToast("code:" + (avException.getCode() + " msg:" + avException.getMessage()));
            }
        } else {
            if (view != null && isShowToast() && e != null) {
                view.showToast(e.getMessage());
            }
        }
        handleError(e);
    }

    protected boolean isShowToast() {
        return true;
    }

    public abstract void handleError(Throwable e);

}
