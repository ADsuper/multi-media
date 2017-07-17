package io.github.adsuper.multi_media.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 作者：luoshen/rsf411613593@gmail.com
 * 时间：2017年07月17日
 * 说明：主页面 MainActivity 中 ViewPager 的适配器
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private List<Fragment> mList;

    public MainViewPagerAdapter(FragmentManager fm, Context mContext, List<Fragment> mList) {
        super(fm);
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }


    public void setmList(List<Fragment> mList) {
        this.mList = mList;
    }
}
