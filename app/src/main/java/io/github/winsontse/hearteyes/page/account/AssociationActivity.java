package io.github.winsontse.hearteyes.page.account;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;

import com.tbruyelle.rxpermissions.RxPermissions;

import javax.inject.Inject;

import butterknife.BindView;
import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.page.account.component.DaggerAssociationComponent;
import io.github.winsontse.hearteyes.page.account.contract.AssociationContract;
import io.github.winsontse.hearteyes.page.account.module.AssociationModule;
import io.github.winsontse.hearteyes.page.account.presenter.AssociationPresenter;
import io.github.winsontse.hearteyes.page.base.BaseActivity;
import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.page.qrcode.ScannerActivity;
import io.github.winsontse.hearteyes.util.AnimatorUtil;
import io.github.winsontse.hearteyes.util.HeartEyesSubscriber;
import io.github.winsontse.hearteyes.util.constant.Extra;

public class AssociationActivity extends BaseActivity implements AssociationContract.View {

    @Inject
    AssociationPresenter presenter;
    @BindView(R.id.iv_qrcode)
    ImageView ivQrcode;
    @BindView(R.id.fab_swipe)
    FloatingActionButton fabSwipe;
    @BindView(R.id.v_cover)
    View vCover;

    public static final int RESULT_CODE = 0X1433;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, AssociationActivity.class);
        activity.startActivityForResult(intent, RESULT_CODE);
    }

    @Nullable
    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        presenter.generateQrcode(500, 500, Color.WHITE, Color.BLACK);
        fabSwipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCoverAndOpenScanner();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_association;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerAssociationComponent.builder()
                .appComponent(appComponent)
                .associationModule(new AssociationModule(this))
                .build()
                .inject(this);
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void showQrcode(Bitmap bitmap) {
        ivQrcode.setImageBitmap(bitmap);

    }

    private void showCoverAndOpenScanner() {
        RxPermissions.getInstance(this)
                .request(Manifest.permission.CAMERA)
                .subscribe(new HeartEyesSubscriber<Boolean>() {
                    @Override
                    public void handleError(Throwable e) {
                        showToast(getStringById(R.string.not_permission_camera));
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        int centerX = (fabSwipe.getLeft() + fabSwipe.getRight()) / 2;
                        int centerY = (fabSwipe.getTop() + fabSwipe.getBottom()) / 2;
                        vCover.setVisibility(View.VISIBLE);
                        AnimatorUtil.createCircularReveal(vCover, centerX, centerY, fabSwipe.getWidth() / 2,
                                (float) Math.hypot((double) centerX, (double) centerY)
                                , new AnimatorListenerAdapter() {
                                    /**
                                     * {@inheritDoc}
                                     *
                                     * @param animation
                                     */
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        ScannerActivity.start(AssociationActivity.this);
                                    }
                                });
                    }
                });

    }

    private void hideCover() {
        int centerX = (fabSwipe.getLeft() + fabSwipe.getRight()) / 2;
        int centerY = (fabSwipe.getTop() + fabSwipe.getBottom()) / 2;
        vCover.setVisibility(View.VISIBLE);
        AnimatorUtil.createCircularReveal(vCover, centerX, centerY,
                (float) Math.hypot((double) centerX, (double) centerY),
                fabSwipe.getWidth() / 2,
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        vCover.setVisibility(View.INVISIBLE);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ScannerActivity.REQUEST_CODE) {
            hideCover();
            if (resultCode == RESULT_OK && data != null) {
                presenter.associate(data.getStringExtra(Extra.QRCODE_RESULT));
            }
        }
    }
}