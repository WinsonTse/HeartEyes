package io.github.winsontse.hearteyes.page.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by winson on 16/6/20.
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    protected Context context;
    protected List<T> data;
    protected BaseRecyclerAdapter.OnItemClickListener onItemClickListener;
    protected BaseRecyclerAdapter.OnItemLongClickListener onItemLongClickListener;

    public BaseViewHolder(List<T> data, ViewGroup parent, int layoutId) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
        ButterKnife.bind(this, itemView);
        this.data = data;
        context = itemView.getContext();
    }

    public BaseViewHolder(List<T> data, BaseRecyclerAdapter.OnItemClickListener onItemClickListener, ViewGroup parent, int layoutId) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
        ButterKnife.bind(this, itemView);
        this.data = data;
        this.onItemClickListener = onItemClickListener;
        context = itemView.getContext();
        initListener();
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
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(getAdapterPosition());
                }
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(getAdapterPosition());
                    return true;
                }
                return false;
            }
        });
    }

}
