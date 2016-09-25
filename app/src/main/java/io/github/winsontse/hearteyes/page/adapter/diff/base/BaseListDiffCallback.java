package io.github.winsontse.hearteyes.page.adapter.diff.base;

import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * Created by winson on 16/9/25.
 */

public abstract class BaseListDiffCallback<T> extends DiffUtil.Callback {
    protected List<T> oldData;
    protected List<T> newData;

    public BaseListDiffCallback newData(List<T> data) {
        this.newData = data;
        return this;
    }

    public BaseListDiffCallback oldData(List<T> data) {
        this.oldData = data;
        return this;
    }

}
