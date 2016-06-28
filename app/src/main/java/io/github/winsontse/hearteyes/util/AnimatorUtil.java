package io.github.winsontse.hearteyes.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewPropertyAnimator;

/**
 * Created by winson on 16/5/24.
 */
public class AnimatorUtil {

    public static final int ANIMATOR_TIME = 500;

    /**
     * 创建一个圆形动画
     *
     * @param v
     * @param listener
     */

    public static void createCircularReveal(final View v, int centerX, int centerY, float startRadius, float finalRadius, final Animator.AnimatorListener listener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            final Animator circularReveal;
            circularReveal = ViewAnimationUtils.createCircularReveal(v, centerX, centerY, startRadius, finalRadius);

            circularReveal.addListener(listener);
            circularReveal.setDuration(ANIMATOR_TIME);
            circularReveal.start();
        } else {
            if (listener != null) {
                listener.onAnimationEnd(null);
            }
        }
    }

    public static void showCircularReveal(final View v, final Animator.AnimatorListener callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            v.setVisibility(View.VISIBLE);
            int centerX = (v.getLeft() + v.getRight()) / 2;
            int centerY = (v.getTop() + v.getBottom()) / 2;
            float finalRadius = (float) Math.hypot((double) centerX, (double) centerY);
//            float finalRadius = (float) Math.hypot((double) v.getWidth(), (double) v.getHeight());

            createCircularReveal(v, centerX, centerY, 0, finalRadius, callback);
        }
    }

    public static void hideCircularReveal(final View v, final Animator.AnimatorListener callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            v.setVisibility(View.VISIBLE);
            int centerX = (v.getLeft() + v.getRight()) / 2;
            int centerY = (v.getTop() + v.getBottom()) / 2;
            float finalRadius = (float) Math.hypot((double) centerX, (double) centerY);
            createCircularReveal(v, centerX, centerY, finalRadius, 0, callback);
        }
    }

    public static ViewPropertyAnimator translationToCorrect(View v) {
        return v.animate().translationY(0).setDuration(ANIMATOR_TIME).setInterpolator(new FastOutSlowInInterpolator());
    }

    public static ViewPropertyAnimator translationToHideBottomBar(View v) {
        return v.animate().translationY(v.getHeight()).setDuration(ANIMATOR_TIME).setInterpolator(new FastOutSlowInInterpolator());
    }

}
