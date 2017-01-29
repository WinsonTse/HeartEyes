package io.github.winsontse.hearteyes.page.adapter.diff;

import android.text.TextUtils;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;

import java.util.List;

import io.github.winsontse.hearteyes.model.entity.leancloud.MomentContract;
import io.github.winsontse.hearteyes.page.adapter.diff.base.BaseListDiffCallback;

/**
 * Created by winson on 16/9/25.
 */

public class MomentListDiffCallback extends BaseListDiffCallback<AVObject> {


    @Override
    public int getOldListSize() {
        return oldData == null ? 0 : oldData.size();
    }


    @Override
    public int getNewListSize() {
        return newData == null ? 0 : newData.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return TextUtils.equals(oldData.get(oldItemPosition).getObjectId(), newData.get(newItemPosition).getObjectId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        AVObject oldItem = oldData.get(oldItemPosition);
        AVObject newItem = oldData.get(newItemPosition);
        if (!TextUtils.equals(oldItem.getString(MomentContract.ADDRESS), newItem.getString(MomentContract.ADDRESS))) {
            return false;
        }
        if (!TextUtils.equals(oldItem.getAVUser(MomentContract.AUTHOR).getUsername(), newItem.getAVUser(MomentContract.AUTHOR).getUsername())) {
            return false;
        }
        if (!TextUtils.equals(oldItem.getString(MomentContract.CIRCLE_ID), newItem.getString(MomentContract.CIRCLE_ID))) {
            return false;
        }
        if (!TextUtils.equals(oldItem.getString(MomentContract.CONTENT), newItem.getString(MomentContract.CONTENT))) {
            return false;
        }
        if (!TextUtils.equals(oldItem.getString(MomentContract.CREATEAD_TIME), newItem.getString(MomentContract.CREATEAD_TIME))) {
            return false;
        }

        if (oldItem.getDouble(MomentContract.LATITUDE) != newItem.getDouble(MomentContract.LATITUDE)) {
            return false;
        }

        if (oldItem.getDouble(MomentContract.LONGITUDE) != newItem.getDouble(MomentContract.LONGITUDE)) {
            return false;
        }

        List oldImgList = oldItem.getList(MomentContract.IMAGES);
        List newImgList = newItem.getList(MomentContract.IMAGES);

        if (oldImgList == null && newImgList == null) {
            return true;
        } else if (oldImgList == null || newImgList == null) {
            return false;
        } else if (oldImgList.size() != newImgList.size()) {
            return false;
        } else {
            return ((List<AVFile>)oldImgList).containsAll((List<AVFile>)(newImgList));
        }

//
//        if (!TextUtils.equals(oldItem.getAVFile(MomentContract.RECORDING).getObjectId(), newItem.getAVFile(MomentContract.RECORDING).getObjectId())) {
//            return false;
//        }
    }
}
