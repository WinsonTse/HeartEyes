package io.github.winsontse.hearteyes.page.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

    public BaseViewHolder(List<T> data, BaseRecyclerAdapter.OnItemClickListener onItemClickListener, ViewGroup parent, int layoutId) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
        ButterKnife.bind(this, itemView);
        this.data = data;
        this.onItemClickListener = onItemClickListener;
        context = itemView.getContext();
    }

    public abstract void bind(T t);


}
