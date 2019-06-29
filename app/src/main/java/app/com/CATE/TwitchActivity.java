package app.com.CATE;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import app.com.CATE.models.YoutubeDataModel;
import app.com.youtubeapiv3.R;

public class TwitchActivity extends AppCompatActivity {
    private YoutubeDataModel youtubeDataModel = null;
    WebView web;
    TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitch);
        youtubeDataModel = getIntent().getParcelableExtra(YoutubeDataModel.class.toString());
        String videoId = youtubeDataModel.getVideo_id();
//        String twitch_URL = "https://www.twitch.tv/videos/442053839";                                           //트위치 URL 예

        web = (WebView) findViewById(R.id.twitchPlayer);
        title = (TextView) findViewById(R.id.twichTitle);

        web.getSettings().setJavaScriptEnabled(true);
        web.setWebChromeClient(new FullscreenableChromeClient(TwitchActivity.this));                //전체화면
        web.setWebViewClient(new WebClient());                                                       //다른 웹브라우저로 여는 것을 방지

//        web.getSettings().setPluginState(WebSettings.PluginState.ON);             //플러그인 설정
//        web.getSettings().setSupportMultipleWindows(true);                        //웹뷰에서 여러 개의 윈도우를 사용가능하게함
//        web.setLayerType(View.LAYER_TYPE_HARDWARE, null);                         //뷰 가속 - 영상 재생 가능하게 설정
//        if (android.os.Build.VERSION.SDK_INT >= 11) {                             //GPU를 가속
//            getWindow().addFlags(16777216);
//        }


        final String html1 = "<iframe frameborder=\"0\"\n" +
                "scrolling=\"no\"\n" +
                "id=\"chat_embed\"\n" +
                "allowfullscreen=\"true\"\n" +
                "src=\"https://player.twitch.tv/?autoplay=false&video=v";
        final String html2 = "\"\n" +
                "height=\"350\"\n" +
                "width=\"400\">\n" +
                "</iframe>";

//        String[] split = twitch_URL.split("/");
        web.loadData(html1 + videoId + html2, "text/html", null);


        title.setText(youtubeDataModel.getTitle());
    }
}

class WebClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return super.shouldOverrideUrlLoading(view, url);
    }
}

class FullscreenableChromeClient extends WebChromeClient {
    private Activity mActivity = null;

    private View mCustomView;
    private CustomViewCallback mCustomViewCallback;
    private int mOriginalOrientation;
    private FrameLayout mFullscreenContainer;
    private static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    public FullscreenableChromeClient(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }

            mOriginalOrientation = mActivity.getRequestedOrientation();
            FrameLayout decor = (FrameLayout) mActivity.getWindow().getDecorView();
            mFullscreenContainer = new FullscreenHolder(mActivity);
            mFullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
            decor.addView(mFullscreenContainer, COVER_SCREEN_PARAMS);
            mCustomView = view;
            setFullscreen(true);
            mCustomViewCallback = callback;
//          mActivity.setRequestedOrientation(requestedOrientation);

        }

        super.onShowCustomView(view, callback);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
        this.onShowCustomView(view, callback);
    }

    @Override
    public void onHideCustomView() {
        if (mCustomView == null) {
            return;
        }

        setFullscreen(false);
        FrameLayout decor = (FrameLayout) mActivity.getWindow().getDecorView();
        decor.removeView(mFullscreenContainer);
        mFullscreenContainer = null;
        mCustomView = null;
        mCustomViewCallback.onCustomViewHidden();
        mActivity.setRequestedOrientation(mOriginalOrientation);

    }

    private void setFullscreen(boolean enabled) {

        Window win = mActivity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        if (enabled) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
            if (mCustomView != null) {
                mCustomView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
        win.setAttributes(winParams);
    }

    private static class FullscreenHolder extends FrameLayout {
        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ContextCompat.getColor(ctx, android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }
}