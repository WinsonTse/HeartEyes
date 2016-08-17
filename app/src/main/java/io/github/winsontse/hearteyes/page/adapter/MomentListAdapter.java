package io.github.winsontse.hearteyes.page.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.List;

import butterknife.BindView;
import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.data.model.leancloud.CircleContract;
import io.github.winsontse.hearteyes.data.model.leancloud.MomentContract;
import io.github.winsontse.hearteyes.data.model.leancloud.UserContract;
import io.github.winsontse.hearteyes.page.adapter.base.BaseRecyclerAdapter;
import io.github.winsontse.hearteyes.page.adapter.base.BaseViewHolder;
import io.github.winsontse.hearteyes.util.ImageLoader;
import io.github.winsontse.hearteyes.util.TimeUtil;
import io.github.winsontse.hearteyes.widget.CircleImageView;
import io.github.winsontse.hearteyes.widget.MoreTextView;

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
                return new HeaderViewHolder(parent, onMomentClickListener);
            case VIEW_TYPE_ITEM:
            default:
                ItemViewHolder itemViewHolder = new ItemViewHolder(data, parent);
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
            ((ItemViewHolder) (holder)).bind(data.get(position - getHeaderCount()));
        } else {
            ((HeaderViewHolder) (holder)).bind(avCircle);
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

    static final class HeaderViewHolder extends BaseViewHolder<AVObject> {

        @BindView(R.id.iv_cover)
        ImageView ivCover;
        @BindView(R.id.tv_love_day)
        TextView tvLoveDay;
        private OnMomentClickListener onMomentClickListener;
        private AVObject avCircle;

        public HeaderViewHolder(ViewGroup parent, final OnMomentClickListener onMomentClickListener) {
            super(parent, R.layout.list_header_moment);
            itemView.setTag(R.id.tag_type, VIEW_TYPE_HEADER);
            this.onMomentClickListener = onMomentClickListener;

            ivCover.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onMomentClickListener != null) {
                        onMomentClickListener.onCoverLongClick(avCircle);
                    }
                    return true;
                }
            });

            tvLoveDay.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onMomentClickListener != null && avCircle != null) {
                        onMomentClickListener.onLoveDayLongClick(avCircle);
                    }
                    return true;
                }
            });

        }

        @Override
        public void bind(AVObject avCircle) {
            this.avCircle = avCircle;
            if (avCircle != null) {
                long loveDay = avCircle.getLong(CircleContract.LOVE_DAY);
                if (loveDay == 0) {
                    tvLoveDay.setText(R.string.tips_set_love_day);
                } else {
                    tvLoveDay.setText(TimeUtil.getLoveDay(loveDay));
                }
                AVFile avFile = avCircle.getAVFile(CircleContract.COVER);
                if (avFile != null) {
                    ImageLoader.getInstance().displayImage(context, avFile.getUrl(), R.color.colorPrimary, ivCover);
                }
            }
            ivCover.setTranslationY(0);
        }
    }

    static final class ItemViewHolder extends BaseViewHolder<AVObject> {

        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_content)
        MoreTextView tvContent;
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
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.ll_address)
        LinearLayout llAddress;

        private ThumbnailListAdapter thumbnailListAdapter;
        private OnMomentClickListener onMomentClickListener;

        public ItemViewHolder(final List<AVObject> data, ViewGroup parent) {
            super(data, parent, R.layout.list_item_moment);
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
            tvContent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    if (onMomentClickListener != null) {
                        int position = getCorrectPosition();
                        onMomentClickListener.onContentLongClick(position, data.get(position));
                    }
                    return true;
                }
            });
            thumbnailListAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
                @Override
                public void onItemLongClick(int imagePosition) {

                    if (onMomentClickListener != null) {
                        int position = getCorrectPosition();
                        onMomentClickListener.onThumbnailLongClick(position, data.get(position), imagePosition);
                    }
                }
            });

            thumbnailListAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int imagePosition) {
                    if (onMomentClickListener != null) {
                        int position = getCorrectPosition();
                        onMomentClickListener.onThumbnailClick(position, data.get(position), imagePosition);
                    }
                }
            });

            llTime.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onMomentClickListener != null) {
                        int position = getCorrectPosition();
                        onMomentClickListener.onDateLongClick(position, data.get(position));
                    }
                    return true;
                }
            });

            llAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onMomentClickListener != null) {
                        int position = getCorrectPosition();
                        onMomentClickListener.onAddressClick(position, data.get(position));
                    }
                }
            });
        }

        @Override
        public void bind(AVObject avObject) {
            int currentPos = getCorrectPosition();
            int lastPos = currentPos - 1;
            String content = avObject.getString(MomentContract.CONTENT);
            tvContent.setContent(content);
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
            itemView.setTag(R.id.tag_data, avObject);
            tvTime.setText(TimeUtil.getFormatTime(currentTime, "HH:mm"));

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

            String address = avObject.getString(MomentContract.ADDRESS);
            llAddress.setVisibility(TextUtils.isEmpty(address) ? View.GONE : View.VISIBLE);
            tvAddress.setText(address);
        }

        void setOnMomentClickListener(OnMomentClickListener onDateLongClickListener) {
            this.onMomentClickListener = onDateLongClickListener;
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
