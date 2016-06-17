package com.androidtitan.spotscore.main.play.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidtitan.spotscore.R;
import com.androidtitan.spotscore.main.data.foursquare.BestPhoto;
import com.androidtitan.spotscore.main.data.foursquare.Venue;
import com.androidtitan.spotscore.main.play.PlayMvp;
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
    private PlayMvp.Presenter mPlayPresenter;

    private ArrayList<Venue> mVenueList;

    public VenueListAdapter(Context context, PlayMvp.Presenter playPresenter, ArrayList<Venue> venues) {
        mContext = context;
        mPlayPresenter = playPresenter;
        mVenueList = venues;
    }

    public static class VenueViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.venueListBgImageView) ImageView mBackgroundImage;
        @Bind(R.id.venueIconImageView) ImageView mIconImage;
        @Bind(R.id.venueCategoryTextView) TextView mCategoryText;
        @Bind(R.id.venueNameTextView) TextView mTitleText;
        @Bind(R.id.venueRatingTextView) TextView mRatingText;
        @Bind(R.id.venueFloatingActionButton) FloatingActionButton mFab;

        @Bind(R.id.circularRevealView) RelativeLayout mRevealLayout;
        @Bind(R.id.venueCallImageView) ImageView mCallImage;
        @Bind(R.id.venueMapImageView) ImageView mMapImage;
        @Bind(R.id.venueFoursqareImageView) ImageView mFoursqareImage;
        @Bind(R.id.venueCloseImageView) ImageView mCloseImage;

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
        View v1 = inflater.inflate(R.layout.row_venue_list, parent, false);
        RecyclerView.ViewHolder viewHolder = new VenueViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        initViewHolder((VenueViewHolder) holder, position);
    }

    private void initViewHolder(VenueViewHolder holder, int position) {
        Venue mVenue = mVenueList.get(position);

        holder.mTitleText.setText(mVenue.getName());
        holder.mCategoryText.setText(mVenue.getCategories().get(0).getName());
        holder.mRatingText.setText(String.format("%.1f", mVenue.getRating()));
        holder.mRatingText.setTextColor(Color.parseColor("#" + mVenue.getRatingColor()));

        if (mVenue.getBestPhoto() == null) {

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

        String downloadUrl = mVenue.getCategories().get(0).getIcon().getPrefix() + "88.png";
        Glide.with(mContext).load(downloadUrl).into(holder.mIconImage);
        holder.mIconImage.setColorFilter(ContextCompat.getColor(mContext, android.R.color.white));

        holder.mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.mFab.hide();

                int centerX = holder.mFab.getRight();
                int centerY = holder.mFab.getBottom();

                int startRadius = 0;
                int endRadius = Math.max(holder.mBackgroundImage.getWidth(), holder.mBackgroundImage.getHeight());

                Animator anim =
                        ViewAnimationUtils.createCircularReveal(holder.mRevealLayout,
                                centerX, centerY, startRadius, endRadius);
                Animation alphaAnimation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);

                holder.mRevealLayout.setVisibility(View.VISIBLE);
                anim.start();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        holder.mCallImage.setVisibility(View.VISIBLE);
                        holder.mMapImage.setVisibility(View.VISIBLE);
                        holder.mFoursqareImage.setVisibility(View.VISIBLE);
                        holder.mIconImage.setVisibility(View.VISIBLE);

                        holder.mCallImage.startAnimation(alphaAnimation);
                        holder.mMapImage.startAnimation(alphaAnimation);
                        holder.mFoursqareImage.startAnimation(alphaAnimation);
                        holder.mIconImage.startAnimation(alphaAnimation);
                    }
                }, anim.getDuration());

            }
        });

        holder.mCloseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int cx = holder.mFab.getRight();
                int cy = holder.mFab.getBottom();

                float initialRadius = (float) Math.hypot(cx, cy);

                Animator anim =
                        ViewAnimationUtils.createCircularReveal(holder.mRevealLayout, cx, cy, initialRadius, 0);

                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        holder.mRevealLayout.setVisibility(View.GONE);
                        holder.mCallImage.setVisibility(View.GONE);
                        holder.mMapImage.setVisibility(View.GONE);
                        holder.mFoursqareImage.setVisibility(View.GONE);

                        holder.mFab.show();
                    }
                });

                anim.start();
            }
        });

        holder.mCallImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri number = Uri.parse("tel:" + mVenue.getContact().getPhone());
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);

                Intent intentChooser = Intent.createChooser(callIntent,
                        mContext.getResources().getString(R.string.intent_chooser_title));

                //check to make sure there is an application that can handle this intent
                if (callIntent.resolveActivity(mContext.getPackageManager()) != null) {
                    mContext.startActivity(intentChooser);
                } else {
                    //todo: Snackbar stating that we cannot find an option to use...
                }
            }
        });

        holder.mMapImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri location = Uri.parse("geo:" + mVenue.getLocation().getLat() +","
                        + mVenue.getLocation().getLng() + "?z=14"); // z param is zoom level
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);

                Intent intentChooser = Intent.createChooser(mapIntent,
                        mContext.getResources().getString(R.string.intent_chooser_title));

                //check to make sure there is an application that can handle this intent
                if (mapIntent.resolveActivity(mContext.getPackageManager()) != null) {
                    mContext.startActivity(intentChooser);
                }
            }
        });
        holder.mFoursqareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String webpageURL = "https://foursquare.com/venue/" + mVenue.getId();

                Intent fourSquareIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webpageURL));
                Intent intentChooser = Intent.createChooser(fourSquareIntent,
                        mContext.getResources().getString(R.string.intent_chooser_title));

                //check to make sure there is an application that can handle this intent
                if (fourSquareIntent.resolveActivity(mContext.getPackageManager()) != null) {
                    mContext.startActivity(intentChooser);
                }

                Log.e(TAG, webpageURL);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mVenueList.size();
    }
}
