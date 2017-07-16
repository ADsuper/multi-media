package io.github.adsuper.multi_media.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.adsuper.multi_media.MainActivity;
import io.github.adsuper.multi_media.R;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class GuideActivity extends AppCompatActivity {

    @BindView(R.id.image_guide)
    ImageView imageGuide;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_blog)
    TextView tvBlog;
    //倒计时方法
    private CountDownTimer mCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);

        setImageGuide();

        setCountDownTimer(4000,1000);
    }

    /**
     * 设置倒计时
     * @param millisInFuture    倒计时总时间
     * @param countDownInterval 倒计时间隔时间
     */
    private void setCountDownTimer(long millisInFuture, final long countDownInterval) {

        if (mCountDownTimer != null) {
            mCountDownTimer.start();
        }

        mCountDownTimer = new CountDownTimer(millisInFuture,countDownInterval) {
            //回调方法中可以直接更新UI

            /**
             * 每完成一次倒计时间隔时间时回调
             * @param millisUntilFinished  剩余总时间
             */
            @Override
            public void onTick(long millisUntilFinished) {

                tvTime.setText(millisUntilFinished / countDownInterval +" s");

            }

            /**
             * 当前任务完成的时候回调
             */
            @Override
            public void onFinish() {
                setupWindowAnimations();
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                finish();

            }

        }.start();
    }

    /**
     * 设置5.0以上转场动画
     */
    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(3000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(fade);
        }
    }

    /**
     * 设置引导页面背景图片
     */
    private void setImageGuide() {
        Glide.with(this)
                .load(R.drawable.guide_bg_me)
                //设置高斯模糊，第二哥参数：模糊度/离散半径，默认25，越大越模糊
                //第三个参数：取样，默认1，2表示：横向、纵向都会每两个像素点取一个像素点(即:图片宽高变为原来一半)
                .bitmapTransform(new BlurTransformation(this,25,1))
                .into(imageGuide);
    }

    @Override
    protected void onDestroy() {

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }

        super.onDestroy();

    }
}
