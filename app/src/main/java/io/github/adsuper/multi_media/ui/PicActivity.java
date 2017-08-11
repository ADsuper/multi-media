package io.github.adsuper.multi_media.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import io.github.adsuper.multi_media.R;
import io.github.adsuper.multi_media.adapter.PicAdapter;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PicActivity extends BaseActivity {


    private ViewPager mViewPager;
    private View mView;
    private ArrayList<String> mListUrl;
    private PicAdapter mPicAdapter;

    @Override
    protected void initOperation(Intent intent) {

        mListUrl = intent.getStringArrayListExtra("piclist");
        int currentPosition = intent.getIntExtra("position", 0);

        mPicAdapter = new PicAdapter(this, mListUrl);
        mViewPager.setAdapter(mPicAdapter);
        if (currentPosition != 0) {
            mViewPager.setCurrentItem(currentPosition);
        }
    }



    @Override
    protected View addChildContentView(LinearLayout rootLayout) {
        mView = View.inflate(this, R.layout.activity_pic, null);
        mViewPager = (ViewPager) mView.findViewById(R.id.pic_viewpager);
        return mView;
    }

    @Override
    protected String setToolbarTitle() {

        return "图片查看";
    }

    @Override
    protected void updateOptionsMenu(Menu menu) {

        menu.findItem(R.id.action_save).setVisible(false);
    }

    @Override
    protected boolean stopSwipe() {
        //取消滑动退出 Activity 效果
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_download:
                downloadImg();
                break;
            case R.id.action_share:
                startShareIntent("text/plain", "分享福利啦：" + mListUrl.get(mViewPager.getCurrentItem()));
                break;
        }
        return true;
    }

    /**
     * 下载图片到本地指定目录（使用Glide实现）
     */
    private void downloadImg() {

        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> e) throws Exception {
                Bitmap bitmap = Glide.with(PicActivity.this)
                        .load(mListUrl.get(mViewPager.getCurrentItem()))
                        .asBitmap()
                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
                e.onNext(bitmap);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Bitmap value) {
                        if (!SDCardUtils.isSDCardEnable()) {
                            ToastUtils.showShortSafe("sd卡不可用，取消保存");
                            return;
                        } else {
                            try {
                                if (saveImg(value)) {
                                    ToastUtils.showShortSafe("图片保存成功");
                                } else {
                                    ToastUtils.showShortSafe("保存图片失败");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShortSafe("保存图片失败");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 保存图片到本地
     */
    private boolean saveImg(Bitmap bitmap) throws IOException {
        String directoryPath = SDCardUtils.getSDCardPath() + "adsuper";
        FileUtils.createOrExistsDir(directoryPath);
        String temp = mListUrl.get(mViewPager.getCurrentItem());
        String fileName = temp.substring(temp.lastIndexOf("/"));
        String filePath = directoryPath + File.separator + fileName;
        return ImageUtils.save(bitmap, filePath, Bitmap.CompressFormat.JPEG);
    }


}
