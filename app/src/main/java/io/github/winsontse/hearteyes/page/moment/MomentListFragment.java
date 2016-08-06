package io.github.winsontse.hearteyes.page.moment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.percent.PercentFrameLayout;
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

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;
import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.data.model.leancloud.MomentContract;
import io.github.winsontse.hearteyes.page.adapter.MomentListAdapter;
import io.github.winsontse.hearteyes.page.adapter.base.BaseRecyclerAdapter;
import io.github.winsontse.hearteyes.page.adapter.base.OnRecyclerViewScrollListener;
import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.page.base.TimelineFragment;
import io.github.winsontse.hearteyes.page.image.GalleryFragment;
import io.github.winsontse.hearteyes.page.map.AddressFragment;
import io.github.winsontse.hearteyes.page.moment.component.DaggerMomentListComponent;
import io.github.winsontse.hearteyes.page.moment.contract.MomentListContract;
import io.github.winsontse.hearteyes.page.moment.module.MomentListModule;
import io.github.winsontse.hearteyes.page.moment.presenter.MomentListPresenter;
import io.github.winsontse.hearteyes.util.AnimatorUtil;
import io.github.winsontse.hearteyes.util.LogUtil;
import io.github.winsontse.hearteyes.util.ScreenUtil;
import io.github.winsontse.hearteyes.util.TimeUtil;
import io.github.winsontse.hearteyes.util.UIUtil;

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
    @BindView(R.id.header)
    PercentFrameLayout header;
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
            setStatusBarViewVisible(false);
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
                AVObject currentAvObject = (AVObject) recyclerView.findChildViewUnder(llTime.getMeasuredWidth(), llTimeOffsize).getTag(R.id.tag_data);
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

                    showDatePickerDialog(time, time, avObject);
                }
                return true;
            }
        });
    }

    @Override
    public void showDatePickerDialog(final long originalTime, long time, final AVObject avObject) {
        final Calendar calendar = TimeUtil.getCalendar();
        calendar.setTimeInMillis(time);

        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                presenter.updateCreateTime(originalTime, calendar.getTimeInMillis(), avObject);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
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
        openPage(this, MomentEditFragment.newInstance(0, null));
    }

    @Override
    public void goToShowLocationPage(AVObject avObject) {
        openPage(this, AddressFragment.newInstance(avObject));
    }

    @Override
    public void goToEditPage(int position, AVObject avObject) {
        openPage(this, MomentEditFragment.newInstance(position, avObject));
    }

    @Override
    public void showDeleteImageDialog(final int position, final AVObject avObject, final int imagePosition) {
        showDialog(getString(R.string.delete), getString(R.string.tips_delete_image), getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.deleteImage(position, avObject, imagePosition);
            }
        }, getString(R.string.cancel), null);
    }

    @Override
    public void onDateLongClick(int position, AVObject avObject) {
        long time = avObject.getLong(MomentContract.CREATEAD_TIME);
        showDatePickerDialog(time, time, avObject);
    }

    @Override
    public void onContentLongClick(int position, AVObject avObject) {
        goToEditPage(position, avObject);

    }

    @Override
    public void onThumbnailClick(int position, AVObject avObject, int imagePosition) {
        openPage(this, GalleryFragment.newInstance(avObject, imagePosition));
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
        LogUtil.e("onScrollChanged===> " + " scrollY:" + scrollY + " firstScroll:" + firstScroll + " dragging" + dragging);
        int headerHeight = header.getHeight();
        if (scrollY <= headerHeight - ScreenUtil.toolbarHeight - ScreenUtil.statusBarHeight) {
            int deltaY = -scrollY / 2;
            header.setTranslationY(deltaY);
            appBar.setAlpha(((float) scrollY) / headerHeight);
        } else {
            if (appBar.getAlpha() < 1) {
                appBar.setAlpha(1);
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
//        LogUtil.e("onUpOrCancelMotionEvent===> " + scrollState);
        if (scrollState == ScrollState.DOWN) {
            if (fabEdit.getVisibility() == View.GONE) {
                fabEdit.show();
                AnimatorUtil.translationToCorrect(getActivity().findViewById(R.id.bottom_bar));

            }
        } else if (scrollState == ScrollState.UP) {
            if (fabEdit.getVisibility() == View.VISIBLE) {
                fabEdit.hide();
                AnimatorUtil.translationToHideBottomBar(getActivity().findViewById(R.id.bottom_bar));
            }
        }
    }
}