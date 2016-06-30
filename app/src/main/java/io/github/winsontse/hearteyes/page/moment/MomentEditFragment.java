package io.github.winsontse.hearteyes.page.moment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.data.model.ImageEntity;
import io.github.winsontse.hearteyes.page.adapter.SelectedImagesAdapter;
import io.github.winsontse.hearteyes.page.base.BaseFragment;
import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.page.image.ImagePickerActivity;
import io.github.winsontse.hearteyes.page.moment.component.DaggerMomentEditComponent;
import io.github.winsontse.hearteyes.page.moment.contract.MomentEditContract;
import io.github.winsontse.hearteyes.page.moment.module.MomentEditModule;
import io.github.winsontse.hearteyes.page.moment.presenter.MomentEditPresenter;
import io.github.winsontse.hearteyes.util.AnimatorUtil;

public class MomentEditFragment extends BaseFragment implements MomentEditContract.View {

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

    public static final int MAX_IMAGES_COUNT = 30;
    private SelectedImagesAdapter selectedImagesAdapter;

    public static MomentEditFragment newInstance() {
        Bundle args = new Bundle();
        MomentEditFragment fragment = new MomentEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_moment_edit, container, false);
        ButterKnife.bind(this, rootView);

        toolbar.setTitle(R.string.create_moment);
        initRecyclerView();
        bindListener();
        AnimatorUtil.translationToCorrect(llBottom).setStartDelay(AnimatorUtil.ANIMATOR_TIME).start();
        showKeyboard(etContent);
        return rootView;
    }

    private void bindListener() {
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = etContent.getText().toString().trim().length();
                if (length > 0) {
                    if (fabSend.getVisibility() == View.GONE) {
                        fabSend.show();
                    }
                } else {
                    if (fabSend.getVisibility() == View.VISIBLE) {
                        fabSend.hide();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabSend.hide();
                presenter.publishMoment(etContent.getText().toString().trim(), selectedImagesAdapter.getData());

            }
        });

        tvImageCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePickerActivity.startImagePicker(MomentEditFragment.this, MAX_IMAGES_COUNT, selectedImagesAdapter.getData());
            }
        });

        tvRecordCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("还没做出来这个功能");
            }
        });

    }

    private void initRecyclerView() {
        selectedImagesAdapter = new SelectedImagesAdapter();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
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
    protected BasePresenter getPresenter() {
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
    }

    @Override
    public void showFab() {
        fabSend.show();
    }
}