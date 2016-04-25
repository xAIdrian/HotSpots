package com.androidtitan.hotspots.main.presenter;

import android.content.Context;

import com.androidtitan.hotspots.R;
import com.androidtitan.hotspots.base.BasePresenter;
import com.androidtitan.hotspots.main.data.UserEntryInteractor;
import com.androidtitan.hotspots.main.data.UserEntryInteractorImpl;
import com.androidtitan.hotspots.main.ui.activity.UserEntryActivity;
import com.androidtitan.hotspots.main.ui.view.UserEntryView;

import javax.inject.Inject;

/**
 * Created by amohnacs on 4/19/16.
 */
public class UserEntryPresenterImpl extends BasePresenter<UserEntryView> implements UserEntryPresenter,
        UserEntryInteractor.OnLoginFinishedListener {

    private Context mContext;
    private UserEntryInteractor mInteractor;
    //private Subscription mSubscription

    private UserEntryActivity mActivity;

    @Inject
    public UserEntryPresenterImpl(Context context) {
        mContext = context;
        this.mInteractor = new UserEntryInteractorImpl();
    }

    @Override
    public void attachView(UserEntryView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        //if (mSubscription != null) mSubscription.unsubscribe();
    }

    @Override
    public void takeActivity(UserEntryActivity activity) {
        mActivity = activity;
    }

    @Override
    public void showLoginFragment() {
        mActivity.showLoginFragment();
    }

    @Override
    public void showSignUpFragment() {
        mActivity.showSignUpFragment();
    }

    @Override
    public void authenticateLogin(String email, String password) {
        mInteractor.authenticateLogin(email, password, this);
    }

    @Override
    public void onAuthenticationSuccess() {
        getMvpView().launchMainActivity();
    }

    @Override
    public void onAuthenticationFailure() {
        getMvpView().showFailureSnack(mContext.getResources().getString(R.string.missing_field));

    }





    /* todo: this is where we convert our Observables from the Datamanager to Results
    public void loadRibots() {
        checkViewAttached();
        mSubscription = mDataManager.getRibots()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Ribot>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the ribots.");
                        getMvpView().showError();
                    }

                    @Override
                    public void onNext(List<Ribot> ribots) {
                        if (ribots.isEmpty()) {
                            getMvpView().showRibotsEmpty();
                        } else {
                            getMvpView().showRibots(ribots);
                        }
                    }
                });
     */

}
