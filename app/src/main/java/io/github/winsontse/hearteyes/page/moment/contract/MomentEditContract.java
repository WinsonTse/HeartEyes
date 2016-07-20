package io.github.winsontse.hearteyes.page.moment.contract;

import com.amap.api.location.AMapLocationClient;
import com.avos.avoscloud.AVObject;

import java.util.List;

import io.github.winsontse.hearteyes.data.model.ImageEntity;
import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.page.base.BaseView;

public interface MomentEditContract {

    interface View extends BaseView {
        void showFab();

        void updateEditContent(String content);
    }

    interface Presenter extends BasePresenter {

        void init(AVObject currentMoment, int itemPosition);

        void publishMoment(String content, List<ImageEntity> images);

        void initLocationClient(AMapLocationClient mLocationClient);

        void saveContent(String content);
    }

}
