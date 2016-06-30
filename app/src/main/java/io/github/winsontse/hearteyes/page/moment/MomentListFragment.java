package io.github.winsontse.hearteyes.page.moment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.data.model.leancloud.MomentContract;
import io.github.winsontse.hearteyes.page.adapter.MomentListAdapter;
import io.github.winsontse.hearteyes.page.adapter.base.BaseRecyclerAdapter;
import io.github.winsontse.hearteyes.page.adapter.base.OnRecyclerViewScrollListener;
import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.page.base.TimelineFragment;
import io.github.winsontse.hearteyes.page.moment.component.DaggerMomentListComponent;
import io.github.winsontse.hearteyes.page.moment.contract.MomentListContract;
import io.github.winsontse.hearteyes.page.moment.module.MomentListModule;
import io.github.winsontse.hearteyes.page.moment.presenter.MomentListPresenter;
import io.github.winsontse.hearteyes.util.AnimatorUtil;
import io.github.winsontse.hearteyes.util.TimeUtil;

public class MomentListFragment extends TimelineFragment<AVObject> implements MomentListContract.View, FragmentManager.OnBackStackChangedListener {

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

    private LinearLayoutManager layoutManager;
    private MomentListAdapter momentListAdapter;
    private long time;
    private Calendar calendar;

    public static MomentListFragment newInstance() {
        Bundle args = new Bundle();
        MomentListFragment fragment = new MomentListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_moment_list, container, false);
        ButterKnife.bind(this, rootView);
        AnimatorUtil.translationToCorrect(appBar).start();
        calendar = TimeUtil.getCalendar();
        fabEdit.postDelayed(new Runnable() {
            @Override
            public void run() {
                fabEdit.show();
            }
        }, AnimatorUtil.ANIMATOR_TIME);
        bindListener();
        getFragmentManager().addOnBackStackChangedListener(this);

        initRecyclerView();
        return rootView;
    }

    private void bindListener() {
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabEdit.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                    @Override
                    public void onHidden(FloatingActionButton fab) {
                        super.onHidden(fab);
                        openPage(MomentEditFragment.newInstance());
                    }
                });
            }
        });
    }

    @Override
    protected boolean isSupportBackNavigation() {
        return false;
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
                int currentPos = layoutManager.findFirstVisibleItemPosition();
                time = momentListAdapter.getData().get(currentPos).getLong(MomentContract.CREATEAD_TIME);
                calendar.setTimeInMillis(time);
                tvDate.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
                llTime.setVisibility((layoutManager.findFirstCompletelyVisibleItemPosition() == 0) ? View.INVISIBLE : View.VISIBLE);
                tvWeek.setText(TimeUtil.getWeek(time));

                int lastPos = layoutManager.findLastCompletelyVisibleItemPosition();
                if (lastPos == RecyclerView.NO_POSITION) {
                    lastPos = layoutManager.findLastVisibleItemPosition();
                }
                time = momentListAdapter.getData().get(lastPos).getLong(MomentContract.CREATEAD_TIME);
                calendar.setTimeInMillis(time);
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

    /**
     * Called whenever the contents of the back stack change.
     */
    @Override
    public void onBackStackChanged() {
        if (getFragmentManager().getBackStackEntryCount() == 0 && fabEdit != null && !fabEdit.isShown()) {
            fabEdit.show();
        }
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
}