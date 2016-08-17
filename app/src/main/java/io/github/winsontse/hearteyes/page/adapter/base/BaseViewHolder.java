package io.github.winsontse.hearteyes.page.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;
import io.github.winsontse.hearteyes.util.LogUtil;

/**
 * Created by winson on 16/6/20.
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    protected Context context;
    protected List<T> data;
    private BaseRecyclerAdapter.OnItemClickListener onItemClickListener;
    private BaseRecyclerAdapter.OnItemLongClickListener onItemLongClickListener;
    private int headerCount;

    public void setHeaderCount(int headerCount) {
        this.headerCount = headerCount;
    }

    public int getHeaderCount() {
        return headerCount;
    }

    protected int getCorrectPosition() {
        return getAdapterPosition() - headerCount;
    }

    public BaseViewHolder(ViewGroup parent, int layoutId) {
        this(null, null, null, parent, layoutId);
    }

    public BaseViewHolder(List<T> data, ViewGroup parent, int layoutId) {
        this(data, null, null, parent, layoutId);
    }

    public BaseViewHolder(List<T> data, BaseRecyclerAdapter.OnItemClickListener onItemClickListener, ViewGroup parent, int layoutId) {
        this(data, onItemClickListener, null, parent, layoutId);
    }

    public BaseViewHolder(List<T> data, BaseRecyclerAdapter.OnItemClickListener onItemClickListener, BaseRecyclerAdapter.OnItemLongClickListener onItemLongClickListener, ViewGroup parent, int layoutId) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
        ButterKnife.bind(this, itemView);
        this.data = data;
        this.onItemClickListener = onItemClickListener;
        this.onItemLongClickListener = onItemLongClickListener;
        context = itemView.getContext();
        initListener();
    }

    public abstract void bind(T t);

    private void initListener() {
        if (onItemClickListener != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }

        if (onItemLongClickListener != null) {
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemLongClickListener.onItemLongClick(getAdapterPosition());
                    return true;
                }
            });
        }

    }

}
