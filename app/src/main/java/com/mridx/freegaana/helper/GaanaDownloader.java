/*
 * Copyright (c) 2020.  by MriDx
 */

package com.mridx.freegaana.helper;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

/*import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;*/
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.mridx.freegaana.dataholder.Song;
import com.mridx.freegaana.dataholder.SongData;

import java.io.File;
import java.io.IOException;

public class GaanaDownloader {

    private static final String TAG = "kaku";
    private float duration;
    private String currentFilePath;
    private FFmpeg fFmpeg;

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

    public void setOnFailed(OnFailed onFailed) {
        this.onFailed = onFailed;
    }

    OnCompleted onCompleted;

    public interface OnCompleted {
        void setOnCompleted();
    }

    public void setOnCompleted(OnCompleted onCompleted) {
        this.onCompleted = onCompleted;
    }


    public void startDownload(Context context, String mediaUrl, Song song) {
        fFmpeg = FFmpeg.getInstance(context);
        try {
            fFmpeg.execute(getCommands(mediaUrl, song.getSongName()), new FFmpegExecuteResponseHandler() {
                @Override
                public void onSuccess(String message) {
                    Log.d(TAG, "onSuccess: " + message);
                    convertFile(song);
                }

                @Override
                public void onProgress(String message) {
                    if (message.contains("Duration")) {
                        onGetMaxDuration.setOnGetMaxDuration(getDuration(message));
                    }

                    if (message.contains("time")) {
                        message = message.replaceAll("\\s+", " ");
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
                    //onCompleted.setOnCompleted();
                    //setTags(song);

                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }
    }

    private void convertFile(Song song) {
        try {
            fFmpeg.execute(getConvertCmd(), new FFmpegExecuteResponseHandler() {
                @Override
                public void onSuccess(String message) {
                    Log.d(TAG, "onSuccess: " + message);
                    setTags(song);
                }

                @Override
                public void onProgress(String message) {
                    Log.d(TAG, "onProgress: " + message);

                    if (message.contains("time")) {
                        message = message.replaceAll("\\s+", " ");
                        String[] data = message.split(" ");
                        for (String time : data) {
                            if (time.contains("time=") && time.contains(":")) {
                                time = time.split("=")[1];
                                //Float sec = getSeconds(time);
                                onProgress.setOnProgress(getSeconds(time) + duration);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(String message) {
                    Log.d(TAG, "onFailure: " + message);
                }

                @Override
                public void onStart() {

                }

                @Override
                public void onFinish() {
                    Log.d(TAG, "onFinish: finished");
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
            Log.d(TAG, "convertFile: "+e);
        }
    }

    private void setTags(Song song) {
        /*if (new Tagger().setTags(song, currentFilePath)) {
            if (renameFile(song.getSongName())) {
                onCompleted.setOnCompleted();
            }
        }*/
        deleteOldFile();
        if (new Tagger().setTags(song, currentFilePath.replace(".aac", ".mp3"))) {

            onCompleted.setOnCompleted();
        }

        /*if (renameFile(song.getSongName())) {
            //onCompleted.setOnCompleted();
            if (new Tagger().setTags(song, currentFilePath)) {
                onCompleted.setOnCompleted();
            }
        }*/
    }

    private void deleteOldFile() {
        File file = new File(currentFilePath);
        if (file.exists()) {
            file.delete();
        }
    }

    private boolean renameFile(String songName) {

        File file = new File(currentFilePath);
        File file2 = new File(currentFilePath.replace(".aac", ".m4a"));
        this.currentFilePath = file2.getPath();
        return file.renameTo(file2);
        //new File(currentFilePath).renameTo(getRenamedFile(songName));
    }

    private File getRenamedFile(String songName) {
        File music = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        return new File(music, songName+".m4a");
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
                        message = message.replaceAll("\\s+", " ");
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
        message = message.replaceAll("\\s+", " ");
        message = message.split(",")[0];
        Log.d(TAG, "getDuration: " + message);
        String time = message.split(" ")[2];
        Log.d(TAG, "getDuration: " + time);
        //time = time.replaceAll(".", "");
        //String[] x = time.split(".");
        //Log.d(TAG, "getDuration: " + x.length);
        time = time.split("\\.")[0];
        float d = getSeconds(time);
        this.duration = d;
        //return getSeconds(time);
        return d * 2;
    }

    private float getSeconds(String time) {
        String[] objects = time.split(":");
        float sec = 0;
        if (Float.parseFloat(objects[0]) > 0) {
            sec = sec + Float.parseFloat(objects[0]) * 60 * 60;
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
        return new String[]{"-y", "-i", getHttpUrl(mediaUrl), "-c:a copy", "copy", createFile(fileName)};
    }

    private String[] getConvertCmd() {
        return new String[] {"-y", "-i", this.currentFilePath, "-acodec", "libmp3lame",  createMP3()};
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
        this.currentFilePath = imageFile.getPath();
        return imageFile.getPath();
    }

    private String createMP3() {
        File file = new File(this.currentFilePath.replace(".aac", ".mp3").replace("\\s+", "_").trim());
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file.getPath();
    }

}
