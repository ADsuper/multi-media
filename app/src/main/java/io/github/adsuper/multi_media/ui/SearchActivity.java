package io.github.adsuper.multi_media.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.adsuper.multi_media.R;
import io.github.adsuper.multi_media.common.PreferenceImpl;
import io.github.adsuper.multi_media.widget.EmptyRecyclerView;

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

    private Context mContext;
    // sp 操作的工具类
    private PreferenceImpl mPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        mContext = this;

        initSP();
    }

    private void initSP() {
        mPreference = (PreferenceImpl) PreferenceImpl.getPreference(this, HISTORY_SEARCH);
    }


    @OnClick({R.id.iv_back, R.id.et_search, R.id.iv_deleteall})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back://返回图标的点击事件
                break;
            case R.id.et_search://搜索按钮点击事件
                break;
            case R.id.iv_deleteall://历史记录删除点击事件
                break;
        }
    }
}
