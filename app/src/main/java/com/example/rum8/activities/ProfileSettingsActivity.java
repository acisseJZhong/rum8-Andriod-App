package com.example.rum8.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.rum8.R;
import com.example.rum8.adapters.ProfileSettingsViewPagerAdapter;
import com.example.rum8.controllers.ProfileSettingsController;
import com.example.rum8.listeners.ProfileSettingsControllerListener;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

public class ProfileSettingsActivity extends FragmentActivity
        implements ProfileSettingsControllerListener {

    private ProfileSettingsController controller;
    private ViewPager viewPager;

    private TextInputEditText firstName;
    private TextInputEditText lastName;

    private Button btnChoose, btnUpload;
    private ImageView imageView;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 65607;

    private Button buttonGeneralInfoNext;
    private Button buttonUploadProfileImage;
    private ImageView imageUserProfile;
    private static int result_load_image = 1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        initViews();
        initController();

    }

    @Override
    public void showToast(final String message, final int toastLength) {
        Toast.makeText(ProfileSettingsActivity.this, message, toastLength).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        //requestCode == PICK_IMAGE_REQUEST &&
        if( resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    private void initViews() {
        viewPager = findViewById(R.id.profile_settings_view_pager);
        viewPager.setAdapter(new ProfileSettingsViewPagerAdapter(getSupportFragmentManager()));

        firstName = (TextInputEditText) findViewById(R.id.general_info_first_name_field);
        lastName = (TextInputEditText) findViewById(R.id.general_info_last_name_field);

        buttonUploadProfileImage = (Button) findViewById(R.id.general_info_profile_image_upload_button);
    }

    private void initController() {
        controller = new ProfileSettingsController(this);
    }

    public Uri getPath (){
        return filePath;
    }

}
