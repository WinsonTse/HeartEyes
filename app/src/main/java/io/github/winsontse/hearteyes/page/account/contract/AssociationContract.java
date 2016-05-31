package io.github.winsontse.hearteyes.page.account.contract;

import android.graphics.Bitmap;

import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.page.base.BaseView;

public interface AssociationContract {

    interface View extends BaseView {
        void showQrcode(Bitmap bitmap);

        void replacePage();
    }

    interface Presenter extends BasePresenter {

        void generateQrcode(int width, int height, final int bgColor, final int fgColor);

        void associate(String uid);
    }

}
