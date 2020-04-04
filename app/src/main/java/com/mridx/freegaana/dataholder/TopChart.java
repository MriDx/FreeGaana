/*
 * Copyright (c) 2020.  by MriDx
 */

package com.mridx.freegaana.dataholder;

import java.io.Serializable;

public class TopChart implements Serializable {

    private String id, title,  url, thumbnail;

    public TopChart(String id, String url, String thumbnail) {
        this.id = id;
        this.url = url;
        this.thumbnail = thumbnail;
    }

    public TopChart(String id, String title, String url, String thumbnail) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.thumbnail = thumbnail;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
