package in.arpaul.inshorts;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOError;
import java.io.IOException;
import java.net.HttpURLConnection;

import in.arpaul.inshorts.common.ApplicationInstance;
import in.arpaul.inshorts.webservices.RestAPICalls;
import in.arpaul.inshorts.webservices.WSConstants;
import in.arpaul.inshorts.webservices.WSResponse;
import in.arpaul.inshorts.webservices.WSType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static in.arpaul.inshorts.common.AppConstant.BUNDLE_DISPLAY;

public class BrowserActivity extends BaseActivity {

    private String TAG = "DashboardActivity";
    private View vBrowserActivity;
    private WebView wvDisplay;
    private String url = "";
    private TextView tvNoWebpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vBrowserActivity = inflaterBase.inflate(R.layout.activity_browser, null);
        llBase.addView(vBrowserActivity, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        intialiseUI();

        if(getIntent().hasExtra(BUNDLE_DISPLAY))
            url = getIntent().getStringExtra(BUNDLE_DISPLAY);

        bindControls();
    }

    private void bindControls() {
        if(!TextUtils.isEmpty(url)) {
            checkUrl();
        }
    }

    private void checkUrl() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    Response response = client.newCall(request).execute();
                    switch (response.code()) {
                        case WSConstants.STATUS_SUCCESS :
                        case WSConstants.STATUS_CREATED :
                        case WSConstants.STATUS_ACCEPTED :
                        case WSConstants.STATUS_NO_CONTENT :
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    wvDisplay.loadUrl(url);
                                    wvDisplay.setVisibility(View.VISIBLE);
                                    tvNoWebpage.setVisibility(View.GONE);
                                }
                            });
                            break;

                        default:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvNoWebpage.setVisibility(View.VISIBLE);
                                    wvDisplay.setVisibility(View.GONE);
                                }
                            });
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private void intialiseUI() {
        wvDisplay       = (WebView) findViewById(R.id.wvDisplay);
        tvNoWebpage     = (TextView) findViewById(R.id.tvNoWebpage);
    }
}
