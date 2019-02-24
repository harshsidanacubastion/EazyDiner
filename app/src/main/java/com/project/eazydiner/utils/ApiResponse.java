package com.project.eazydiner.utils;



import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.project.eazydiner.utils.Status.ERROR;
import static com.project.eazydiner.utils.Status.LOADING;
import static com.project.eazydiner.utils.Status.SUCCESS;


public class ApiResponse {

    public final Status status;

    @Nullable
    public final Response<ResponseBody> data;

    @Nullable
    public final Throwable error;

    private ApiResponse(Status status, @Nullable Response<ResponseBody> data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static ApiResponse loading() {
        return new ApiResponse(LOADING, null, null);
    }

    public static ApiResponse success(@NonNull Response<ResponseBody> data) {
        return new ApiResponse(SUCCESS, data, null);
    }

    public static ApiResponse error(@NonNull Throwable error) {
        return new ApiResponse(ERROR, null, error);
    }

}