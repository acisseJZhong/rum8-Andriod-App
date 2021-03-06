package com.example.rum8.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rum8.R;
import com.example.rum8.controllers.PreviewProfileController;
import com.example.rum8.database.Db;
import com.example.rum8.listeners.PreviewProfileControllerListener;

import java.util.Map;

/**
 * Class that contains the preview profile tab one for preview
 * profile activity.
 */
public class PreviewProfileTab1Fragment extends Fragment implements PreviewProfileControllerListener {

    // Initialize class variable
    private View view;
    private PreviewProfileController controller;
    private TextView budgetField;
    private TextView livingAccommodationsField;
    private TextView otherThingsField;
    private TextView roomTypeField;

    public PreviewProfileTab1Fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_preview_profile_tab1, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        controller = new PreviewProfileController(this);
        controller.loadUserInfo();
        budgetField = view.findViewById(R.id.budget_text);

        livingAccommodationsField = view.findViewById(R.id.living_accommodations_text);
        otherThingsField = view.findViewById(R.id.other_thing_text);
        roomTypeField = view.findViewById(R.id.room_type_text);

    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCurrentUserInfo(final Map<String, Object> data) {
        final String room_type = (String) data.get(Db.Keys.ROOM_TYPE);
        final String budget_min = String.valueOf(data.get(Db.Keys.BUDGET_MIN));
        final String budget_max = String.valueOf(data.get(Db.Keys.BUDGET_MAX));
        final String living_accommodations = (String) data.get(Db.Keys.LIVING_ACCOMMODATIONS);
        final String other_things_you_should_know = (String) data.get(Db.Keys.OTHER_THINGS_YOU_SHOULD_KNOW);

        budgetField.setText("$" + budget_min + " - " + budget_max);
        livingAccommodationsField.setText(living_accommodations);
        otherThingsField.setText(other_things_you_should_know);
        roomTypeField.setText(room_type);

        onResume();
    }

    @Override
    public void setFragment() {

    }

    @Override
    public void setUserProfileImage(Bitmap bitmap) {

    }

    @Override
    public void showDefaultImage() {

    }
}
