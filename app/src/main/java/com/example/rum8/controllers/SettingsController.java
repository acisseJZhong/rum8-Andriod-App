package com.example.rum8.controllers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.example.rum8.database.Db;
import com.example.rum8.listeners.SettingsControllerListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;

import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SettingsController {

    private static final double ONE_HUNDRED_PERCENT = 100.0;

    private SettingsControllerListener controllerListener;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    public String usernameEntered;
    public String usernameEntered_lastName;

    public SettingsController(final SettingsControllerListener controllerListener) {
        this.controllerListener = controllerListener;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public void onSaveButtonClicked(final Map<String, Object> userHash) {
        Db.updateUser(db, auth.getCurrentUser(), userHash)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot successfully written");
                    controllerListener.showToast("Saved");
                })
                .addOnFailureListener(e -> controllerListener.showToast("Network error"));
    }

    public void loadUserInfo() {
        Db.fetchUserInfo(db, auth.getCurrentUser())
                .addOnSuccessListener(documentSnapshot -> {
                    final Map<String, Object> data = documentSnapshot.getData();
                    controllerListener.showCurrentUserInfo(data);
                })
                .addOnFailureListener(e -> {
                    final String message = "Network error";
                    controllerListener.showToast(message);
                });
    }

    public void onChooseImageCliked() {
        controllerListener.chooseImage();
    }

    public void onUploadImageClicked(final Uri filePath) {
        if (filePath != null) {
            final FirebaseUser user = auth.getCurrentUser();
            final FirebaseStorage storage = FirebaseStorage.getInstance();
            controllerListener.showUploadImageProgress();
            Db.updateProfilePicture(storage, user, filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        controllerListener.hideUploadImageProgress();
                        final String message = "Successfully uploaded";
                        controllerListener.showToast(message);
                    })
                    .addOnFailureListener(e -> {
                        controllerListener.hideUploadImageProgress();
                        final String message = "Network error";
                        controllerListener.showToast(message);
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (ONE_HUNDRED_PERCENT * taskSnapshot.getBytesTransferred() / taskSnapshot
                                .getTotalByteCount());
                        controllerListener.updateUploadImagePercentage(progress);
                    });
        }
    }

    public void loadUserProfileImage() {
        Db.fetchUserProfilePicture(storage, auth.getCurrentUser())
                .addOnSuccessListener(bytes -> {
                    final Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    controllerListener.setUserProfileImage(bmp);
                })
                .addOnFailureListener(e -> {
                    // fetch default if the user does not upload
                    controllerListener.showDefaultImage();
                    // show error message if both way fails
                    int errorCode = ((StorageException) e).getErrorCode();
                    if (errorCode != StorageException.ERROR_OBJECT_NOT_FOUND) {
                        final String message = "Network error";
                        controllerListener.showToast(message);
                    }
                });
    }

}