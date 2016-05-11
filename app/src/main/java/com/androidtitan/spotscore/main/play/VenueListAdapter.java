package com.androidtitan.spotscore.main.play;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidtitan.spotscore.R;
import com.androidtitan.spotscore.main.data.BestPhoto;
import com.androidtitan.spotscore.main.data.Venue;
import com.androidtitan.spotscore.main.play.presenter.ScorePresenter;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by amohnacs on 5/6/16.
 */
public class VenueListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = getClass().getSimpleName();

    private Context mContext;
    private ScorePresenter mScorePresenter;

    private ArrayList<Venue> mVenueList;


    public VenueListAdapter(Context context, ScorePresenter scorePresenter, ArrayList<Venue> venues) {
        mContext = context;
        mScorePresenter = scorePresenter;
        mVenueList = venues;
    }

    public static class VenueViewHolder extends RecyclerView.ViewHolder {

        //TODO :: we need to add categories top left!!!

        @Bind(R.id.venueListBgImageView) ImageView mBackgroundImage;
        @Bind(R.id.venueCategoryTextView) TextView mCategoryText;
        @Bind(R.id.venueNameTextView) TextView mTitleText;
        @Bind(R.id.venueRatingTextView) TextView mRatingText;

        public VenueViewHolder(View itemView) {
            super(itemView);
            try {
                ButterKnife.bind(this, itemView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v1 = inflater.inflate(R.layout.venue_list_row, parent, false);
        RecyclerView.ViewHolder viewHolder = new VenueViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        
        initViewHolder((VenueViewHolder)holder, position);
    }

    private void initViewHolder(VenueViewHolder holder, int position) {
        Venue mVenue = mVenueList.get(position);

        holder.mTitleText.setText(mVenue.getName());
        holder.mCategoryText.setText(mVenue.getCategories().get(0).getName());
        holder.mRatingText.setText(String.format("%.1f", mVenue.getRating()));
        holder.mRatingText.setTextColor(Color.parseColor("#" + mVenue.getRatingColor()));

        if(mVenue.getBestPhoto() == null) {

            Glide.with(mContext)
                    .load("https://unsplash.it/500/400/?random&gravity=west")
                    .into(holder.mBackgroundImage);

        } else {
            BestPhoto photo = mVenue.getBestPhoto();

            String size = photo.getWidth() + "x" + photo.getHeight();
            String photoUrl = photo.getPrefix() + size + photo.getSuffix();

            Glide.with(mContext)
                    .load(photoUrl)
                    .into(holder.mBackgroundImage);
        }
    }

    @Override
    public int getItemCount() {
        return mVenueList.size();
    }
}
