package com.arieldiax.codelab.fireblood.utils;

import android.text.format.DateUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

public final class AnimationUtils {

    /**
     * Creates a new AnimationUtils object (no, it won't).
     */
    private AnimationUtils() {
        // Required empty private constructor (to prevent instantiation).
    }

    /**
     * Gets the fade in animation.
     *
     * @return The fade in animation.
     */
    public static Animation getFadeInAnimation() {
        Animation fadeInAnimation = new AlphaAnimation(0, 1);
        fadeInAnimation.setInterpolator(new AccelerateInterpolator());
        fadeInAnimation.setDuration(DateUtils.SECOND_IN_MILLIS / 4);
        fadeInAnimation.setStartOffset(DateUtils.SECOND_IN_MILLIS / 2);
        return fadeInAnimation;
    }

    /**
     * Gets the fade out animation.
     *
     * @return The fade out animation.
     */
    public static Animation getFadeOutAnimation() {
        Animation fadeOutAnimation = new AlphaAnimation(1, 0);
        fadeOutAnimation.setInterpolator(new AccelerateInterpolator());
        fadeOutAnimation.setDuration(DateUtils.SECOND_IN_MILLIS / 4);
        return fadeOutAnimation;
    }

    /**
     * Gets the blink animation.
     *
     * @return The blink animation.
     */
    public static Animation getBlinkAnimation() {
        Animation blinkAnimation = new AlphaAnimation(1, 0);
        blinkAnimation.setInterpolator(new LinearInterpolator());
        blinkAnimation.setDuration(DateUtils.SECOND_IN_MILLIS);
        blinkAnimation.setRepeatMode(Animation.REVERSE);
        blinkAnimation.setRepeatCount(Animation.INFINITE);
        return blinkAnimation;
    }
}
