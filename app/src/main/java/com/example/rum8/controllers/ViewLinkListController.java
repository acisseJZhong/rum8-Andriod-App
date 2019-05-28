package com.example.rum8.controllers;

import com.example.rum8.dataModels.LinkListSingleLink;
import com.example.rum8.database.Db;
import com.example.rum8.listeners.ViewLinkListControllerListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ViewLinkListController {

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private Map<String, Object> linkListUidMap; //the hashmap with link uids as keys
    private Set<String> linkListUidSet;

    private ViewLinkListControllerListener controllerListener;

    /**
     * Constructor
     * */
    public ViewLinkListController(final ViewLinkListControllerListener controllerListener) {
        this.controllerListener = controllerListener;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    /**
     * menu buttons
     * */
    public void onGoToProfileSettingsButtonClicked(){ controllerListener.goToProfileSettings();};
    public void onLogoutButtonClicked(){
        FirebaseAuth.getInstance().signOut();
        controllerListener.goToLogin();
    }
    public void onGoToViewLinkListButtonClicked(){ controllerListener.goToViewLinkList();}
    public void onGoToAdvSettingsButtonClicked(){ controllerListener.goToAdvSettings();}

    /**
     * Fetch matched links and display
     */
    public void prepareLinks(){
        linkListUidMap = new HashMap<>();
        linkListUidSet = new HashSet<>();
        Db.fetchUserInfo(db, auth.getCurrentUser()).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                linkListUidMap = (HashMap<String, Object>) task.getResult().get("matched");
                linkListUidSet = linkListUidMap.keySet();
                for(String uid:linkListUidSet)
                    Db.fetchUserInfoById(db, uid).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            String first_name = (String) task1.getResult().get("first_name");
                            String last_name = (String) task1.getResult().get("last_name");

                            LinkListSingleLink newLink = new LinkListSingleLink(first_name, last_name, uid);
                            controllerListener.addNewLink(newLink);
                            controllerListener.displayLinks(controllerListener.getLinks());
                            /*
                            //fetch link's profile image
                            Db.fetchUserProfilePictureById(storage, uid).addOnSuccessListener(bytes -> {
                                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                String first_name = (String) task1.getResult().get("first_name");
                                String last_name = (String) task1.getResult().get("last_name");

                                LinkListSingleLink newLink = new LinkListSingleLink(first_name, last_name, uid);
                                controllerListener.addNewLink(newLink);
                                controllerListener.displayLinks(controllerListener.getLinks());
                            }).addOnFailureListener(e ->
                                    //fetch default user profile image
                                    Db.fetchDefaultUserProfilePicture(storage).addOnSuccessListener(
                                            bytes -> {
                                                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                                                String first_name = (String) task1.getResult().get("first_name");
                                                String last_name = (String) task1.getResult().get("last_name");

                                                LinkListSingleLink newLink = new LinkListSingleLink(first_name, last_name, uid);
                                                controllerListener.addNewLink(newLink);
                                                controllerListener.displayLinks(controllerListener.getLinks());
                                            }
                                    )
                            );*/
                        }
                    });

            }
        });
    }

    public Task<byte[]> loadDefaultUserProfileImage(final FirebaseStorage storage){
        return Db.fetchDefaultUserProfilePicture(storage);
    }

    public Task<byte[]> loadLinkProfileImage(final FirebaseStorage storage, final String linkUid){
        return Db.fetchUserProfilePictureById(storage, linkUid);
    }

}
