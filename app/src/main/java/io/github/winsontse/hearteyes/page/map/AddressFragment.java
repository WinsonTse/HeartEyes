package io.github.winsontse.hearteyes.page.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.avos.avoscloud.AVObject;

import javax.inject.Inject;

import butterknife.BindView;
import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.model.entity.leancloud.MomentContract;
import io.github.winsontse.hearteyes.page.base.BaseFragment;
import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.page.map.component.DaggerAddressComponent;
import io.github.winsontse.hearteyes.page.map.contract.AddressContract;
import io.github.winsontse.hearteyes.page.map.module.AddressModule;
import io.github.winsontse.hearteyes.page.map.presenter.AddressPresenter;

public class AddressFragment extends BaseFragment implements AddressContract.View {

    @Inject
    AddressPresenter presenter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.v_map)
    MapView vMap;
    private AVObject moment;
    private float longitude;
    private float latitude;

    public static AddressFragment newInstance(AVObject momentObject) {
        Bundle args = new Bundle();
        args.putParcelable(MomentContract.KEY, momentObject);
        AddressFragment fragment = new AddressFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moment = getArguments().getParcelable(MomentContract.KEY);
        if (moment != null) {
            longitude = (float) moment.getDouble(MomentContract.LONGITUDE);
            latitude = (float) moment.getDouble(MomentContract.LATITUDE);
        }
    }

    @Override
    public void initView( @Nullable Bundle savedInstanceState) {
        initMap(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_address;
    }

    private void initMap(Bundle savedInstanceState) {
        vMap.onCreate(savedInstanceState);
        AMap aMap = vMap.getMap();
        LatLng latLng = new LatLng(latitude, longitude);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.draggable(false);
        markerOptions.visible(true);
        //当用户点击标记，在信息窗口上显示的字符串。
        Marker marker = aMap.addMarker(new MarkerOptions());
//        marker.setTitle(moment.getString(MomentContract.ADDRESS));
        marker.showInfoWindow();
        marker.setPosition(latLng);
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerAddressComponent.builder()
                .appComponent(appComponent)
                .addressModule(new AddressModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        vMap.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        vMap.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        vMap.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        vMap.onSaveInstanceState(outState);
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }
}