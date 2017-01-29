package io.github.winsontse.hearteyes.page.qrcode.presenter;

import android.text.TextUtils;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.page.base.BasePresenterImpl;
import io.github.winsontse.hearteyes.page.qrcode.contract.ScannerContract;
import io.github.winsontse.hearteyes.util.HeartEyesException;
import io.github.winsontse.hearteyes.util.HeartEyesSubscriber;
import rx.Observable;
import rx.Subscriber;

public class ScannerPresenter extends BasePresenterImpl implements ScannerContract.Presenter {
    private ScannerContract.View view;

    @Inject
    public ScannerPresenter(ScannerContract.View view) {
        this.view = view;
    }

    @Override
    public void decodeQrResult(final String content) {
        rxLifeAndSchedule(Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (TextUtils.isEmpty(content)) {
                    subscriber.onError(new HeartEyesException(view.getStringById(R.string.error_qrcode)));
                } else {
                    subscriber.onNext(content);
                    subscriber.onCompleted();
                }
            }
        })).subscribe(new HeartEyesSubscriber<String>(view) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void handleError(Throwable e) {
                view.resumeCamera();
            }

            @Override
            public void onNext(String resultStr) {
                view.closePage(resultStr);
            }
        });
    }
}
