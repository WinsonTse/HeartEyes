package io.github.winsontse.hearteyes.page.moment.contract;

import com.avos.avoscloud.AVObject;

import java.util.List;

import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.page.base.BaseView;
import io.github.winsontse.hearteyes.page.base.TimelinePresenter;
import io.github.winsontse.hearteyes.page.base.TimelineView;

public interface MomentListContract {

    interface View extends TimelineView<AVObject> {

    }

    interface Presenter extends TimelinePresenter<AVObject> {

    }

}
