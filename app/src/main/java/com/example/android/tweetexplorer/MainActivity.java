package com.example.android.tweetexplorer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.tweetexplorer.data.TweetItem;
import com.example.android.tweetexplorer.data.Status;
import com.example.android.tweetexplorer.data.TweetPreferences;
import com.example.android.tweetexplorer.utils.TwitterUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TweetAdapter.OnForecastItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText mSearchBoxET;

    private TextView mForecastLocationTV;
    private RecyclerView mForecastItemsRV;
    private ProgressBar mLoadingIndicatorPB;
    private TextView mLoadingErrorMessageTV;
    private TweetAdapter mTweetAdapter;

    private TwitterViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        SharedPreferences.getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxET = findViewById(R.id.et_search_box);
        Button searchButton = (Button)findViewById(R.id.btn_search);


        // Remove shadow under action bar.
//        getSupportActionBar().setElevation(0);

//        mForecastLocationTV = findViewById(R.id.tv_forecast_location);
//        mForecastLocationTV.setText(WeatherPreferences.getDefaultForecastLocation());

        mLoadingIndicatorPB = findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessageTV = findViewById(R.id.tv_loading_error_message);
        mForecastItemsRV = findViewById(R.id.rv_forecast_items);

        mTweetAdapter = new TweetAdapter(this);
        mForecastItemsRV.setAdapter(mTweetAdapter);
        mForecastItemsRV.setLayoutManager(new LinearLayoutManager(this));
        mForecastItemsRV.setHasFixedSize(true);

        mViewModel = new ViewModelProvider(this).get(TwitterViewModel.class);

        mViewModel.getSearchResults().observe(this, new Observer<ArrayList<TweetItem>>() {
            @Override
            public void onChanged(ArrayList<TweetItem> tweetItems) {
//                  mForecastAdapter.updateForecastItems(new ArrayList<ForecastItem>(forecastItems));
                mTweetAdapter.updateForecastItems(tweetItems);
            }
        });

        mViewModel.getLoadingStatus().observe(this, new Observer<Status>() {
            @Override
            public void onChanged(Status status) {
                if (status == Status.LOADING){
                    mLoadingIndicatorPB.setVisibility(View.VISIBLE);
                } else if (status == Status.SUCCESS){
                    mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
                    mForecastItemsRV.setVisibility(View.VISIBLE);
                    mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
                } else {
                    mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
                    mForecastItemsRV.setVisibility(View.INVISIBLE);
                    mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
                }
            }
        });

//        loadForecast();
    }

    @Override
    public void onForecastItemClick(TweetItem tweetItem) {
        Intent intent = new Intent(this, TweetItemDetailActivity.class);
        intent.putExtra(TwitterUtils.EXTRA_FORECAST_ITEM, tweetItem);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String location = preferences.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
        switch (item.getItemId()) {
            case R.id.action_location:
                showForecastLocation();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadForecast() {
//        String openWeatherMapForecastURL = OpenWeatherMapUtils.buildForecastURL(
//                WeatherPreferences.getDefaultForecastLocation(),
//                WeatherPreferences.getDefaultTemperatureUnits()
//        );
//        Log.d(TAG, "got forecast url: " + openWeatherMapForecastURL);
//        mViewModel.loadSearchResults(openWeatherMapForecastURL);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        PreferenceManager.setDefaultValues(this, R.xml.prefs, false);
        String units = preferences.getString(getString(R.string.pref_temp_key),
                getString(R.string.pref_temp_default));
        String location = preferences.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
        if(units.equals("imperial")){
            TweetPreferences.setDefaultTemperatureUnits("F");
        } else if (units.equals("metric")){
            TweetPreferences.setDefaultTemperatureUnits("C");
        } else if (units.equals("kelvin")){
            TweetPreferences.setDefaultTemperatureUnits("K");
        }
//        mForecastLocationTV.setText(location);
        mViewModel.loadSearchResults(location, units);
//        new OpenWeatherMapForecastTask().execute(openWeatherMapForecastURL);
    }

    public void showForecastLocation() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String location = preferences.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
        Uri geoUri = Uri.parse("geo:0,0").buildUpon()
                .appendQueryParameter("q", location)
                .build();
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, geoUri);
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG, "onResume()");
//        loadForecast();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }
}
