package com.robillo.readrush.ui.search;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.robillo.readrush.R;
import com.robillo.readrush.ReadRushApp;
import com.robillo.readrush.data.db.model.search.SearchName;
import com.robillo.readrush.data.db.model.search.SearchNameRepository;
import com.robillo.readrush.data.network.retrofit.ApiClient;
import com.robillo.readrush.data.network.retrofit.ApiInterface;
import com.robillo.readrush.data.network.retrofit.model.Featured;
import com.robillo.readrush.data.network.retrofit.model.FeaturedSuper;
import com.robillo.readrush.data.network.retrofit.model.SearchResultItem;
import com.robillo.readrush.data.network.retrofit.model.SearchResultSuper;
import com.robillo.readrush.data.prefs.AppPreferencesHelper;
import com.robillo.readrush.ui.base.BaseActivity;
import com.robillo.readrush.ui.main.discover.adapters.FeaturedAdapter;
import com.robillo.readrush.ui.search.adapters.SearchNamesAdapter;
import com.robillo.readrush.utils.KeyboardUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends BaseActivity implements SearchMvpView {

    FeaturedAdapter mFeatureAdapter;
    FeaturedAdapter mSearchAdapter;
    SearchNamesAdapter mSearchNamesAdapter;
    LiveData<List<SearchName>> mSearchNameList;
    List<SearchName> mSearches;
    List<Featured> mFeatureList = new ArrayList<>();
    List<SearchResultItem> mSearchList = new ArrayList<>();
    @SuppressWarnings("FieldCanBeLocal")
    private AppPreferencesHelper mPrefsHelper;
    @SuppressWarnings("FieldCanBeLocal")
    private ApiInterface mApiService;

    @BindView(R.id.delete)
    ImageView mDeleteSearchNames;

    @BindView(R.id.search)
    EditText mSearchEditText;

    @BindView(R.id.search_buttom)
    ImageView mSearchButton;

    @BindView(R.id.suggestions)
    RecyclerView mSuggestionsRv;

    @BindView(R.id.layout_suggestions)
    LinearLayout mLayoutSuggestions;

    @BindView(R.id.search_history)
    RecyclerView mSearchHistory;

    @BindView(R.id.layout_search_history)
    LinearLayout mLayoutSearchHistory;

    @Inject
    SearchMvpPresenter<SearchMvpView> mPresenter;

    @Inject
    SearchNameRepository mSearchNameRepository;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SearchActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getActivityComponent().inject(SearchActivity.this);

        setUnBinder(ButterKnife.bind(this));

        mPresenter.onAttach(SearchActivity.this);

        setUp();
    }

    @Override
    protected void setUp() {

        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    searchForResults();
                    KeyboardUtils.INSTANCE.hideSoftInput(SearchActivity.this);
                }
                return false;
            }
        });

        //noinspection ConstantConditions
        mPrefsHelper = new AppPreferencesHelper(this, ReadRushApp.PREF_FILE_NAME);
        mApiService = ApiClient.getClient().create(ApiInterface.class);
        mSuggestionsRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        GridLayoutManager manager = new GridLayoutManager(this, 5, LinearLayoutManager.HORIZONTAL, false);
        mSearchHistory.setLayoutManager(manager);

        mLayoutSuggestions.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_left));
        mSearchNameList = mSearchNameRepository.getAllSearches();
        loadSearchNameList();
        loadDefaultFeaturedBooks();
    }

    @Override
    public void loadDefaultFeaturedBooks() {
        Call<FeaturedSuper> call = mApiService.getFeaturedBooks(mPrefsHelper.getUserId());
        if(call!=null){
            call.enqueue(new Callback<FeaturedSuper>() {
                @SuppressWarnings("ConstantConditions")
                @Override
                public void onResponse(@NonNull Call<FeaturedSuper> call, @NonNull Response<FeaturedSuper> response) {
                    if(response.body().getMessage()!=null){
                        mFeatureList = response.body().getMessage();
                        mFeatureAdapter = new FeaturedAdapter(mFeatureList, SearchActivity.this);
                        mSuggestionsRv.setAdapter(mFeatureAdapter);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<FeaturedSuper> call, @NonNull Throwable t) {
                    Toast.makeText(SearchActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void loadSuggestions(String search_tag) {

        Call<SearchResultSuper> call = mApiService.searchResultsFetch(search_tag);
        if(call!=null){
            call.enqueue(new Callback<SearchResultSuper>() {
                @SuppressWarnings("ConstantConditions")
                @Override
                public void onResponse(@NonNull Call<SearchResultSuper> call, @NonNull Response<SearchResultSuper> response) {
                    if(response.body().getMessage()!=null){
                        mSearchList = response.body().getMessage();
                        mSearchAdapter = new FeaturedAdapter(SearchActivity.this, mSearchList);
                        mSuggestionsRv.setAdapter(mSearchAdapter);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<SearchResultSuper> call, @NonNull Throwable t) {
                    Toast.makeText(SearchActivity.this, "No Matching Searches", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void showSearchHistory() {
        loadSearchNameList();
    }

    @Override
    public void loadSearchNameList() {
        mSearchNameList.observe(this, new Observer<List<SearchName>>() {
            @Override
            public void onChanged(@Nullable List<SearchName> searchNames) {
                mSearches = searchNames;
//                //INFLATE SEARCHES RECYCLER VIEW

                mSearchNamesAdapter = new SearchNamesAdapter(mSearches, SearchActivity.this);
                mSearchHistory.setAdapter(mSearchNamesAdapter);
//                mSearchHistory.invalidateItemDecorations();
            }
        });
    }

    @OnClick(R.id.search_buttom)
    public void searchForResults(){
        if(mSearchEditText.getText().length()>0){
            loadSuggestions(mSearchEditText.getText().toString());
            hideKeyboard();
            SearchName searchName = new SearchName(" " + mSearchEditText.getText().toString());
            mSearchNameRepository.insertSearchItem(searchName);
            mSearchEditText.setText("");
            showSearchHistory();
        }
        else {
            Toast.makeText(this, "Please Enter A Search Query", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.delete)
    public void setmDeleteSearchNames() {
        Toast.makeText(this, "deleted", Toast.LENGTH_SHORT).show();
    }
}
