package io.github.winsontse.hearteyes.page.moment.presenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.data.model.leancloud.MomentContract;
import io.github.winsontse.hearteyes.data.model.leancloud.UserContract;
import io.github.winsontse.hearteyes.page.base.TimelinePresenterImpl;
import io.github.winsontse.hearteyes.page.moment.contract.MomentListContract;
import io.github.winsontse.hearteyes.util.HeartEyesSubscriber;
import io.github.winsontse.hearteyes.util.LogUtil;
import io.github.winsontse.hearteyes.util.rxbus.event.MomentEvent;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

public class MomentListPresenter extends TimelinePresenterImpl<AVObject> implements MomentListContract.Presenter {
    private MomentListContract.View view;

    @Inject
    public MomentListPresenter(final MomentListContract.View view) {
        super(view);
        this.view = view;

        registerEventReceiver(MomentEvent.class, new Action1<MomentEvent>() {
            @Override
            public void call(MomentEvent momentEvent) {

                switch (momentEvent.getCode()) {
                    case MomentEvent.REFRESH_MOMENT_LIST:
                        refresh();
                        break;

                    case MomentEvent.UPDATE_MOMENT_LIST_ITEM:
                        view.replaceItem(momentEvent.getPostion(), momentEvent.getAvObject());
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
                    subscriber.onError(e);
                }
            }
        });

    }

    public void deleteImage(final int position, final AVObject avObject, final int imagePosition) {
        final List list = avObject.getList(MomentContract.IMAGES);
        addSubscription(Observable.create(new Observable.OnSubscribe<AVObject>() {
            @Override
            public void call(Subscriber<? super AVObject> subscriber) {
                List<AVFile> fileList = new ArrayList<AVFile>();
                fileList.addAll(list);
                fileList.remove(imagePosition);
                try {
                    avObject.put(MomentContract.IMAGES, fileList);
                    avObject.save();
                    subscriber.onNext(avObject);
                } catch (AVException e) {
                    avObject.put(MomentContract.IMAGES, list);
                    e.printStackTrace();
                    subscriber.onError(e);
                }

            }
        }), new HeartEyesSubscriber<AVObject>() {
            @Override
            public void handleError(Throwable e) {
                view.hideProgressDialog();
            }

            @Override
            public void onNext(AVObject avObject) {
                view.hideProgressDialog();
                view.updateItem(position);
            }

            @Override
            public void onStart() {
                super.onStart();
                view.showProgressDialog(false, view.getStringById(R.string.deleting));
            }
        });
    }

    @Override
    public void updateCreateTime(final int position, final long originalTime, final long timeInMillis, final AVObject avObject) {
        addSubscription(Observable.create(new Observable.OnSubscribe<AVObject>() {
            @Override
            public void call(Subscriber<? super AVObject> subscriber) {
                avObject.put(MomentContract.CREATEAD_TIME, timeInMillis);
                try {
                    avObject.save();
                    subscriber.onNext(avObject);
                } catch (AVException e) {
                    avObject.put(MomentContract.CREATEAD_TIME, originalTime);
                    subscriber.onError(e);
                }
            }
        }), new HeartEyesSubscriber<AVObject>(view) {
            @Override
            public void handleError(Throwable e) {
                view.showDatePickerDialog(position, originalTime, timeInMillis, avObject);
            }

            @Override
            public void onNext(AVObject avObject) {
                refresh();
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
