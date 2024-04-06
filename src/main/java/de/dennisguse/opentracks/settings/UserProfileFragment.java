package de.dennisguse.opentracks.settings;

import static de.dennisguse.opentracks.settings.PreferencesUtils.getUnitSystem;

import android.app.AlertDialog;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.net.Uri;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.Intent;
import androidx.core.content.ContextCompat;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import androidx.core.app.ActivityCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;

import java.util.Objects;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.data.models.Height;
import de.dennisguse.opentracks.data.models.HeightFormatter;
import de.dennisguse.opentracks.data.models.Speed;
import de.dennisguse.opentracks.data.models.SpeedFormatter;
import de.dennisguse.opentracks.data.models.Weight;
import de.dennisguse.opentracks.data.models.WeightFormatter;

 // You can choose any value for the request code


public class UserProfileFragment extends PreferenceFragmentCompat {

    private static final int PERMISSION_REQUEST_CODE = 1001;
    private static final int IMAGE_PICKER_REQUEST_CODE = 101;


    SwitchPreference leaderboardSwitch;

    private void startImagePicker() {
        try {
            ImagePicker.with(this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start();
        } catch (Exception e) {
            Log.e("UserProfileFragment", "Error starting image picker: " + e.getMessage());
        }
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_user_profile);

        Preference editPreference = findPreference(getString(R.string.edit_profile_key));
        if (editPreference != null) {
            editPreference.setOnPreferenceClickListener(preference -> {
                showEditProfileDialog();
                return true;
            });
        }

        Preference editProfilePic = findPreference("edit_profile_pic");
        if (editProfilePic != null) {
            editProfilePic.setOnPreferenceClickListener(preference -> {
                Log.d("UserProfileFragment", "Edit profile picture button clicked");
                // Check if permission is granted
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted, request it from the user
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            PERMISSION_REQUEST_CODE);
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        // Permission is already granted, start the image picker
                        Log.d("UserProfileFragment", "Calling startImagePicker()");
                        startImagePicker();
                    }
                }
                return true;
            });
        }

        // Check toggle status for leaderboard preferences
        leaderboardSwitch = findPreference("leaderboard_switch");
        assert leaderboardSwitch != null;
        leaderboardSwitch.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                if(leaderboardSwitch.isChecked())
                {
                    // Form to check/ uncheck shared details

                    displayCustomSharingDialog();

                }
                return false;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allPermissionsGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                Log.d("UserProfileFragment", "Permissions granted, starting image picker");
                startImagePicker();
            } else {
                Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri imageUri = data.getData();
        if (imageUri != null) {
            // Update the profile picture with the selected image
            updateProfilePicture(imageUri);
        }
    }

    private void updateProfilePicture(Uri imageUri) {
        // Load the image from the URI and set it to the profile picture ImageView
        ImageView profilePictureImageView = requireView().findViewById(R.id.profileImageView);
        profilePictureImageView.setImageURI(imageUri);
    }

    private void showEditProfileDialog() {
        // Inflate the custom layout for the edit dialog.
        View formView = LayoutInflater.from(getContext()).inflate(R.layout.edit_profile_form, null);

        // Initialize all the EditText fields.
        EditText editNickname = formView.findViewById(R.id.editNickname);
        EditText editDateOfBirth = formView.findViewById(R.id.editDateOfBirth);
        EditText editHeight = formView.findViewById(R.id.editHeight);
        EditText editWeight = formView.findViewById(R.id.editWeight);
        EditText editGender = formView.findViewById(R.id.editGender);
        EditText editLocation = formView.findViewById(R.id.editLocation);

        // Create the AlertDialog.
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.edit_profile_title)
                .setView(formView)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    // Collect data from the EditText fields.
                    String nickname = editNickname.getText().toString();
                    String dateOfBirth = editDateOfBirth.getText().toString();
                    String height = editHeight.getText().toString();
                    String weight = editWeight.getText().toString();
                    String gender = editGender.getText().toString();
                    String location = editLocation.getText().toString();

                    // Validate and save the data if valid.
                    if (validateInputs(nickname, dateOfBirth, height, weight, gender, location)) {
                        saveProfileData(nickname, dateOfBirth, height, weight, gender, location);
                        showToast("Profile updated successfully!");
                    } else {
                        showToast("Please check your inputs.");
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    // A simple method to show toast messages.
    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    // A method to validate the user inputs.
    private boolean validateInputs(String nickname, String dateOfBirth, String height, String weight, String gender, String location) {
        if (nickname.isEmpty() || gender.isEmpty()) {
            showToast("Nickname and gender cannot be empty.");
            return false;
        }
        try {
            if (Double.parseDouble(height) < 0) {
                showToast("Height cannot be negative.");
                return false;
            }
            if (Double.parseDouble(weight) < 0) {
                showToast("Weight cannot be negative.");
                return false;
            }
        } catch (NumberFormatException e) {
            showToast("Height and weight must be valid numbers.");
            return false;
        }

        return true;
    }
    private void displayCustomSharingDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Array to store user information
        String[] userInfo = new String[5];
        int[] textViewIds = {R.id.nickname, R.id.userLocation, R.id.dateOfBirth, R.id.userHeight, R.id.userWeight};

        // Array to store detail labels
        String[] detailNames = {"Nickname", "Location", "Date of Birth", "Height", "Weight"};

        StringBuilder alertMessageBuilder = new StringBuilder("Do you allow OpenTracks to store and display the following information on the leaderboard?\n\n");

        // Retrieve values from TextViews and populate user info
        for (int i = 0; i < textViewIds.length; i++) {

            TextView textView = getView().findViewById(textViewIds[i]);

            if(textView!=null) {
                userInfo[i] = textView.getText().toString();

                // Construct custom message
                alertMessageBuilder.append(detailNames[i]).append(": ").append(userInfo[i]).append("\n");

            }
        }

        String alertMessage = alertMessageBuilder.toString();

        builder.setTitle("Confirm Selection")

                .setMessage(alertMessage)
                .setPositiveButton("ALLOW", (dialog, which) -> {

                    showToast("Updated sharing permissions");

                    // TODO: Implement saving sharing permissions here.

                })
                .setNegativeButton("CANCEL", (dialog, which) -> {
                    // Un-toggle leaderboard switch

                    leaderboardSwitch.setChecked(false);
                })
                .show();
    }

    // TODO: Implement saving logic here.
    private void saveProfileData(String nickname, String dateOfBirth, String height, String weight, String gender, String location) {

    }

    @Override
    public void onStart() {
        super.onStart();
        ((SettingsActivity) getActivity()).getSupportActionBar().setTitle(R.string.settings_ui_title);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            TextView heightView = getView().findViewById(R.id.userHeight);
            TextView weightView = getView().findViewById(R.id.userWeight);

            UnitSystem unitSystem = getUnitSystem();

            Height height = new Height(180);
            Pair<String, String> heightStrings = HeightFormatter.Builder().setUnit(unitSystem).build(getContext()).getHeightParts(height);

            Weight weight = new Weight(80);
            Pair<String, String> weightStrings = WeightFormatter.Builder().setUnit(unitSystem).build(getContext()).getWeightParts(weight);

            heightView.setText(heightStrings.first + heightStrings.second);
            weightView.setText(weightStrings.first + weightStrings.second);
        }, 50);
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        DialogFragment dialogFragment = null;

        if (preference instanceof ResetDialogPreference) {
            dialogFragment = ResetDialogPreference.ResetPreferenceDialog.newInstance(preference.getKey());
        }

        if (dialogFragment != null) {
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(getParentFragmentManager(), getClass().getSimpleName());
            return;
        }

        super.onDisplayPreferenceDialog(preference);
    }



}
