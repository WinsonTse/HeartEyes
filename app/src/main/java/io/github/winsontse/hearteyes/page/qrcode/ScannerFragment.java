package io.github.winsontse.hearteyes.page.qrcode;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.page.qrcode.component.DaggerScannerComponent;
import io.github.winsontse.hearteyes.page.qrcode.contract.ScannerContract;
import io.github.winsontse.hearteyes.page.qrcode.module.ScannerModule;
import io.github.winsontse.hearteyes.page.qrcode.presenter.ScannerPresenter;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.page.base.BaseFragment;
import io.github.winsontse.hearteyes.page.base.BasePresenter;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerFragment extends BaseFragment implements ScannerContract.View, ZXingScannerView.ResultHandler {

    @Inject
    ScannerPresenter presenter;

    private ZXingScannerView scannerView;

    public static ScannerFragment newInstance() {
        Bundle args = new Bundle();
        ScannerFragment fragment = new ScannerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_scanner, container, false);
//        return rootView;
        scannerView = new ZXingScannerView(getActivity());
        return scannerView;
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
    protected BasePresenter getPresenter() {
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
        scannerView.resumeCameraPreview(ScannerFragment.this);

    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }
}