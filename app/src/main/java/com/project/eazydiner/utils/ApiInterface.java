package com.project.eazydiner.utils;

import com.google.gson.JsonElement;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;


public interface ApiInterface {


    @GET("5/items/testmp3testfile/mpthreetest.mp3")
    Observable<Response<ResponseBody>> downloadImage();

}
