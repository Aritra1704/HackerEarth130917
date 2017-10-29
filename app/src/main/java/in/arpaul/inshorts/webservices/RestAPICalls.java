package in.arpaul.inshorts.webservices;

import android.text.TextUtils;

import com.arpaul.utilitieslib.FileUtils;
import com.arpaul.utilitieslib.JSONUtils;
import com.arpaul.utilitieslib.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import in.arpaul.inshorts.BuildConfig;
import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

import static in.arpaul.inshorts.webservices.WSConstants.CONNECT_TIMEOUT;
import static in.arpaul.inshorts.webservices.WSConstants.DOWNLOADPATH;
import static in.arpaul.inshorts.webservices.WSConstants.FILENAME;
import static in.arpaul.inshorts.webservices.WSConstants.READ_TIMEOUT;
import static in.arpaul.inshorts.webservices.WSConstants.RESPONSE_BODY;
import static in.arpaul.inshorts.webservices.WSConstants.RESPONSE_ERROR;
import static in.arpaul.inshorts.webservices.WSConstants.RESPONSE_STATUS;
import static in.arpaul.inshorts.webservices.WSConstants.RESPONSE_STATUS_TRUE;
import static in.arpaul.inshorts.webservices.WSConstants.ServerKeys.CONTENT_TYPE;
import static in.arpaul.inshorts.webservices.WSConstants.WRITE_TIMEOUT;
import static in.arpaul.inshorts.webservices.WSType.WSTypePref.DELETE;
import static in.arpaul.inshorts.webservices.WSType.WSTypePref.DOWNLOAD_FILE;
import static in.arpaul.inshorts.webservices.WSType.WSTypePref.GET;
import static in.arpaul.inshorts.webservices.WSType.WSTypePref.POST;
import static in.arpaul.inshorts.webservices.WSType.WSTypePref.PUT;
import static in.arpaul.inshorts.webservices.WSType.WSTypePref.UPLOAD_FILE;


/**
 * Created by Aritra on 01-08-2016.
 */
public class RestAPICalls {

    private final String TAG = "RestAPICalls";
    private String url = "", sessionToken = "";
    private String baseURl = BuildConfig.BASE_URL;
    private LinkedHashMap<String, Object> params = null;
    private LinkedHashMap<String, Object> query = null;
    private LinkedHashMap<String, Object> body = null;
    private static boolean DEBUG = BuildConfig.DEBUG;
    private int type;
    private final int TIMEOUT = 10000;
    private WSResponse responseDo;

    private OkHttpClient okHttpClient;
    private HttpUrl.Builder httpBuilder;
    private String param = "", queryPar = "";
    private HashMap<String, String> mHeadersMap = new HashMap<>();
    private String REQUEST_TAG = "okhttprequest";
    String attachmentFileName = "bitmap.png";

    /**
     * Rest API calls
     * @param url
     * @param params
     * @param body
     * @param type
     */
    public RestAPICalls(String url, String sessionToken, LinkedHashMap<String, Object> params, LinkedHashMap<String, Object> query, LinkedHashMap<String, Object> body, @WSType.WSTypePref int type){
        this.url                = url;
        this.sessionToken       = sessionToken;
        this.params             = params;
        this.query              = query;
        this.body               = body;
        this.type               = type;

        this.responseDo         = new WSResponse();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        builder.writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS);

        CookieJar cookieJar = new CookieJar() {
            private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url, cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url);
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        };
        builder.cookieJar(cookieJar);

        okHttpClient = builder.build();

        mHeadersMap.put(WSConstants.ServerKeys.contentType, CONTENT_TYPE);
