package com.project.eazydiner.ui;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import com.project.eazydiner.utils.ApiResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.project.eazydiner.utils.Const.ATTACHMENT_FOLDER;
import static com.project.eazydiner.utils.Const.FILE_NAME;

public class MainViewModel extends ViewModel {


    private Repository repository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();


    public MainViewModel(Repository repository) {
        this.repository = repository;
    }

    public MutableLiveData<ApiResponse> downloadResponse() {
        return responseLiveData;
    }

//Retrofit call for downloading image from the URL and handeling the response through rx Java//

    public void downloadImage() {

        disposables.add(repository.downloadImage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> responseLiveData.setValue(ApiResponse.loading()))
                .subscribe(
                        result -> responseLiveData.setValue(ApiResponse.success(result)),
                        throwable -> responseLiveData.setValue(ApiResponse.error(throwable))
                ));
    }




    //Saving image in phone storage//

    public String saveImage(Context context, Response<ResponseBody> response) {
        String ext_type = "mp3";
        String filePath = null;
        InputStream in = null;
        FileOutputStream out = null;

        ContextWrapper cw = new ContextWrapper(context);
        String fileName = "";
        byte dataBuffer[] = new byte[1024 * 100];
        File directory = cw.getDir(ATTACHMENT_FOLDER, Context.MODE_PRIVATE);
        File file = null;
        try {
            in = response.body().byteStream();
            fileName = String.valueOf(FILE_NAME) + "." + ext_type;
            file = new File(directory, fileName);
            filePath = file.getAbsolutePath();
            out = new FileOutputStream(file);

            int c;
            while ((c = in.read(dataBuffer)) != -1) {
                out.write(dataBuffer, 0, c);
            }
        } catch (IOException e) {

            Log.d("Error", e.toString());

        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return filePath;

    }


    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
