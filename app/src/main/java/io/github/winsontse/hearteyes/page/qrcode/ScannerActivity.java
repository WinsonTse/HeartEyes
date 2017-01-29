package io.github.winsontse.hearteyes.page.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.zxing.Result;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.page.base.BaseActivity;
import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.page.qrcode.component.DaggerScannerComponent;
import io.github.winsontse.hearteyes.page.qrcode.contract.ScannerContract;
import io.github.winsontse.hearteyes.page.qrcode.module.ScannerModule;
import io.github.winsontse.hearteyes.page.qrcode.presenter.ScannerPresenter;
import io.github.winsontse.hearteyes.util.constant.Extra;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends BaseActivity implements ScannerContract.View, ZXingScannerView.ResultHandler {

    public static final int REQUEST_CODE = 0x1412;

    public static void start(Activity activity) {
        activity.startActivityForResult(new Intent(activity, ScannerActivity.class), REQUEST_CODE);
    }

    @Inject
    ScannerPresenter presenter;

    private ZXingScannerView scannerView;


    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerScannerComponent.builder()
                .appComponent(appComponent)
                .scannerModule(new ScannerModule(this))
                .build()
                .inject(this);
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void handleResult(Result rawResult) {
        presenter.decodeQrResult(rawResult.getText());
        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//            }
//        }, 2000);
    }

    @Override
    public void resumeCamera() {
        scannerView.resumeCameraPreview(ScannerActivity.this);

    }

    @Override
    public void closePage(String resultStr) {
        Intent intent = getIntent();
        intent.putExtra(Extra.QRCODE_RESULT, resultStr);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }
}