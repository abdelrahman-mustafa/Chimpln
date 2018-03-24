package com.indeves.chmplinapp.API;

import java.util.ArrayList;

/**
 * Created by khalid on 24/03/18.
 */

public class CloudStorageListener {
    public interface UploadUserImageListener {
        void onImageUpload(String downloadUrl);
    }

    public interface UploadEventImagesListener {
        void onImagesUploaded(ArrayList<String> imagesUrls);
    }
}