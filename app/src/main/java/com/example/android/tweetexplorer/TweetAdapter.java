package com.example.android.tweetexplorer;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.tweetexplorer.data.TweetItem;
import com.example.android.tweetexplorer.data.TweetPreferences;
import com.example.android.tweetexplorer.utils.TwitterUtils;

import java.text.DateFormat;
import java.util.ArrayList;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ForecastItemViewHolder> {

    private ArrayList<TweetItem> mTweetItems;
    private OnForecastItemClickListener mForecastItemClickListener;

    public interface OnForecastItemClickListener {
        void onForecastItemClick(TweetItem tweetItem);
    }

    public TweetAdapter(OnForecastItemClickListener clickListener) {
        mForecastItemClickListener = clickListener;
    }

    public void updateForecastItems(ArrayList<TweetItem> tweetItems) {
        mTweetItems = tweetItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mTweetItems != null) {
            return mTweetItems.size();
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
        holder.bind(mTweetItems.get(position));
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

        public void bind(TweetItem tweetItem) {
            String dateString = DateFormat.getDateTimeInstance().format(tweetItem.dateTime);
            String detailString = mForecastTempDescriptionTV.getContext().getString(
                    R.string.forecast_item_details, tweetItem.temperature,
                    TweetPreferences.getDefaultTemperatureUnitsAbbr(), tweetItem.description
            );
            String iconURL = TwitterUtils.buildIconURL(tweetItem.icon);
            mForecastDateTV.setText(dateString);
            mForecastTempDescriptionTV.setText(detailString);
            Glide.with(mWeatherIconIV.getContext()).load(iconURL).into(mWeatherIconIV);
        }

        @Override
        public void onClick(View v) {
            TweetItem tweetItem = mTweetItems.get(getAdapterPosition());
            mForecastItemClickListener.onForecastItemClick(tweetItem);
        }
    }
}
