package io.github.winsontse.hearteyes.page.adapter;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.data.model.ImageEntity;
import io.github.winsontse.hearteyes.page.adapter.base.BaseRecyclerAdapter;
import io.github.winsontse.hearteyes.page.adapter.base.BaseViewHolder;
import io.github.winsontse.hearteyes.page.image.ImagePickerActivity;
import io.github.winsontse.hearteyes.util.ImageLoader;
import io.github.winsontse.hearteyes.util.LogUtil;

/**
 * Created by winson on 16/6/20.
 */
public class ImagePickerAdaper extends BaseRecyclerAdapter<ImageEntity> {
    private List<ImageEntity> selectedList = new ArrayList<>();
    private int maxCount = ImagePickerActivity.DEFUALT_MAX_COUNT;

    public void setMaxCount(int maxCount) {
        if (maxCount < 1) {
            this.maxCount = ImagePickerActivity.DEFUALT_MAX_COUNT;
        } else {
            this.maxCount = maxCount;
        }
    }

    public List<ImageEntity> getSelectedList() {
        return selectedList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(data, maxCount, onItemClickListener, parent, selectedList);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder) holder).bind(data.get(position));

    }

    protected static final class ItemViewHolder extends BaseViewHolder<ImageEntity> {

        @BindView(R.id.iv)
        ImageView iv;
        @BindView(R.id.cb)
        CheckBox cb;

        private List<ImageEntity> selectedList;
        private int maxCount;

        public ItemViewHolder(final List<ImageEntity> data, final int maxCount, final OnItemClickListener onItemClickListener, ViewGroup parent, final List<ImageEntity> selectedList) {
            super(data, onItemClickListener, parent, R.layout.list_item_image_picker);
            this.selectedList = selectedList;
            this.maxCount = maxCount;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean checked = cb.isChecked();
                    int size = selectedList.size();
                    if (!checked) {
                        if (size < maxCount) {
                            cb.setChecked(true);
                            ItemViewHolder.this.selectedList.add(data.get(getAdapterPosition()));

                        } else {
                            Toast.makeText(view.getContext(), R.string.tips_selected_images_max, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        cb.setChecked(false);
                        ImageEntity entity = data.get(getAdapterPosition());
                        ImageEntity item = contains(entity);
                        if (item != null) {
                            selectedList.remove(item);
                        }
                    }
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClickListener(getAdapterPosition());
                    }
                }
            });
        }

        private ImageEntity contains(ImageEntity entity) {
            if (selectedList == null || selectedList.size() == 0) {
                return null;
            } else {
                for (ImageEntity item : selectedList) {
                    if (item.equals(entity)) {
                        return item;
                    }
                }
            }
            return null;
        }

        @Override
        public void bind(ImageEntity imageEntity) {
            ImageLoader.getInstance().displayImage(context, new File(imageEntity.getData()), iv);
            cb.setChecked(contains(data.get(getAdapterPosition())) != null);
        }
    }
}
