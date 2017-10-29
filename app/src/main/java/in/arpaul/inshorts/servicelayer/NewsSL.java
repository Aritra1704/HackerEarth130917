package in.arpaul.inshorts.servicelayer;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import in.arpaul.inshorts.common.ApplicationInstance;
import in.arpaul.inshorts.dataobjects.NewsDO;
import in.arpaul.inshorts.webservices.RestAPICalls;
import in.arpaul.inshorts.webservices.WSConstants;
import in.arpaul.inshorts.webservices.WSResponse;
import in.arpaul.inshorts.webservices.WSType;

import static in.arpaul.inshorts.webservices.WSConstants.TYPE_DEFAULT;


/**
 * Created by aritrapal on 24/07/17.
 */

public class NewsSL {

    public ArrayList<NewsDO> getNewsApi(Context context, Bundle bundle, int type) {
        WSResponse response = null;
        ArrayList<NewsDO> arrNews = null;
        try {

            response = new RestAPICalls(ApplicationInstance.URL_NEWS, null, null, null, null, type).getData();
            if(response != null) {
                JSONObject joResponse = null;
                if(response.getResponseCode() == WSResponse.SUCCESS) {
                    Type listType = new TypeToken<ArrayList<NewsDO>>(){}.getType();
                    arrNews = (ArrayList<NewsDO>) new Gson().fromJson(response.getResponseMessage(), listType);
//                    joResponse = new JSONObject(response.getResponseMessage());
                } else {
//                    if(!TextUtils.isEmpty(response.getResponseMessage())) {
////                        joResponse = new JSONObject(response.getResponseMessage());
//                    }
                }
                if(joResponse != null)
                    response.setResponseMessage(joResponse.toString());
            }
        } /*catch (JSONException ex) {
            ex.printStackTrace();
        } */catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return arrNews;
        }
    }
}
