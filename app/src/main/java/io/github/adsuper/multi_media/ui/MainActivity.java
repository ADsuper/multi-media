package io.github.adsuper.multi_media.ui;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.adsuper.multi_media.R;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewpager_main)
    ViewPager viewpagerMain;
    //底部导航
    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView bottomNavigationView;
    //侧滑菜单
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.drawerlayout)
    DrawerLayout drawerlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolBar();
    }

    /**
     * 初始化 toolbar
     */
    private void initToolBar() {

        setSupportActionBar(toolbar);
    }
}
