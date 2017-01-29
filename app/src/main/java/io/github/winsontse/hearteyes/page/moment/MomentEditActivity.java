package io.github.winsontse.hearteyes.page.moment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocationClient;
import com.avos.avoscloud.AVObject;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.model.entity.ImageEntity;
import io.github.winsontse.hearteyes.model.entity.leancloud.MomentContract;
import io.github.winsontse.hearteyes.page.adapter.SelectedImagesAdapter;
import io.github.winsontse.hearteyes.page.base.BaseActivity;
import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.page.image.ImagePickerActivity;
import io.github.winsontse.hearteyes.page.moment.component.DaggerMomentEditComponent;
import io.github.winsontse.hearteyes.page.moment.contract.MomentEditContract;
import io.github.winsontse.hearteyes.page.moment.module.MomentEditModule;
import io.github.winsontse.hearteyes.page.moment.presenter.MomentEditPresenter;
import io.github.winsontse.hearteyes.util.constant.Extra;
import rx.functions.Action1;

public class MomentEditActivity extends BaseActivity implements MomentEditContract.View {

    @Inject
    MomentEditPresenter presenter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.fab_send)
    FloatingActionButton fabSend;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_image_count)
    TextView tvImageCount;
    @BindView(R.id.tv_record_count)
    TextView tvRecordCount;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.iv_keyboard)
    ImageView ivKeyboard;
    @BindView(R.id.ll_content)
    LinearLayout llContent;

    private static final int MAX_IMAGES_COUNT = 30;
    private SelectedImagesAdapter selectedImagesAdapter;
    private AVObject currentMoment;
    private int itemPosition;
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient;

    public static void start(Activity activity, View shareElement, int itemPosition, AVObject avObject) {
        Intent intent = new Intent(activity, MomentEditActivity.class);

        if (avObject != null) {
            Bundle args = new Bundle();
            args.putParcelable(MomentContract.KEY, avObject);
            args.putInt(Extra.ITEM_POSITION, itemPosition);
            intent.putExtras(args);
        }

        ActivityCompat.startActivity(activity, intent, ActivityOptionsCompat.makeSceneTransitionAnimation(activity, shareElement, ViewCompat.getTransitionName(shareElement)).toBundle());

    }

    @Override
    public void initView(Bundle savedInstanceState) {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(MomentContract.KEY)) {
            currentMoment = bundle.getParcelable(MomentContract.KEY);
            itemPosition = bundle.getInt(Extra.ITEM_POSITION);
        }
        presenter.init(currentMoment, itemPosition);
        if (currentMoment == null) {
            toolbar.setTitle(R.string.create_moment);
        } else {
            toolbar.setTitle(R.string.edit_moment);
        }
        initRecyclerView();
        bindListener();
        requestLocationPermission();
//        toggleKeyboard(this, ll);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_moment_edit;
    }

    private void requestLocationPermission() {
        RxPermissions.getInstance(this)
                .request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
                            //初始化定位
                            mLocationClient = new AMapLocationClient(MomentEditActivity.this);
                            presenter.initLocationClient(mLocationClient);
                            mLocationClient.startLocation();
                        }
                    }
                });
    }

    private void bindListener() {
        etContent.requestFocus();
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = etContent.getText().toString().trim().length();
//                if (length > 0) {
//                    if (fabSend.getVisibility() == View.GONE) {
//                        fabSend.show();
//                    }
//                } else {
//                    if (fabSend.getVisibility() == View.VISIBLE) {
//                        fabSend.hide();
//                    }
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                fabSend.hide();
                presenter.publishMoment(etContent.getText().toString().trim(), selectedImagesAdapter.getData());

            }
        });

        tvImageCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePickerActivity.startImagePicker(MomentEditActivity.this, MAX_IMAGES_COUNT, selectedImagesAdapter.getData());
            }
        });

        tvRecordCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("还没做出来这个功能");
            }
        });

        llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleKeyboard(MomentEditActivity.this, ll);
            }
        });

        ivKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleKeyboard(MomentEditActivity.this, ll);
            }
        });

    }

    private void initRecyclerView() {
        selectedImagesAdapter = new SelectedImagesAdapter();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(selectedImagesAdapter);
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerMomentEditComponent.builder()
                .appComponent(appComponent)
                .momentEditModule(new MomentEditModule(this))
                .build()
                .inject(this);
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<ImageEntity> imageEntities = ImagePickerActivity.handleSelectedImageResult(resultCode, data);
        if (imageEntities != null) {
            selectedImagesAdapter.setItems(imageEntities);
            tvImageCount.setText(String.valueOf(imageEntities.size()));
        }
        hideKeyboard();
    }

    @Override
    public void updateEditContent(String content) {
        etContent.setText(content);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.saveContent(etContent.getText().toString());
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
    }

    @Override
    public void closePage() {
//        hideKeyboard();
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.playTogether(ObjectAnimator.ofFloat(appBar, "translationY", -appBar.getHeight()),
//                ObjectAnimator.ofFloat(llBottom, "translationY", llBottom.getHeight()),
//                ObjectAnimator.ofFloat(fabSend, "x", (int) ScreenUtil.screenWidth / 2 - fabSend.getWidth() / 2),
//                ObjectAnimator.ofFloat(fabSend, "y", (int) (ScreenUtil.screenHeight / 2) - fabSend.getHeight() / 2));
//        animatorSet.setDuration(AnimatorUtil.ANIMATOR_TIME);
//        animatorSet.setInterpolator(new DecelerateInterpolator());
//        animatorSet.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                MomentEditActivity.super.closePage();
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
//        animatorSet.start();
        super.closePage();
    }

    @Override
    public void onBackPressed() {
        closePage();
    }
}