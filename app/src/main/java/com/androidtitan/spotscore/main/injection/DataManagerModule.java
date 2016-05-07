package com.androidtitan.spotscore.main.injection;

import android.content.Context;

import com.androidtitan.spotscore.main.web.DataManager;
import com.androidtitan.spotscore.main.web.DataManagerImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by amohnacs on 5/5/16.
 */
@Module (
        includes = { AppModule.class }
)
public class DataManagerModule {

    public DataManagerModule() {

    }

    @Provides
    public DataManager providesDataManagerModule(Context context) {
        return new DataManagerImpl(context);
    }

}
