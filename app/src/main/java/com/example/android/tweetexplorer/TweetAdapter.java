package com.example.android.tweetexplorer;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.tweetexplorer.data.Tweet;
import com.example.android.tweetexplorer.data.TweetPreferences;
import com.example.android.tweetexplorer.utils.TwitterUtils;

import java.text.DateFormat;
import java.util.ArrayList;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ForecastItemViewHolder> {

    private ArrayList<Tweet> mTweets;
    private OnForecastItemClickListener mForecastItemClickListener;

    public interface OnForecastItemClickListener {
        void onForecastItemClick(Tweet tweet);
    }

    public TweetAdapter(OnForecastItemClickListener clickListener) {
        mForecastItemClickListener = clickListener;
    }

    public void updateForecastItems(ArrayList<Tweet> tweets) {
        mTweets = tweets;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mTweets != null) {
            return mTweets.size();
        } else {
            return 0;
        }
    }

    @Override
    public ForecastItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.forecast_list_item, parent, false);
        return new ForecastItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ForecastItemViewHolder holder, int position) {
        holder.bind(mTweets.get(position));
    }

    class ForecastItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mForecastDateTV;
        private TextView mForecastTempDescriptionTV;
        private ImageView mWeatherIconIV;

        public ForecastItemViewHolder(View itemView) {
            super(itemView);
            mForecastDateTV = itemView.findViewById(R.id.tv_forecast_date);
            mForecastTempDescriptionTV = itemView.findViewById(R.id.tv_forecast_temp_description);
            mWeatherIconIV = itemView.findViewById(R.id.iv_weather_icon);
            itemView.setOnClickListener(this);
        }

        public void bind(Tweet tweet) {
            String dateString = DateFormat.getDateTimeInstance().format(tweet.dateTime);
            String detailString = mForecastTempDescriptionTV.getContext().getString(
                    R.string.forecast_item_details, tweet.temperature,
                    TweetPreferences.getDefaultTemperatureUnitsAbbr(), tweet.description
            );
            mForecastDateTV.setText(dateString);
            mForecastTempDescriptionTV.setText(detailString);
        }

        @Override
        public void onClick(View v) {
            Tweet tweet = mTweets.get(getAdapterPosition());
            mForecastItemClickListener.onForecastItemClick(tweet);
        }
    }
}
