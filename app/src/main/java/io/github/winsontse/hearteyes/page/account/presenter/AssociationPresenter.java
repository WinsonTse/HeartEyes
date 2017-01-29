package io.github.winsontse.hearteyes.page.account.presenter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.signature.Base64Encoder;

import java.util.Arrays;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.model.entity.leancloud.CircleContract;
import io.github.winsontse.hearteyes.model.entity.leancloud.CircleMemberContract;
import io.github.winsontse.hearteyes.model.entity.leancloud.UserContract;
import io.github.winsontse.hearteyes.page.account.contract.AssociationContract;
import io.github.winsontse.hearteyes.page.base.BasePresenterImpl;
import io.github.winsontse.hearteyes.util.HeartEyesException;
import io.github.winsontse.hearteyes.util.HeartEyesSubscriber;
import io.github.winsontse.hearteyes.util.ZxingUtil;
import io.github.winsontse.hearteyes.util.constant.SecretConstant;
import io.github.winsontse.hearteyes.util.rxbus.RxBus;
import io.github.winsontse.hearteyes.util.rxbus.event.PushEvent;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class AssociationPresenter extends BasePresenterImpl implements AssociationContract.Presenter {
    private AssociationContract.View view;

    @Inject
    public AssociationPresenter(final AssociationContract.View view) {
        this.view = view;
    }


    @Override
    public void generateQrcode(int width, int height, final int bgColor, final int fgColor) {
        rxLifeAndSchedule(Observable.just(AVUser.getCurrentUser())
                .map(new Func1<AVUser, Bitmap>() {
                    @Override
                    public Bitmap call(AVUser avUser) {
//                        String str = System.currentTimeMillis() + SecretConstant.QRCODE_CONTENT_SPLIT + "584a905eac502e00691d3222";
                        String str = avUser.getCreatedAt().getTime() + SecretConstant.QRCODE_CONTENT_SPLIT + avUser.getObjectId();
                        String encodeStr = Base64Encoder.encode(str);
                        return ZxingUtil.generateQRCode(encodeStr, 500, 500, bgColor, fgColor);
                    }
                }))
                .subscribe(new Subscriber<Bitmap>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        if (bitmap != null) {
                            view.showQrcode(bitmap);
                        }
                    }
                });
    }

    @Override
    public void associate(String content) {


        rxLifeAndSchedule(Observable.just(content)
                .flatMap(new Func1<String, Observable<AVUser>>() {
                    @Override
                    public Observable<AVUser> call(final String str) {
                        return Observable.create(new Observable.OnSubscribe<AVUser>() {
                            @Override
                            public void call(Subscriber<? super AVUser> subscriber) {
                                try {
                                    String result = new String(Base64.decode(str, Base64.DEFAULT));
                                    if (result.contains(SecretConstant.QRCODE_CONTENT_SPLIT)) {
                                        String[] splitArray = result.split(SecretConstant.QRCODE_CONTENT_SPLIT);
                                        if (splitArray.length == 2 && !TextUtils.isEmpty(splitArray[1])) {
                                            String objectId = splitArray[1];
                                            subscriber.onNext((AVUser) AVUser.createWithoutData(AVUser.class, objectId).fetch());
                                            subscriber.onCompleted();
                                        } else {
                                            subscriber.onError(new HeartEyesException(view.getStringById(R.string.error_qrcode)));
                                        }
                                    } else {
                                        subscriber.onError(new HeartEyesException(view.getStringById(R.string.error_qrcode)));
                                    }

                                } catch (AVException e) {
                                    subscriber.onError(e);
                                }
                            }
                        });
                    }
                })
                .flatMap(new Func1<AVUser, Observable<AVObject>>() {
                    @Override
                    public Observable<AVObject> call(final AVUser otherUser) {
                        return Observable.create(new Observable.OnSubscribe<AVObject>() {
                            @Override
                            public void call(Subscriber<? super AVObject> subscriber) {
                                AVUser me = AVUser.getCurrentUser();

                                //同一人
                                if (TextUtils.equals(me.getObjectId(), otherUser.getObjectId())) {
                                    subscriber.onError(new HeartEyesException(view.getStringById(R.string.error_cannot_associate_self)));
                                    return;
                                }

                                try {
                                    AVQuery<AVObject> meQuery = new AVQuery<>(CircleMemberContract.KEY);
                                    meQuery.whereEqualTo(CircleMemberContract.USER, me);
                                    if (meQuery.count() != 0) {
                                        subscriber.onError(new HeartEyesException(view.getStringById(R.string.error_you_has_friends)));
                                        return;
                                    }

                                    AVQuery<AVObject> otherQuery = new AVQuery<>(CircleMemberContract.KEY);
                                    otherQuery.whereEqualTo(CircleMemberContract.USER, otherUser);
                                    if (otherQuery.count() != 0) {
                                        subscriber.onError(new HeartEyesException(view.getStringById(R.string.error_she_has_friends)));
                                        return;
                                    }

                                    //创建圈子
                                    AVObject circle = new AVObject(CircleContract.KEY);
                                    circle.put(CircleContract.NAME, me.getString(UserContract.NICKNAME) + "和" + otherUser.getString(UserContract.NICKNAME) + "的圈子");
                                    circle.save();
                                    circle.fetch();

                                    //把我加到圈子成员表
                                    AVObject circleMemberMe = new AVObject(CircleMemberContract.KEY);
                                    circleMemberMe.put(CircleMemberContract.CIRCLE, circle);
                                    circleMemberMe.put(CircleMemberContract.USER, me);

                                    //把ta加到圈子成员表
                                    AVObject circleMemberOther = new AVObject(CircleMemberContract.KEY);
                                    circleMemberOther.put(CircleMemberContract.CIRCLE, circle);
                                    circleMemberOther.put(CircleMemberContract.USER, otherUser);

                                    AVObject.saveAll(Arrays.asList(circleMemberMe, circleMemberOther));
                                    subscriber.onNext(me);
                                    subscriber.onCompleted();

                                } catch (AVException e) {
                                    subscriber.onError(e);
                                }

                            }

                        });
                    }
                }))
                .subscribe(new HeartEyesSubscriber<AVObject>(view) {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void handleError(Throwable e) {
                        view.hideProgressDialog();
                    }

                    @Override
                    public void onNext(AVObject avObject) {
                        view.hideProgressDialog();
                        Log.d("winson", "执行");
                        Log.d("winson", "正确:" + avObject.toString() + avObject.getString(UserContract.NICKNAME));
                        view.setResult(Activity.RESULT_OK);
                        view.closePage();
                        RxBus.getInstance().post(new PushEvent(PushEvent.RESTART_AND_NOTIFY_FRIEND));
                    }
                });
    }

}
