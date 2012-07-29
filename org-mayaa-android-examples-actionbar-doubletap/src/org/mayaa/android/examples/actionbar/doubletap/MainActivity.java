package org.mayaa.android.examples.actionbar.doubletap;

import org.mayaa.android.examples.actionbar.doubletap.widget.ActionBarViewCompat;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private WebView mContentWeb;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        // アクションバーのプログレスバーを表示可能にする。
        requestWindowFeature(Window.FEATURE_PROGRESS);
        // アクションバーのプログレスアイコンを表示可能にする。
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        onCreateView();

        // アクションバーのタイトル部分と空白部分の double tap event hook
        new ActionBarTouchListener(this);

        setProgressBarIndeterminateVisibility(false);

        mContentWeb.loadUrl("http://www.google.com/search?q=Android");
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void onCreateView() {
        mContentWeb = (WebView) findViewById(R.id.content_web);
        mContentWeb.setVerticalScrollbarOverlay(true);
        mContentWeb.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
        mContentWeb.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(final WebView view, final int newProgress) {
                if (newProgress < 100) {
                    setProgressBarVisibility(true);
                    setProgress(newProgress * 100);
                } else {
                    setProgressBarVisibility(false);
                }
            }
        });
        mContentWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
                setProgressBarIndeterminateVisibility(true);
                return false;
            }

            @Override
            public void onPageStarted(final WebView view, final String url, final Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(final WebView view, final String url) {
                super.onPageFinished(view, url);
                setProgressBarIndeterminateVisibility(false);
            }
        });

        final WebSettings webSettings = mContentWeb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
    }

    /**
     * {@linkplain ActionBar} の DoubleTap を検知する
     */
    /* package */class ActionBarTouchListener extends GestureDetector.SimpleOnGestureListener implements
            View.OnTouchListener {

        private final View mActionBarView;
        private final View mActionBarTitleView;
        private final View mActionBarSubtitleView;
        private final GestureDetector mGestureDetector;

        public ActionBarTouchListener(final Activity activity) {
            final ActionBar actionBar = activity.getActionBar();
            mGestureDetector = new GestureDetector(activity, this);
            mActionBarView = ActionBarViewCompat.getActionBarView(actionBar);
            if (mActionBarView != null) {
                mActionBarView.setOnTouchListener(this);
            }
            mActionBarTitleView = ActionBarViewCompat.getActionBarViewTitleView(actionBar);
            if (mActionBarTitleView != null) {
                mActionBarTitleView.setOnTouchListener(this);
            }
            mActionBarSubtitleView = ActionBarViewCompat.getActionBarViewSubtitleView(actionBar);
            if (mActionBarSubtitleView != null) {
                mActionBarSubtitleView.setOnTouchListener(this);
            }
        }

        /**
         * {@inheritDoc}
         * @see {@linkplain View.OnTouchListener}
         */
        @Override
        public boolean onTouch(final View v, final MotionEvent event) {
            // ActionBar のタッチイベントをhookする。
            mGestureDetector.onTouchEvent(event);

            return true;
        }

        /**
         * {@inheritDoc}
         * @see {@linkplain GestureDetector.OnDoubleTapListener}
         */
        @Override
        public boolean onDoubleTap(final MotionEvent e) {
            Log.v(TAG, "onDoubleTap e=" + e);

            // ダブルタップしたら、ページの一番上に移動する。
            mContentWeb.pageUp(true);
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(final MotionEvent e) {
            Log.v(TAG, "onDoubleTapEvent e=" + e);
            return false;
        }
    }
}
