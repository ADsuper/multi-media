package io.github.adsuper.multi_media.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.github.adsuper.multi_media.R;
import io.github.adsuper.multi_media.adapter.ReadFragmentAdapter;
import io.github.adsuper.multi_media.common.Constant;
import io.github.adsuper.multi_media.model.ReadModel;
import io.github.adsuper.multi_media.net.Httpmanager;
import io.github.adsuper.multi_media.ui.MainActivity;
import io.github.adsuper.multi_media.ui.PicActivity;
import io.github.adsuper.multi_media.ui.ReadDetailActivity;
import io.github.adsuper.multi_media.widget.EmptyRecyclerView;
import io.github.adsuper.multi_media.widget.MyItemDecoration;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者：luoshen/rsf411613593@gmail.com
 * 时间：2017年07月17日
 * 说明：底部导航 read 对应的 fragment
 */

public class ReadFragment extends Fragment {

    @BindView(R.id.tv_nonetwork)
    TextView mTvNonetwork;
    @BindView(R.id.avi_loadmore)
    AVLoadingIndicatorView mAviLoadmore;
    @BindView(R.id.layout_loadmore)
    LinearLayout mLayoutLoadmore;
    @BindView(R.id.fragment_recyclerview)
    EmptyRecyclerView mFragmentRecyclerview;
    @BindView(R.id.tv_empty)
    TextView mTvEmpty;
    @BindView(R.id.iv_empty)
    ImageView mIvEmpty;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.menu_item_linearlayout)
    FloatingActionButton mMenuItemLinearlayout;
    @BindView(R.id.menu_item_gridlayout)
    FloatingActionButton mMenuItemGridlayout;
    @BindView(R.id.menu_item_staggeredlayout)
    FloatingActionButton mMenuItemStaggeredlayout;
    @BindView(R.id.actionmenu)
    FloatingActionMenu mActionmenu;
    Unbinder unbinder;
    @BindView(R.id.avi)
    AVLoadingIndicatorView mAvi;
    //布局填充 view
    private View mInflateView;
    //宿主 Activity
    private MainActivity mMainActivity;
    //RecycleView 数据源
    private List<ReadModel.NewslistEntity> mList = new ArrayList<>();
    //适配器
    private ReadFragmentAdapter mReadFragmentAdapter;
    private Observer<ReadModel> mObserver;

    private int mPage = 1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mInflateView == null) {
            mInflateView = inflater.inflate(R.layout.fragment_android_ios_fuli_read, container, false);
        }
        unbinder = ButterKnife.bind(this, mInflateView);
        return mInflateView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSwipeRefreshLayout();
        initAdapter(view);
        initListener();
//        if (isConnected()) {
            startLoading();
            getDataFromServer(Constant.GET_DATA_TYPE_NOMAL);
