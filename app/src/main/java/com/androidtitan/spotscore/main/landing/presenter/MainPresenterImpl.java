package com.androidtitan.spotscore.main.landing.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.androidtitan.spotscore.common.BasePresenter;
import com.androidtitan.spotscore.main.landing.ui.MainView;
import com.androidtitan.spotscore.main.login.ui.LoginView;
import com.androidtitan.spotscore.main.login.web.LoginInteractorImpl;
import com.androidtitan.spotscore.main.web.DataManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import javax.inject.Inject;

/**
 * Created by amohnacs on 5/5/16.
 */
public class MainPresenterImpl extends BasePresenter<MainView> implements MainPresenter,
    DataManager.MainViewListener{
    private final String TAG = getClass().getSimpleName();

    private DataManager mDataManager;
    private Context mContext;

    @Inject
    public MainPresenterImpl(Context context, DataManager dataManager) {
        mContext = context;
        mDataManager = dataManager;
    }

    @Override
    public void attachView(MainView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        //if (mSubscription != null) mSubscription.unsubscribe();
    }

    @Override
    public void setNavDrawerUserName(String userId) {
        mDataManager.setNavDrawerUserName(userId, this);
    }

    @Override
    public void setNavHeaderImageView(ImageView imageView) {
        Glide.with(mContext)
                .load("https://unsplash.it/g/200/200/?random")
                .skipMemoryCache( true )
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    @Override
    public void onUsernameFinished(String string) {
        getMvpView().setNavDrawerUserName(string);
    }

}
