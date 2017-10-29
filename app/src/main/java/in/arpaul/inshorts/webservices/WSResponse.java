package in.arpaul.inshorts.webservices;

import android.support.annotation.IntDef;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Aritra on 01-08-2016.
 */
public class WSResponse implements Serializable {

    public static final int SUCCESS = 1;
    public static final int FAILURE = 0;


    @IntDef({SUCCESS, FAILURE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface WSResponsePref {};

    private int responseCode;
    private String responseMessage;

    public int getResponseCode(){
        return responseCode;
    }

    public String getResponseMessage(){
        return responseMessage;
    }

    public void setResponseCode(int responseCode){
        this.responseCode = responseCode;
    }

    public void setResponseMessage(String responseMessage){
        this.responseMessage = responseMessage;
    }
}
