package com.example.android.tweetexplorer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.tweetexplorer.data.Tweet;
import com.example.android.tweetexplorer.utils.GeoUtils;

import java.util.List;

public class TweetDetailActivity extends AppCompatActivity {
    public static final String EXTRA_TWEET = "Tweet";

    private Tweet mTweet;
    private GeoUtils mGeoUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_TWEET)) {
            mTweet = (Tweet)intent.getSerializableExtra(EXTRA_TWEET);

            TextView tweetUserTV = findViewById(R.id.tv_tweet_user);
            tweetUserTV.setText("@" + mTweet.user.screen_name);

            TextView tweetTextTV = findViewById(R.id.tv_tweet_text);
            tweetTextTV.setText(mTweet.text);

            TextView tweetLocationTV = findViewById(R.id.tv_tweet_location);
            if (mTweet.place != null && mTweet.place.full_name != null) {
                tweetLocationTV.setText(mTweet.place.full_name);
            } else {
                LinearLayout tweetLocationLayout = findViewById(R.id.ll_tweet_location_layout);
                tweetLocationLayout.setVisibility(View.INVISIBLE);
            }

            //Distance
            //Initiate variables
            TextView geoTV = findViewById(R.id.tv_tweet_distance);
            mGeoUtils = new GeoUtils();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String distance_unit = preferences.getString(getString(R.string.pref_unit_key), getString(R.string.pref_unit_default));


            //Calculate distance
            String result = mGeoUtils.calculate(getApplicationContext(), mTweet.place.bounding_box.coordinates.get(0).get(0).get(1), mTweet.place.bounding_box.coordinates.get(0).get(0).get(0), distance_unit);
            //Set result string to TextView
            if (result.endsWith("mile")) {
                result = result.substring(0, result.length()-2);
            }
            geoTV.setText(result);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tweet_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_view_tweet:
                viewTweetURL();
                return true;
            case R.id.action_share:
                shareTweet();
                return true;
            case R.id.action_location:
                viewMap();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void viewTweetURL() {
        if (mTweet != null) {
            Uri tweetUri = Uri.parse(getString(R.string.tweet_full_url, mTweet.id_str));
            Intent webIntent = new Intent(Intent.ACTION_VIEW, tweetUri);

            PackageManager pm = getPackageManager();
            List<ResolveInfo> activities = pm.queryIntentActivities(webIntent, PackageManager.MATCH_DEFAULT_ONLY);
            if (activities.size() > 0) {
                startActivity(webIntent);
            }
        }
    }

    private void shareTweet() {
        if (mTweet != null) {
            String shareText = getString(R.string.tweet_full_url, mTweet.id_str);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            shareIntent.setType("text/plain");

            Intent chooserIntent = Intent.createChooser(shareIntent, null);
            startActivity(chooserIntent);
        }
    }

    private void viewMap(){
        String coord1 = mTweet.place.bounding_box.coordinates.get(0).get(0).get(0).toString();
        String coord2 = mTweet.place.bounding_box.coordinates.get(0).get(0).get(1).toString();
        String location = coord2 + "," + coord1;
        Log.d("testing: ", "location is:"+ location);
        Uri geoUri = Uri.parse("geo:0,0").buildUpon()
                .appendQueryParameter("q", location)
                .build();
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, geoUri);
        if(mapIntent.resolveActivity((getPackageManager())) != null){
            startActivity(mapIntent);
        }
    }
}
