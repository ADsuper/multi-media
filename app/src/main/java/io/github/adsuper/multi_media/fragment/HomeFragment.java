package io.github.adsuper.multi_media.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.github.adsuper.multi_media.R;
import io.github.adsuper.multi_media.adapter.HomeFragmentViewPagerAdapter;
import io.github.adsuper.multi_media.common.Constant;
import io.github.adsuper.multi_media.ui.MainActivity;

/**
 * 作者：luoshen/rsf411613593@gmail.com
 * 时间：2017年07月17日
 * 说明：底部导航 home 对应的 fragment
 */

public class HomeFragment extends Fragment {
//    @BindView(R.id.tablayout)
    TabLayout tablayout;
//    @BindView(R.id.viewpager_homefragment)
    ViewPager viewpagerHomefragment;

//    Unbinder unbinder;
    //填充的布局
    private View inflate;
    //HomeFragment 的 宿主 activity
    private MainActivity mainActivity;
    //viewpager 数据源
    private List<Fragment> mListFragments;
    //pagetitle
    private String[] mTitles = new String[]{Constant.home_tab1, Constant.home_tab2, Constant.home_tab3};

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (inflater == null) {
            inflate = inflater.inflate(R.layout.fragment_home, container, false);
        }
//        unbinder = ButterKnife.bind(this, inflate);
        return inflate;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setTablayout();
        setViewpagerData();
        setViewpagerAdapter();
    }

    /**
     * 初始化 view
     * @param view
     */
    private void initView(View view) {
        tablayout = (TabLayout) view.findViewById(R.id.tablayout);
        viewpagerHomefragment = (ViewPager) view.findViewById(R.id.viewpager_homefragment);
    }

    /**
     * 设置 viewpager 的适配器
     */
    private void setViewpagerAdapter() {
        HomeFragmentViewPagerAdapter adapter =
                new HomeFragmentViewPagerAdapter(getChildFragmentManager(),
                        mainActivity, mListFragments, mTitles);
        viewpagerHomefragment.setAdapter(adapter);
        //viewpager 与 tablelayout 关联
        tablayout.setupWithViewPager(viewpagerHomefragment);
    }

    /**
     * 设置 viewpager 的数据源
     */
    private void setViewpagerData() {
        mListFragments = new ArrayList<>();
        mListFragments.add(new HomeTab1Fragment());
        mListFragments.add(new HomeTab1Fragment());
        mListFragments.add(new HomeTab3Fragment());
    }

    /**
     * 设置 tablayout 条目
     */
    private void setTablayout() {
        tablayout.addTab(tablayout.newTab().setText(Constant.home_tab1));
        tablayout.addTab(tablayout.newTab().setText(Constant.home_tab2));
        tablayout.addTab(tablayout.newTab().setText(Constant.home_tab3));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();
    }
}
