package io.github.winsontse.hearteyes.page.moment.presenter;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import io.github.winsontse.hearteyes.util.constant.Preference;
import io.github.winsontse.hearteyes.util.rxbus.RxBus;
import io.github.winsontse.hearteyes.util.rxbus.event.MomentEvent;
import rx.Observable;
import rx.Subscriber;

public class MomentEditPresenter extends BasePresenterImpl implements MomentEditContract.Presenter {
    private MomentEditContract.View view;
    private AVObject currentMoment;
    private boolean isCreateMoment = true;
    private int itemPosition;
    private double latitude;
    private double longitude;
    private String address;

    private static final int LOCATION_INTERVAL = 30 * 1000;
    private SharedPreferences sp;

    @Inject
    public MomentEditPresenter(MomentEditContract.View view, SharedPreferences sp) {
        this.view = view;
        this.sp = sp;
    }

    public void init(AVObject currentMoment, int itemPosition) {
        if (currentMoment != null) {
            isCreateMoment = false;
            this.currentMoment = currentMoment;
            this.itemPosition = itemPosition;
            view.updateEditContent(currentMoment.getString(MomentContract.CONTENT));
        }

        if (isCreateMoment && sp.contains(Preference.MOMENT_CONTENT)) {
            view.updateEditContent(sp.getString(Preference.MOMENT_CONTENT, ""));
        }

    }

    @Override
    public void publishMoment(final String content, final List<ImageEntity> images) {
        view.hideKeyboard();
        if (TextUtils.isEmpty(content)) {
            view.showToast(view.getStringById(R.string.tips_moment_content_not_empty));
            return;
        }

        addSubscription(Observable.create(new Observable.OnSubscribe<AVObject>() {
            @Override
            public void call(Subscriber<? super AVObject> subscriber) {

                try {
                    if (isCreateMoment) {
                        currentMoment = AVObject.create(MomentContract.KEY);
                        //只有新建时添加地理位置信息
                        if (!TextUtils.isEmpty(address) && longitude != 0 && latitude != 0) {
                            currentMoment.put(MomentContract.ADDRESS, address);
                            currentMoment.put(MomentContract.LONGITUDE, longitude);
                            currentMoment.put(MomentContract.LATITUDE, latitude);
                        }
                    }

//                    if (!TextUtils.isEmpty(address) && longitude != 0 && latitude != 0) {
//                        currentMoment.put(MomentContract.ADDRESS, address);
//                        currentMoment.put(MomentContract.LONGITUDE, longitude);
//                        currentMoment.put(MomentContract.LATITUDE, latitude);
//                    }

                    currentMoment.put(MomentContract.CONTENT, content);


                    List<AVFile> avFiles = new ArrayList<>();
                    for (ImageEntity entity : images) {
                        AVFile avFile;
                        if (entity.getData().endsWith(ImageUtil.GIF)) {
                            avFile = AVFile.withAbsoluteLocalPath(System.currentTimeMillis() + "_" + entity.getTitle() + ImageUtil.getNameSuffix(entity.getMineType()), entity.getData());
                        } else {
                            avFile = new AVFile(System.currentTimeMillis() + "_" + entity.getTitle() + ".jpg", ImageUtil.compressImageFile(entity.getData()));
                        }
                        avFile.save();
                        avFiles.add(avFile);
                    }

                    //新建
                    if (isCreateMoment) {
                        AVUser currentUser = getCurrentUser();
                        currentMoment.put(MomentContract.AUTHOR, currentUser);
                        currentMoment.put(MomentContract.CIRCLE_ID, currentUser.getString(UserContract.CIRCLE_ID));
                        if (avFiles.size() > 0) {
                            currentMoment.put(MomentContract.IMAGES, avFiles);
                        }
                        currentMoment.put(MomentContract.CREATEAD_TIME, System.currentTimeMillis());
                    }
                    //更新
                    else {
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
            }

            @Override
            public void onNext(AVObject avObject) {
                if (isCreateMoment) {
                    RxBus.getInstance().post(new MomentEvent(MomentEvent.REFRESH_MOMENT_LIST));
                } else {
                    RxBus.getInstance().post(new MomentEvent(MomentEvent.UPDATE_MOMENT_LIST_ITEM, itemPosition, avObject));
                }
                sp.edit().remove(Preference.MOMENT_CONTENT).apply();
                view.updateEditContent("");
                view.hideProgressDialog();
                view.closePage();
                LogUtil.i("发送动态成功:" + avObject.toString());
            }
        });
    }

    @Override
    public void initLocationClient(AMapLocationClient mLocationClient) {

        //声明mLocationOption对象
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);

        if (mLocationOption.isOnceLocationLatest()) {
            mLocationOption.setOnceLocationLatest(true);
            //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
            //如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会。
        }

        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(LOCATION_INTERVAL);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);

        //设置定位回调监听
        mLocationClient.setLocationListener(new AMapLocationListener() {

            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        String currentAddress = aMapLocation.getAddress();
                        if (!TextUtils.isEmpty(currentAddress)) {
                            address = currentAddress;
                        }
                        double currentLatitude = aMapLocation.getLatitude();
                        if (currentLatitude != 0) {
                            latitude = currentLatitude;
                        }
                        double currentLongitude = aMapLocation.getLongitude();
                        if (currentLongitude != 0) {
                            longitude = currentLongitude;
                        }
                        //定位成功回调信息，设置相关消息
                        aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                        aMapLocation.getLatitude();//获取纬度
                        aMapLocation.getLongitude();//获取经度
                        aMapLocation.getAccuracy();//获取精度信息
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(aMapLocation.getTime());
                        df.format(date);//定位时间
                        aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                        aMapLocation.getCountry();//国家信息
                        aMapLocation.getProvince();//省信息
                        aMapLocation.getCity();//城市信息
                        aMapLocation.getDistrict();//城区信息
                        aMapLocation.getStreet();//街道信息
                        aMapLocation.getStreetNum();//街道门牌号信息
                        aMapLocation.getCityCode();//城市编码
                        aMapLocation.getAdCode();//地区编码
                        aMapLocation.getAoiName();//获取当前定位点的AOI信息
                        LogUtil.e("成功获得位置:" + aMapLocation.toStr());
                    } else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        LogUtil.e("location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo() + "  "
                                + aMapLocation.getLocationDetail());
                    }
                }

                //启动定位

            }
        });
    }

    @Override
    public void saveContent(String content) {
        if (!isCreateMoment || TextUtils.isEmpty(content)) {
            return;
        }
        sp.edit().putString(Preference.MOMENT_CONTENT, content).apply();
    }

}
