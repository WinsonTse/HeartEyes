package io.github.winsontse.hearteyes.page.moment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;

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
import io.github.winsontse.hearteyes.util.TimeUtil;
import io.github.winsontse.hearteyes.util.UIUtil;

public class MomentListFragment extends TimelineFragment<AVObject> implements MomentListContract.View, MomentListAdapter.OnMomentClickListener {

    @Inject
    MomentListPresenter presenter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.fab_edit)
    FloatingActionButton fabEdit;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
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
    @BindView(R.id.cl)
    CoordinatorLayout cl;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R.id.ctl)
    CollapsingToolbarLayout ctl;

    private LinearLayoutManager layoutManager;
    private MomentListAdapter momentListAdapter;
    private long time;
    private Calendar calendar;

    public static MomentListFragment newInstance() {
        return new MomentListFragment();
    }

    @Override
    public void initView(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AnimatorUtil.translationToCorrect(appBar).start();
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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            fabEdit.show();
        }
    }

    private void initRecyclerView() {
        momentListAdapter = new MomentListAdapter();
        rv.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(momentListAdapter);
        addOnRecyclerViewScrollListener(new OnRecyclerViewScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int currentPos = layoutManager.findFirstVisibleItemPosition() - momentListAdapter.getHeaderCount();
                if (currentPos < 0) {
                    llTime.setVisibility(View.INVISIBLE);
                    appBar.setElevation(0);
                    return;
                }
                appBar.setElevation(UIUtil.dpToPx(recyclerView.getContext(), 8));

                AVObject currentAvObject = momentListAdapter.getData().get(currentPos);
                time = currentAvObject.getLong(MomentContract.CREATEAD_TIME);
                calendar.setTimeInMillis(time);
                tvDate.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
                llTime.setVisibility((layoutManager.findFirstCompletelyVisibleItemPosition() == 0) ? View.INVISIBLE : View.VISIBLE);
                tvWeek.setText(TimeUtil.getWeek(time));

                llTime.setTag(R.id.tag_position, currentPos);
                llTime.setTag(R.id.tag_data, currentAvObject);

                toolbar.setTitle(calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月");

                View itemView = recyclerView.findChildViewUnder(llTime.getMeasuredWidth(), llTime.getMeasuredHeight());
                if (itemView != null) {
                    int tag = (int) itemView.getTag(R.id.tag_type);
                    int deltaY = itemView.getTop() - llTime.getMeasuredHeight();
                    if (tag == MomentListAdapter.TAG_HEADER_VISIBLE) {
                        if (itemView.getTop() > 0) {
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

                    showDatePickerDialog(pos, time, time, avObject);
                }
                return true;
            }
        });
    }

    @Override
    public void showDatePickerDialog(final int position, final long originalTime, long time, final AVObject avObject) {
        final Calendar calendar = TimeUtil.getCalendar();
        calendar.setTimeInMillis(time);

        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                presenter.updateCreateTime(position, originalTime, calendar.getTimeInMillis(), avObject);
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
        showDatePickerDialog(position, time, time, avObject);
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
}