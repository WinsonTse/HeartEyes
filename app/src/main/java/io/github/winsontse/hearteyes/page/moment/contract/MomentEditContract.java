package io.github.winsontse.hearteyes.page.moment.contract;

import java.util.List;

import io.github.winsontse.hearteyes.data.model.ImageEntity;
import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.page.base.BaseView;

public interface MomentEditContract {

    interface View extends BaseView {
        void showFab();
    }

    interface Presenter extends BasePresenter {
        void publishMoment(String content, List<ImageEntity> images);
    }

}
