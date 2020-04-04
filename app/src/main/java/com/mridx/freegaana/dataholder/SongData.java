/*
 * Copyright (c) 2020.  by MriDx
 */

package com.mridx.freegaana.dataholder;

import java.io.Serializable;

public class SongData implements Serializable {

    private String songName, artistDetails, thumbnail, songUrl, albumName, albumId;

    public SongData(String songName, String artistDetails, String thumbnail) {
        this.songName = songName;
        this.artistDetails = artistDetails;
        this.thumbnail = thumbnail;
    }

    public SongData(String songName, String artistDetails, String thumbnail, String songUrl) {
        this.songName = songName;
        this.artistDetails = artistDetails;
        this.thumbnail = thumbnail;
        this.songUrl = songUrl;
    }

    public SongData(String songName, String artistDetails, String thumbnail, String songUrl, String albumName) {
        this.songName = songName;
        this.artistDetails = artistDetails;
        this.thumbnail = thumbnail;
        this.songUrl = songUrl;
        this.albumName = albumName;
    }

    public SongData(String songName, String artistDetails, String thumbnail, String songUrl, String albumName, String albumId) {
        this.songName = songName;
        this.artistDetails = artistDetails;
        this.thumbnail = thumbnail;
        this.songUrl = songUrl;
        this.albumName = albumName;
        this.albumId = albumId;
    }

    public String getSongName() {
        return songName;
    }

    public String getArtistDetails() {
        return artistDetails;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getAlbumId() {
        return albumId;
    }
}
