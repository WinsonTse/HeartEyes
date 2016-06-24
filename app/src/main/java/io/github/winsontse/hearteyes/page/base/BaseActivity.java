package io.github.winsontse.hearteyes.page.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
        setNavigavitionColor();
    }

    private void setNavigavitionColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(resources.getColor(R.color.material_cyan_700));
        }
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

    public Fragment findFragmentByClass(Class cls) {
        return getSupportFragmentManager().findFragmentByTag(cls.getSimpleName());
    }

    protected void addFragment(int containerId, Fragment fragment, boolean isAddToBackStack) {
        String tag = fragment.getClass().getSimpleName();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .add(containerId, fragment, tag);
        if (isAddToBackStack) {
            transaction.addToBackStack(tag);

        }
        transaction.commitAllowingStateLoss();
    }

    protected void replaceFragment(int containerId, Fragment fragment, boolean isAddToBackStack) {
        String tag = fragment.getClass().getSimpleName();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(containerId, fragment, tag);

        if (isAddToBackStack) {
            transaction.addToBackStack(tag);

        }
        transaction.commitNow();
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
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if (!isFinishing() && progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

    @Override
    public void closePage() {
        finish();
    }

    @Override
    public void showToast(String msg) {
        if (!isFinishing() && !TextUtils.isEmpty(msg)) {
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

    @Override
    public void showKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, 0);
    }

    @Override
    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }
}
