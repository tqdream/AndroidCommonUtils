package com.autohome.main.car.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.support.annotation.IntDef;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.CycleInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.autohome.barragephoto.listener.SimpleAnimationListener;
import com.autohome.commontools.android.ScreenUtils;
import com.autohome.uikit.picture.view.imageview.AHDisplayOptions;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 动画工具类
 * 包含常用的动画，后续有在添加
 *
 */
public final class AnimatorUtils {

    /**
     * Don't let anyone instantiate this class.
     */
    private AnimatorUtils() {
        throw new Error("Do not need instantiate!");
    }


    /**
     * 默认动画持续时间
     */
    public static final long ANIMATION_DURATION_300 = 300;
    public static final long ANIMATION_DURATION_1000 = 1000;
    public static final long DEFAULT_ANIMATION_DURATION = 400;

    public static final float ALPHA_MIN = 0.0f;
    public static final float ALPHA_MAX = 1.0f;

    /**
     * alpha
     *
     * @param v
     * @param fromAlpha
     * @param toAlpha
     * @param duration
     */
    public static ObjectAnimator alpha(View v, float fromAlpha, float toAlpha, long duration) {
        return alpha(v, fromAlpha, toAlpha, duration, null);
    }

    /**
     * alpha
     *
     * @param v
     * @param fromAlpha
     * @param toAlpha
     * @param duration
     * @param animatorListener
     */
    public static ObjectAnimator alpha(View v, float fromAlpha, float toAlpha, long duration, Animator.AnimatorListener
            animatorListener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, View.ALPHA, fromAlpha, toAlpha);
        animator.setDuration(duration);
        if (animatorListener != null) {
            animator.addListener(animatorListener);
        }
        return animator;
    }
    /**
     * translation x
     *
     * @param v
     * @param fromX
     * @param toX
     * @param duration
     */
    public static ObjectAnimator translationX(View v, float fromX, float toX, long duration) {
        return translationX(v, fromX, toX, duration, null);
    }

    /**
     * translation x
     *
     * @param v
     * @param fromX
     * @param toX
     * @param duration
     * @param animatorListener
     */
    public static ObjectAnimator translationX(View v, float fromX, float toX, long duration, Animator.AnimatorListener
            animatorListener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, View.TRANSLATION_X, fromX, toX);
        animator.setDuration(duration);
        if (animatorListener != null) {
            animator.addListener(animatorListener);
        }
        return animator;
    }

    /**
     * translation y
     *
     * @param v
     * @param fromY
     * @param toY
     * @param duration
     */
    public static ObjectAnimator translationY(View v, float fromY, float toY, int duration) {
        return translationY(v, fromY, toY, duration, null);
    }

    /**
     * translation y
     *
     * @param v
     * @param fromY
     * @param toY
     * @param duration
     * @param animatorListener
     */
    public static ObjectAnimator translationY(View v, float fromY, float toY, int duration, Animator.AnimatorListener
            animatorListener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, View.TRANSLATION_Y, fromY, toY);
        animator.setDuration(duration);
        if (animatorListener != null) {
            animator.addListener(animatorListener);
        }
        return animator;
    }

    /**
     * rotation x
     *
     * @param v
     * @param fromX
     * @param toX
     * @param duration
     */
    public static ObjectAnimator rotationX(View v, float fromX, float toX, int duration) {
        return rotationX(v, fromX, toX, duration, null);
    }

    /**
     * rotation x
     *
     * @param v
     * @param fromX
     * @param toX
     * @param duration
     * @param animatorListener
     */
    public static ObjectAnimator rotationX(View v, float fromX, float toX, int duration, Animator.AnimatorListener
            animatorListener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, View.ROTATION_X, fromX, toX);
        animator.setDuration(duration);
        if (animatorListener != null) {
            animator.addListener(animatorListener);
        }
        return animator;
    }

    /**
     * rotation y
     *
     * @param v
     * @param fromY
     * @param toY
     * @param duration
     */
    public static ObjectAnimator rotationY(View v, float fromY, float toY, int duration) {
        return rotationY(v, fromY, toY, duration, null);
    }

    /**
     * rotation y
     *
     * @param v
     * @param fromY
     * @param toY
     * @param duration
     * @param animatorListener
     */
    public static ObjectAnimator rotationY(View v, float fromY, float toY, int duration, Animator.AnimatorListener
            animatorListener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, View.ROTATION_Y, fromY, toY);
        animator.setDuration(duration);
        if (animatorListener != null) {
            animator.addListener(animatorListener);
        }
        return animator;
    }

    /**
     * scale x
     *
     * @param v
     * @param fromX
     * @param toX
     * @param duration
     */
    public static ObjectAnimator scaleX(View v, float fromX, float toX, int duration) {
        return scaleX(v, fromX, toX, duration, null);
    }

    /**
     * scale x
     *
     * @param v
     * @param fromX
     * @param toX
     * @param duration
     * @param animatorListener
     */
    public static ObjectAnimator scaleX(View v, float fromX, float toX, int duration, Animator.AnimatorListener
            animatorListener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, View.SCALE_X, fromX, toX);
        animator.setDuration(duration);
        if (animatorListener != null) {
            animator.addListener(animatorListener);
        }
        return animator;
    }

    /**
     * scale y
     *
     * @param v
     * @param fromY
     * @param toY
     * @param duration
     */
    public static ObjectAnimator scaleY(View v, float fromY, float toY, int duration) {
        return scaleY(v, fromY, toY, duration, null);
    }

    /**
     * scale y
     *
     * @param v
     * @param fromY
     * @param toY
     * @param duration
     * @param animatorListener
     */
    public static ObjectAnimator scaleY(View v, float fromY, float toY, int duration, Animator.AnimatorListener
            animatorListener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, View.SCALE_Y, fromY, toY);
        animator.setDuration(duration);
        if (animatorListener != null) {
            animator.addListener(animatorListener);
        }
        return animator;
    }

    /**
     * shake x
     *
     * @param v
     */
    public static ObjectAnimator shakeX(View v) {
        return shakeX(v, 10, ANIMATION_DURATION_1000, 5.0f);
    }

    /**
     * shake x
     *
     * @param v
     * @param offset
     * @param duration
     * @param times
     */
    public static ObjectAnimator shakeX(View v, float offset, long duration, float times) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, View.TRANSLATION_X, 0, offset);
        animator.setDuration(duration);
        animator.setInterpolator(new CycleInterpolator(times));
        return animator;
    }

    /**
     * shake y
     *
     * @param v
     */
    public static ObjectAnimator shakeY(View v) {
        return shakeY(v, 10, ANIMATION_DURATION_1000, 5.0f);
    }

    /**
     * shake y
     *
     * @param v
     * @param offset
     * @param duration
     * @param times
     */
    public static ObjectAnimator shakeY(View v, float offset, long duration, float times) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, View.TRANSLATION_Y, 0, offset);
        animator.setDuration(duration);
        animator.setInterpolator(new CycleInterpolator(times));
        return animator;
    }

    /**
     * 呼吸灯效果
     *
     * @param v
     */
    public static ObjectAnimator breath(View v) {
        return breath(v, 0.0f, 1.0f, 1000);
    }

    /**
     * 呼吸灯效果
     *
     * @param v
     * @param fromRange
     * @param toRange
     * @param duration
     */
    public static ObjectAnimator breath(View v, float fromRange, float toRange, long duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, View.ALPHA, fromRange, toRange);
        animator.setDuration(duration);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        return animator;
    }

    public static AnimatorSet playTogether(Animator.AnimatorListener animatorListener, Animator... items) {
        return playTogether(0, animatorListener, items);
    }

    public static AnimatorSet playTogether(long duration, Animator.AnimatorListener animatorListener, Animator... items) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(items);
        if (duration != 0) {
            animatorSet.setDuration(duration);
        }
        if (animatorListener != null) {
            animatorSet.addListener(animatorListener);
        }
        return animatorSet;
    }

    public static final int WIDTH = 0;
    public static final int HEIGHT = 1;

    @IntDef({WIDTH, HEIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LayoutAnimatorType {
    }

    public static ValueAnimator viewLayoutAnimator(final View v, int start, int end, @LayoutAnimatorType int type) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                int value = (int) arg0.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                if (WIDTH == type) {
                    layoutParams.width = value;
                } else if (HEIGHT == type) {
                    layoutParams.height = value;
                }
                v.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }
}