//        mHeadersMap.put(WSConstants.ServerKeys.apiKey, WSConstants.API_KEY);
        if(!TextUtils.isEmpty(sessionToken))
            mHeadersMap.put(WSConstants.ServerKeys.sessionToken, sessionToken);
        // Session token is set by the caller.

        httpBuilder = HttpUrl.parse(baseURl + url).newBuilder();

        if(params != null && !params.isEmpty())
            param = new PostParamBuilder().prepareParam(params);

        if(query != null && !query.isEmpty()) {
            queryPar = new PostParamBuilder().prepareQuery(query);
            httpBuilder.query(queryPar);
        }

        callAPI();
    }

    public WSResponse getData(){
        return responseDo;
    }

    private void callAPI() {
        Request request;
        Request.Builder builder = new Request.Builder();

        String downloadPath = "", fileName = "";
        try {
            switch (type){
                case GET:
                    builder = getMethodCall();
                    break;

                case PUT:
                    builder = putMethodCall();
                    break;

                case POST:
                    builder = postMethodCall();
                    break;

                case DELETE:
                    builder = deleteMethodCall();
                    break;

                case DOWNLOAD_FILE:
                    builder = downloadFileCall();

                    downloadPath = (String) params.get(DOWNLOADPATH);
                    fileName = (String) params.get(FILENAME);
                    break;

                case UPLOAD_FILE:
                    builder = uploadFileCall();
                    break;
            }
            request = builder.build();
            Call call = okHttpClient.newCall(request);

            if (DEBUG && (request != null)) {
                String strType = "";
                switch (type) {
                    case GET: strType = "GET";
                        break;
                    case POST: strType = "POST";
                        break;
                    case PUT: strType = "PUT";
                        break;
                    case DELETE: strType = "DELETE";
                        break;
                    case DOWNLOAD_FILE: strType = "DOWNLOAD_FILE";
                        break;
                    case UPLOAD_FILE: strType = "UPLOAD_FILE";
                        break;
                }
                final String requestDump = String.format("****Request: %s %s\nHeaders:%s\nParams:%s\nBody:%s",
                        strType,
                        url,
                        request.headers(),
                        (params == null) ? ("empty") : (params.toString()),
                        (request.body() == null) ? ("empty") : (bodyToString(request))
                );
                LogUtils.debugLog(TAG, requestDump);
            }

            Response response = call.execute();
            if(response != null) {

                int status = response.code();
                switch (status) {
                    case WSConstants.STATUS_SUCCESS :
                    case WSConstants.STATUS_CREATED :
                    case WSConstants.STATUS_ACCEPTED :
                    case WSConstants.STATUS_NO_CONTENT :
                        responseDo.setResponseCode(WSResponse.SUCCESS);

                        if(type == DOWNLOAD_FILE)
                            FileUtils.saveInputStreamAsFile(response.body().byteStream(), downloadPath, fileName);
                        else {
                            responseDo.setResponseMessage(response.body().string());
                        }

                        break;

                    case WSConstants.STATUS_FAILED:
                    default:
                        responseDo.setResponseCode(WSResponse.FAILURE);
                        String errResp = response.body().string();
                        if(!TextUtils.isEmpty(errResp)) {
                            JSONObject joError = new JSONObject(errResp);
                            if(JSONUtils.hasJSONtag(joError, RESPONSE_ERROR)) {
                                String responseError = joError.getString(RESPONSE_ERROR);
                                responseDo.setResponseMessage(responseError);
                            }
                        } else
                            responseDo.setResponseMessage(response.body().string());

                        break;
                }
                if(DEBUG) {
                    final String responseDump = String.format("****Response: %s %s\n%s\n%s %s\n%s",
                            request.method(),
                            url,
                            request.headers(),
                            "" + response.code(),
                            (response == null) ? ("empty") : (response.message()),
                            (responseDo == null) ? ("empty") : (responseDo.getResponseMessage()));
                    LogUtils.debugLog(TAG, responseDump);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }  catch (JSONException ex) {
            ex.printStackTrace();
        } finally {

        }
    }

    /**
     * GET method call
     * @return
     */
    private Request.Builder getMethodCall() {
        Request.Builder builder = new Request.Builder();

        builder.url(httpBuilder.build() + param);
        builder.headers(Headers.of(mHeadersMap));
        builder.tag(REQUEST_TAG);

        return builder;
    }

    /**
     * PUT method call
     * @return
     */
    private Request.Builder putMethodCall() {
        Request.Builder builder = new Request.Builder();

        RequestBody requestBody = null;
        if(body != null && body.size() > 0) {
            JSONObject objBody = new JSONObject(body);
            requestBody = RequestBody.create(MediaType.parse(CONTENT_TYPE), objBody.toString());
        }

        builder.url(httpBuilder.build() + param);
        builder.headers(Headers.of(mHeadersMap));
        if(requestBody != null)
            builder.put(requestBody);
        builder.tag(REQUEST_TAG);

        return builder;
    }

    /**
     * POST method call
     * @return
     */
    private Request.Builder postMethodCall() {
        Request.Builder builder = new Request.Builder();

//        MultipartBody.Builder multiBuilder = new MultipartBody.Builder();
//
//        multiBuilder.setType(MultipartBody.FORM);
//        if(body != null && body.size() > 0) {
//            Set<String> keyset = body.keySet();
//            for (String key : keyset) {
//                multiBuilder.addFormDataPart(key, (String) body.get(key));
//            }
//        }
//
//        RequestBody requestBody = multiBuilder.build();

        RequestBody requestBody = null;
        if(body != null && body.size() > 0) {
            JSONObject joBody = new JSONObject(body);
            requestBody = RequestBody.create(MediaType.parse(CONTENT_TYPE), joBody.toString());
        }

        builder.url(httpBuilder.build() + param);
        builder.headers(Headers.of(mHeadersMap));
        if(requestBody != null) {
//                    builder.post(requestBody);
            builder.method("POST", RequestBody.create(null, new byte[0])).post(requestBody);
        }
        builder.tag(REQUEST_TAG);

        return builder;
    }

    /**
     * DELETE method call
     * @return
     */
    private Request.Builder deleteMethodCall() {
        Request.Builder builder = new Request.Builder();


        builder.url(httpBuilder.build() + param);
        builder.headers(Headers.of(mHeadersMap));

        if(body != null) {
            JSONObject po = new JSONObject(body);
            RequestBody requestBody = RequestBody.create(MediaType.parse(CONTENT_TYPE), po.toString());
            builder.method("DELETE", RequestBody.create(null, new byte[0])).delete(requestBody);
        } else
            builder.delete();

        builder.tag(REQUEST_TAG);

        return builder;
    }

    /**
     * DOWNLOAD file call
     * @return
     */
    private Request.Builder downloadFileCall() {
        Request.Builder builder = new Request.Builder();

        builder.url(httpBuilder.build() + param);
        builder.headers(Headers.of(mHeadersMap));
        builder.tag(REQUEST_TAG);

        return builder;
    }

    /**
     * UPLOAD file call
     * @return
     */
    private Request.Builder uploadFileCall() {
        Request.Builder builder = new Request.Builder();

        String stFileName = (String) params.get(FILENAME);
        File file = new File(stFileName);

        MultipartBody.Builder builderUpload = new MultipartBody.Builder();

        builderUpload.setType(MultipartBody.FORM);

        builderUpload.addFormDataPart("Content-type", "image/png");
        builderUpload.addFormDataPart("file", attachmentFileName, RequestBody.create(MediaType.parse("image/png"), file));

        RequestBody formBody = builderUpload.build();

        builder.url(httpBuilder.build() + param);
        builder.headers(Headers.of(mHeadersMap));
//                    builder.post(formBody);
        builder.method("POST", RequestBody.create(MediaType.parse(CONTENT_TYPE), formBody.toString()));
        builder.tag(REQUEST_TAG);

        return builder;
    }

    private String bodyToString(final Request request){

        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