//        }

    }

    /**
     * 设置 recyclerview 的条目点击事件
     */
    private void initListener() {
       mReadFragmentAdapter.addOnClickListener(new ReadFragmentAdapter.OnBaseClickListener() {
           @Override
           public void onClick(int position, ReadModel.NewslistEntity entity) {
               Intent intent = new Intent(mMainActivity, ReadDetailActivity.class);
               intent.putExtra("entity", entity);
               startActivity(intent);
           }

           @Override
           public void onCoverClick(int position, ReadModel.NewslistEntity entity) {

               Intent intent = new Intent(mMainActivity, PicActivity.class);
               ArrayList<String> listPicUrl = new ArrayList<String>();
               listPicUrl.add(entity.getPicUrl());
               intent.putStringArrayListExtra("piclist",listPicUrl);
               startActivity(intent);
           }
       });
    }

    /**
     * 初始化 recyclerview 的适配器
     */
    private void initAdapter(View view) {
        //设置分割线
        mFragmentRecyclerview.addItemDecoration(new MyItemDecoration(getActivity(),LinearLayoutManager.VERTICAL));
        //设置布局样式
        mFragmentRecyclerview.setLayoutManager(new LinearLayoutManager(mMainActivity, LinearLayoutManager.VERTICAL, false));
        mReadFragmentAdapter = new ReadFragmentAdapter(mMainActivity, mList, Constant.ITEM_TYPE_TEXT);
        mFragmentRecyclerview.setAdapter(mReadFragmentAdapter);
        //根据数据源来判断是否显示空白 view
        mFragmentRecyclerview.setmEmptyView(view.findViewById(R.id.empty_view));
        mFragmentRecyclerview.hideEmptyView();
    }

    /**
     * 初始化下拉刷新控件 SwipeRefreshLayout
     */
    private void initSwipeRefreshLayout() {
        //设置刷新进度条的颜色变化，最多可以设置 4 种，加载的颜色是循环播放的。
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        //设置下拉刷新事件
        mSwipeRefreshLayout.setOnRefreshListener(new MySwipeRefreshLayout());
        //设置上拉加载更多
        mFragmentRecyclerview.addOnScrollListener(new RecyclerViewScrollListener());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.tv_nonetwork)
    public void onViewClicked() {
        //打开网络设置界面
        NetworkUtils.openWirelessSettings();
    }

    /**
     * 开启加载中动画
     */
    public void startLoading() {
        mAvi.smoothToShow();
    }

    /**
     * 开启加载更多动画
     */
    public void startLoadingMore() {
        mLayoutLoadmore.setVisibility(View.VISIBLE);
        mAviLoadmore.smoothToShow();
    }

    /**
     * 关闭加载中动画
     */
    public void stopLoading() {
        if (mAvi.isShown()) {
            mAvi.smoothToHide();
        }
    }

    /**
     * 关闭加载更多动画
     */
    public void stopLoadingMore() {
        mLayoutLoadmore.setVisibility(View.GONE);
        mAviLoadmore.smoothToHide();
    }
    /**
     * 停止下拉刷新
     */
    public void stopRefreshing() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * SwipeRefreshLayout 下拉刷新监听
     */
    private class MySwipeRefreshLayout implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            //TODO 下拉刷新逻辑操作
            mPage = 1;
            getDataFromServer(Constant.GET_DATA_TYPE_NOMAL);
        }
    }
    private Disposable mDisposable;

    /**
     * 从网络获取数据
     * @param type
     */
    public void getDataFromServer(final int type) {
        Httpmanager.getInstance()
                .getApiService(Constant.BASE_URL_READ)
                .getReadData(Constant.APIKEY, Constant.PAGE_SIZE, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(type));
    }

    @NonNull
    private Observer<ReadModel> getObserver(final int type) {

        mObserver = new Observer<ReadModel>() {
           @Override
           public void onSubscribe(Disposable d) {
               //此方法在主线程调用，可以做一些准备工作，比如显示一个loading框
               mDisposable = d;
           }

           @Override
           public void onNext(ReadModel value) {
               if (value.getCode() != 200) {
                   //服务端请求数据发生错误
                   ToastUtils.showShortSafe("服务端异常，请稍后再试");
                   return;
               }
               //更新界面数据
               if (Constant.GET_DATA_TYPE_NOMAL == type) {
                   //正常模式下，清空之前数据，重新加载
                   mList.clear();
                   mList = value.getNewslist();
               } else {
                   //加载更多模式
                   mList.addAll(value.getNewslist());
               }

               mReadFragmentAdapter.setmListData(mList);
               mReadFragmentAdapter.notifyDataSetChanged();
           }

           @Override
           public void onError(Throwable e) {
               stopRefreshing();
               stopLoading();
               stopLoadingMore();
           }

           @Override
           public void onComplete() {
               stopRefreshing();
               stopLoading();
               stopLoadingMore();
           }
       };
        return mObserver;
    }

    /**
     * RecyclerView 滑动监听器，实现上拉加载更多
     */
    class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (mList.size() < 1) {
                return;
            }
            //如果正在下拉刷新则放弃监听状态
            if (mSwipeRefreshLayout.isRefreshing()) {
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
                if (!recyclerView.canScrollVertically(1)) {
                    //此时需要请求等过数据，显示加载更多界面
                    recyclerView.smoothScrollToPosition(lastPosition);
                    mPage++;
                    startLoadingMore();
                    getDataFromServer(Constant.GET_DATA_TYPE_LOADMORE);
                }
            }
        }
    }

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
