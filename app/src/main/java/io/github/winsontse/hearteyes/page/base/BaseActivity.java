package io.github.winsontse.hearteyes.page.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.app.HeartEyesApplication;

/**
 * Created by hao.xie on 16/5/9.
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseView {
    private ProgressDialog progressDialog;
    private AppComponent appComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupComponent(getAppComponent());
        setNavigavitionColor();
    }

    private AppComponent getAppComponent() {
        if (appComponent == null) {
            appComponent = HeartEyesApplication.get(this).getAppComponent();
        }
        return appComponent;
    }

    private Resources getResourcesInstance() {
        return getAppComponent().getResources();
    }

    private void setNavigavitionColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResourcesInstance().getColor(R.color.md_cyan_700));
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
    public void showDialog(String title, String msg, String okTitle, DialogInterface.OnClickListener onOkClickListener) {
        showDialog(title, msg, okTitle, onOkClickListener, null, null);
    }

    @Override
    public void showDialog(String title, String msg, String okTitle, DialogInterface.OnClickListener onOkClickListener, String cancelTitle, DialogInterface.OnClickListener onCancelClickListener) {
        if (isFinishing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(msg)) {
            builder.setMessage(msg);
        }

        if (!TextUtils.isEmpty(okTitle)) {
            builder.setPositiveButton(okTitle, onOkClickListener);
        }

        if (!TextUtils.isEmpty(cancelTitle)) {
            builder.setNegativeButton(cancelTitle, onCancelClickListener);
        }
        builder.create().show();
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
        return getResourcesInstance().getString(strId);
    }

    @Override
    public String[] getStringArrayById(int arrayId) {
        return getResourcesInstance().getStringArray(arrayId);
    }

    @Override
    public Drawable getDrawableById(int drawableId) {
        return getResourcesInstance().getDrawable(drawableId);
    }

    @Override
    public int getColorById(int colorId) {
        return getResourcesInstance().getColor(colorId);
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
