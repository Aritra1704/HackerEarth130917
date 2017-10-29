package in.arpaul.inshorts;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.util.Predicate;
import com.arpaul.utilitieslib.LogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import in.arpaul.inshorts.adapter.DashboardAdapter;
import in.arpaul.inshorts.common.AppPreference;
import in.arpaul.inshorts.common.EndlessRecyclerOnScrollListener;
import in.arpaul.inshorts.common.QuickSort;
import in.arpaul.inshorts.common.SelectedNews;
import in.arpaul.inshorts.dataobjects.NewsDO;
import in.arpaul.inshorts.webservices.DataService;
import in.arpaul.inshorts.webservices.WSCall;

import static in.arpaul.inshorts.common.AppPreference.SAVED_NEWS;
import static in.arpaul.inshorts.common.ApplicationInstance.LOADER_FETCH_NEWS;
import static in.arpaul.inshorts.webservices.WSType.WSTypePref.GET;

public class DashboardActivity extends BaseActivity implements LoaderManager.LoaderCallbacks, SelectedNews {

    private String TAG = "DashboardActivity";
    private View vDashboardActivity;
    private RecyclerView rvNews;
    private ImageView ivPageUp, ivPageDown;
    private TextView tvFilter, tvSort;
    private DashboardAdapter adapter;
    private ArrayList<NewsDO> arrNews;
    private String ASC = "ASC", DESC = "DESC";
    private final String BUSINESS = "BUSINESS", SCIENCE = "SCIENCE", HEALTH = "HEALTH", ENTERTAINMENT = "ENTERTAINMENT", ALL = "ALL";

    private int MAX_PAGE_LIMIT = 50;
    private int MAX_PAGE = 0;
    private int CURRENT_PAGE = 0;
    private EndlessRecyclerOnScrollListener mScrollListener = null;
    private LinkedHashMap<Integer, ArrayList<NewsDO>> hashPage = new LinkedHashMap<>();
    private ArrayList<String> arrSelect = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vDashboardActivity = inflaterBase.inflate(R.layout.activity_dashboard, null);
        llBase.addView(vDashboardActivity, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        intialiseUI();

        bindControls();
    }

