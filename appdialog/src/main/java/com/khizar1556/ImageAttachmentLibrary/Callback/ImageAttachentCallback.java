package com.khizar1556.ImageAttachmentLibrary.Callback;

import android.content.Intent;
import android.graphics.Bitmap;

import java.io.File;

public interface ImageAttachentCallback {
    void onSuccess(File originalFile,File compressedFile);
    void onSuccess(Bitmap originalFileBitmap, Bitmap compressedFileBitmap);
    void onFailure(String error);
    void onClick(Intent intent, int id);

}
