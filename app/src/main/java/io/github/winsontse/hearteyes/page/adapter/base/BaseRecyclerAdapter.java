package io.github.winsontse.hearteyes.page.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by winson on 16/6/20.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter {
    protected OnItemClickListener onItemClickListener;
    protected OnItemLongClickListener onItemLongClickListener;
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

    public int getHeaderCount() {
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

    public void replaceItem(int position, T t) {
        data.remove(position);
        data.add(position, t);
        notifyItemChanged(position);
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
