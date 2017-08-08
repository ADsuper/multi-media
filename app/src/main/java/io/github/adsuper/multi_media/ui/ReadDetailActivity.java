package io.github.adsuper.multi_media.ui;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.just.library.AgentWeb;
import com.just.library.ChromeClientCallbackManager;
import com.just.library.LogUtils;

import io.github.adsuper.multi_media.model.ReadModel;

/**
 * read 详情页面
 */
public class ReadDetailActivity extends BaseActivity {

    private View mView;

    private ReadModel.NewslistEntity mEntity;

    private String mUrl;
    private AgentWeb mAgentWeb;
    private LinearLayout mLinearLayout;


    @Override
    protected void initOperation(Intent intent) {
//        startLoading();
        mEntity = (ReadModel.NewslistEntity) intent.getSerializableExtra("entity");


        mUrl = mEntity.getUrl();
        if (StringUtils.isEmpty(mUrl)) {
            ToastUtils.showShort("地址加载失败，请稍后再试");
            return;
        }

        mAgentWeb = AgentWeb.with(this)//传入Activity
                .setAgentWebParent(mLinearLayout, new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams ,第一个参数和第二个参数应该对应。
                .useDefaultIndicator()// 使用默认进度条
                .defaultProgressBarColor() // 使用默认进度条颜色
                .setReceivedTitleCallback(mCallback) //设置 Web 页面的 title 回调
                .createAgentWeb()//
                .ready()
                .go(mUrl);
    }

    private ChromeClientCallbackManager.ReceivedTitleCallback mCallback = new ChromeClientCallbackManager.ReceivedTitleCallback() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            if (mToolbar != null)
                mToolbar.setTitle(title);
        }
    };

    @Override
    protected View addChildContentView(LinearLayout rootLayout) {

        mLinearLayout = rootLayout;
//        mView = View.inflate(this, R.layout.activity_read_detail, null);
        return null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        LogUtils.i("Info", "result:" + requestCode + " result:" + resultCode);
        mAgentWeb.uploadFileResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mAgentWeb.destroy();
        mAgentWeb.getWebLifeCycle().onDestroy();
    }


}
