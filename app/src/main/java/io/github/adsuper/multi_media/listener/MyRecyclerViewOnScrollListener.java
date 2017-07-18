package io.github.adsuper.multi_media.listener;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.List;

import io.github.adsuper.multi_media.model.ReadModel;

/**
 * 作者：luoshen/rsf411613593@gmail.com
 * 时间：2017年07月18日
 * 说明：ReadFragment 中 RecycleView 的滑动监听，主要是为了实现上拉加载功能
 *
 */

public abstract class MyRecyclerViewOnScrollListener extends RecyclerView.OnScrollListener {

    private List<ReadModel.NewslistEntity> mList;
    private SwipeRefreshLayout mRefreshLayout;

    public MyRecyclerViewOnScrollListener(List<ReadModel.NewslistEntity> list, SwipeRefreshLayout refreshLayout) {
        mList = list;
        mRefreshLayout = refreshLayout;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (mList.size() < 1) {
            return;
        }
        //如果正在下拉刷新则放弃监听状态
        if (mRefreshLayout.isRefreshing()) {
            return;
        }
        //当前RecyclerView显示出来的最后一个的item的position,默认为-1
        int lastPosition = -1;
        //当前状态为停止滑动状态SCROLL_STATE_IDLE时
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            //分别判断三种类型
            if (layoutManager instanceof GridLayoutManager) {
                //通过LayoutManager找到当前显示的最后的item的position
                lastPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof LinearLayoutManager) {
                lastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                //因为StaggeredGridLayoutManager的特殊性可能导致最后显示的item存在多个，所以这里取到的是一个数组
                //得到这个数组后再取到数组中position值最大的那个就是最后显示的position值了
                int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(lastPositions);
                lastPosition = findMax(lastPositions);
            }
            // 判断界面显示的最后item的position是否等于itemCount总数-1也就是最后一个item的position
            //如果相等则说明已经滑动到最后了
            if (lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
                //此时需要请求等过数据，显示加载更多界面
                loadMoreDate();
            }
        }
    }

    public abstract void loadMoreDate();

    /**
     * 查找一个数组中的最大值
     * @param lastPositions
     * @return
     */
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
}
