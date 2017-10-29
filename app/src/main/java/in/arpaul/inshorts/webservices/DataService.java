package in.arpaul.inshorts.webservices;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import in.arpaul.inshorts.common.AppPreference;
import in.arpaul.inshorts.servicelayer.NewsSL;

import static in.arpaul.inshorts.webservices.WSCall.WSCallPref.FETCHNEWS;
import static in.arpaul.inshorts.webservices.WSConstants.TYPE_DEFAULT;


/**
 * Created by aritrapal on 19/07/17.
 */

public class DataService extends AsyncTaskLoader {

    private Context context;
    private Bundle bundle;
    private int type = TYPE_DEFAULT;
    private int call = TYPE_DEFAULT;
    private AppPreference preference;

    public DataService(Context context, Bundle bundle, @WSCall.WSCallPref int call){
        super(context);
        this.context = context;
        this.bundle = bundle;
        this.type = WSType.WSTypePref.POST;
        this.call = call;

        preference = new AppPreference(context);
    }

    public DataService(Context context, Bundle bundle, @WSType.WSTypePref int type, @WSCall.WSCallPref int call){
        super(context);
        this.context = context;
        this.bundle = bundle;
        this.type = type;
        this.call = call;

        preference = new AppPreference(context);
    }

    @Override
    public Object loadInBackground() {

        switch (call) {
            case FETCHNEWS:
                return new NewsSL().getNewsApi(context, bundle, type);

            default:
                return null;
        }
    }
}
