package com.androidtitan.spotscore.main.injection;

/**
 * Created by amohnacs on 5/16/16.
 */

import android.content.Context;

import com.androidtitan.spotscore.main.play.PlayMvp;
import com.androidtitan.spotscore.main.play.presenter.ScorePresenter;
import com.androidtitan.spotscore.main.settings.SettingsMvp;
import com.androidtitan.spotscore.main.settings.presenter.SettingsPresenter;

import dagger.Module;
import dagger.Provides;

@Module(
        includes = {
                AppModule.class,
                DataManagerModule.class}
)
public class SettingsPresenterModule {

    public SettingsPresenterModule() {

    }

    @Provides
    public SettingsMvp.Presenter providesSettingsPresenterModule(Context context, SettingsMvp.Model manager) {
        return new SettingsPresenter(context, manager);
    }
}
