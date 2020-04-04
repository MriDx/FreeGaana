/*
 * Copyright (c) 2020.  by MriDx
 */

package com.mridx.freegaana.helper;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;

import java.io.File;
import java.io.IOException;

public class GaanaDownloader {

    private static final String TAG = "kaku";
    private float duration;

    public GaanaDownloader() {
    }

    OnGetMaxDuration onGetMaxDuration;
    public interface OnGetMaxDuration {
        void setOnGetMaxDuration(Float duration);
    }
    public void setOnGetMaxDuration(OnGetMaxDuration onGetMaxDuration) {
        this.onGetMaxDuration = onGetMaxDuration;
    }

    OnProgress onProgress;
    public interface OnProgress {
        void setOnProgress(float progress);
    }
    public void setOnProgress(OnProgress onProgress) {
        this.onProgress = onProgress;
    }

    OnFailed onFailed;
    public interface OnFailed {
        void setOnFailed();
    }
    public void setOnFailed(OnFailed onFailed){
        this.onFailed = onFailed;
    }

    OnCompleted onCompleted;
    public interface OnCompleted {
        void setOnCompleted();
    }
    public void setOnCompleted(OnCompleted onCompleted){
        this.onCompleted = onCompleted;
    }


    public void startDownload(Context context, String mediaUrl, String songName) {

        FFmpeg fFmpeg = FFmpeg.getInstance(context);
        try {
            fFmpeg.execute(getCommands(mediaUrl, songName), new FFmpegExecuteResponseHandler() {
                @Override
                public void onSuccess(String message) {
                    Log.d(TAG, "onSuccess: " + message);
                }

                @Override
                public void onProgress(String message) {
                    if (message.contains("Duration")) {
                        onGetMaxDuration.setOnGetMaxDuration(getDuration(message));
                    }
                    //Log.d(TAG, "onProgress: " + message);
                    /*if (message.startsWith("size=")) {
                        String[] array = message.split(" ");
                        Log.d(TAG, "onProgress: " + TextUtils.join(",", array));
                        Log.d(TAG, "onProgress: " + array[4]);
                    }*/
                    /*if (message.contains("speed=")) {
                        message = message.replaceAll("\\s+"," ");
                        Log.d(TAG, "onProgress: " + message);
                        String[] data = message.split(" ");
                        String speedD = data[data.length - 1];
                        Log.d(TAG, "onProgress: " + speedD);
                        String speedData = speedD.contains("speed=") ? speedD.split("=")[1] : speedD;
                        speedData = speedData.replace("x", "");
                        Log.d(TAG, "onProgress: " + speedData);
                    }*/
                    if (message.contains("time")) {
                        message = message.replaceAll("\\s+"," ");
                        String[] data = message.split(" ");
                        for (String time : data) {
                            if (time.contains("time=") && time.contains(":")) {
                                time = time.split("=")[1];
                                //Float sec = getSeconds(time);
                                onProgress.setOnProgress(getSeconds(time));
                            }
                        }
                    }
                }

                @Override
                public void onFailure(String message) {
                    Log.d(TAG, "onFailure: " + message);
                    onFailed.setOnFailed();
                }

                @Override
                public void onStart() {

                }

                @Override
                public void onFinish() {
                    Log.d(TAG, "onFinish: Finished");
                    onCompleted.setOnCompleted();
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }

    }

    private float getDuration(String message) {
        message = message.replaceAll("\\s+"," ");
        message = message.split(",")[0];
        Log.d(TAG, "getDuration: " + message);
        String time = message.split(" ")[2];
        Log.d(TAG, "getDuration: " + time);
        //time = time.replaceAll(".", "");
        //String[] x = time.split(".");
        //Log.d(TAG, "getDuration: " + x.length);
        time = time.split("\\.")[0];
       return getSeconds(time);
    }

    private float getSeconds(String time) {
        String[] objects = time.split(":");
        float sec = 0;
        if (Float.parseFloat(objects[0]) > 0) {
            sec = sec +  Float.parseFloat(objects[0]) * 60 * 60;
        }
        if (Float.parseFloat(objects[1]) > 0) {
            sec = sec + Float.parseFloat(objects[1]) * 60;
        }
        if (Float.parseFloat(objects[2]) > 0) {
            sec = sec + Float.parseFloat(objects[2]);
        }
        Log.d(TAG, "getSeconds: " + sec);
        return sec;
    }

    private String[] getCommands(String mediaUrl, String fileName) {
        return new String[] {"-y", "-i", getHttpUrl(mediaUrl), "-c:a copy", "copy", createFile(fileName)};
    }

    private String getHttpUrl(String mediaUrl) {
        if (mediaUrl.contains("https")) {
            return mediaUrl.replace("https", "http");
        }
        return mediaUrl;
    }

    private String createFile(String fileName) {
        String file = fileName + ".aac";
        File music = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!music.exists()) {
            music.mkdirs();
        }
        File imageFile = new File(music, file);
        try {
            imageFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile.getPath();
    }

}
