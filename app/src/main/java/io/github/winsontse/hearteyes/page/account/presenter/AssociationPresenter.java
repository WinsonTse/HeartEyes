package io.github.winsontse.hearteyes.page.account.presenter;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.signature.Base64Encoder;

import java.util.Arrays;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.data.model.leancloud.CircleContract;
import io.github.winsontse.hearteyes.data.model.leancloud.CircleMemberContract;
import io.github.winsontse.hearteyes.data.model.leancloud.UserContract;
import io.github.winsontse.hearteyes.page.account.contract.AssociationContract;
import io.github.winsontse.hearteyes.page.base.BasePresenterImpl;
import io.github.winsontse.hearteyes.page.main.MainActivity;
import io.github.winsontse.hearteyes.util.HeartEyesSubscriber;
import io.github.winsontse.hearteyes.util.HeartEyesException;
import io.github.winsontse.hearteyes.util.RxUtil;
import io.github.winsontse.hearteyes.util.ZxingUtil;
import io.github.winsontse.hearteyes.util.constant.SecretConstant;
import io.github.winsontse.hearteyes.util.rxbus.event.UidEvent;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

public class AssociationPresenter extends BasePresenterImpl implements AssociationContract.Presenter {
    private AssociationContract.View view;

    @Inject
    public AssociationPresenter(final AssociationContract.View view) {
        this.view = view;

        registerEventReceiver(UidEvent.class, new Action1<UidEvent>() {
            @Override
            public void call(UidEvent uidEvent) {
                associate(uidEvent.getUid());
            }
        });

    }


    @Override
    public void generateQrcode(int width, int height, final int bgColor, final int fgColor) {
        addSubscription(Observable.just(AVUser.getCurrentUser())
                .map(new Func1<AVUser, Bitmap>() {
                    @Override
                    public Bitmap call(AVUser avUser) {
                        String str = avUser.getCreatedAt().getTime() + SecretConstant.QRCODE_CONTENT_SPLIT + avUser.getObjectId();
                        String encodeStr = Base64Encoder.encode(str);
                        return ZxingUtil.generateQRCode(encodeStr, 500, 500, bgColor, fgColor);
                    }
                })
                .compose(RxUtil.rxSchedulerHelper(Bitmap.class))
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
                })
        );
    }

    @Override
    public void associate(String uid) {

        view.showProgressDialog(false, view.getStringById(R.string.tips_associating));
        addSubscription(Observable.just(uid)
                        .map(new Func1<String, AVUser>() {
                            @Override
                            public AVUser call(String str) {
                                try {
                                    return (AVUser) AVUser.createWithoutData(AVUser.class, str).fetch();
                                } catch (AVException e) {
                                    e.printStackTrace();
                                    return null;
                                }
                            }
                        }).flatMap(new Func1<AVUser, Observable<AVObject>>() {
                            @Override
                            public Observable<AVObject> call(final AVUser other) {
                                return Observable.create(new Observable.OnSubscribe<AVObject>() {
                                    @Override
                                    public void call(Subscriber<? super AVObject> subscriber) {
                                        AVUser me = AVUser.getCurrentUser();
                                        //同一人
                                        if (TextUtils.equals(me.getObjectId(), other.getObjectId())) {
                                            subscriber.onError(new HeartEyesException(view.getStringById(R.string.error_cannot_associate_self)));
                                        } else if (me.getAVObject(UserContract.FRIEND) != null || !TextUtils.isEmpty(me.getString(UserContract.CIRCLE_ID))) {
                                            subscriber.onError(new HeartEyesException(view.getStringById(R.string.error_you_has_friends)));
                                        } else if (other.getAVObject(UserContract.FRIEND) != null || !TextUtils.isEmpty(other.getString(UserContract.CIRCLE_ID))) {
                                            subscriber.onError(new HeartEyesException(view.getStringById(R.string.error_she_has_friends)));
                                        } else {

                                            //创建圈子
                                            String cid = Base64Encoder.encode(System.currentTimeMillis() + me.getObjectId() + other.getObjectId());
                                            AVObject circle = new AVObject(CircleContract.TABLE);
                                            circle.put(CircleContract.NAME, me.getString(UserContract.NICKNAME) + "和" + other.getString(UserContract.NICKNAME) + "的圈子");
                                            circle.put(CircleContract.CREATOR, me);
                                            circle.put(CircleContract.INVITEE, other);
                                            circle.put(CircleContract.CID, cid);


                                            //为我增加圈子
                                            me.put(UserContract.FRIEND, other);
                                            me.put(UserContract.CIRCLE_ID, cid);

                                            //为她增加圈子
//                                    other.put(UserContract.FRIEND, me);
//                                    other.put(UserContract.CIRCLE_ID, circle);


                                            //把我加到圈子成员表
                                            AVObject circleMemberMe = new AVObject(CircleMemberContract.TABLE);
                                            circleMemberMe.put(CircleMemberContract.CIRCLE_ID, cid);
                                            circleMemberMe.put(CircleMemberContract.MEMBER, me);
                                            circleMemberMe.put(CircleMemberContract.CREATOR, me);
                                            circleMemberMe.put(CircleMemberContract.INVITEE, other);

                                            //把我加到圈子成员表
                                            AVObject circleMemberOther = new AVObject(CircleMemberContract.TABLE);
                                            circleMemberOther.put(CircleMemberContract.CIRCLE_ID, cid);
                                            circleMemberOther.put(CircleMemberContract.MEMBER, other);
                                            circleMemberOther.put(CircleMemberContract.CREATOR, me);
                                            circleMemberOther.put(CircleMemberContract.INVITEE, other);

                                            try {
                                                AVObject.saveAll(Arrays.asList(circle, me, circleMemberMe, circleMemberOther));
                                                subscriber.onNext(me);
                                            } catch (Exception e) {
                                                me.put(UserContract.FRIEND, null);
                                                me.put(UserContract.CIRCLE_ID, null);
                                                subscriber.onError(e);
                                            }
                                        }

                                    }
                                });
                            }
                        })
                        .compose(RxUtil.rxSchedulerHelper(AVObject.class))
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
                                view.replacePage();
                            }
                        })
        );
    }

}
