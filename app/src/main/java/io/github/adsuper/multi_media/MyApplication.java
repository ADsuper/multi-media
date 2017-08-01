package io.github.adsuper.multi_media;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

/**
 * 作者：luoshen/rsf411613593@gmail.com
 * 时间：2017年08月01日
 * 说明：
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //工具类初始化
        Utils.init(this);

    }
}
