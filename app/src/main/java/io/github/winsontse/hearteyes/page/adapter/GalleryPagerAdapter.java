package io.github.winsontse.hearteyes.page.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import io.github.winsontse.hearteyes.data.model.ImageEntity;
import io.github.winsontse.hearteyes.page.image.GalleryItemFragment;

/**
 * Created by winson on 16/7/15.
 */

public class GalleryPagerAdapter extends FragmentStatePagerAdapter {

    private List<ImageEntity> images;

    public GalleryPagerAdapter(FragmentManager fm, List<ImageEntity> images) {
        super(fm);
        this.images = images;
    }


    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        return GalleryItemFragment.newInstance(images.get(position));
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return images.size();
    }
}
