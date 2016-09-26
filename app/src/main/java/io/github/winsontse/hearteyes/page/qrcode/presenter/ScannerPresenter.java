package io.github.winsontse.hearteyes.page.qrcode.presenter;

import android.text.TextUtils;
import android.util.Base64;

import com.avos.avoscloud.AVUser;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.page.base.BasePresenterImpl;
import io.github.winsontse.hearteyes.page.qrcode.contract.ScannerContract;
import io.github.winsontse.hearteyes.util.HeartEyesException;
import io.github.winsontse.hearteyes.util.HeartEyesSubscriber;
import io.github.winsontse.hearteyes.util.RxUtil;
import io.github.winsontse.hearteyes.util.constant.SecretConstant;
import io.github.winsontse.hearteyes.util.rxbus.RxBus;
import io.github.winsontse.hearteyes.util.rxbus.event.UidEvent;
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
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (TextUtils.isEmpty(content)) {
                    subscriber.onError(new HeartEyesException(view.getStringById(R.string.error_qrcode)));
                } else {
                    String str = new String(Base64.decode(content, Base64.DEFAULT));
                    if (str.contains(SecretConstant.QRCODE_CONTENT_SPLIT)) {
                        String[] splitArray = str.split(SecretConstant.QRCODE_CONTENT_SPLIT);
                        if (splitArray.length == 2 && !TextUtils.isEmpty(splitArray[1])) {
                            String objectId = splitArray[1];
                            if (TextUtils.equals(objectId, AVUser.getCurrentUser().getObjectId())) {
                                subscriber.onError(new HeartEyesException(view.getStringById(R.string.error_cannot_associate_self)));
                            } else {
                                subscriber.onNext(objectId);
                            }
                        } else {
                            subscriber.onError(new HeartEyesException(view.getStringById(R.string.error_qrcode)));
                        }
                    } else {
                        subscriber.onError(new HeartEyesException(view.getStringById(R.string.error_qrcode)));
                    }
                }
            }
        }).compose(RxUtil.rxSchedulerHelper(String.class))
                .subscribe(new HeartEyesSubscriber<String>(view) {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void handleError(Throwable e) {
                        view.resumeCamera();
                    }

                    @Override
                    public void onNext(String str) {
                        RxBus.getInstance().post(new UidEvent(str));
                        view.closePage();
                    }
                });
    }
}
