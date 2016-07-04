package io.github.winsontse.hearteyes.page.moment.presenter;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;

import java.util.List;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.data.model.leancloud.MomentContract;
import io.github.winsontse.hearteyes.data.model.leancloud.UserContract;
import io.github.winsontse.hearteyes.page.base.TimelinePresenterImpl;
import io.github.winsontse.hearteyes.page.moment.contract.MomentListContract;
import io.github.winsontse.hearteyes.util.LogUtil;
import io.github.winsontse.hearteyes.util.rxbus.event.MomentEvent;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

public class MomentListPresenter extends TimelinePresenterImpl<AVObject> implements MomentListContract.Presenter {
    private MomentListContract.View view;

    @Inject
    public MomentListPresenter(MomentListContract.View view) {
        super(view);
        this.view = view;

        registerEventReceiver(MomentEvent.class, new Action1<MomentEvent>() {
            @Override
            public void call(MomentEvent momentEvent) {
                LogUtil.e("老子还是收到消息了");

                switch (momentEvent.getCode()) {
                    case MomentEvent.REFRESH_MOMENT_LIST:
                        refresh();
                        break;
                }
            }
        });
    }

    @Override
    public Observable<List<AVObject>> requestRemoteData(final int skipCount, final int limitCount) {
        return Observable.create(new Observable.OnSubscribe<List<AVObject>>() {
            @Override
            public void call(Subscriber<? super List<AVObject>> subscriber) {
                AVQuery<AVObject> query = new AVQuery<>(MomentContract.KEY);
                query.include(MomentContract.AUTHOR);
                query.include(MomentContract.IMAGES);
                query.whereEqualTo(MomentContract.CIRCLE_ID, getCurrentUser().getString(UserContract.CIRCLE_ID));
                query.skip(skipCount);
                query.limit(limitCount);
                query.orderByDescending(MomentContract.CREATEAD_TIME);
                try {
                    List<AVObject> avObjects = query.find();
                    subscriber.onNext(avObjects);
                } catch (Exception e) {
                    LogUtil.e("moment list error: " + e.getMessage());
                    subscriber.onError(e);
                }
            }
        });

    }


//    @Override
//    public Observable<List<AVObject>> requestRemoteData(int start, int end) {
//        return Observable.create(new Observable.OnSubscribe<List<AVObject>>() {
//            @Override
//            public void call(Subscriber<? super List<AVObject>> subscriber) {
//                AVQuery<AVObject> query = new AVQuery<>(MomentContract.KEY);
//                query.include(MomentContract.AUTHOR);
//                query.include(MomentContract.IMAGES);
//                query.orderByDescending(AVObject.CREATED_AT);
//                try {
//                    List<AVObject> avObjects = query.find();
//                    subscriber.onNext(avObjects);
//                } catch (AVException e) {
//                    e.printStackTrace();
//                    subscriber.onError(e);
//                }
//            }
//        });
//    }

}
