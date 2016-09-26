package io.github.winsontse.hearteyes.page.moment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.data.model.ImageEntity;
import io.github.winsontse.hearteyes.data.model.leancloud.CircleContract;
import io.github.winsontse.hearteyes.data.model.leancloud.MomentContract;
import io.github.winsontse.hearteyes.page.adapter.MomentListAdapter;
import io.github.winsontse.hearteyes.page.adapter.base.BaseRecyclerAdapter;
import io.github.winsontse.hearteyes.page.adapter.base.OnRecyclerViewScrollListener;
import io.github.winsontse.hearteyes.page.adapter.diff.MomentListDiffCallback;
import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.page.base.TimelineFragment;
import io.github.winsontse.hearteyes.page.image.GalleryFragment;
import io.github.winsontse.hearteyes.page.image.ImagePickerActivity;
import io.github.winsontse.hearteyes.page.main.MainActivity;
import io.github.winsontse.hearteyes.page.map.AddressFragment;
import io.github.winsontse.hearteyes.page.moment.component.DaggerMomentListComponent;
import io.github.winsontse.hearteyes.page.moment.contract.MomentListContract;
import io.github.winsontse.hearteyes.page.moment.module.MomentListModule;
import io.github.winsontse.hearteyes.page.moment.presenter.MomentListPresenter;
import io.github.winsontse.hearteyes.util.AnimatorUtil;
import io.github.winsontse.hearteyes.util.FileUtil;
import io.github.winsontse.hearteyes.util.LogUtil;
import io.github.winsontse.hearteyes.util.ScreenUtil;
import io.github.winsontse.hearteyes.util.TimeUtil;
import io.github.winsontse.hearteyes.widget.crop.Crop;

