package io.github.winsontse.hearteyes.page.moment.presenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;

import java.util.List;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.data.model.leancloud.MomentContract;
import io.github.winsontse.hearteyes.data.model.leancloud.UserContract;
import io.github.winsontse.hearteyes.page.base.TimelinePresenterImpl;
import io.github.winsontse.hearteyes.page.moment.contract.MomentListContract;
import io.github.winsontse.hearteyes.util.LogUtil;
import rx.Observable;
import rx.Subscriber;

public class MomentListPresenter extends TimelinePresenterImpl<AVObject> implements MomentListContract.Presenter {
    private MomentListContract.View view;

    @Inject
    public MomentListPresenter(MomentListContract.View view) {
        super(view);
        this.view = view;
    }

    @Override
    public Observable<List<AVObject>> requestRemoteData(final int skipCount, final int limitCount) {
        return Observable.create(new Observable.OnSubscribe<List<AVObject>>() {
            @Override
            public void call(Subscriber<? super List<AVObject>> subscriber) {
                AVQuery<AVObject> query = new AVQuery<>(MomentContract.TABLE);
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
//                AVQuery<AVObject> query = new AVQuery<>(MomentContract.TABLE);
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
