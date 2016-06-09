package com.androidtitan.spotscore.main.play.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidtitan.spotscore.R;
import com.androidtitan.spotscore.common.BaseFragment;
import com.androidtitan.spotscore.main.data.Venue;
import com.androidtitan.spotscore.main.play.PlayMvp;
import com.androidtitan.spotscore.main.play.adapter.VenueListAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class VenueListFragment extends BaseFragment {
    private final String TAG = getClass().getSimpleName();

    private PlayMvp.Presenter mPlayPresenter;

    @Bind(R.id.venueListEmptyState) TextView mEmptyState;
    @Bind(R.id.venueIndProgressBar) ProgressBar mProgress;
    @Bind(R.id.list) RecyclerView mRecyclerView;
    private VenueListAdapter mAdapter;

    private ArrayList<Venue> mVenueList;


    public VenueListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPlayPresenter = ((ScoreActivity)getActivity()).getScorePresenter();
        mVenueList = mPlayPresenter.getNearbyVenuesList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_venue_list, container, false);
        ButterKnife.bind(this, v);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        if(mVenueList.size() == 0) {
            //empty state
            mEmptyState.setVisibility(View.VISIBLE);
            mProgress.setVisibility(View.GONE);
        } else {
            mProgress.setVisibility(View.GONE);
        }

        mAdapter = new VenueListAdapter(getActivity(), mPlayPresenter, mVenueList);
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    /**
     * When true, we are displaying the progressbar
     * When false, we are hiding the progressbar
     */
    private void progressVisibility(boolean setVisible) {

        if(setVisible) {
            mProgress.setVisibility(View.GONE);
        } else {
            mProgress.setVisibility(View.VISIBLE);
        }

    }
}
