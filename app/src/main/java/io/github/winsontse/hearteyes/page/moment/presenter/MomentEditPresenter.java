package io.github.winsontse.hearteyes.page.moment.presenter;

import android.text.TextUtils;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.data.model.ImageEntity;
import io.github.winsontse.hearteyes.data.model.leancloud.MomentContract;
import io.github.winsontse.hearteyes.data.model.leancloud.UserContract;
import io.github.winsontse.hearteyes.page.base.BasePresenterImpl;
import io.github.winsontse.hearteyes.page.moment.contract.MomentEditContract;
import io.github.winsontse.hearteyes.util.HeartEyesSubscriber;
import io.github.winsontse.hearteyes.util.ImageUtil;
import io.github.winsontse.hearteyes.util.LogUtil;
import io.github.winsontse.hearteyes.util.rxbus.RxBus;
import io.github.winsontse.hearteyes.util.rxbus.event.MomentEvent;
import rx.Observable;
import rx.Subscriber;

public class MomentEditPresenter extends BasePresenterImpl implements MomentEditContract.Presenter {
    private MomentEditContract.View view;
    private AVObject currentMoment;
    private boolean isCreateMoment = true;
    private int itemPosition;

    @Inject
    public MomentEditPresenter(MomentEditContract.View view) {
        this.view = view;
    }

    public void init(AVObject currentMoment, int itemPosition) {
        if (currentMoment != null) {
            isCreateMoment = false;
            this.currentMoment = currentMoment;
            this.itemPosition = itemPosition;
            view.updateEditContent(currentMoment.getString(MomentContract.CONTENT));
        }
    }

    @Override
    public void publishMoment(final String content, final List<ImageEntity> images) {
        if (TextUtils.isEmpty(content)) {
            view.showToast(view.getStringById(R.string.tips_moment_content_not_empty));
            return;
        }

        addSubscription(Observable.create(new Observable.OnSubscribe<AVObject>() {
            @Override
            public void call(Subscriber<? super AVObject> subscriber) {

                try {
                    List<AVFile> avFiles = new ArrayList<>();
                    for (ImageEntity entity : images) {
                        AVFile avFile = AVFile.withFile(System.currentTimeMillis() + "_" + entity.getTitle() + ImageUtil.getNameSuffix(entity.getMineType()), new File(entity.getData()));
                        avFile.save();
                        avFiles.add(avFile);
                    }

                    //新建
                    if (currentMoment == null) {
                        currentMoment = AVObject.create(MomentContract.KEY);
                        AVUser currentUser = getCurrentUser();
                        currentMoment.put(MomentContract.AUTHOR, currentUser);
                        currentMoment.put(MomentContract.CIRCLE_ID, currentUser.getString(UserContract.CIRCLE_ID));
                        currentMoment.put(MomentContract.CONTENT, content);
                        if (avFiles.size() > 0) {
                            currentMoment.put(MomentContract.IMAGES, avFiles);
                        }
                        currentMoment.put(MomentContract.CREATEAD_TIME, System.currentTimeMillis());
                    }
                    //更新
                    else {
                        currentMoment.put(MomentContract.CONTENT, content);
                        if (avFiles.size() > 0) {
                            List list = currentMoment.getList(MomentContract.IMAGES);
                            List<AVFile> updateFiles = new ArrayList<AVFile>();
                            if (list != null && list.size() > 0) {
                                updateFiles.addAll(list);
                            }
                            updateFiles.addAll(avFiles);
                            currentMoment.put(MomentContract.IMAGES, updateFiles);
                        }
                    }
                    currentMoment.setFetchWhenSave(isCreateMoment);
                    currentMoment.save();
                    subscriber.onNext(currentMoment);
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }), new HeartEyesSubscriber<AVObject>(view) {

            @Override
            public void onStart() {
                super.onStart();
                if (isCreateMoment) {
                    view.showProgressDialog(false, view.getStringById(R.string.publishing_moment));
                } else {
                    view.showProgressDialog(false, view.getStringById(R.string.updating_moment));

                }
            }

            @Override
            public void handleError(Throwable e) {
                view.hideProgressDialog();
                view.showFab();
            }

            @Override
            public void onNext(AVObject avObject) {
                if (isCreateMoment) {
                    RxBus.getInstance().post(new MomentEvent(MomentEvent.REFRESH_MOMENT_LIST));
                } else {
                    RxBus.getInstance().post(new MomentEvent(MomentEvent.UPDATE_MOMENT_LIST_ITEM, itemPosition, avObject));
                }
                view.hideProgressDialog();
                view.closePage();
                LogUtil.i("发送动态成功:" + avObject.toString());
            }
        });
    }

}
