package com.androidtitan.spotscore.main.web;

import android.content.Context;
import android.util.Log;

import com.androidtitan.spotscore.R;
import com.androidtitan.spotscore.common.data.Constants;
import com.androidtitan.spotscore.main.data.DetailedVenueResponse;
import com.androidtitan.spotscore.main.data.User;
import com.androidtitan.spotscore.main.data.Venue;
import com.androidtitan.spotscore.main.data.VenueResponse;
import com.androidtitan.spotscore.main.web.deserializers.DetailedResponseDeserializer;
import com.androidtitan.spotscore.main.web.deserializers.ResponseDeserializer;
import com.androidtitan.spotscore.main.web.deserializers.VenueDeserializer;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by amohnacs on 5/3/16.
 */
public class DataManagerImpl implements DataManager {
    private final String TAG = getClass().getSimpleName();

    private Context mContext;
    private Retrofit mRetrofit;

    private User mUser;

    RetrofitEndpointInterface newsService;

    private final Observable.Transformer<Observable, Observable> mScheduleTransformer =
            observable -> observable.subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread());

    @Inject
    public DataManagerImpl(Context context) {

        mContext = context;

        mUser = User.getInstance();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.FOURSQUARE_SEARCH_URL)
                .client(httpClient)
                .addConverterFactory(buildGsonConverter())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        newsService = mRetrofit.create(RetrofitEndpointInterface.class);
    }

    @SuppressWarnings("unchecked")
    private <T> Observable.Transformer<T, T> applySchedulers() {
        return (Observable.Transformer<T, T>) mScheduleTransformer;
    }

    @Override
    public void setNavDrawerUserName(String userId, final ScoreViewListener listener) {
        Firebase fb = new Firebase(Constants.FIREBASE_URL + "/users/" + userId + "/email");

        try {
            //this is used to populate using data from firebase
            fb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    mUser.setEmail(dataSnapshot.getValue().toString());
                    listener.onUsernameFinished(mUser.getEmail());
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.e(TAG, "Cancelled :" + firebaseError);
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Observable<Venue> getVenuesOneByOne(double latitude, double longitude) {
        //todo: create our observable and return it

        Observable<VenueResponse> call = newsService.getVenues(
                mContext.getResources().getString(R.string.foursquare_client_id),
                mContext.getResources().getString(R.string.foursquare_client_secret),
                getVersion(),
                latitude + "," + longitude);

        return call.compose(applySchedulers())
                        .flatMap(result -> Observable.from(result.getVenues()));

    }

    @Override
    public Observable<Venue> getAdditionalVenueInfo(String venueIdentifier) {

        Observable<DetailedVenueResponse> call = newsService.getDetailedVenue(
                venueIdentifier,
                mContext.getResources().getString(R.string.foursquare_client_id),
                mContext.getResources().getString(R.string.foursquare_client_secret),
                getVersion());

        return call.compose(applySchedulers())
                .map(detailedVenue -> detailedVenue.getVenue());

    }

    private static GsonConverterFactory buildGsonConverter() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        //adding custom deserializer
        gsonBuilder.registerTypeAdapter(VenueResponse.class, new ResponseDeserializer());
        gsonBuilder.registerTypeAdapter(DetailedVenueResponse.class, new DetailedResponseDeserializer());
        gsonBuilder.registerTypeAdapter(Venue.class, new VenueDeserializer());
        gsonBuilder.serializeNulls();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();

        Gson myGson = gsonBuilder.create();

        return GsonConverterFactory.create(myGson);
    }

    private String getVersion() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date).toString();
    }

}
