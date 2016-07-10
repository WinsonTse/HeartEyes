package io.github.winsontse.hearteyes.page.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.avos.avoscloud.AVFile;

import java.util.List;

import butterknife.BindView;
import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.page.adapter.base.BaseRecyclerAdapter;
import io.github.winsontse.hearteyes.page.adapter.base.BaseViewHolder;
import io.github.winsontse.hearteyes.util.ImageLoader;
import io.github.winsontse.hearteyes.util.UIUtil;

/**
 * Created by winson on 16/6/28.
 */

public class ThumbnailListAdapter extends BaseRecyclerAdapter<AVFile> {


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(data, onItemClickListener, onItemLongClickListener, parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder) (holder)).bind(data.get(position));
    }


    static class ItemViewHolder extends BaseViewHolder<AVFile> {
        private int width;
        @BindView(R.id.iv)
        ImageView iv;

        public ItemViewHolder(List<AVFile> data, OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener, ViewGroup parent) {
            super(data, onItemClickListener, onItemLongClickListener, parent, R.layout.list_item_thumbnail);
            width = UIUtil.dpToPx(context, 100);
        }

        @Override
        public void bind(AVFile avFile) {
//            ImageLoader.getInstance().displayImage(context, avFile.getUrl(), iv);
            ImageLoader.getInstance().displayImage(context, avFile.getThumbnailUrl(true, width, width), iv);
        }
    }
}
