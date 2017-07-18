package io.github.adsuper.multi_media.common;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 作者：luoshen/rsf411613593@gmail.com
 * 时间：2017年07月18日
 * 说明：
 */

public class Utils {

    /**
     * 根据指定格式字符串返回日期
     *
     * @param dateStr
     * @return
     */
    public static Date formatDateFromStr(final String dateStr) {
        Date date = new Date();
        if (!TextUtils.isEmpty(dateStr)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
            try {
                date = sdf.parse(dateStr);
            } catch (Exception e) {
                System.out.print("Error,format Date error");
            }
        }
        return date;
    }
}
