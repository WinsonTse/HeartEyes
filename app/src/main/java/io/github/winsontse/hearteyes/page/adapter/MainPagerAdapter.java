package io.github.winsontse.hearteyes.page.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import io.github.winsontse.hearteyes.page.account.AccountFragment;
import io.github.winsontse.hearteyes.page.date.DateListFragment;
import io.github.winsontse.hearteyes.page.main.MainActivity;
import io.github.winsontse.hearteyes.page.moment.MomentListFragment;
import io.github.winsontse.hearteyes.page.todo.TodoListFragment;

/**
 * Created by winson on 16/5/21.
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter {
    public static final int PAGE_COUNT = 3;

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case MainActivity.PAGE_MOMENT:
                return MomentListFragment.newInstance();
            case MainActivity.PAGE_TODO:
                return TodoListFragment.newInstance();
            case MainActivity.PAGE_ACCOUNT:
                return AccountFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
