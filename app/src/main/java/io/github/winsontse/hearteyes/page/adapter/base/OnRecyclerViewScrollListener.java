package io.github.winsontse.hearteyes.page.adapter.base;

import android.support.v7.widget.RecyclerView;

/**
 * Created by winson on 16/6/29.
 */

public interface OnRecyclerViewScrollListener {

     void onScrollStateChanged(RecyclerView recyclerView, int newState);

     void onScrolled(RecyclerView recyclerView, int dx, int dy);
}
