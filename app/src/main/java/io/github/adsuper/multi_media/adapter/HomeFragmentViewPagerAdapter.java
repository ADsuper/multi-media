package io.github.adsuper.multi_media.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/7/17 0017.
 * HomeFragment中的ViewPage适配器
 */

public class HomeFragmentViewPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private List<Fragment> mList;
    private String[] mTitles;

    public HomeFragmentViewPagerAdapter(FragmentManager fm, Context mContext, List<Fragment> mList, String[] mTitles) {
        super(fm);
        this.mList = mList;
        this.mContext = mContext;
        this.mTitles = mTitles;
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

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
