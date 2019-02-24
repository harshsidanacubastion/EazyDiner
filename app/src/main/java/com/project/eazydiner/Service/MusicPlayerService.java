package com.project.eazydiner.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.io.IOException;

import javax.annotation.Nullable;

import static com.project.eazydiner.utils.Const.BROADCAST_ACTION;
import static com.project.eazydiner.utils.Const.FILE_PATH;
import static com.project.eazydiner.utils.Const.STOP_MUSIC;
import static com.project.eazydiner.utils.Const.STOP_VALUE;

public class MusicPlayerService extends Service {
    private MediaPlayer mPlayer;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String filePath = intent.getStringExtra(FILE_PATH);
        String stop = intent.getStringExtra(STOP_MUSIC);

        if (stop.equals(STOP_VALUE)) {
            mPlayer.stop();
        } else {
            mPlayer = new MediaPlayer();


            try {
                mPlayer.setDataSource(filePath);
                mPlayer.prepare();

            } catch (IOException e) {
                e.printStackTrace();
            }
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    mPlayer.start();

                }
            });
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mMediaPlayer) {

                    mMediaPlayer.release();
                    musicCompleted();
                }
            });
        }
        return START_STICKY;
    }

    private void musicCompleted() {
        try {
            Intent broadCastIntent = new Intent();
            broadCastIntent.setAction(BROADCAST_ACTION);
            sendBroadcast(broadCastIntent);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.stop();
    }
}
