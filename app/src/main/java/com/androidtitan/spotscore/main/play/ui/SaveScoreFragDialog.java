package com.androidtitan.spotscore.main.play.ui;



import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.androidtitan.spotscore.R;
import com.androidtitan.spotscore.main.data.Score;
import com.androidtitan.spotscore.main.play.PlayMvp;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SaveScoreFragDialog extends DialogFragment {
    private final String TAG = getClass().getSimpleName();

    //private SaveScoreInterface mInterface;
    private PlayMvp.Presenter mPresenter;

    @Bind(R.id.saveScoreTextView) TextView mScoreToSaveText;
    @Bind(R.id.saveScoreEditText) EditText mNoteEdit;
    @Bind(R.id.remainingSavesTextView) TextView mRemainingText;
    @Bind(R.id.sendSaveTextView) TextView mSendText;
    @Bind(R.id.cancelTextView) TextView mCancelText;


    private String mScore;

    public SaveScoreFragDialog() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mScore = getArguments().getString(((ScoreActivity)getActivity()).SAVE_DIALOG_DISPLAY_SCORE);
        }

        mPresenter = ((ScoreActivity)getActivity()).getScorePresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Get it to go");
        View view = inflater.inflate(R.layout.fragment_dialog_save_score, container, false);
        ButterKnife.bind(this, view);

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.ENGLISH);
        String strCurrentDate = calendar.getTime().toString();

        Date date = new Date();
        DateFormat dateFormat =  DateFormat.getDateTimeInstance();
        String formattedDate = dateFormat.format(date);

        mScoreToSaveText.setText(mScore);
        mNoteEdit.setText("Saved on " + formattedDate);

        mRemainingText.setText("You have " + mPresenter.getRemainingSaves() + " saves remaining.");

        mCancelText.setOnClickListener(v -> {
            getDialog().dismiss();
        });

        mSendText.setOnClickListener(v -> {
            //todo: create an interface that sends info back to the
            LatLng smallLatLng = mPresenter.getLastKnownLocation();
            String note = mNoteEdit.getText().toString();
            Score scoreToSave = new Score(smallLatLng, note, Double.valueOf(mScore));

            mPresenter.saveUserScore(scoreToSave);

        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof SaveScoreInterface) {
            mInterface = (SaveScoreInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mInterface = null;
    }

}
