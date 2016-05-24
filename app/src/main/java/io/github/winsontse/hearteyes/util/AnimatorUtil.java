package io.github.winsontse.hearteyes.util;

import android.animation.Animator;
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
     * @param callback
     */
    public static void circularReveal(View v, final AnimatorCallback callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            int centerX = (v.getLeft() + v.getRight()) / 2;
            int centerY = (v.getTop() + v.getBottom()) / 2;
            float finalRadius = (float) Math.hypot((double) centerX, (double) centerY);

            final Animator circularReveal;
            circularReveal = ViewAnimationUtils.createCircularReveal(v, centerX, centerY, 0, finalRadius);

            circularReveal.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    circularReveal.removeListener(this);
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
            });
            circularReveal.setDuration(1000);
            circularReveal.start();
        } else {
            if (callback != null) {
                callback.onAnimatorEnd();
            }
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
                        v.setVisibility(View.GONE);
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
                        v.setVisibility(View.VISIBLE);

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
