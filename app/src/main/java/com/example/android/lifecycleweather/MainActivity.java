package com.example.android.lifecycleweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.lifecycleweather.data.ForecastItem;
import com.example.android.lifecycleweather.data.Status;
import com.example.android.lifecycleweather.data.WeatherPreferences;
import com.example.android.lifecycleweather.utils.NetworkUtils;
import com.example.android.lifecycleweather.utils.OpenWeatherMapUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ForecastAdapter.OnForecastItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView mForecastLocationTV;
    private RecyclerView mForecastItemsRV;
    private ProgressBar mLoadingIndicatorPB;
    private TextView mLoadingErrorMessageTV;
    private ForecastAdapter mForecastAdapter;

    private OpenWeatherMapViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        SharedPreferences.getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Remove shadow under action bar.
        getSupportActionBar().setElevation(0);

        mForecastLocationTV = findViewById(R.id.tv_forecast_location);
//        mForecastLocationTV.setText(WeatherPreferences.getDefaultForecastLocation());

        mLoadingIndicatorPB = findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessageTV = findViewById(R.id.tv_loading_error_message);
        mForecastItemsRV = findViewById(R.id.rv_forecast_items);

        mForecastAdapter = new ForecastAdapter(this);
        mForecastItemsRV.setAdapter(mForecastAdapter);
        mForecastItemsRV.setLayoutManager(new LinearLayoutManager(this));
        mForecastItemsRV.setHasFixedSize(true);

        mViewModel = new ViewModelProvider(this).get(OpenWeatherMapViewModel.class);

        mViewModel.getSearchResults().observe(this, new Observer<ArrayList<ForecastItem>>() {
            @Override
            public void onChanged(ArrayList<ForecastItem> forecastItems) {
//                  mForecastAdapter.updateForecastItems(new ArrayList<ForecastItem>(forecastItems));
                mForecastAdapter.updateForecastItems(forecastItems);
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
//                    Log.d(TAG, "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHHHHHHHHHHHH");
                    mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
                    mForecastItemsRV.setVisibility(View.INVISIBLE);
                    mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
                }
            }
        });

        loadForecast();
    }

    @Override
    public void onForecastItemClick(ForecastItem forecastItem) {
        Intent intent = new Intent(this, ForecastItemDetailActivity.class);
        intent.putExtra(OpenWeatherMapUtils.EXTRA_FORECAST_ITEM, forecastItem);
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
            WeatherPreferences.setDefaultTemperatureUnits("F");
        } else if (units.equals("metric")){
            WeatherPreferences.setDefaultTemperatureUnits("C");
        } else if (units.equals("kelvin")){
            WeatherPreferences.setDefaultTemperatureUnits("K");
        }
//        Log.d(TAG, "Location is currently: " + location + " and the units are: " + units);
        mForecastLocationTV.setText(location);
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
        loadForecast();
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
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences preferences = getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

//    class OpenWeatherMapForecastTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            mLoadingIndicatorPB.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String openWeatherMapURL = params[0];
//            String forecastJSON = null;
//            try {
//                forecastJSON = NetworkUtils.doHTTPGet(openWeatherMapURL);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return forecastJSON;
//        }
//
//        @Override
//        protected void onPostExecute(String forecastJSON) {
//            mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
//            if (forecastJSON != null) {
//                mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
//                mForecastItemsRV.setVisibility(View.VISIBLE);
//                ArrayList<ForecastItem> forecastItems = OpenWeatherMapUtils.parseForecastJSON(forecastJSON);
//                mForecastAdapter.updateForecastItems(forecastItems);
//            } else {
//                mForecastItemsRV.setVisibility(View.INVISIBLE);
//                mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
//            }
//        }
//    }
}
