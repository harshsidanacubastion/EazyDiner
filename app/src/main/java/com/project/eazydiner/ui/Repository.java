package com.project.eazydiner.ui;



import android.content.Context;


import com.project.eazydiner.utils.ApiInterface;


import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class Repository {
    Context context;
    private ApiInterface apiCallInterface;


    public Repository(ApiInterface apiCallInterface) {
        this.apiCallInterface = apiCallInterface;

    }
    public Observable<Response<ResponseBody>> downloadImage() {


        return apiCallInterface.downloadImage();
    }



    }

