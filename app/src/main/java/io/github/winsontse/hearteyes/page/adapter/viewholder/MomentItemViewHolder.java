package io.github.winsontse.hearteyes.page.adapter.viewholder;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import io.github.winsontse.hearteyes.page.adapter.MomentListAdapter;
import io.github.winsontse.hearteyes.page.adapter.ThumbnailListAdapter;
import io.github.winsontse.hearteyes.page.adapter.base.BaseRecyclerAdapter;
import io.github.winsontse.hearteyes.page.adapter.base.BaseViewHolder;
import io.github.winsontse.hearteyes.util.ImageLoader;
import io.github.winsontse.hearteyes.util.TimeUtil;
import io.github.winsontse.hearteyes.widget.CircleImageView;
import io.github.winsontse.hearteyes.widget.MoreTextView;

/**
 * Created by winson on 16/9/25.
 */

public class MomentItemViewHolder extends BaseViewHolder<AVObject> {

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
    private MomentListAdapter.OnMomentClickListener onMomentClickListener;

    public MomentItemViewHolder(final List<AVObject> data, ViewGroup parent) {
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
        thumbnailListAdapter.setOnItemLongClickListener(new BaseRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int imagePosition) {

                if (onMomentClickListener != null) {
                    int position = getCorrectPosition();
                    onMomentClickListener.onThumbnailLongClick(position, data.get(position), imagePosition);
                }
            }
        });

        thumbnailListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
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
            itemView.setTag(R.id.tag_type, MomentListAdapter.TAG_HEADER_INVISIBLE);
        } else {
            llTime.setVisibility(View.VISIBLE);
            itemView.setTag(R.id.tag_type, MomentListAdapter.TAG_HEADER_VISIBLE);
        }
        if (currentPos == 0) {
            itemView.setTag(R.id.tag_type, MomentListAdapter.TAG_HEADER_VISIBLE);
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

    public void setOnMomentClickListener(MomentListAdapter.OnMomentClickListener onDateLongClickListener) {
        this.onMomentClickListener = onDateLongClickListener;
    }

}
