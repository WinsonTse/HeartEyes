package io.github.winsontse.hearteyes.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.ViewAnimationUtils;

/**
 * Created by winson on 16/5/24.
 */
public class AnimatorUtil {
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
            circularReveal.setDuration(800);
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

    public static void hideFab(final View v, final AnimatorCallback callback) {
        v.animate().cancel();
        v.animate()
                .scaleX(0f)
                .scaleY(0f)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (callback != null) {
                            callback.onAnimatorEnd();
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();
    }

    public static void showFab(final View v, final AnimatorCallback callback) {
        v.animate().cancel();
        v.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (callback != null) {
                            callback.onAnimatorEnd();
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();
    }


    public interface AnimatorCallback {
        void onAnimatorEnd();
    }
}
