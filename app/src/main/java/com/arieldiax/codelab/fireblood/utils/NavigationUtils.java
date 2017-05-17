package com.arieldiax.codelab.fireblood.utils;

import android.content.Context;
import android.content.Intent;

import java.util.Stack;

/**
 * Due to various issues with `Intent.FLAG_ACTIVITY_REORDER_TO_FRONT`.
 * A custom activity manager has been implemented.
 *
 * @see http://stackoverflow.com/questions/20695522/puzzling-behavior-with-reorder-to-front
 * @see https://issuetracker.google.com/issues/36986021
 */
public final class NavigationUtils {

    /**
     * Stack of Class instances.
     */
    private static final Stack<Class> sClasses = new Stack<>();

    /**
     * Creates a new NavigationUtils object (no, it won't).
     */
    private NavigationUtils() {
        // Required empty private constructor (to prevent instantiation).
    }

    /**
     * Determines whether or not the stack has reached the root.
     *
     * @return Whether or not the stack has reached the root.
     */
    public static boolean isTaskRoot() {
        return (sClasses.size() == 1);
    }

    /**
     * Stacks the custom activity.
     *
     * @param context             Instance of the Context class.
     * @param activityClass       Class of the activity.
     * @param shouldActivityStart Whether or not the activity should be started.
     */
    public static void stackCustomActivity(
            Context context,
            Class activityClass,
            boolean shouldActivityStart
    ) {
        sClasses.remove(activityClass);
        sClasses.push(activityClass);
        if (shouldActivityStart) {
            startCustomActivity(context, activityClass);
        }
    }

    /**
     * Unstacks the custom activity.
     *
     * @param context Instance of the Context class.
     */
    public static void unstackCustomActivity(Context context) {
        sClasses.pop();
        startCustomActivity(context, sClasses.peek());
    }

    /**
     * Starts the custom activity.
     *
     * @param context       Instance of the Context class.
     * @param activityClass Class of the activity.
     */
    public static void startCustomActivity(
            Context context,
            Class activityClass
    ) {
        Intent activityIntent = new Intent(context, activityClass);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(activityIntent);
    }

    /**
     * Clears the classes stack.
     */
    public static void clearClassesStack() {
        sClasses.removeAllElements();
    }
}
