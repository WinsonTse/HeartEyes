package io.github.winsontse.hearteyes.page.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.avos.avoscloud.AVObject;

import io.github.winsontse.hearteyes.page.adapter.base.BaseRecyclerAdapter;
import io.github.winsontse.hearteyes.page.adapter.viewholder.MomentHeaderViewHolder;
import io.github.winsontse.hearteyes.page.adapter.viewholder.MomentItemViewHolder;

/**
 * Created by winson on 16/6/25.
 */
public class MomentListAdapter extends BaseRecyclerAdapter<AVObject> {
    public static final int TAG_HEADER_VISIBLE = 1;
    public static final int TAG_HEADER_INVISIBLE = 2;
    public static final int TAG_HEADER_FIRST_POSITION = 3;

    public static final int VIEW_TYPE_HEADER = 101;
    public static final int VIEW_TYPE_ITEM = 102;

    private OnMomentClickListener onMomentClickListener;
    private AVObject avCircle;

    public AVObject getAvCircle() {
        return avCircle;
    }

    public void setAvCircle(AVObject avCircle) {
        this.avCircle = avCircle;
        notifyItemChanged(0);
    }

    public void setOnMomentClickListener(OnMomentClickListener onMomentClickListener) {
        this.onMomentClickListener = onMomentClickListener;
    }

    public MomentListAdapter() {
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return new MomentHeaderViewHolder(parent, onMomentClickListener);
            case VIEW_TYPE_ITEM:
            default:
                MomentItemViewHolder itemViewHolder = new MomentItemViewHolder(data, parent);
                itemViewHolder.setOnMomentClickListener(onMomentClickListener);
                itemViewHolder.setHeaderCount(getHeaderCount());
                return itemViewHolder;
        }

    }

    @Override
    public int getHeaderCount() {
        return 1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position != 0) {
            ((MomentItemViewHolder) (holder)).bind(data.get(position - getHeaderCount()));
        } else {
            ((MomentHeaderViewHolder) (holder)).bind(avCircle);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return VIEW_TYPE_HEADER;
            default:
                return VIEW_TYPE_ITEM;
        }
    }

    public interface OnMomentClickListener {
        void onLoveDayLongClick(AVObject avCircle);

        void onCoverLongClick(AVObject avCircle);

        void onDateLongClick(int position, AVObject avObject);

        void onContentLongClick(int position, AVObject avObject);

        void onThumbnailClick(int position, AVObject avObject, int imagePosition);

        void onThumbnailLongClick(int position, AVObject avObject, int imagePosition);

        void onAddressClick(int position, AVObject avObject);

    }
}
