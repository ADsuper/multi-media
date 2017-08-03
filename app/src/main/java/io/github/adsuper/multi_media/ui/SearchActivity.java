package io.github.adsuper.multi_media.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.flexbox.FlexboxLayout;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.adsuper.multi_media.R;
import io.github.adsuper.multi_media.adapter.HistorySearchAdapter;
import io.github.adsuper.multi_media.adapter.HomeRecyclerviewAdapter;
import io.github.adsuper.multi_media.common.Constant;
import io.github.adsuper.multi_media.common.PreferenceImpl;
import io.github.adsuper.multi_media.model.GankModel;
import io.github.adsuper.multi_media.net.Api;
import io.github.adsuper.multi_media.net.Httpmanager;
import io.github.adsuper.multi_media.widget.EmptyRecyclerView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 搜索页面
 */
public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_search)
    TextView mTvSearch;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.flexbox_layout)
    FlexboxLayout mFlexboxLayout;
    @BindView(R.id.iv_deleteall)
    ImageView mIvDeleteall;
    @BindView(R.id.layout_history)
    RelativeLayout mLayoutHistory;
    @BindView(R.id.avi)
    AVLoadingIndicatorView mAvi;
    @BindView(R.id.avi_loadmore)
    AVLoadingIndicatorView mAviLoadmore;
    @BindView(R.id.layout_loadmore)
    LinearLayout mLayoutLoadmore;
    @BindView(R.id.recyclerview_history)
    EmptyRecyclerView mRecyclerviewHistory;
    @BindView(R.id.activity_search)
    LinearLayout mActivitySearch;
    /**
     * SharedPreferences 存储的历史搜索记录文件夹名称
     */
    private final String HISTORY_SEARCH = "history_search";
    @BindView(R.id.tv_hotsearch)
    TextView mTvHotsearch;

    private Context mContext;
    // sp 操作的工具类
    private PreferenceImpl mPreference;
    //历史搜索记录
    private List<String> mHistorySearchList;
    private HistorySearchAdapter mHistorySearchAdapter;
    private List<String> mHotSearchList;
    //搜索关键字
    private String mKeywords;
    //搜索结果
    protected List<GankModel.ResultsEntity> mSearchResultList = new ArrayList<>();
    private HomeRecyclerviewAdapter mHomeRecyclerviewAdapter;

    private boolean mIsLoadMore = true;//是否可以加载更多

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        SwipeBackHelper.onCreate(this);//activity 滑动关闭库 SwipeBackHelper

        ButterKnife.bind(this);
        mContext = this;


        initSP();
        initHistorySearch();
        initFlexboxLayout();
        initHistorySearchAdapter();
        stopLoading();
        initListener();
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
     * 初始化各种监听事件
     */
    private void initListener() {
        //设置历史搜索记录条目点击事件
        mHistorySearchAdapter.setmOnMyClickListener(new HistorySearchAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                mKeywords = mHistorySearchList.get(position);
                mEtSearch.setText(mKeywords);
                showSearchResult(true);
                startLoading();
                getDataFromServer(mKeywords, Constant.GET_DATA_TYPE_NOMAL);
            }
        });
        //监听上拉加载更多
        mRecyclerviewHistory.addOnScrollListener(new RecyclerViewScrollListener());
        //监听搜索结果页面的条目点击事件
        mHomeRecyclerviewAdapter = new HomeRecyclerviewAdapter(this, mSearchResultList, Constant.ITEM_TYPE_TEXT);
        mHomeRecyclerviewAdapter.addOnClickListener(new HomeRecyclerviewAdapter.OnBaseClickListener() {
            @Override
            public void onClick(int position, GankModel.ResultsEntity entity) {
                //详情页面
                Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
                intent.putExtra("entity",entity);
                startActivity(intent);
            }

            @Override
            public void onCoverClick(int position, GankModel.ResultsEntity entity) {
                //展示大图页面
                Intent intent = new Intent(SearchActivity.this, PicActivity.class);
                ArrayList<String> mListPicUrls = new ArrayList<String>();
                mListPicUrls = (ArrayList) entity.getImages();
                intent.putStringArrayListExtra("piclist", mListPicUrls);
                startActivity(intent);
            }
        });
    }

    /**
     * 动态设置 FlexboxLayout 的子布局
     */
    private void initFlexboxLayout() {
        if (mHotSearchList != null) {
            mHotSearchList = null;
        }
        mHotSearchList = new ArrayList<>();
        mHotSearchList.add("RxJava");
        mHotSearchList.add("RxAndroid");
        mHotSearchList.add("数据库");
        mHotSearchList.add("自定义控件");
        mHotSearchList.add("下拉刷新");
        mHotSearchList.add("mvp");
        mHotSearchList.add("直播");
        mHotSearchList.add("权限管理");
        mHotSearchList.add("Retrofit");
        mHotSearchList.add("OkHttp");
        mHotSearchList.add("WebWiew");
        mHotSearchList.add("热修复");
        // 通过代码向FlexboxLayout添加View
        for (int i = 0; i < mHotSearchList.size(); i++) {
            TextView textView = new TextView(this);
            textView.setBackground(getResources().getDrawable(R.drawable.flexbox_text_bg));
            textView.setText(mHotSearchList.get(i));
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(30, 30, 30, 30);
            textView.setClickable(true);
            textView.setFocusable(true);
            textView.setTextColor(getResources().getColor(R.color.flexbox_text_color));
            mFlexboxLayout.addView(textView);
            //通过FlexboxLayout.LayoutParams 设置子元素支持的属性
            ViewGroup.LayoutParams params = textView.getLayoutParams();
            if (params instanceof FlexboxLayout.LayoutParams) {
                FlexboxLayout.LayoutParams layoutParams = (FlexboxLayout.LayoutParams) params;
                //layoutParams.setFlexBasisPercent(0.5f);
                layoutParams.setMargins(10, 10, 20, 10);
            }

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tv = (TextView) v;
                    //放入历史搜索记录
                    mHistorySearchList.add(tv.getText().toString().trim());
                    mPreference.putAll(HISTORY_SEARCH, mHistorySearchList);
                    //得到搜索条件，首先屏蔽掉历史搜索和热门搜索
                    showSearchResult(true);
                    //发起服务请求
                    mKeywords = tv.getText().toString().trim();
                    startLoading();
                    mEtSearch.setText(mKeywords);
                    getDataFromServer(mKeywords, Constant.GET_DATA_TYPE_NOMAL);
                }
            });
        }

    }

    /**
     * 初始化历史搜索记录的 adapter
     */
    private void initHistorySearchAdapter() {

        mHistorySearchAdapter = new HistorySearchAdapter(this, mHistorySearchList);
        mRecyclerviewHistory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerviewHistory.setAdapter(mHistorySearchAdapter);
        mRecyclerviewHistory.setmEmptyView(new TextView(this));
    }

    /**
     * 获取历史搜索记录
     */
    private void initHistorySearch() {
        mHistorySearchList = mPreference.getAll(HISTORY_SEARCH);
        if (mHistorySearchList == null) {
            mHistorySearchList = new ArrayList<>();
        }
    }

    /**
     * 初始化 sp
     */
    private void initSP() {
        mPreference = (PreferenceImpl) PreferenceImpl.getPreference(this, HISTORY_SEARCH);
    }


    @OnClick({R.id.iv_back, R.id.tv_search, R.id.iv_deleteall})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back://返回图标的点击事件
                finish();
                break;
            case R.id.tv_search://搜索按钮点击事件
                mKeywords = mEtSearch.getText().toString().trim();
                if (StringUtils.isEmpty(mKeywords)) {
                    ToastUtils.showShortSafe("请输入搜索条件");
                    return;
                }
                mHistorySearchList.add(mKeywords);
                mPreference.putAll(HISTORY_SEARCH, mHistorySearchList);
                showSearchResult(true);
                startLoading();
                getDataFromServer(mKeywords, Constant.GET_DATA_TYPE_NOMAL);
                break;
            case R.id.iv_deleteall://历史记录删除全部点击事件
                mPreference.remove(HISTORY_SEARCH);
                mHistorySearchList.clear();
                mHistorySearchAdapter.setmListData(mPreference.getAll(HISTORY_SEARCH));
                mHistorySearchAdapter.notifyDataSetChanged();
                break;
        }
    }
    /**
     * 开启加载中动画
     */
    public void startLoading() {
        mAvi.setVisibility(View.VISIBLE);
        mRecyclerviewHistory.setVisibility(View.GONE);
        mAvi.smoothToShow();
    }

    /**
     * 开启加载更多动画
     */
    public void startLoadingMore() {
        stopLoading();
        mLayoutLoadmore.setVisibility(View.VISIBLE);
        mAviLoadmore.smoothToShow();
    }

    /**
     * 关闭加载中动画
     */
    public void stopLoading() {
        mAvi.setVisibility(View.GONE);
        mRecyclerviewHistory.setVisibility(View.VISIBLE);
        mAvi.smoothToHide();
    }

    /**
     * 关闭加载更多动画
     */
    public void stopLoadingMore() {
        mLayoutLoadmore.setVisibility(View.GONE);
        mAviLoadmore.smoothToHide();
    }
    /**
     * RecyclerView 滑动监听器
     */
    class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (mSearchResultList.size() < 1) {
                return;
            }
            //如果当前是显示搜索历史则不响应上拉事件
            if (mRecyclerviewHistory.getAdapter() instanceof HistorySearchAdapter) {
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
                    if (!mIsLoadMore) {
                        ToastUtils.showShortSafe("木有更多数据了...");
                        return;
                    }

                    startLoadingMore();
                    getDataFromServer(mKeywords, Constant.GET_DATA_TYPE_LOADMORE);
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
    private int mPage = 1;
    private Disposable mDisposable;
    /**
     * 发起搜索请求
     */
    private void getDataFromServer(String searchkey, final int type) {
        Api api = Httpmanager.getInstance().getApiService();
        api.getSearchData(searchkey, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GankModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(GankModel value) {
                        if (value.getError()) {
                            //服务端请求数据发生错误
                            ToastUtils.showShortSafe("服务端异常，请稍后再试");
                            return;
                        }
                        //更新界面数据
                        if (Constant.GET_DATA_TYPE_NOMAL == type) {
                            //正常模式下，清空之前数据，重新加载
                            mSearchResultList.clear();
                            mSearchResultList = value.getResults();
                        } else {
                            //加载更多模式
                            mSearchResultList.addAll(value.getResults());
                        }
                        //如果获取的数据不足一页，代表当前已经没有更过数据，关闭加载更多
                        if (value.getResults().size() < Constant.PAGE_SIZE) {
                            mIsLoadMore = false;
                        }

                        if (mRecyclerviewHistory.getAdapter() instanceof HistorySearchAdapter) {
                            mRecyclerviewHistory.setAdapter(mHomeRecyclerviewAdapter);
                        }
                        mHomeRecyclerviewAdapter.setmListData(mSearchResultList);
                        mHomeRecyclerviewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopLoading();
                        stopLoadingMore();
                    }

                    @Override
                    public void onComplete() {
                        stopLoading();
                        stopLoadingMore();
                    }
                });
    }
    /**
     * 是否显示搜索结果,显示时需要隐藏掉热门搜索和历史搜索条件
     *
     * @param flag
     */
    public void showSearchResult(boolean flag) {
        if (flag) {
            mTvHotsearch.setVisibility(View.GONE);
            mLayoutHistory.setVisibility(View.GONE);
            mFlexboxLayout.setVisibility(View.GONE);
        } else {
            mTvHotsearch.setVisibility(View.VISIBLE);
            mLayoutHistory.setVisibility(View.VISIBLE);
            mFlexboxLayout.setVisibility(View.VISIBLE);
        }
    }
}
