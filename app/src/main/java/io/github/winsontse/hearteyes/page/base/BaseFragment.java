package io.github.winsontse.hearteyes.page.base;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.app.HeartEyesApplication;
import io.github.winsontse.hearteyes.page.main.MainActivity;

/**
 * Created by hao.xie on 16/5/10.
 */
public abstract class BaseFragment extends Fragment implements BaseView {

    private MainActivity mainActivity;
    private View rootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            mainActivity = (MainActivity) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupComponent(HeartEyesApplication.get(getActivity()).getAppComponent());
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
        if (mainActivity != null && isVisible()) {
            mainActivity.showProgressDialog(cancelable, msg);
        }
    }

    @Override
    public void hideProgressDialog() {
        if (mainActivity != null && isVisible()) {
            mainActivity.hideProgressDialog();
        }
    }

    @Override
    public void closePage() {
        hideKeyboard();
        if (mainActivity != null) {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void showToast(String msg) {
        if (mainActivity != null && isVisible()) {
            mainActivity.showToast(msg);
        }
    }

    protected void openPage(BaseFragment oldFragment, BaseFragment newFragment) {
        mainActivity.openPage(oldFragment, newFragment, true);
    }

    protected void replacePage(BaseFragment fragment) {
        mainActivity.replacePage(fragment, false);
    }

    @Override
    public void showDialog(String title, String msg, String okTitle, DialogInterface.OnClickListener onOkClickListener) {
        mainActivity.showDialog(title, msg, okTitle, onOkClickListener);
    }

    @Override
    public void showDialog(String title, String msg, String okTitle, DialogInterface.OnClickListener onOkClickListener, String cancelTitle, DialogInterface.OnClickListener onCancelClickListener) {
        mainActivity.showDialog(title, msg, okTitle, onOkClickListener, cancelTitle, onCancelClickListener);
    }

    @Override
    public Drawable getDrawableById(int drawableId) {
        return mainActivity.getDrawableById(drawableId);
    }

    @Override
    public String getStringById(int stringId) {
        return mainActivity.getStringById(stringId);
    }

    @Override
    public String[] getStringArrayById(int arrayId) {
        return mainActivity.getStringArrayById(arrayId);
    }

    @Override
    public int getColorById(int colorId) {
        return mainActivity.getColorById(colorId);
    }

    @Override
    public void showKeyboard(View view) {
        mainActivity.showKeyboard(view);
    }

    @Override
    public void hideKeyboard() {
        mainActivity.hideKeyboard();
    }
}
