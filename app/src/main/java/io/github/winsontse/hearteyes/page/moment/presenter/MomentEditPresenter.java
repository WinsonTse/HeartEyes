package io.github.winsontse.hearteyes.page.moment.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.data.model.ImageEntity;
import io.github.winsontse.hearteyes.data.model.leancloud.MomentContract;
import io.github.winsontse.hearteyes.data.model.leancloud.UserContract;
import io.github.winsontse.hearteyes.page.moment.contract.MomentEditContract;
import io.github.winsontse.hearteyes.page.base.BasePresenterImpl;
import io.github.winsontse.hearteyes.util.HeartEyesSubscriber;
import io.github.winsontse.hearteyes.util.ImageUtil;
import io.github.winsontse.hearteyes.util.LogUtil;
import io.github.winsontse.hearteyes.util.RxUtil;
import rx.Observable;
import rx.Subscriber;

public class MomentEditPresenter extends BasePresenterImpl implements MomentEditContract.Presenter {
    private MomentEditContract.View view;

    @Inject
    public MomentEditPresenter(MomentEditContract.View view) {
        this.view = view;
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
                    AVObject moment = AVObject.create(MomentContract.TABLE);
                    AVUser currentUser = getCurrentUser();
                    moment.put(MomentContract.AUTHOR, currentUser);
                    moment.put(MomentContract.CIRCLE_ID, currentUser.getString(UserContract.CIRCLE_ID));
                    moment.put(MomentContract.CONTENT, content);
                    moment.put(MomentContract.IMAGES, avFiles);
                    moment.put(MomentContract.CREATEAD_TIME, System.currentTimeMillis());
                    moment.setFetchWhenSave(true);
                    moment.save();
                    subscriber.onNext(moment);
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }), new HeartEyesSubscriber<AVObject>(view) {

            @Override
            public void onStart() {
                super.onStart();
                view.showProgressDialog(false, view.getStringById(R.string.publishing_moment));
            }

            @Override
            public void handleError(Throwable e) {
                view.hideProgressDialog();
                view.showFab();
            }

            @Override
            public void onNext(AVObject avObject) {
                view.hideProgressDialog();
                view.closePage();
                LogUtil.e("成功:" + avObject.toString());
            }
        });
    }
}
