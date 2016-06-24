package io.github.winsontse.hearteyes.page.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.data.model.ImageEntity;
import io.github.winsontse.hearteyes.page.adapter.base.BaseRecyclerAdapter;
import io.github.winsontse.hearteyes.page.adapter.base.BaseViewHolder;
import io.github.winsontse.hearteyes.util.ImageLoader;

/**
 * Created by winson on 16/6/23.
 */
public class SelectedImagesAdapter extends BaseRecyclerAdapter<ImageEntity> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(data, onItemClickListener, parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((ItemViewHolder) holder).bind(data.get(position));
    }

    static final class ItemViewHolder extends BaseViewHolder<ImageEntity> {

        @BindView(R.id.iv)
        ImageView iv;

        public ItemViewHolder(List<ImageEntity> data, OnItemClickListener onItemClickListener, ViewGroup parent) {
            super(data, onItemClickListener, parent, R.layout.list_item_selected_image);
        }

        @Override
        public void bind(ImageEntity entity) {
            ImageLoader.getInstance().displayImage(context, new File(entity.getData()), iv);
        }
    }
}
