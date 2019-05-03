package com.example.kiemtragk;

import android.graphics.Bitmap;

import org.json.JSONArray;

public interface IViewItem {
    void onRequestSuccess(Bitmap bitmap);
    void onGetDataSuccess(JSONArray jsonArray);
    void onSuccess(String message);
    void onFail(String message);
}
