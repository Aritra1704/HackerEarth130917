package in.arpaul.inshorts.common;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by aritrapal on 30/06/17.
 */

public class AppConstant {
    public static final String EXTERNAL_FOLDER              = "/EZOrderBiz/";

    public static final String BUNDLE_DISPLAY               = "BUNDLE_DISPLAY";

    public static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }
}
