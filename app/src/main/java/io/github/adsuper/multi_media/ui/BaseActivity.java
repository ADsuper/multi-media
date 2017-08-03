package io.github.adsuper.multi_media.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import com.jude.swipbackhelper.SwipeBackHelper;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.adsuper.multi_media.R;

public abstract class BaseActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.root_layout)
    LinearLayout mRootLayout;
    @BindView(R.id.avi_loading)
    AVLoadingIndicatorView mAviLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        ButterKnife.bind(this);
        SwipeBackHelper.onCreate(this);
        initToolbar();
        initContentView();
        Intent intent = getIntent();
        initOperation(intent);
    }

    /**
     * 获取 intent 参数，进行逻辑操作
     * @param intent
     */
    protected abstract void initOperation(Intent intent);

    /**
     * 将子类布局添加至父类中
     */
    private void initContentView() {
        mRootLayout.addView(addChildContentView());
    }

    /**
     * 添加子类布局
     * @return
     */
    protected abstract View addChildContentView() ;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_base_toolbar,menu);
        updateOptionsMenu(menu);
        return true;
    }

    /**
     * 由子类选择显示或者隐藏或者更新某个菜单
     * @param menu
     */
    protected void updateOptionsMenu(Menu menu) {
    }

    /**
     * 初始化 toolbar
     */
    private void initToolbar() {
        mToolbar.setTitle(setToolbarTitle());
        setSupportActionBar(mToolbar);
    }

    /**
     * 设置 toolbar 标题
     * @return
     */
    protected String setToolbarTitle() {
        return "珞神";
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }

    /**
     * 开启加载中动画
     */
    public void startLoading(){
        mAviLoading.smoothToShow();
    }

    /**
     * 关闭加载中动画
     */
    public void stopLoading(){
        if (mAviLoading.isShown()) {
            mAviLoading.smoothToHide();
        }
    }
}
