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
import java.util.Random;

/**
 * Created by khalid on 19/03/18.
 */

public class CloudStorageAPI {
    private StorageReference imagesRef, eventsImages;
    private ArrayList<String> imagesURLs;
//    private int counter;

    public CloudStorageAPI() {
        this.imagesRef = FirebaseStorage.getInstance().getReference().child("usersImages");
        this.eventsImages = FirebaseStorage.getInstance().getReference().child("eventsImages");
    }

    public void UploadImage(Bitmap image, boolean userImage, final CloudStorageListener.UploadUserImageListener cloudStorageListener) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        String imageId;
        if (userImage) {
            imageId = FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg";
        } else {
            imageId = getSaltString() + ".jpg";

        }
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

    public void UploadEventImages(String eventId, final ArrayList<Bitmap> images, final CloudStorageListener.UploadEventImagesListener uploadEventImagesListener) {
        //Firebase cloud storage SDK doesn't have a multiple files upload method, so we will make it manually
        imagesURLs = new ArrayList<>();
//        counter = 1;
        StorageReference eventImagesIds = eventsImages.child(eventId);
        for (Bitmap image : images) {
            if (image != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();
                String imageId = getSaltString() + ".jpg";
//                counter++;
                StorageReference imageRef = eventImagesIds.child(imageId);
                UploadTask uploadTask = imageRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.v("UploadImageEx", exception.toString());
                        uploadEventImagesListener.onImagesUploaded(null);
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                        imagesURLs.add(downloadUrl);
                        if (imagesURLs.size() == images.size()) {
                            uploadEventImagesListener.onImagesUploaded(imagesURLs);
                        }

                    }
                });
            }
        }

    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }


}
