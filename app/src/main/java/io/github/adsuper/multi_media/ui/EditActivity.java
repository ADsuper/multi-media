package io.github.adsuper.multi_media.ui;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;

import io.github.adsuper.multi_media.R;

/**
 * 编辑页面
 */
public class EditActivity extends BaseActivity implements View.OnClickListener {

    // 编辑页面布局
    private View mInflate;
    private TextInputLayout mLayoutNickname;//昵称
    private TextInputLayout mLayoutBlog;//blog
    private TextInputLayout mLayoutOther;//其他地址
    private Button mBtnSave;//保存按钮
    private String mBlog;
    private String mNickName;
    private String mOther;

    @Override
    protected void initOperation(Intent intent) {


    }

    @Override
    protected View addChildContentView(LinearLayout rootLayout) {
        mInflate = View.inflate(this, R.layout.activity_edit, null);
        initView();
        return mInflate;
    }

    /**
     * 查找控件，设置保存按钮点击事件
     */
    private void initView() {
        mLayoutNickname = (TextInputLayout) mInflate.findViewById(R.id.layout_nickname);
        mLayoutBlog = (TextInputLayout) mInflate.findViewById(R.id.layout_blog);
        mLayoutOther = (TextInputLayout) mInflate.findViewById(R.id.layout_other);
        mBtnSave =(Button)mInflate.findViewById(R.id.btn_save);
        mBtnSave.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        if (!checkInfo()) {
            return;
        }
        //保存信息
        SPUtils.getInstance().put("nickname", mNickName);
        SPUtils.getInstance().put("blogurl", mBlog);
        SPUtils.getInstance().put("otherurl", mOther);
        finish();
    }

    private boolean checkInfo() {
        mNickName = mLayoutNickname.getEditText().getText().toString().trim();
        if (StringUtils.isEmpty(mNickName)) {
            mLayoutNickname.setError("请填写昵称");
            return false;
        }
        mBlog = mLayoutBlog.getEditText().getText().toString().trim();
        if (StringUtils.isEmpty(mBlog)) {
            mLayoutBlog.setError("请填写博客");
            return false;
        }
        mOther = mLayoutOther.getEditText().getText().toString().trim();
        if (StringUtils.isEmpty(mOther)) {
            mLayoutOther.setError("请填写其他地址");
            return false;
        }

        mLayoutNickname.setError("");
        mLayoutBlog.setError("");
        mLayoutOther.setError("");

        return true;
    }

    @Override
    protected void updateOptionsMenu(Menu menu) {
        super.updateOptionsMenu(menu);
        menu.findItem(R.id.action_save).setVisible(false);
        menu.findItem(R.id.action_share).setVisible(false);
        menu.findItem(R.id.action_download).setVisible(false);
    }

    @Override
    protected String setToolbarTitle() {
        return "编辑~";
    }
}
