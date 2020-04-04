/*
 * Copyright (c) 2020.  by MriDx
 */

package com.mridx.freegaana.config;

import android.app.Application;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.mridx.freegaana.helper.FirstRunChecker;

public class AppConfig extends Application {

    private String TAG = "kaku";

    @Override
    public void onCreate() {
        super.onCreate();

        if (new FirstRunChecker().isFirstRun(this)) {
            initFFMPEG();
        }
    }

    private void initFFMPEG() {
        FFmpeg fFmpeg = FFmpeg.getInstance(this);
        try {
            fFmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onStart() {
                    Log.d(TAG, "onStart: started ffmpeg init");
                }

                @Override
                public void onFailure() {
                    Log.d(TAG, "onFailure: Failed");
                }

                @Override
                public void onSuccess() {
                    Log.d(TAG, "onSuccess: success");
                }

                @Override
                public void onFinish() {
                    Log.d(TAG, "onFinish: finished");
                }
            });
        } catch (FFmpegNotSupportedException e) {
            e.printStackTrace();
            showNotSupported();
        }
    }

    private void showNotSupported() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Error !");
        alertDialog.setMessage("FFMPEG is not supported by your device. \nWithout FFMPEG this app won't work");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", (dialog, which) -> {
            alertDialog.dismiss();
        });
        alertDialog.show();
    }
}
