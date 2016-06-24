package io.github.winsontse.hearteyes.page.moment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.page.base.BaseFragment;
import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.page.moment.component.DaggerMomentListComponent;
import io.github.winsontse.hearteyes.page.moment.contract.MomentListContract;
import io.github.winsontse.hearteyes.page.moment.module.MomentListModule;
import io.github.winsontse.hearteyes.page.moment.presenter.MomentListPresenter;
import io.github.winsontse.hearteyes.util.AnimatorUtil;

public class MomentListFragment extends BaseFragment implements MomentListContract.View, FragmentManager.OnBackStackChangedListener {

    @Inject
    MomentListPresenter presenter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.fab_edit)
    FloatingActionButton fabEdit;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;

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
//        AnimatorUtil.translationToCorrect(fabEdit);
        AnimatorUtil.translationToCorrect(appBar).start();
        fabEdit.postDelayed(new Runnable() {
            @Override
            public void run() {
                fabEdit.show();
            }
        }, AnimatorUtil.ANIMATOR_TIME);
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPage(MomentEditFragment.newInstance());
            }
        });

        getFragmentManager().addOnBackStackChangedListener(this);
        return rootView;
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

    }
}