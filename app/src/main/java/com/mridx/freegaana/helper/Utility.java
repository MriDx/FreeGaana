/*
 * Copyright (c) 2020.  by MriDx
 */

package com.mridx.freegaana.helper;

import android.text.Html;
import android.text.TextUtils;

public class Utility {

    public Utility() {
    }

    public String formatArtist(String data) {
        String[] artists = data.split(",");
        for (int i = 0; i < artists.length; i++) {
            artists[i] = artists[i].split("###")[0];
        }
        return TextUtils.join(", ", artists);
    }

    public String unescapeString(String data) {
        return Html.fromHtml(data).toString().replaceAll("&quot;", "\"");
    }

    public String getHDThumbnail(String url) {
        String[] data = url.split("/");
        data[0] = data[0] + "/";
        String[] endPart = data[7].split("_");
        endPart[1] = "480x480";
        data[7] = TextUtils.join("_", endPart);
        //data[7] = data[7].split("_")[0] + "_480x480_" + data[7].split("_")[2];
        return TextUtils.join("/", data);
    }

}
