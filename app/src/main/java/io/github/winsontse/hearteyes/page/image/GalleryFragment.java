package io.github.winsontse.hearteyes.page.image;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.model.entity.image.ImageEntity;
import io.github.winsontse.hearteyes.model.entity.leancloud.MomentContract;
import io.github.winsontse.hearteyes.page.adapter.GalleryPagerAdapter;
import io.github.winsontse.hearteyes.page.base.BaseFragment;
import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.page.image.component.DaggerGalleryComponent;
import io.github.winsontse.hearteyes.page.image.contract.GalleryContract;
import io.github.winsontse.hearteyes.page.image.module.GalleryModule;
import io.github.winsontse.hearteyes.page.image.presenter.GalleryPresenter;
import io.github.winsontse.hearteyes.util.TimeUtil;
import io.github.winsontse.hearteyes.util.constant.Extra;
import io.github.winsontse.hearteyes.widget.HackyProblematicViewPager;

public class GalleryFragment extends BaseFragment implements GalleryContract.View {

    @Inject
    GalleryPresenter presenter;
    @BindView(R.id.vp)
    HackyProblematicViewPager vp;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_indicator)
    TextView tvIndicator;
    @BindView(R.id.nsv_bottom)
    NestedScrollView nsvBottom;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    private int position;
    private List<ImageEntity> images;
    private long createTime;
    private String content;
    private int colorPrimaryDark;
    private int colorBlack;
    private int colorBottomSheet;
    private RequestManager requestManager;

    public static GalleryFragment newInstance(AVObject moment, int position) {
        Bundle args = new Bundle();
        List<AVFile> avFiles = moment.getList(MomentContract.IMAGES);
        ArrayList<ImageEntity> images = new ArrayList<>();
        for (AVFile avFile : avFiles) {
            ImageEntity entity = new ImageEntity();
            entity.setData(avFile.getUrl());
            entity.setThumbnail(avFile.getThumbnailUrl(true, 200, 200));
            images.add(entity);
        }
        args.putParcelableArrayList(Extra.IMAGES, images);
        args.putInt(Extra.ITEM_POSITION, position);
        args.putLong(Extra.TIME, moment.getLong(MomentContract.CREATEAD_TIME));
        args.putString(Extra.CONTENT, moment.getString(MomentContract.CONTENT));
        GalleryFragment fragment = new GalleryFragment();
        fragment.setArguments(args);
        return fragment;
    }

//    public static GalleryFragment newInstance(List<AVFile> avFiles, int position) {
//        Bundle args = new Bundle();
//        ArrayList<ImageEntity> images = new ArrayList<>();
//        for (AVFile avFile : avFiles) {
//            ImageEntity entity = new ImageEntity();
//            entity.setData(avFile.getUrl());
//            entity.setThumbnail(avFile.getThumbnailUrl(true, 200, 200));
//            images.add(entity);
//        }
//        args.putParcelableArrayList(Extra.IMAGES, images);
//        args.putInt(Extra.COUNT, images.size());
//        args.putInt(Extra.ITEM_POSITION, position);
//        GalleryFragment fragment = new GalleryFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle args = getArguments();
        images = args.getParcelableArrayList(Extra.IMAGES);
        position = args.getInt(Extra.ITEM_POSITION);
        createTime = args.getLong(Extra.TIME);
        content = args.getString(Extra.CONTENT);
    }

    @Override
    protected boolean isStatusBarViewVisible() {
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        colorPrimaryDark = getColorById(R.color.colorPrimaryDark);
        colorBlack = getColorById(R.color.md_black);
        colorBottomSheet = getColorById(R.color.md_grey_500);

        Window window = getActivity().getWindow();
        window.setNavigationBarColor(colorBlack);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void initView( @Nullable Bundle savedInstanceState) {

        final GalleryPagerAdapter galleryPagerAdapter = new GalleryPagerAdapter(getChildFragmentManager(), images);

        requestManager = Glide.with(getActivity());
        vp.setAdapter(galleryPagerAdapter);
        vp.setCurrentItem(position);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                requestManager = Glide.with(getActivity());
                if (positionOffset == 0) {
                    if (requestManager.isPaused()) {
                        requestManager.resumeRequests();
                    }
                } else {
                    if (!requestManager.isPaused()) {
                        requestManager.pauseRequests();
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                tvIndicator.setText((position + 1) + "/" + galleryPagerAdapter.getCount());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tvIndicator.setText((vp.getCurrentItem() + 1) + "/" + galleryPagerAdapter.getCount());
        tvDate.setText(TimeUtil.getFormatTime(createTime, "创建于 yyyy-MM-dd HH:mm"));
        tvContent.setText(content);

        final BottomSheetBehavior sheetBehavior = BottomSheetBehavior.from(nsvBottom);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                nsvBottom.setBackgroundColor(ColorUtils.setAlphaComponent(colorBottomSheet, (int) (127 * slideOffset)));
            }
        });
        llBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Window window = getActivity().getWindow();
        window.setNavigationBarColor(colorPrimaryDark);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gallery;
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerGalleryComponent.builder()
                .appComponent(appComponent)
                .galleryModule(new GalleryModule(this))
                .build()
                .inject(this);
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

}