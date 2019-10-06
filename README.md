# ImageAttachmentDialog
Image Attachment Dialog is library that will allow you to select image from gallery or camera and compressed it and give it to you size will reduce

# Screenshots:
<!---ImageAttachmentDialog--->
![](https://raw.githubusercontent.com/khizar1556/ImageAttachmentDialog/master/screenshots/s1.png)
![](https://raw.githubusercontent.com/khizar1556/ImageAttachmentDialog/master/screenshots/s2.png)

# features
1. Make dialog of this
2. Capture image using Camera
3. Select Image From Gallery
4. Callbacks gives file and bitmaps
5.Also Give Compressed Files and Bitmaps

# How to add Library into Your project
<!---ImageAttachmentDialog--->
The easiest way to get started using ImageAttachmentDialog is to add it as a gradle
dependency. You need to make sure you have the maven { url 'https://jitpack.io' } repositories
included in the `build.gradle` file in the root of your project:

<!---ImageAttachmentDialog--->
```gradle
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
<!---ImageAttachmentDialog---><!---MK video playerImageAttachmentDialog--->
Next add a gradle compile dependency to the `build.gradle` file of your app
module. The following will add a dependency to the full library:
<!---ImageAttachmentDialogr--->
```gradle
dependencies {
	         implementation 'com.github.khizar1556:ImageAttachmentDialog:-SNAPSHOT'
	}
```

# How to use 
## case 1: Decalare a variable
``` java

 private DialogImageAttachment dialogImageAttachment;
```
## case 2: Initialize it
``` java

 dialogImageAttachment = DialogImageAttachment.make(this).show();
```
## case 3: Set Callback and Do some actions in callback functions
``` java
 dialogImageAttachment = DialogImageAttachment.make(this).setCallback(new ImageAttachentCallback() {
                @Override
                public void onSuccess(File originalFile, File compressedFile) {

                }

                @Override
                public void onSuccess(Bitmap originalFileBitmap, Bitmap compressedFileBitmap) {
                   // if (originalFileBitmap != null) {
                   //     originalIv.setImageBitmap(originalFileBitmap);
                   // }
                   // if (compressedFileBitmap != null) {
                   //     compressedIv.setImageBitmap(compressedFileBitmap);
                   // }

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
```
## case 4: Call OnActivityResult Method for Images

``` java
@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dialogImageAttachment.onActivityResult(requestCode, resultCode, data);
    }
```

## case 5: Call onRequestPermissionsResult Method for Permission handling

``` java
 @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        dialogImageAttachment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
```
