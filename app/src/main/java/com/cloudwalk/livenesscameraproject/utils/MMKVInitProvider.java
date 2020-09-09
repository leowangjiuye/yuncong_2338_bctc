package com.cloudwalk.livenesscameraproject.utils;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.getkeepsafe.relinker.ReLinker;
import com.tencent.mmkv.MMKV;

import java.io.File;

public class MMKVInitProvider extends ContentProvider {
    @Override
    public boolean onCreate() {
        if (android.os.Build.VERSION.SDK_INT == 19) {  //MMKV 适配android-19
            File file = new File(Environment.getExternalStorageState() + File.separator + "midware_config");
            if (!file.exists()) {
                file.mkdirs();
            }
            MMKV.initialize(file.getAbsolutePath(), new MMKV.LibLoader() {
                @Override
                public void loadLibrary(String libName) {
                    ReLinker.loadLibrary(getContext(), libName);
                }
            });
        } else {
            MMKV.initialize(getContext());
        }
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