    private void bindControls() {

        loadData();

        tvFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<String> categoryList = new ArrayList<String>();
                categoryList.add(ALL);
                categoryList.add(SCIENCE);
                categoryList.add(BUSINESS);
                categoryList.add(ENTERTAINMENT);
                categoryList.add(HEALTH);


                SingleClickDialog dialog = SingleClickDialog.newInstance("Select Grid", categoryList);
                dialog.setOnSelectedListener(new SingleClickDialog.OnSelectedListener() {
                    @Override
                    public void onSelect(int position) {
                        String selectedGrid = categoryList.get(position);
                        tvFilter.setText(selectedGrid);
                        String filterby = "";
                        switch (selectedGrid) {
                            case ALL:
                                filterby = "a"; break;
                            case SCIENCE:
                                filterby = "t"; break;
                            case BUSINESS:
                                filterby = "b"; break;
                            case ENTERTAINMENT:
                                filterby = "e"; break;
                            case HEALTH:
                                filterby = "m"; break;
                        }
                        filterCategory(filterby);
                    }
                });

                dialog.show(getSupportFragmentManager(), "select_grid");
            }
        });

        tvSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<NewsDO> newsTemp = (ArrayList<NewsDO>) hashPage.get(CURRENT_PAGE).clone();
                new QuickSort().sort(newsTemp);

                if(tvSort.getText().toString().equalsIgnoreCase(ASC)) {
                    tvSort.setText(DESC);
                    Collections.reverse(newsTemp);
                } else {
                    tvSort.setText(ASC);
                }
                adapter.refresh(newsTemp);
            }
        });

        ivPageUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CURRENT_PAGE == 0)
                    Toast.makeText(DashboardActivity.this, "This is the topmost page.", Toast.LENGTH_SHORT).show();
                else {
                    CURRENT_PAGE--;
                    adapter.refresh(hashPage.get(CURRENT_PAGE));
                }
            }
        });

        ivPageDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CURRENT_PAGE == MAX_PAGE)
                    Toast.makeText(DashboardActivity.this, "This is the last page.", Toast.LENGTH_SHORT).show();
                else {
                    CURRENT_PAGE++;
                    adapter.refresh(hashPage.get(CURRENT_PAGE));
                }
            }
        });
    }

    private void loadData() {
        if(getSupportLoaderManager().getLoader(LOADER_FETCH_NEWS) == null)
            getSupportLoaderManager().initLoader(LOADER_FETCH_NEWS, null, this).forceLoad();
        else
            getSupportLoaderManager().restartLoader(LOADER_FETCH_NEWS, null, this).forceLoad();


    }

    @Override
    public Loader onCreateLoader(int id, Bundle bundle) {
        showLoader("Please wait", "Loading..", false);
        switch (id) {
            case LOADER_FETCH_NEWS:
                return new DataService(this, bundle, GET, WSCall.WSCallPref.FETCHNEWS);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        switch (loader.getId()) {
            case LOADER_FETCH_NEWS:
                if(data != null) {
                    if(data instanceof List<?>) {
                        arrNews = (ArrayList<NewsDO>) data;
                        if(arrNews != null && arrNews.size() > 0) {

                            ArrayList<NewsDO> arrTempNews = new ArrayList<>();
                            int i = 0;
                            for(; i < arrNews.size(); i++) {
                                if(i > 0 && i % MAX_PAGE_LIMIT == 0) {
                                    hashPage.put(CURRENT_PAGE, (ArrayList<NewsDO>) arrTempNews.clone());
                                    CURRENT_PAGE++;
                                    arrTempNews.clear();
                                }

                                arrTempNews.add(arrNews.get(i));
                            }
                            if(i > 0 && i == arrNews.size() && arrTempNews != null && arrTempNews.size() > 0) {
                                hashPage.put(CURRENT_PAGE, arrTempNews);
                            }
                            MAX_PAGE = CURRENT_PAGE;
                            arrTempNews.clear();
                            CURRENT_PAGE = 0;

//                            adapter.refresh(hashPage.get(CURRENT_PAGE));
                            addNewsToList();
                        }
                    }
                }
                break;

            default:
                return;
        }
        hideLoader();
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    private final String FILTER_CATEGORIES = "FILTER_CATEGORIES";
    private void filterCategory(final String searchCategory){
        ArrayList<NewsDO> tmpSearched = new ArrayList<>();
        synchronized (FILTER_CATEGORIES){
            Predicate<NewsDO> searchFarms = null;
            if(!TextUtils.isEmpty(searchCategory)) {
                searchFarms = new Predicate<NewsDO>() {
                    @Override
                    public boolean apply(NewsDO NewsDO) {
                        return NewsDO.category.toLowerCase().contains(searchCategory);
                    }
                };
            }

            if(tmpSearched != null)
                tmpSearched.clear();

            if (searchFarms!=null){
                ArrayList<NewsDO> arrFilter;
                if(searchCategory.equalsIgnoreCase("a"))
                    tmpSearched = arrTempNewsDO;
                else {
                    arrFilter = arrNews;

                    Collection<NewsDO> filteredResult = filter(arrFilter, searchFarms);
                    if (filteredResult != null && filteredResult.size() > 0) {
                        tmpSearched.addAll((ArrayList<NewsDO>) filteredResult);
                    }
                }
            } else {
                tmpSearched = (ArrayList<NewsDO>) arrTempNewsDO.clone();
            }

            adapter.refresh(tmpSearched);
        }
    }

    void intialiseUI() {
        rvNews              = (RecyclerView) vDashboardActivity.findViewById(R.id.rvNews);
        ivPageDown          = (ImageView) vDashboardActivity.findViewById(R.id.ivPageDown);
        ivPageUp            = (ImageView) vDashboardActivity.findViewById(R.id.ivPageUp);

        tvFilter            = (TextView) vDashboardActivity.findViewById(R.id.tvFilter);
        tvSort              = (TextView) vDashboardActivity.findViewById(R.id.tvSort);

        String selected = new AppPreference(this).getStringFromPreference(SAVED_NEWS, "");
        if(!TextUtils.isEmpty(selected)) {
            Type listType = new TypeToken<ArrayList<String>>(){}.getType();
            arrSelect = (ArrayList<String>) new Gson().fromJson(selected, listType);
        }

        arrNews = new ArrayList<>();
        adapter = new DashboardAdapter(this, arrNews, arrSelect, this);
        rvNews.setAdapter(adapter);

        tvFilter.setText(ALL);
    }

    private ArrayList<NewsDO> arrTempNewsDO = new ArrayList<>();
    void addNewsToList() {

        if(CURRENT_PAGE == 0) {
            arrTempNewsDO = hashPage.get(CURRENT_PAGE);
            adapter.refresh(arrTempNewsDO);
        }
        else {
            arrTempNewsDO.addAll(hashPage.get(CURRENT_PAGE));

            LogUtils.debugLog(TAG, "addNewsToList " + CURRENT_PAGE);

        }
    }

    @Override
    public void selected(String id) {
        if(arrSelect != null && arrSelect.size() > 0 && arrSelect.contains(id))
            arrSelect.remove(id);
        else {
            if(arrSelect == null)
                arrSelect = new ArrayList<>();

            arrSelect.add(id);
        }

        JSONArray jsArray = new JSONArray(arrSelect);
        new AppPreference(this).saveStringInPref(SAVED_NEWS, jsArray.toString());
    }
}
