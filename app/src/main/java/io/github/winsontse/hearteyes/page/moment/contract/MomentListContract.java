package io.github.winsontse.hearteyes.page.moment.contract;

import com.avos.avoscloud.AVObject;

import io.github.winsontse.hearteyes.page.base.timeline.TimelinePresenter;
import io.github.winsontse.hearteyes.page.base.timeline.TimelineView;

public interface MomentListContract {

    interface View extends TimelineView<AVObject> {
        void goToEditPage();

        void goToShowLocationPage(AVObject avObject);

        void goToEditPage(int position, AVObject avObject);

        void showDeleteImageDialog(int position, AVObject avObject, int imagePosition);

        void showMomentDatePickerDialog(long originalTime, long time, AVObject avObject);

        void showLoveDayPickerDialog(long originalTime, long timeInMillis, AVObject avCircle);

        void updateHeaderView(AVObject avCircle);

        void showUpdateCoverRetryDialog(AVObject avCircle, String path);
    }

    interface Presenter extends TimelinePresenter<AVObject> {
        void updateMomentCreateTime(long originalTime, long timeInMillis, AVObject avObject);

        void updateLoveDay(long originalTime, long timeInMillis, AVObject avCircle);

        void loadCircle();

        void updateCircleCover(AVObject avCircle, String path);
    }

}
