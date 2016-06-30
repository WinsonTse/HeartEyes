package io.github.winsontse.hearteyes.page.adapter.base;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by winson on 16/6/20.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter {
    protected OnItemClickListener onItemClickListener;
    protected List<T> data = new ArrayList<>();

    public BaseRecyclerAdapter() {
    }

    public List<T> getData() {
        return data;
    }

    public void addItems(List<T> items) {
        if (items == null) {
            return;
        }
        int size = data.size();
        data.addAll(items);
        notifyItemRangeInserted(size, items.size());
    }

    public void setItems(List<T> items) {
        if (items == null) {
            return;
        }
        data.clear();
        data.addAll(items);
        notifyDataSetChanged();
    }

    protected int getHeaderCount() {
        return 0;
    }

    @Override
    public int getItemCount() {
        return data.size() + getHeaderCount();
    }

    public void clearItems() {
        data.clear();
        notifyDataSetChanged();
    }


    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
