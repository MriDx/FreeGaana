/*
 * Copyright (c) 2020.  by MriDx
 */

package com.mridx.freegaana.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

public class PermissionHelper {

    public static final int STORAGE_PERMISSION_REQUEST = 201, STORAGE_PERMISSION_SETTINGS = 202;
    public static final int STORAGE_PERMISSION = 101;

    public PermissionHelper() {
    }

    public boolean checkStoragePermission(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    public void askStoragePermission(Context context) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(((Activity)context), Manifest.permission.READ_EXTERNAL_STORAGE) ||
        ActivityCompat.shouldShowRequestPermissionRationale(((Activity)context), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            showPermissionRationalAlert(context, 0);
        } else {
            ActivityCompat.requestPermissions(((Activity)context), new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST);
        }
    }

    private void showPermissionRationalAlert(Context context, int i) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle(getTitle(i));
        alertDialog.setMessage(getMessage(i));
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "GO TO SETTINGS", (dialog, which) -> {
            openSettings(context, i);
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> alertDialog.dismiss());
        alertDialog.show();
    }

    private void openSettings(Context context, int i) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ((Activity) context).startActivityForResult(intent, STORAGE_PERMISSION_SETTINGS);
    }

    private String getMessage(int i) {
        return "Allow storage permission to download songs";
    }

    private String getTitle(int i) {
        return "Permission Denied !";
    }
}
