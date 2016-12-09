package io.github.winsontse.hearteyes.page.base;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Fade;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import butterknife.ButterKnife;
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
        setTransition();
        if (isSupportLayoutFullScreen()) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setNavigavitionColor();
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
            ButterKnife.bind(this);
        }
        initToolbar();
        initView(savedInstanceState);
    }

    protected void setTransition() {
        getWindow().setEnterTransition(new Fade());
        getWindow().setExitTransition(null);
    }

    private void initToolbar() {
        View v = findViewById(R.id.toolbar);
        if (isSupportNavigationBack() && v != null && v instanceof Toolbar) {
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

    protected boolean isSupportNavigationBack() {
        return true;
    }

    protected boolean isSupportLayoutFullScreen() {
        return false;
    }

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract int getLayoutId();


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

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (keyboardUtil != null) {
//            keyboardUtil.disable();
//        }
        hideKeyboard();
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
        if (progressDialog.isShowing()) {
            return;
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
        AlertDialog dialog = builder.create();
        dialog.show();
        setDialogButtonStyle(dialog.getButton(DialogInterface.BUTTON_POSITIVE));
        setDialogButtonStyle(dialog.getButton(DialogInterface.BUTTON_NEGATIVE));
        setDialogButtonStyle(dialog.getButton(DialogInterface.BUTTON_NEUTRAL));
    }

    @Override
    public void showDatePicker(int year, int month, int day, DatePickerDialog.OnDateSetListener onDateSetListener) {
        DatePickerDialog dialog = new DatePickerDialog(this, onDateSetListener, year, month, day);
        dialog.show();
        setDialogButtonStyle(dialog.getButton(DialogInterface.BUTTON_POSITIVE));
        setDialogButtonStyle(dialog.getButton(DialogInterface.BUTTON_NEGATIVE));
        setDialogButtonStyle(dialog.getButton(DialogInterface.BUTTON_NEUTRAL));
    }

    private void setDialogButtonStyle(Button bn) {
        if (bn != null) {
            bn.setTextColor(getColorById(R.color.colorAccent));
        }
    }

    @Override
    public void closePage() {
        supportFinishAfterTransition();
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
    public void toggleKeyboard(Activity activity, View contentView) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void hideKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
