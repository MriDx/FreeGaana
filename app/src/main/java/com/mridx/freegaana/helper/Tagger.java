/*
 * Copyright (c) 2020.  by MriDx
 */

package com.mridx.freegaana.helper;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.mridx.freegaana.dataholder.Song;


import org.apache.http.util.ByteArrayBuffer;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.ID3v24FieldKey;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.images.AndroidArtwork;
import org.jaudiotagger.tag.mp4.Mp4FieldKey;
import org.jaudiotagger.tag.mp4.Mp4Tag;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

public class Tagger {


    public Tagger() {
    }

    public boolean setTags(Song song, String currentFile) {
        //byte[] bitmapData = getByteArrayImage(new Utility().getHDThumbnail(song.getArtwork()));
        //getFile(currentFile);
        /*try {
            AudioFile audioFile = AudioFileIO.read(getFile(currentFile));
            Mp4Tag mp4Tag = (Mp4Tag) audioFile.getTag();
            mp4Tag.setField(Mp4FieldKey.ALBUM, song.getAlbumTitle());
            mp4Tag.setField(Mp4FieldKey.ARTIST, song.getArtists());
            mp4Tag.setField(Mp4FieldKey.DAY, song.getRelease_date());
            mp4Tag.setField(Mp4FieldKey.TITLE, song.getSongName());
            mp4Tag.setField(Mp4FieldKey.LYRICS, "HEllo World");
            AndroidArtwork cover = new AndroidArtwork();
            cover.setBinaryData(bitmapData);
            mp4Tag.setField(cover);
            audioFile.commit();
            return true;
        } catch (IOException | CannotReadException | ReadOnlyFileException | TagException | InvalidAudioFrameException | CannotWriteException e) {
            e.printStackTrace();
            return false;
        }*/

        try {
            AudioFile audioFile = AudioFileIO.read(getFile(currentFile));
            ID3v24Tag tags = (ID3v24Tag) audioFile.getTag();
            tags.addField(tags.createField(ID3v24FieldKey.ALBUM, song.getAlbumTitle()));
            tags.addField(tags.createField(ID3v24FieldKey.ARTIST, song.getArtists()));
            tags.addField(tags.createField(ID3v24FieldKey.TITLE, song.getSongName()));
            //tags.addField(tags.createField(ID3v24FieldKey.LYRICS, "Hello World !"));
            tags.addField(tags.createArtworkField(getByteArrayImage(new Utility().getHDThumbnail(song.getArtwork())), "image/jpeg"));
            audioFile.setTag(tags);
            audioFile.commit();
        } catch (IOException | CannotReadException | ReadOnlyFileException | TagException | InvalidAudioFrameException | CannotWriteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private File getFile(String currentFile) {
        return new File(currentFile);
    }

    public boolean SetTags(String currentSong, String currentAlbum, String currentSingers, String currentYear, String currentHDImage, File file) {

        //File file = new File(getExternalStorageDirectory() + "/Music/" + name + "." + ext);
        //String ARTIST = "Mridul Baishya";
        boolean TagSet = false;
        byte[] bitmapdata = getByteArrayImage(currentHDImage);
        if (file != null) {
            try {
                AudioFile audioFile = AudioFileIO.read(file);
                Mp4Tag mp4Tag = (Mp4Tag) audioFile.getTag();
                mp4Tag.setField(Mp4FieldKey.ALBUM, currentAlbum);
                mp4Tag.setField(Mp4FieldKey.ARTIST, currentSingers);
                //mp4tag.setField(Mp4FieldKey.ALBUM_ARTIST, ALBUM_ARTISTS);
                mp4Tag.setField(Mp4FieldKey.DAY, currentYear);
                //mp4tag.setField(Mp4FieldKey.COMPOSER, COMPOSER);
                // mp4tag.setField(Mp4FieldKey.DESCRIPTION, DESCR);
                // mp4tag.setField(Mp4FieldKey.COPYRIGHT, COPYR);
                mp4Tag.setField(Mp4FieldKey.TITLE, currentSong);
                AndroidArtwork cover = new AndroidArtwork();
                cover.setBinaryData(bitmapdata);
                mp4Tag.setField(cover);
                audioFile.commit();
                Log.d("Tag", "Tag added");
                return TagSet = true;


            } catch (CannotReadException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TagException e) {
                e.printStackTrace();
            } catch (ReadOnlyFileException e) {
                e.printStackTrace();
            } catch (InvalidAudioFrameException e) {
                e.printStackTrace();
            } catch (CannotWriteException e) {
                e.printStackTrace();
            }
        }
        return TagSet;

    }



    private byte[] getByteArrayImage(String url) {


        class x extends AsyncTask<Void, Void, byte[]> {

            @Override
            protected byte[] doInBackground(Void... voids) {
                try {
                    URL imageUrl = new URL(url);
                    URLConnection ucon = imageUrl.openConnection();

                    InputStream is = ucon.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    ByteArrayBuffer baf = new ByteArrayBuffer(500);
                    int current = 0;
                    while ((current = bis.read()) != -1) {
                        baf.append((byte) current);
                    }
                    return baf.toByteArray();
                } catch (Exception e) {
                    Log.d("ImageManager", "Error: " + e.toString());
                }
                return null;
            }

            @Override
            protected void onPostExecute(byte[] bytes) {
                super.onPostExecute(bytes);
            }
        }
        try {
            return new x().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }


}
