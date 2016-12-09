package io.github.winsontse.hearteyes.page.account;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import javax.inject.Inject;

import butterknife.BindView;
import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.page.account.component.DaggerAssociationComponent;
import io.github.winsontse.hearteyes.page.account.contract.AssociationContract;
import io.github.winsontse.hearteyes.page.account.module.AssociationModule;
import io.github.winsontse.hearteyes.page.account.presenter.AssociationPresenter;
import io.github.winsontse.hearteyes.page.base.BaseFragment;
import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.page.main.MainActivity;
import io.github.winsontse.hearteyes.page.qrcode.ScannerFragment;
import io.github.winsontse.hearteyes.util.AnimatorUtil;

public class AssociationFragment extends BaseFragment implements AssociationContract.View, FragmentManager.OnBackStackChangedListener {

    @Inject
    AssociationPresenter presenter;
    @BindView(R.id.iv_qrcode)
    ImageView ivQrcode;
    @BindView(R.id.fab_swipe)
    FloatingActionButton fabSwipe;
    @BindView(R.id.v_cover)
    View vCover;

    public static AssociationFragment newInstance() {
        Bundle args = new Bundle();
        AssociationFragment fragment = new AssociationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public void initView( @Nullable Bundle savedInstanceState) {
        presenter.generateQrcode(500, 500, Color.WHITE, Color.BLACK);
        fabSwipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCoverAndOpenScanner();
            }
        });
        getFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_association;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getFragmentManager().removeOnBackStackChangedListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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

    /**
     * Called whenever the contents of the back stack change.
     */
    @Override
    public void onBackStackChanged() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            hideCover();
        }
    }

    private void showCoverAndOpenScanner() {
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
                        if (getActivity() instanceof MainActivity) {
                            ((MainActivity) getActivity()).openPage(AssociationFragment.this, ScannerFragment.newInstance(), true);
                        }
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
}