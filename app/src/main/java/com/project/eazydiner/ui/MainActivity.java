package com.project.eazydiner.ui;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.project.eazydiner.utils.ApiResponse;
import com.project.eazydiner.utils.Constant;
import com.project.eazydiner.Service.MusicPlayerService;
import com.project.eazydiner.MyApplication;
import com.project.eazydiner.R;
import com.project.eazydiner.ViewModelFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.project.eazydiner.utils.Const.BROADCAST_ACTION;
import static com.project.eazydiner.utils.Const.DONT_STOP_VALUE;
import static com.project.eazydiner.utils.Const.FILE_PATH;
import static com.project.eazydiner.utils.Const.STOP_MUSIC;
import static com.project.eazydiner.utils.Const.STOP_VALUE;


public class MainActivity extends AppCompatActivity {

    MainViewModel mainViewModel;
    @Inject
    ViewModelFactory viewModelFactory;
    @BindView(R.id.download)
    Button downloadButton;
    @BindView(R.id.playButton)
    Button playButton;
    @BindView(R.id.stopButton)
    Button stopButton;

    String filePath;
    MusicFinishedReceiver musicStopReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((MyApplication) getApplication()).getAppComponent().doInjection(this);
        ButterKnife.bind(this);
        /*Receiver*/
        musicStopReceiver = new MusicFinishedReceiver();
        /*ViewModel*/
        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);
        mainViewModel.downloadResponse().observe((LifecycleOwner) this, this::consumeResponse);


        registerMyReceiver();
    }

    @OnClick(R.id.download)
    void onDownloadClicked() {
        if (!Constant.checkInternetConnection(MainActivity.this)) {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.checkInternet), Toast.LENGTH_SHORT).show();
        } else {
            mainViewModel.downloadImage();
        }


    }

    @OnClick(R.id.playButton)
    void onPlayClicked() {

        playButton.setEnabled(false);
        stopButton.setEnabled(true);
        Intent serviceIntent = new Intent(MainActivity.this, MusicPlayerService.class);
        serviceIntent.putExtra(FILE_PATH, filePath);
        serviceIntent.putExtra(STOP_MUSIC, DONT_STOP_VALUE);
        startService(serviceIntent);
    }

    @OnClick(R.id.stopButton)
    void onStopClicked() {
        playButton.setEnabled(true);
        stopButton.setEnabled(false);
        Intent serviceIntent = new Intent(MainActivity.this, MusicPlayerService.class);
        serviceIntent.putExtra(STOP_MUSIC, STOP_VALUE);
        startService(serviceIntent);

    }

    private void registerMyReceiver() {

        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BROADCAST_ACTION);
            registerReceiver(musicStopReceiver, intentFilter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void consumeResponse(ApiResponse apiResponse) {

        switch (apiResponse.status) {

            case LOADING:

                Toast.makeText(this, getResources().getString(R.string.downloading_file), Toast.LENGTH_SHORT).show();
                break;

            case SUCCESS:

                Toast.makeText(this, getResources().getString(R.string.download_success), Toast.LENGTH_SHORT).show();
                successReponse(apiResponse.data);
                break;

            case ERROR:
                Toast.makeText(this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();


            default:
                break;
        }
    }


    public void successReponse(Response<ResponseBody> response) {
        filePath = mainViewModel.saveImage(MainActivity.this, response);
        if (filePath != null && !filePath.equals("")) {
            playButton.setEnabled(true);

        }

    }

    class MusicFinishedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                playButton.setEnabled(true);
                stopButton.setEnabled(false);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}





