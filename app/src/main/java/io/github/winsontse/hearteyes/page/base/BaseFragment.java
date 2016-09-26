package io.github.winsontse.hearteyes.page.base;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.app.HeartEyesApplication;

/**
 * Created by hao.xie on 16/5/10.
 */
public abstract class BaseFragment extends DialogFragment implements BaseView {

    private BaseActivity baseActivity;
    private View rootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            baseActivity = (BaseActivity) context;
            setStatusBarViewVisible(isStatusBarViewVisible());
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupComponent(HeartEyesApplication.get(getActivity()).getAppComponent());
        setStatusBarViewVisible(isStatusBarViewVisible());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setStatusBarViewVisible(isStatusBarViewVisible());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, rootView);
        initToolbar();
        initView(container, savedInstanceState);

        return rootView;
    }

    private void initToolbar() {
        if (rootView == null) {
            return;
        }
        View v = rootView.findViewById(R.id.toolbar);
        if (isSupportBackNavigation() && v != null && v instanceof Toolbar) {
            Toolbar toolbar = (Toolbar) v;
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closePage();
                }
            });
        }
    }

    protected boolean isSupportBackNavigation() {
        return true;
    }

    protected boolean isStatusBarViewVisible() {
        return true;
    }

    protected abstract void initView(ViewGroup container, Bundle savedInstanceState);

    protected abstract int getLayoutId();

    protected abstract void setupComponent(AppComponent appComponent);

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
    public void closePage() {
        hideKeyboard();
        if (baseActivity != null) {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void showToast(String msg) {
        if (baseActivity != null && isVisible()) {
            baseActivity.showToast(msg);
        }
    }

    @Override
    public void showDialog(String title, String msg, String okTitle, DialogInterface.OnClickListener onOkClickListener) {
        baseActivity.showDialog(title, msg, okTitle, onOkClickListener);
    }

    @Override
    public void showDialog(String title, String msg, String okTitle, DialogInterface.OnClickListener onOkClickListener, String cancelTitle, DialogInterface.OnClickListener onCancelClickListener) {
        baseActivity.showDialog(title, msg, okTitle, onOkClickListener, cancelTitle, onCancelClickListener);
    }

    @Override
    public void showDatePicker(int year, int month, int day, DatePickerDialog.OnDateSetListener onDateSetListener) {
        baseActivity.showDatePicker(year, month, day, onDateSetListener);
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

    @Override
    public void toggleKeyboard(Activity activity, View contentView) {
        baseActivity.toggleKeyboard(activity, contentView);
    }

    @Override
    public void hideKeyboard() {
        baseActivity.hideKeyboard();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        baseActivity.hideKeyboard();
    }

    @Override
    public void setStatusBarViewVisible(boolean isVisible) {
        baseActivity.setStatusBarViewVisible(isVisible);
    }
}
