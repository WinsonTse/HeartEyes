package io.github.winsontse.hearteyes.page.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.List;

import butterknife.BindView;
import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.data.model.leancloud.MomentContract;
import io.github.winsontse.hearteyes.data.model.leancloud.UserContract;
import io.github.winsontse.hearteyes.page.adapter.base.BaseRecyclerAdapter;
import io.github.winsontse.hearteyes.page.adapter.base.BaseViewHolder;
import io.github.winsontse.hearteyes.util.ImageLoader;
import io.github.winsontse.hearteyes.util.TimeUtil;
import io.github.winsontse.hearteyes.widget.CircleImageView;
import io.github.winsontse.hearteyes.widget.MoreTextLayout;

/**
 * Created by winson on 16/6/25.
 */
public class MomentListAdapter extends BaseRecyclerAdapter<AVObject> {
    public static final int TAG_HEADER_VISIBLE = 1;
    public static final int TAG_HEADER_INVISIBLE = 2;
    public static final int TAG_HEADER_FIRST_POSITION = 3;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(data, onItemClickListener, parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder) (holder)).bind(data.get(position));
    }

    static final class ItemViewHolder extends BaseViewHolder<AVObject> {

        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_content)
        MoreTextLayout tvContent;
        @BindView(R.id.iv_avatar)
        CircleImageView ivAvatar;
        @BindView(R.id.rv_image)
        RecyclerView rvImage;
        @BindView(R.id.ll_time)
        LinearLayout llTime;
        @BindView(R.id.divider)
        View divider;
        @BindView(R.id.ll_content)
        LinearLayout llContent;
        @BindView(R.id.tv_week)
        TextView tvWeek;

        private ThumbnailListAdapter thumbnailListAdapter;

        public ItemViewHolder(List<AVObject> data, OnItemClickListener onItemClickListener, ViewGroup parent) {
            super(data, onItemClickListener, parent, R.layout.list_item_moment);
            thumbnailListAdapter = new ThumbnailListAdapter();
            rvImage.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
            rvImage.setAdapter(thumbnailListAdapter);
            final RequestManager requestManager = Glide.with(context);
            rvImage.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        requestManager.resumeRequests();
                    } else {
                        requestManager.pauseRequests();
                    }
                }
            });
        }

        @Override
        public void bind(AVObject avObject) {
            int currentPos = getAdapterPosition();
            int lastPos = currentPos - 1;
            String content = avObject.getString(MomentContract.CONTENT);
            tvContent.setText(content);
            String avatar = avObject.getAVUser(MomentContract.AUTHOR).getAVFile(UserContract.AVATAR).getUrl();
            ImageLoader.getInstance().displayAvatar(context, avatar, ivAvatar);

            int lastDay = 0;
            if (lastPos >= 0) {
                lastDay = TimeUtil.getDay(data.get(lastPos).getLong(MomentContract.CREATEAD_TIME));
            }
            long currentTime = avObject.getLong(MomentContract.CREATEAD_TIME);
            int currentDay = TimeUtil.getDay(currentTime);
            if ((lastDay == currentDay)) {
                llTime.setVisibility(View.INVISIBLE);
                itemView.setTag(R.id.tag_type, TAG_HEADER_INVISIBLE);
            } else {
                llTime.setVisibility(View.VISIBLE);
                itemView.setTag(R.id.tag_type, TAG_HEADER_VISIBLE);
            }
            if (currentPos == 0) {
                itemView.setTag(R.id.tag_type, TAG_HEADER_VISIBLE);
            }
            tvDate.setText(String.valueOf(currentDay));
            tvWeek.setText(TimeUtil.getWeek(currentTime));

            List list = avObject.getList(MomentContract.IMAGES);
            if (list == null || list.size() == 0) {
                rvImage.setVisibility(View.GONE);
                thumbnailListAdapter.clearItems();
            } else {
                rvImage.setVisibility(View.VISIBLE);
                List<AVFile> avFiles = list;
                thumbnailListAdapter.setItems(avFiles);
            }
        }
    }
}
