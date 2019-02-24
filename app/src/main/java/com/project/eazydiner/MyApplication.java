package com.project.eazydiner;

import android.app.Application;
import android.content.Context;

import com.project.eazydiner.di.component.AppComponent;
import com.project.eazydiner.di.component.DaggerAppComponent;
import com.project.eazydiner.di.module.AppModule;
import com.project.eazydiner.utils.UtilsModule;


public class MyApplication extends Application {
    AppComponent appComponent;
    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).utilsModule(new UtilsModule()).build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }
}