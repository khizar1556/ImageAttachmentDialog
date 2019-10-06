package com.khizar1556.ImageAttachmentLibrary.dialogs;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;


import com.khizar1556.ImageAttachmentLibrary.Callback.ImageAttachentCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import id.zelory.compressor.Compressor;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;

public class DialogImageAttachment {
    public Activity activity;
    private ImageAttachentCallback callback;
    String mCurrentPhotoPath;
    public static final int WRITE_PERMISSION = 10001;
    public static final int PICK_IMAGE_CAMERA = 111;
    public static final int PICK_IMAGE_GALLERY = 122;
    public DialogImageAttachment(Activity activity) {
        this.activity = activity;
    }

    public static DialogImageAttachment make(Activity activity) {
        DialogImageAttachment dialogImageAttachment = new DialogImageAttachment(activity);
        return dialogImageAttachment;
    }

    public DialogImageAttachment show() {
        selectImage();
        return this;
    }

    public DialogImageAttachment setCallback(ImageAttachentCallback callback) {
        this.callback = callback;
        return this;
    }


    private void selectImage() {
        if (callback == null) {
            Toast.makeText(activity,"ImageAttachmentCallback is null.Please set callback",Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if (EasyPermissions.hasPermissions(activity, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Select Option");
                builder.setItems(options, (dialog, item) -> {
                    if (options[item].equals("Take Photo")) {
                        dialog.dismiss();
                        dispatchTakePictureIntent();
                    } else if (options[item].equals("Choose From Gallery")) {
                        dialog.dismiss();
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        callback.onClick(pickPhoto, PICK_IMAGE_GALLERY);
                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, WRITE_PERMISSION);
            }
        } catch (Exception e) {
            Toast.makeText(activity,"Camera Permission error",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(activity,
                        activity.getPackageName() + ".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                callback.onClick(takePictureIntent, PICK_IMAGE_CAMERA);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (callback != null) {
            if (resultCode == RESULT_OK) {
                if (requestCode ==PICK_IMAGE_GALLERY) {
                    if (data != null) {
                        try {
                            Uri pickedImage = data.getData();
                            String[] filePath = {MediaStore.Images.Media.DATA};
                            Cursor cursor = activity.getContentResolver().query(pickedImage, filePath, null, null, null);
                            cursor.moveToFirst();
                            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
                            File file1 = new File(imagePath);
                       /* new ImageCompressionAsyncTask(activity).execute(mCurrentPhotoPath,
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Silicompressor/images");
                       */
                            Bitmap bitmap = MediaStore.Images.Media
                                    .getBitmap(activity.getContentResolver(), FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", file1));
                            /* Bitmap bitmap = (Bitmap) data.getExtras().get("data");*/
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                            File compressesdFile = null;

                            compressesdFile = new Compressor(activity)
                                    .setMaxWidth(bitmap.getWidth()/2 )
                                    .setMaxHeight(bitmap.getHeight()/2 )
                                    .setQuality(100)
                                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/compressor/images")
                                    .compressToFile(file1);
                            Bitmap bitmapcompress = MediaStore.Images.Media
                                    .getBitmap(activity.getContentResolver(), FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", compressesdFile));
                            /* Bitmap bitmap = (Bitmap) data.getExtras().get("data");*/
                            ByteArrayOutputStream bytesCompress = new ByteArrayOutputStream();
                            bitmapcompress.compress(Bitmap.CompressFormat.JPEG, 100, bytesCompress);
                            callback.onSuccess(file1,compressesdFile);
                            callback.onSuccess(bitmap,bitmapcompress);
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.onFailure(e.getMessage());
                        }
                    }
                } else if (requestCode == PICK_IMAGE_CAMERA) {
                    try {
                        File file1 = new File(mCurrentPhotoPath);
                       /* new ImageCompressionAsyncTask(activity).execute(mCurrentPhotoPath,
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Silicompressor/images");
                       */
                        Bitmap bitmap = MediaStore.Images.Media
                                .getBitmap(activity.getContentResolver(), FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", file1));
                        /* Bitmap bitmap = (Bitmap) data.getExtras().get("data");*/
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        File compressesdFile = null;

                        compressesdFile = new Compressor(activity)
                                .setMaxWidth(bitmap.getWidth() / 2)
                                .setMaxHeight(bitmap.getHeight() / 2)
                                .setQuality(100)
                                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/compressor/images")
                                .compressToFile(file1);

                        Bitmap bitmapcompress = MediaStore.Images.Media
                                .getBitmap(activity.getContentResolver(), FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", compressesdFile));
                        /* Bitmap bitmap = (Bitmap) data.getExtras().get("data");*/
                        ByteArrayOutputStream bytesCompress = new ByteArrayOutputStream();
                        bitmapcompress.compress(Bitmap.CompressFormat.JPEG, 100, bytesCompress);
                        callback.onSuccess(file1,compressesdFile);
                        callback.onSuccess(bitmap,bitmapcompress);
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                }
            }
        } else {
            Toast.makeText(activity,"ImageAttachmentCallback is null.Please set callback",Toast.LENGTH_SHORT).show();
        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode==WRITE_PERMISSION){
            if (grantResults.length > 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED
            ) {
                selectImage();
            }
        }

    }
}
