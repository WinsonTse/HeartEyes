package io.github.winsontse.hearteyes.page.qrcode.contract;

import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.page.base.BaseView;

public interface ScannerContract {

    interface View extends BaseView {
        void resumeCamera();
    }

    interface Presenter extends BasePresenter {
        void decodeQrResult(String content);
    }

}
