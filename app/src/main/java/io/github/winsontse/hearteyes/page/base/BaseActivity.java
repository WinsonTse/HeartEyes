package io.github.winsontse.hearteyes.page.base;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import javax.annotation.Resource;

import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.app.HeartEyesApplication;

/**
 * Created by hao.xie on 16/5/9.
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseView {
    private ProgressDialog progressDialog;
    private Resources resources;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resources = getResources();
        setupComponent(HeartEyesApplication.get(this).getAppComponent());
    }

    protected abstract void setupComponent(AppComponent appComponent);

    protected abstract BasePresenter getPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BasePresenter presenter = getPresenter();
        if (presenter != null) {
            presenter.unsubscribe();
        }
    }

    public boolean findFragmentByClass(Class cls) {
        return getSupportFragmentManager().findFragmentByTag(cls.getSimpleName()) == null;
    }

    protected void addFragment(int containerId, BaseFragment fragment, boolean isAddToBackStack) {
        String tag = fragment.getClass().getSimpleName();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .add(containerId, fragment, tag);
        if (isAddToBackStack) {
            transaction.addToBackStack(tag);

        }
        transaction.commitAllowingStateLoss();
    }

    protected void replaceFragment(int containerId, BaseFragment fragment, boolean isAddToBackStack) {
        String tag = fragment.getClass().getSimpleName();
        Log.d("winson", "tag:" + tag);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(containerId, fragment, tag);

        if (isAddToBackStack) {
            transaction.addToBackStack(tag);

        }
        transaction.commitAllowingStateLoss();
    }


    @Override
    public void showProgressDialog(boolean cancelable, String msg) {
        if (isFinishing()) {
            return;
        }
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        if (TextUtils.isEmpty(msg)) {
            progressDialog.setMessage("");
        } else {
            progressDialog.setMessage(msg);
        }
        progressDialog.setCancelable(cancelable);
        progressDialog.setCanceledOnTouchOutside(cancelable);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    @Override
    public void hideProgressDialog() {
        if (!isFinishing() && progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

    @Override
    public void showToast(String msg) {
        if (!isFinishing()) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public String getStringById(int strId) {
        return resources.getString(strId);
    }

    @Override
    public String[] getStringArrayById(int arrayId) {
        return resources.getStringArray(arrayId);
    }

    @Override
    public Drawable getDrawableById(int drawableId) {
        return resources.getDrawable(drawableId);
    }

    @Override
    public int getColorById(int colorId) {
        return resources.getColor(colorId);
    }
}
