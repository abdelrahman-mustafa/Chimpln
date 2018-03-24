package com.indeves.chmplinapp.API;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by khalid on 19/03/18.
 */

public class CloudStorageAPI {
    private StorageReference imagesRef, eventsImages;

    public CloudStorageAPI() {
        this.imagesRef = FirebaseStorage.getInstance().getReference().child("usersImages");
        this.eventsImages = FirebaseStorage.getInstance().getReference().child("eventsImages");
    }

    public void UploadImage(Bitmap image, final CloudStorageListener.UploadUserImageListener cloudStorageListener) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        String imageId = FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg";
        StorageReference imageRef = imagesRef.child(imageId);
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.v("UploadImageEx", exception.toString());
                cloudStorageListener.onImageUpload(null);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                cloudStorageListener.onImageUpload(downloadUrl);

            }
        });

    }

    public void UploadEventImages(int eventId, final ArrayList<Bitmap> images) {
        //Firebase cloud storage SDK doesn't have a multiple files upload method, so we will make it manually

    }


}
