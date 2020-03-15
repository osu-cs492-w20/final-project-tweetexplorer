package com.example.android.tweetexplorer;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.tweetexplorer.data.TweetItem;
import com.example.android.tweetexplorer.data.TweetPreferences;
import com.example.android.tweetexplorer.utils.TwitterUtils;

import java.text.DateFormat;

public class TweetItemDetailActivity extends AppCompatActivity {

    private TextView mDateTV;
    private TextView mTempDescriptionTV;
    private TextView mLowHighTempTV;
    private TextView mWindTV;
    private TextView mHumidityTV;
    private ImageView mWeatherIconIV;

    private TweetItem mTweetItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_item_detail);

        mDateTV = findViewById(R.id.tv_date);
        mTempDescriptionTV = findViewById(R.id.tv_temp_description);
        mLowHighTempTV = findViewById(R.id.tv_low_high_temp);
        mWindTV = findViewById(R.id.tv_wind);
        mHumidityTV = findViewById(R.id.tv_humidity);
        mWeatherIconIV = findViewById(R.id.iv_weather_icon);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(TwitterUtils.EXTRA_FORECAST_ITEM)) {
            mTweetItem = (TweetItem)intent.getSerializableExtra(
                    TwitterUtils.EXTRA_FORECAST_ITEM
            );
            fillInLayout(mTweetItem);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forecast_item_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                shareForecast();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void shareForecast() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String location = preferences.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
        if (mTweetItem != null) {
            String dateString = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
                    .format(mTweetItem.dateTime);
            String shareText = getString(R.string.forecast_item_share_text,
                    location, dateString,
                    mTweetItem.temperature, TweetPreferences.getDefaultTemperatureUnitsAbbr(),
                    mTweetItem.description);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            shareIntent.setType("text/plain");

            Intent chooserIntent = Intent.createChooser(shareIntent, null);
            startActivity(chooserIntent);
        }
    }

    private void fillInLayout(TweetItem tweetItem) {
        String dateString = DateFormat.getDateTimeInstance().format(tweetItem.dateTime);
        String detailString = getString(R.string.forecast_item_details, tweetItem.temperature,
                TweetPreferences.getDefaultTemperatureUnitsAbbr(), tweetItem.description);
        String lowHighTempString = getString(R.string.forecast_item_low_high_temp,
                tweetItem.temperatureLow, tweetItem.temperatureHigh,
                TweetPreferences.getDefaultTemperatureUnitsAbbr());

        String windString = getString(R.string.forecast_item_wind, tweetItem.windSpeed,
                tweetItem.windDirection);
        String humidityString = getString(R.string.forecast_item_humidity, tweetItem.humidity);
        String iconURL = TwitterUtils.buildIconURL(tweetItem.icon);

        mDateTV.setText(dateString);
        mTempDescriptionTV.setText(detailString);
        mLowHighTempTV.setText(lowHighTempString);
        mWindTV.setText(windString);
        mHumidityTV.setText(humidityString);
        Glide.with(this).load(iconURL).into(mWeatherIconIV);
    }
}
