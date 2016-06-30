/*
 * Copyright (c) 2016 Copyright Istuary Innovation Group 2016.
 * Unauthorized copying of this file, via any medium is strictly prohibited proprietary and
 *  confidential.
 * Created on 5/26/16 11:53 AM
 * ProjectName: IronHide ; ModuleName: IronHideLibrary ; ClassName: ScrollAwareFABBehavior.
 * Author: Lena; Last Modified: 5/26/16 11:53 AM.
 *  This file is originally created by Rayman Yan.
 */
package io.github.winsontse.hearteyes.behavior;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.util.AnimatorUtil;

public class ScrollAwareFABBehavior extends FloatingActionButton.Behavior {

    public ScrollAwareFABBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final FloatingActionButton child,
                                       final View directTargetChild, final View target, final int nestedScrollAxes) {
        // Ensure we react to vertical scrolling
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(final CoordinatorLayout coordinatorLayout, final FloatingActionButton child,
                               final View target, final int dxConsumed, final int dyConsumed,
                               final int dxUnconsumed, final int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

        Activity activity = (Activity) child.getContext();
        View bottomBar = activity.findViewById(R.id.bottom_bar);

        if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
            // User scrolled down and the FAB is currently visible -> hide the FAB
            child.hide();
            AnimatorUtil.translationToHideBottomBar(bottomBar).start();

        } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
            // User scrolled up and the FAB is currently not visible -> show the FAB
            child.show();
            AnimatorUtil.translationToCorrect(bottomBar).start();

        }
    }
}
