package io.github.adsuper.multi_media.common;

/**
 * 作者：luoshen/rsf411613593@gmail.com
 * 时间：2017年07月17日
 * 说明：常量类
 */

public class Constant {
    //网络请求
    public static String BASE_URL = "http://gank.io/api/";
    public static String BASE_URL_READ = "http://api.tianapi.com/";
    public static final String APIKEY = "692622924e6ae9d170868b43dbb91956";
    //每次请求大小
    public static final int PAGE_SIZE = 10;

    //侧滑菜单的额条目
    public static String CATEGORY_ALL = "all";
    public static String CATEGORY_VIDEO = "休息视频";
    public static String CATEGORY_EXPANDRESOURCE = "拓展资源";
    public static String CATEGORY_CLIENT = "前端";
    public static String CATEGROY_RECOMMEND = "瞎推荐";
    public static String CATEGORY_APP = "App";

    //home 导航栏的 tab
    public static String home_tab1 = "Android";
    public static String home_tab2 = "IOS";
    public static String home_tab3 = "福利";

    //更新数据类型 0:正常加载、下拉刷新   1: 加载更多
    public static final int GET_DATA_TYPE_NOMAL = 0;
    public static final int GET_DATA_TYPE_LOADMORE = 1;

    //列表条目布局类型 0：文字布局（Android ,iOS Fragement）  1：图片布局（福利 Fragement）
    public static final int ITEM_TYPE_TEXT = 0;
    public static final int ITEM_TYPE_IMAGE = 1;

}
