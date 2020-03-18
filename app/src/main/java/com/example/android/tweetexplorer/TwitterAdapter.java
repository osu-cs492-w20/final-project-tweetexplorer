package com.example.android.tweetexplorer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.tweetexplorer.data.Tweet;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TwitterAdapter extends RecyclerView.Adapter<TwitterAdapter.SearchResultViewHolder> {
    private List<Tweet> mTweetList;
    private OnSearchResultClickListener mResultClickListener;

    interface OnSearchResultClickListener {
        void onSearchResultClicked(Tweet repo);
    }

    public TwitterAdapter(OnSearchResultClickListener listener) {
        mResultClickListener = listener;
    }

    public void updateTweets(List<Tweet> tweetList) {
        mTweetList = tweetList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mTweetList != null) {
            return mTweetList.size();
        } else {
            return 0;
        }
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.tweet, parent, false);
        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchResultViewHolder holder, int position) {
        holder.bind(mTweetList.get(position));
    }

    class SearchResultViewHolder extends RecyclerView.ViewHolder {
        private TextView mSearchResultTV;

        SearchResultViewHolder(View itemView) {
            super(itemView);
            mSearchResultTV = itemView.findViewById(R.id.tv_search_result);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mResultClickListener.onSearchResultClicked(
                            mTweetList.get(getAdapterPosition())
                    );
                }
            });
        }

        void bind(Tweet tweet) {
            mSearchResultTV.setText(tweet.text);
        }
    }
}
