package org.mayaa.android.examples.actionbar.doubletap.widget;

import java.lang.reflect.Field;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import android.app.ActionBar;
import android.os.Build;
import android.util.Log;
import android.view.View;

public final class ActionBarViewCompat {

    private static final String TAG = ActionBarViewCompat.class.getSimpleName();

    private ActionBarViewCompat() {
    }

    /**
     * Interface for the full API.
     */
    interface Impl {
        @CheckForNull
        View getActionBarView(@Nonnull final ActionBar actionBar);

        @CheckForNull
        View getActionBarViewTitleView(@Nonnull final ActionBar actionBar);

        @CheckForNull
        View getActionBarViewSubtitleView(@Nonnull final ActionBar actionBar);
    }

    /**
     * Interface implementation that doesn't use anything about v14 APIs.
     */
    static class BaseImpl implements Impl {

        private static Class<?> ACTION_BAR_IMPL_CLASS;
        private static Field ACTION_BAR_IMPL_FIELD_ACTION_VIEW;

        private static Class<?> ACTION_BAR_VIEW_CLASS;
        private static Field ACTION_BAR_VIEW_FIELD_TITLE_VIEW;
        private static Field ACTION_BAR_VIEW_FIELD_SUBTITLE_VIEW;

        static {
            try {
                ACTION_BAR_IMPL_CLASS = Class.forName("com.android.internal.app.ActionBarImpl");
                try {
                    ACTION_BAR_IMPL_FIELD_ACTION_VIEW = ACTION_BAR_IMPL_CLASS.getDeclaredField("mActionView");
                    ACTION_BAR_IMPL_FIELD_ACTION_VIEW.setAccessible(true);
                } catch (final NoSuchFieldException e) {
                    ACTION_BAR_IMPL_FIELD_ACTION_VIEW = null;
                }
            } catch (final Throwable t) {
                ACTION_BAR_IMPL_CLASS = null;
            }
            try {
                ACTION_BAR_VIEW_CLASS = Class.forName("com.android.internal.widget.ActionBarView");
                try {
                    ACTION_BAR_VIEW_FIELD_TITLE_VIEW = ACTION_BAR_VIEW_CLASS.getDeclaredField("mTitleView");
                    ACTION_BAR_VIEW_FIELD_TITLE_VIEW.setAccessible(true);
                } catch (final NoSuchFieldException e) {
                    ACTION_BAR_VIEW_FIELD_TITLE_VIEW = null;
                }
                try {
                    ACTION_BAR_VIEW_FIELD_SUBTITLE_VIEW = ACTION_BAR_VIEW_CLASS.getDeclaredField("mSubtitleView");
                    ACTION_BAR_VIEW_FIELD_SUBTITLE_VIEW.setAccessible(true);
                } catch (final NoSuchFieldException e) {
                    ACTION_BAR_VIEW_FIELD_SUBTITLE_VIEW = null;
                }
            } catch (final Throwable t) {
                ACTION_BAR_VIEW_CLASS = null;
            }
        }

        @Override
        public View getActionBarView(final ActionBar actionBar) {
            if (ACTION_BAR_IMPL_FIELD_ACTION_VIEW == null) {
                return null;
            }
            try {
                return (View) ACTION_BAR_IMPL_FIELD_ACTION_VIEW.get(actionBar);
            } catch (final Exception e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return null;
            }
        }

        @Override
        public View getActionBarViewTitleView(final ActionBar actionBar) {
            final View actionBarView = getActionBarView(actionBar);
            if (actionBarView == null) {
                return null;
            }
            if (ACTION_BAR_VIEW_FIELD_TITLE_VIEW == null) {
                Log.w(TAG, "Not implement 'com.android.internal.widget.ActionBarView' mTitleView");
                return null;
            }
            try {
                return (View) ACTION_BAR_VIEW_FIELD_TITLE_VIEW.get(actionBarView);
            } catch (final Exception e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return null;
            }
        }

        @Override
        public View getActionBarViewSubtitleView(final ActionBar actionBar) {
            final View actionBarView = getActionBarView(actionBar);
            if (actionBarView == null) {
                return null;
            }
            if (ACTION_BAR_VIEW_FIELD_SUBTITLE_VIEW == null) {
                Log.w(TAG, "Not implement 'com.android.internal.widget.ActionBarView' mSubtitleView");
                return null;
            }
            try {
                return (View) ACTION_BAR_VIEW_FIELD_SUBTITLE_VIEW.get(actionBarView);
            } catch (final Exception e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return null;
            }
        }
    }

    /**
     * Select the correct implementation to use for the current platform.
     */
    static final Impl IMPL;
    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            IMPL = new BaseImpl();
        } else {
            throw new RuntimeException("No Support Android Runtime Version: " + Build.VERSION.SDK_INT);
        }
    }

    /**
     * {@linkplain com.android.internal.app.ActionBarImpl} Class の mActionView Field を返す。
     * @param actionBar {@linkplain android.app.ActionBar} の実装。
     * @return mActionView
     */
    @CheckForNull
    public static View getActionBarView(@Nonnull final ActionBar actionBar) {
        return IMPL.getActionBarView(actionBar);
    }

    /**
     * {@linkplain com.android.internal.widget.ActionBarView} Class の mTitleView Field を返す。
     * @param actionBar {@linkplain android.app.ActionBar} の実装。
     * @return mTitleView
     */
    @CheckForNull
    public static View getActionBarViewTitleView(@Nonnull final ActionBar actionBar) {
        return IMPL.getActionBarViewTitleView(actionBar);
    }

    /**
     * {@linkplain com.android.internal.widget.ActionBarView} Class の mSubtitleView Field を返す。
     * @param actionBar {@linkplain android.app.ActionBar} の実装。
     * @return mSubtitleView
     */
    @CheckForNull
    public static View getActionBarViewSubtitleView(@Nonnull final ActionBar actionBar) {
        return IMPL.getActionBarViewSubtitleView(actionBar);
    }
}
