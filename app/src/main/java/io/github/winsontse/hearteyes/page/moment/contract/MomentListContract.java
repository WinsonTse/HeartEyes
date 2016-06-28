package io.github.winsontse.hearteyes.page.moment.contract;

import com.avos.avoscloud.AVObject;

import java.util.List;

import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.page.base.BaseView;

public interface MomentListContract {

    interface View extends BaseView {

        void addItems(List<AVObject> avObjects);
    }

    interface Presenter extends BasePresenter {

    }

}
