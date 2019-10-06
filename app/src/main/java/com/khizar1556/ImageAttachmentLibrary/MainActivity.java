package com.khizar1556.ImageAttachmentLibrary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.khizar1556.ImageAttachmentLibrary.Callback.ImageAttachentCallback;
import com.khizar1556.ImageAttachmentLibrary.dialogs.DialogImageAttachment;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private DialogImageAttachment dialogImageAttachment;
    private ImageView originalIv, compressedIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        originalIv = findViewById(R.id.originalIv);
        compressedIv = findViewById(R.id.compressedIv);
        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(v -> {
            dialogImageAttachment = DialogImageAttachment.make(this).setCallback(new ImageAttachentCallback() {
                @Override
                public void onSuccess(File originalFile, File compressedFile) {

                }

                @Override
                public void onSuccess(Bitmap originalFileBitmap, Bitmap compressedFileBitmap) {
                    if (originalFileBitmap != null) {
                        originalIv.setImageBitmap(originalFileBitmap);
                    }
                    if (compressedFileBitmap != null) {
                        compressedIv.setImageBitmap(compressedFileBitmap);
                    }

                }

                @Override
                public void onFailure(String error) {
                    Toast.makeText(MainActivity.this,error,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onClick(Intent intent, int id) {
                    startActivityForResult(intent, id);
                }
            }).show();
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dialogImageAttachment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        dialogImageAttachment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
