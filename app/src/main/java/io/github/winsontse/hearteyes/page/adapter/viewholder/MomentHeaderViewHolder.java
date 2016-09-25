package io.github.winsontse.hearteyes.page.adapter.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;

import butterknife.BindView;
import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.data.model.leancloud.CircleContract;
import io.github.winsontse.hearteyes.page.adapter.MomentListAdapter;
import io.github.winsontse.hearteyes.page.adapter.base.BaseViewHolder;
import io.github.winsontse.hearteyes.util.ImageLoader;
import io.github.winsontse.hearteyes.util.TimeUtil;

/**
 * Created by winson on 16/9/25.
 */
public class MomentHeaderViewHolder extends BaseViewHolder<AVObject> {

    @BindView(R.id.iv_cover)
    ImageView ivCover;
    @BindView(R.id.tv_love_day)
    TextView tvLoveDay;
    private MomentListAdapter.OnMomentClickListener onMomentClickListener;
    private AVObject avCircle;

    public MomentHeaderViewHolder(ViewGroup parent, final MomentListAdapter.OnMomentClickListener onMomentClickListener) {
        super(parent, R.layout.list_header_moment);
        itemView.setTag(R.id.tag_type, MomentListAdapter.VIEW_TYPE_HEADER);
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