package in.arpaul.inshorts.webservices;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Aritra on 05-08-2016.
 */
public class PostParamBuilder {

    public String prepareParam(LinkedHashMap<String, Object> hashParam) {
        StringBuilder strBuilder = new StringBuilder();

        if(hashParam != null && hashParam.size() > 0) {
            Set<String> keyset = hashParam.keySet();
            strBuilder.append("/");
            for (String key : keyset) {
                strBuilder.append(hashParam.get(key));
                break;
            }
        }
        return strBuilder.toString();
    }

    public String prepareQuery(LinkedHashMap<String, Object> querys) {
        StringBuilder strBuilder = new StringBuilder();
        try {
            if(querys != null && querys.size() > 0) {
                strBuilder.append("?");
                int i=0;
                for(Map.Entry<String, Object> e : querys.entrySet()){
                    if(e.getValue() != null){
                        strBuilder.append(e.getKey() + "=" + e.getValue().toString());
                        i++;
                        if(i < querys.size()){
                            strBuilder.append("&");
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return strBuilder.toString();
        }
    }
}
