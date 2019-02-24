package com.project.eazydiner.di.component;
import com.project.eazydiner.di.module.AppModule;
import com.project.eazydiner.ui.MainActivity;
import com.project.eazydiner.utils.UtilsModule;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class, UtilsModule.class})
@Singleton
public interface AppComponent {

    void doInjection(MainActivity mainActivity);



}