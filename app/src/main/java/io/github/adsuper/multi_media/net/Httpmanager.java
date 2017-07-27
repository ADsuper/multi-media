package io.github.adsuper.multi_media.net;

import java.util.concurrent.TimeUnit;

import io.github.adsuper.multi_media.common.Constant;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者：luoshen/rsf411613593@gmail.com
 * 时间：2017年07月18日
 * 说明：
 */

public class Httpmanager {
    private static Httpmanager httpManager;

    private Httpmanager() {
    }

    public synchronized static Httpmanager getInstance() {
        if (httpManager == null) {
            httpManager = new Httpmanager();
        }
        return httpManager;
    }

    /**
     * 获取对应的接口服务
     *
     * @return Api
     */
    public Api getApiService() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(15, TimeUnit.SECONDS);
        builder.connectTimeout(10, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build();
        Api api = retrofit.create(Api.class);
        return api;
    }

    /**
     * 获取对应的接口服务
     *
     * @return Api
     */
    public Api getApiService(String baseUrl) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(15, TimeUnit.SECONDS);
        builder.connectTimeout(10, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build();
        Api api = retrofit.create(Api.class);
        return api;
    }
}
