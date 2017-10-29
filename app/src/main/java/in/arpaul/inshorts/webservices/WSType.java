package in.arpaul.inshorts.webservices;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Aritra on 01-08-2016.
 */
public class WSType {

    @IntDef({WSTypePref.GET, WSTypePref.POST, WSTypePref.PUT, WSTypePref.DELETE, WSTypePref.DOWNLOAD_FILE, WSTypePref.UPLOAD_FILE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface WSTypePref {
        int GET             = 101;
        int POST            = 102;
        int PUT             = 103;
        int DELETE          = 104;
        int DOWNLOAD_FILE   = 105;
        int UPLOAD_FILE     = 106;
    };
}
