package io.github.winsontse.hearteyes.page.moment.presenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;

import java.util.List;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.data.model.leancloud.MomentContract;
import io.github.winsontse.hearteyes.page.base.BasePresenterImpl;
import io.github.winsontse.hearteyes.page.moment.contract.MomentListContract;
import rx.Observable;
import rx.Subscriber;

public class MomentListPresenter extends BasePresenterImpl implements MomentListContract.Presenter {
    private MomentListContract.View view;

    @Inject
    public MomentListPresenter(MomentListContract.View view) {
        this.view = view;
    }

    public void loadMoments() {
        addSubscription(Observable.create(new Observable.OnSubscribe<List<AVObject>>() {
            @Override
            public void call(Subscriber<? super List<AVObject>> subscriber) {
                AVQuery<AVObject> query = new AVQuery<>(MomentContract.TABLE);
                query.include(MomentContract.AUTHOR);
                query.include(MomentContract.IMAGES);
                query.orderByDescending(AVObject.CREATED_AT);
                try {
                    List<AVObject> avObjects = query.find();
                    subscriber.onNext(avObjects);
                } catch (AVException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }), new Subscriber<List<AVObject>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<AVObject> avObjects) {
                view.addItems(avObjects);
            }
        });
    }
}
