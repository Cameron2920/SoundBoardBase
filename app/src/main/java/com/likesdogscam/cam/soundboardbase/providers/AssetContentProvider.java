package com.likesdogscam.cam.soundboardbase.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by likesdogscam on 1969-04-20.
 */

public class AssetContentProvider extends ContentProvider {
  @Override
  public AssetFileDescriptor openAssetFile(Uri uri, String mode) throws FileNotFoundException {
    if(getContext() == null){
      throw new FileNotFoundException();
    }
    AssetManager assetManager = getContext().getAssets();
    String fileName = uri.getPath();

    if(fileName == null) {
      throw new FileNotFoundException();
    }
    AssetFileDescriptor assetFileDescriptor = null;

    if(fileName.length() > 0 && fileName.charAt(0) == '/'){
      fileName = fileName.substring(1);
    }

    try {
      assetFileDescriptor = assetManager.openFd(fileName);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return assetFileDescriptor;
  }

  @Override
  public boolean onCreate() {
    return false;
  }

  @Nullable
  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    return null;
  }

  @Nullable
  @Override
  public String getType(Uri uri) {
    if(uri.getPath().endsWith(".mp3")) {
      return "audio/mpeg";
    }
    return null;
  }

  @Nullable
  @Override
  public Uri insert(Uri uri, ContentValues values) {
    return null;
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    return 0;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    return 0;
  }
}
