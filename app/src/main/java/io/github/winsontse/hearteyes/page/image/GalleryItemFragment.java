package io.github.winsontse.hearteyes.page.image;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.data.model.ImageEntity;
import io.github.winsontse.hearteyes.util.LogUtil;
import io.github.winsontse.hearteyes.util.constant.Extra;
import io.github.winsontse.hearteyes.widget.photoview.PhotoView;
import io.github.winsontse.hearteyes.widget.photoview.PhotoViewAttacher;

public class GalleryItemFragment extends Fragment {


    @BindView(R.id.iv)
    PhotoView iv;
    @BindView(R.id.iv_thumb)
    ImageView ivThumb;
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.bn_retry)
    Button bnRetry;

    private ImageEntity image;
    private Target<GlideDrawable> into;

    public GalleryItemFragment() {
    }

    public static GalleryItemFragment newInstance(ImageEntity image) {
        GalleryItemFragment fragment = new GalleryItemFragment();
        Bundle args = new Bundle();
        args.putParcelable(Extra.IMAGE, image);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        image = getArguments().getParcelable(Extra.IMAGE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery_item, container, false);
        ButterKnife.bind(this, view);
        initItemView();
        return view;
    }


    private void initItemView() {

        bnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImage();
            }
        });

        iv.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View v, float x, float y) {
                closePage();
            }

            @Override
            public void onOutsidePhotoTap() {
                closePage();
            }
        });

        loadImage();
    }

    private void closePage() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void loadImage() {
        bnRetry.setVisibility(View.GONE);
        ivThumb.setVisibility(View.VISIBLE);
        pb.setVisibility(View.VISIBLE);
        String url = image.getData();
        into = Glide.with(getActivity()).load(url).placeholder(R.color.md_black).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                LogUtil.e("大图加载出错:" + e.getMessage());
                ivThumb.setVisibility(View.GONE);
                pb.setVisibility(View.GONE);
                bnRetry.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                ivThumb.setVisibility(View.GONE);
                pb.setVisibility(View.GONE);
                return false;

            }
        }).into(iv);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && iv != null) {
            iv.setScale(1, 0, 0, true);
        }
    }
}
