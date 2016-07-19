package io.github.winsontse.hearteyes.page.moment.contract;

import com.avos.avoscloud.AVObject;

import java.util.List;

import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.page.base.BaseView;
import io.github.winsontse.hearteyes.page.base.TimelinePresenter;
import io.github.winsontse.hearteyes.page.base.TimelineView;

public interface MomentListContract {

    interface View extends TimelineView<AVObject> {
        void goToEditPage();

        void goToShowLocationPage(AVObject avObject);

        void goToEditPage(int position, AVObject avObject);

        void showDeleteImageDialog(int position, AVObject avObject, int imagePosition);

        void showDatePickerDialog(int position, long originalTime, long time, final AVObject avObject);

    }

    interface Presenter extends TimelinePresenter<AVObject> {
        void updateCreateTime(int position, long originalTime, long timeInMillis, AVObject avObject);

    }

}
