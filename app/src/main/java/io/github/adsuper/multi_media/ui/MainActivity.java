package io.github.adsuper.multi_media.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.adsuper.multi_media.R;
import io.github.adsuper.multi_media.adapter.MainViewPagerAdapter;
import io.github.adsuper.multi_media.common.Constant;
import io.github.adsuper.multi_media.fragment.HomeFragment;
import io.github.adsuper.multi_media.fragment.MeFragment;
import io.github.adsuper.multi_media.fragment.ReadFragment;

public class MainActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener
         {

    //底部导航栏对应的三个页面
    private final int NAVIGATION_HOME = 0;
    private final int NAVIGATION_READ = 1;
    private final int NAVIGATION_ME = 2;

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
    //viewPager 数据源
    private List<Fragment> mList;
    //toolbar 右侧 menu
    private Menu mMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolBar();
        initClickEvent();
        initAdapter();

    }

    /**
     * 设置点击事件
     */
    private void initClickEvent() {
        //设置底部导航栏按钮点击监听
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        //侧滑菜单的 item 点击事件
        navigationView.setNavigationItemSelectedListener(new DrawerLayoutItemSelectListener());
    }

    /**
     * 初始化 viewPager 的适配器
     */
    private void initAdapter() {

        initAdapterData();
        MainViewPagerAdapter mMainViewPagerAdapter = new MainViewPagerAdapter(
                getSupportFragmentManager(),getApplicationContext(),mList);
        viewpagerMain.setAdapter(mMainViewPagerAdapter);
        viewpagerMain.addOnPageChangeListener(new MyOnPageChangeListener());
//        viewpagerMain.setCurrentItem(0);
    }

    /**
     * 初始化 viewPager 适配器数据源
     */
    private void initAdapterData() {
        mList = new ArrayList<>();
        mList.add(new HomeFragment());
        mList.add(new ReadFragment());
        mList.add(new MeFragment());

    }

    /**
     * 初始化 toolbar
     */
    private void initToolBar() {

        setSupportActionBar(toolbar);
    }

    /**
     * 实现 BottomNavigationView.OnNavigationItemReselectedListener 的方法
     * BottomNavigationView 底部导航栏按钮点击事件处理
     * @param item
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                viewpagerMain.setCurrentItem(NAVIGATION_HOME);
                return true;
            case R.id.navigation_read:
                viewpagerMain.setCurrentItem(NAVIGATION_READ);
                return true;
            case R.id.navigation_me:
                viewpagerMain.setCurrentItem(NAVIGATION_ME);
                return true;
        }
        return false;
    }

    /**
     * viewPager 页面改变监听
     */
    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case NAVIGATION_HOME:
                    bottomNavigationView.setSelectedItemId(R.id.navigation_home);
                    break;
                case NAVIGATION_READ:
                    bottomNavigationView.setSelectedItemId(R.id.navigation_read);
                    break;
                case NAVIGATION_ME:
                    bottomNavigationView.setSelectedItemId(R.id.navigation_me);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 侧滑菜单中的 条目点击事件
     */
    private class DrawerLayoutItemSelectListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            String categroy = Constant.CATEGORY_ALL;
            switch (id) {
                case R.id.menu_draw_client:
                    categroy = Constant.CATEGORY_CLIENT;
                    showCategoryInfo(categroy);
                    break;
                case R.id.menu_draw_recommend:
                    categroy = Constant.CATEGROY_RECOMMEND;
                    showCategoryInfo(categroy);
                    break;
                case R.id.menu_draw_app:
                    categroy = Constant.CATEGORY_APP;
                    showCategoryInfo(categroy);
                    break;
                case R.id.menu_draw_expandresource:
                    categroy = Constant.CATEGORY_EXPANDRESOURCE;
                    showCategoryInfo(categroy);
                    break;
                case R.id.menu_draw_video:
                    categroy = Constant.CATEGORY_VIDEO;
                    showCategoryInfo(categroy);
                    break;
                case R.id.menu_draw_theme:
                    Intent intent = new Intent(MainActivity.this,ThemeActivity.class);
                    startActivity(intent);
                    break;
            }
            item.setCheckable(true);
            drawerlayout.closeDrawer(GravityCompat.START);
            return true;
        }
    }

    /**
     * 跳转到分类展示界面
     *
     * @param categroy
     */
    private void showCategoryInfo(String categroy) {
        Intent intent = new Intent(this, OtherCategoryActivity.class);
        intent.putExtra("categroy", categroy);
        startActivity(intent);
    }

    /**
     * toolbar 右侧菜单项
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main_toolbar,menu);
        mMenu = menu;
        return true;
    }

    /**
     * toolbar 右侧菜单项 点击事件
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            startActivity(new Intent(this, SearchActivity.class));
        }
        if (item.getItemId() == R.id.action_edit) {
            startActivity(new Intent(this, EditActivity.class));
        }
        return true;
    }
}