public class MomentListFragment extends TimelineFragment<AVObject>
        implements MomentListContract.View,
        MomentListAdapter.OnMomentClickListener,
        ObservableScrollViewCallbacks {

    @Inject
    MomentListPresenter presenter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv)
    ObservableRecyclerView rv;
    @BindView(R.id.fab_edit)
    FloatingActionButton fabEdit;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.ll_time)
    LinearLayout llTime;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.tv_week)
    TextView tvWeek;
    @BindView(R.id.v_empty)
    NestedScrollView vEmpty;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.fl_time)
    FrameLayout flTime;
    @BindView(R.id.v_moment_list_status)
    View vMomentListStatus;

    private LinearLayoutManager layoutManager;
    private MomentListAdapter momentListAdapter;
    private long time;
    private Calendar calendar;
    private Uri cropOutUri;


    public static MomentListFragment newInstance() {
        return new MomentListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        flTime.setPadding(0, ScreenUtil.statusBarHeight + ScreenUtil.toolbarHeight, 0, 0);
        vMomentListStatus.getLayoutParams().height = ScreenUtil.statusBarHeight;
        vMomentListStatus.requestLayout();

        calendar = TimeUtil.getCalendar();
        fabEdit.postDelayed(new Runnable() {
            @Override
            public void run() {
                fabEdit.show();
            }
        }, AnimatorUtil.ANIMATOR_TIME);
        bindListener();
        initRecyclerView();
        initSwipeRefreshLayout();
    }

    private void initSwipeRefreshLayout() {
        srl.setProgressViewOffset(false, ScreenUtil.statusBarHeight, ScreenUtil.statusBarHeight * 2 + ScreenUtil.toolbarHeight);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_moment_list;
    }

    private void bindListener() {
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabEdit.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                    @Override
                    public void onHidden(FloatingActionButton fab) {
                        super.onHidden(fab);
                        goToEditPage();
                    }
                });
            }
        });

    }

    @Override
    protected boolean isSupportBackNavigation() {
        return false;
    }

    @Override
    protected boolean isStatusBarViewVisible() {
        return false;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            fabEdit.show();
            setStatusBarViewVisible(false);
        } else {
//            fabEdit.hide();
        }
    }

    private void initRecyclerView() {
        final int llTimeOffsize = ScreenUtil.statusBarHeight + ScreenUtil.toolbarHeight;
        momentListAdapter = new MomentListAdapter();
        rv.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(momentListAdapter);

        llTime.setVisibility(View.INVISIBLE);
        addOnRecyclerViewScrollListener(new OnRecyclerViewScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                View childViewUnder = recyclerView.findChildViewUnder(llTime.getMeasuredWidth(), llTimeOffsize);
                if (childViewUnder == null) {
                    return;
                }
                AVObject currentAvObject = (AVObject) childViewUnder.getTag(R.id.tag_data);
                if (currentAvObject == null) {
                    llTime.setVisibility(View.INVISIBLE);
                    return;
                }

                if (layoutManager.findFirstCompletelyVisibleItemPosition() == momentListAdapter.getHeaderCount()) {
                    llTime.setVisibility(View.VISIBLE);
                }
                time = currentAvObject.getLong(MomentContract.CREATEAD_TIME);
                calendar.setTimeInMillis(time);
                tvDate.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
                tvWeek.setText(TimeUtil.getWeek(time));
                llTime.setTag(R.id.tag_data, currentAvObject);
                toolbar.setTitle(calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月");

                View itemView = recyclerView.findChildViewUnder(llTime.getMeasuredWidth(), llTime.getMeasuredHeight() + llTimeOffsize);
                if (itemView != null) {
                    int tag = (int) itemView.getTag(R.id.tag_type);
                    int deltaY = itemView.getTop() - llTime.getMeasuredHeight() - llTimeOffsize;
                    if (tag == MomentListAdapter.TAG_HEADER_VISIBLE) {
                        if (itemView.getTop() - llTimeOffsize > 0) {
                            llTime.setTranslationY(deltaY);
                        } else {
                            llTime.setTranslationY(0);
                        }
                    } else if (tag == MomentListAdapter.TAG_HEADER_INVISIBLE) {
                        llTime.setTranslationY(0);
                    }
                }
            }
        });
        rv.setScrollViewCallbacks(this);
        momentListAdapter.setOnMomentClickListener(this);
        llTime.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Object posTag = v.getTag(R.id.tag_position);
                Object dataTag = v.getTag(R.id.tag_data);
                if (posTag != null && dataTag != null) {
                    int pos = (int) posTag;
                    AVObject avObject = (AVObject) dataTag;
                    long time = avObject.getLong(MomentContract.CREATEAD_TIME);

                    showMomentDatePickerDialog(time, time, avObject);
                }
                return true;
            }
        });

        presenter.loadCircle();
    }

    @Override
    public void showMomentDatePickerDialog(final long originalTime, long time, final AVObject avObject) {
        calendar.setTimeInMillis(time);
        showDatePicker(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                presenter.updateMomentCreateTime(originalTime, calendar.getTimeInMillis(), avObject);
            }
        });
    }

    @Override
    public void showLoveDayPickerDialog(final long originalTime, long timeInMillis, final AVObject avCircle) {
        calendar.setTimeInMillis(timeInMillis);
        showDatePicker(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.HOUR, 0);
                calendar.set(Calendar.HOUR_OF_DAY, 0);

                presenter.updateLoveDay(originalTime, calendar.getTimeInMillis(), avCircle);
            }
        });
    }

    @Override
    public void updateHeaderView(AVObject avCircle) {
        momentListAdapter.setAvCircle(avCircle);
    }

    @Override
    public void showUpdateCoverRetryDialog(final AVObject avCircle, final String path) {
        showDialog("失败", "上传失败,是否重试", getStringById(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.updateCircleCover(avCircle, path);
            }
        }, getStringById(R.string.cancel), null);
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerMomentListComponent.builder()
                .appComponent(appComponent)
                .momentListModule(new MomentListModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return srl;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return rv;
    }

    @Override
    public BaseRecyclerAdapter<AVObject> getBaseRecyclerAdapter() {
        return momentListAdapter;
    }

    @Override
    public View getLoadingViewContainer() {
        return pbLoading;
    }

    @Override
    protected View getEmptyView() {
        return vEmpty;
    }

    @Override
    public void goToEditPage() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).openPage(this, MomentEditFragment.newInstance(0, null), true);
        }
    }

    @Override
    public void goToShowLocationPage(AVObject avObject) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).openPage(this, AddressFragment.newInstance(avObject), true);
        }
    }

    @Override
    public void goToEditPage(int position, AVObject avObject) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).openPage(this, MomentEditFragment.newInstance(position, avObject), true);
        }
    }

    @Override
    public void showDeleteImageDialog(final int position, final AVObject avObject,
                                      final int imagePosition) {
        showDialog(getString(R.string.delete), getString(R.string.tips_delete_image), getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.deleteImage(position, avObject, imagePosition);
            }
        }, getString(R.string.cancel), null);
    }

    @Override
    public void onLoveDayLongClick(final AVObject avCircle) {
        final long originalTime = avCircle.getLong(CircleContract.LOVE_DAY);
        showLoveDayPickerDialog(originalTime, originalTime, avCircle);

    }

    @Override
    public void onCoverLongClick(AVObject avCircle) {
        ImagePickerActivity.startImagePicker(this, 1, null);
    }

    @Override
    public void onDateLongClick(int position, AVObject avObject) {
        long time = avObject.getLong(MomentContract.CREATEAD_TIME);
        showMomentDatePickerDialog(time, time, avObject);
    }

    @Override
    public void onContentLongClick(int position, AVObject avObject) {
        goToEditPage(position, avObject);

    }

    @Override
    public void onThumbnailClick(int position, AVObject avObject, int imagePosition) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).openPage(this, GalleryFragment.newInstance(avObject, imagePosition), true);
        }
    }

    @Override
    public void onThumbnailLongClick(int position, AVObject avObject, int imagePosition) {
        showDeleteImageDialog(position, avObject, imagePosition);

    }

    @Override
    public void onAddressClick(int position, AVObject avObject) {
        goToShowLocationPage(avObject);
    }

    /**
     * Called when the scroll change events occurred.
     * This won't be called just after the view is laid out, so if you'd like to
     * initialize the position of your views with this method, you should call this manually
     * or invoke scroll as appropriate.
     *
     * @param scrollY     scroll position in Y axis
     * @param firstScroll true when this is called for the first time in the consecutive motion events
     * @param dragging    true when the view is dragged and false when the view is scrolled in the inertia
     */
    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        View chidView = rv.getChildAt(0);
        if (chidView != null) {
            Object tag = chidView.getTag(R.id.tag_type);
            if (tag != null && (int) tag == MomentListAdapter.VIEW_TYPE_HEADER) {
                int headerHeight = chidView.getHeight() - ScreenUtil.toolbarHeight - ScreenUtil.statusBarHeight;
                if (scrollY <= headerHeight) {
                    int deltaY = -scrollY / 2;
                    View vCover = chidView.findViewById(R.id.iv_cover);
                    if (vCover != null) {
                        vCover.setTranslationY(-deltaY);
                    }
                    appBar.setAlpha(((float) scrollY) / headerHeight);
                } else {
                    if (appBar.getAlpha() < 1) {
                        appBar.setAlpha(1);
                    }
                    View vCover = chidView.findViewById(R.id.iv_cover);
                    if (vCover != null) {
                        vCover.setTranslationY(0);
                    }
                }
            } else {
                if (appBar.getAlpha() < 1) {
                    appBar.setAlpha(1);
                }
            }
        }

    }

    /**
     * Called when the down motion event occurred.
     */
    @Override
    public void onDownMotionEvent() {
//        LogUtil.e("onDownMotionEvent");
    }

    /**
     * Called when the dragging ended or canceled.
     *
     * @param scrollState state to indicate the scroll direction
     */
    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        if (scrollState == ScrollState.DOWN) {
            if (fabEdit.getVisibility() == View.GONE) {
                fabEdit.show();
                AnimatorUtil.translationToCorrect(getActivity().findViewById(R.id.bottom_container));

            }
        } else if (scrollState == ScrollState.UP) {
            if (fabEdit.getVisibility() == View.VISIBLE) {
                fabEdit.hide();
                AnimatorUtil.translationToHideBottomBar(getActivity().findViewById(R.id.bottom_container));
            }
        }
    }

    @Override
    public void onRefreshStart() {
        super.onRefreshStart();
        presenter.loadCircle();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ImagePickerActivity.REQUEST_PICKER_IMAGE:
                    List<ImageEntity> imageEntities = ImagePickerActivity.handleSelectedImageResult(resultCode, data);
                    if (imageEntities == null || imageEntities.size() == 0) {
                        return;
                    }
                    cropOutUri = Uri.fromFile(FileUtil.createTempInternalImageFile(getActivity(), presenter.getCurrentUser().getObjectId() + System.currentTimeMillis() + ".jpg"));
                    Crop.of(Uri.fromFile(new File(imageEntities.get(0).getData())), cropOutUri).withAspect(16, 9).start(getActivity(), this);
                    break;
                case Crop.REQUEST_CROP:
                    presenter.updateCircleCover(momentListAdapter.getAvCircle(), cropOutUri.getPath());
                    break;
            }
        }
    }

    @Override
    public MomentListDiffCallback getMomentListDiffCallback() {
        return new MomentListDiffCallback();
    }
}