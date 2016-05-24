package io.github.winsontse.hearteyes.page.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import io.github.winsontse.hearteyes.R;

/**
 * Created by hao.xie on 16/5/10.
 */
public abstract class BaseFragment extends Fragment implements BaseView {

    private BaseActivity baseActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            baseActivity = (BaseActivity) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupComponent(((BaseActivity) getActivity()).getActivityComponent());
    }

    protected abstract void setupComponent(ActivityComponent activityComponent);

    @Override
    public void onDestroy() {
        super.onDestroy();
        BasePresenter presenter = getPresenter();
        if (presenter != null) {
            presenter.unsubscribe();
        }
    }

    protected abstract BasePresenter getPresenter();

    @Override
    public void showProgressDialog(boolean cancelable, String msg) {
        if (baseActivity != null && isVisible()) {
            baseActivity.showProgressDialog(cancelable, msg);
        }
    }

    @Override
    public void hideProgressDialog() {
        if (baseActivity != null && isVisible()) {
            baseActivity.hideProgressDialog();
        }
    }

    @Override
    public void showToast(String msg) {
        if (baseActivity != null && isVisible()) {
            baseActivity.showToast(msg);
        }
    }

    @Override
    public Drawable getDrawableById(int drawableId) {
        return baseActivity.getDrawableById(drawableId);
    }

    @Override
    public String getStringById(int stringId) {
        return baseActivity.getStringById(stringId);
    }

    @Override
    public String[] getStringArrayById(int arrayId) {
        return baseActivity.getStringArrayById(arrayId);
    }

    @Override
    public int getColorById(int colorId) {
        return baseActivity.getColorById(colorId);
    }
}
