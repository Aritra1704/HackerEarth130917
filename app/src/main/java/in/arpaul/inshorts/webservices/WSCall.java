package in.arpaul.inshorts.webservices;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by aritrapal on 19/07/17.
 */

public class WSCall {
    @IntDef({WSCallPref.FETCHNEWS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface WSCallPref {
        int FETCHNEWS          = 501;
    };
}
