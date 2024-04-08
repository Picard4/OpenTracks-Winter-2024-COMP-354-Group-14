package de.dennisguse.opentracks.settings;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;
import de.dennisguse.opentracks.R;

public class SkiProfileFragment extends PreferenceFragmentCompat {
    private static final String PREF_LEADERBOARD_TERMS_ACCEPTED = "leaderboard_terms_accepted";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_ski_profile);

        // Handle Sharing Location preference click
        Preference sharingPref = findPreference("sharing_location");
        if (sharingPref != null) {
            sharingPref.setOnPreferenceClickListener(preference -> {
                showEditSharingPreferencesDialog();
                return true;
            });
        }

        // Handle Leaderboard Participation preference change
        SwitchPreferenceCompat leaderboardParticipationPref = findPreference("leaderboard_participation");
        if (leaderboardParticipationPref != null) {
            leaderboardParticipationPref.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean optedIn = (Boolean) newValue;
                if (optedIn && !getTermsAccepted()) {
                    showTermsAndConditionsDialog();
                    return false; // Do not toggle yet
                }
                return true; // Toggle as usual
            });
        }
    }

    private boolean getTermsAccepted() {
        // Check if the terms have already been accepted
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        return prefs.getBoolean(PREF_LEADERBOARD_TERMS_ACCEPTED, false);
    }

    private void setTermsAccepted(boolean accepted) {
        // Save the acceptance of the terms in the preferences
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        editor.putBoolean(PREF_LEADERBOARD_TERMS_ACCEPTED, accepted);
        editor.apply();
    }

    private void showTermsAndConditionsDialog() {
        // Show an AlertDialog for the Terms and Conditions
        new AlertDialog.Builder(getContext())
                .setMessage("Terms and Conditions for Participating in the Leaderboard.\n" +
                        "\n" +
                        "Welcome to our app's leaderboard participation feature! Before you proceed, please take a moment to review and accept the following terms and conditions:\n" +
                        "\n" +
                        "Eligibility: Participation in the leaderboard is voluntary and open to all registered users of the app who are 18 years of age or older. By opting into the leaderboard, you confirm that you meet these eligibility requirements.\n" +
                        "\n" +
                        "Data Usage: By participating in the leaderboard, you agree to the collection and use of your activity data (such as scores, achievements, and ranks) for the purpose of displaying your standing on the leaderboard. Your profile name and scores will be visible to other users.\n" +
                        "\n" +
                        "Privacy: We are committed to protecting your privacy. Other than the information shared on the leaderboard (as described above), no other personal data will be disclosed without your explicit consent. Please refer to our Privacy Policy for more details.\n" +
                        "\n" +
                        "Changes and Termination: We reserve the right to modify these terms, the criteria for ranking on the leaderboard, or terminate the leaderboard feature at any time without prior notice. Continued participation in the leaderboard after any such changes constitutes your acceptance of the new terms.\n" +
                        "\n" +
                        "Liability: We shall not be liable for any damages or losses arising from your participation in the leaderboard, to the fullest extent permitted by law.\n" +
                        "\n" +
                        "Acceptance: By toggling the \"Participate In Leaderboard\" switch to ON, you acknowledge that you have read, understood, and agree to be bound by these Terms and Conditions.")
                .setPositiveButton("Accept", (dialogInterface, i) -> {
                    // User accepted the terms
                    setTermsAccepted(true);
                    updateLeaderboardParticipationPreference(true);
                })
                .setNegativeButton("Decline", (dialogInterface, i) -> {
                    // User declined the terms
                    dialogInterface.dismiss();
                    updateLeaderboardParticipationPreference(false);
                })
                .create()
                .show();
    }

    private void updateLeaderboardParticipationPreference(boolean participate) {
        SwitchPreferenceCompat leaderboardParticipationPref = findPreference("leaderboard_participation");
        if (leaderboardParticipationPref != null) {
            leaderboardParticipationPref.setChecked(participate);
        }
    }

    private void showEditSharingPreferencesDialog() {
        // Inflate the layout.
        View formView = LayoutInflater.from(getContext()).inflate(R.layout.sharing_preferences_form, null);

        RadioGroup radioGroup = formView.findViewById(R.id.radioGroup); // Assuming your RadioGroup has the ID 'radioGroup'

        String currentSharingOption = PreferencesUtils.getString(R.string.sharing_location_key, "no_one"); // Get this from your settings
        switch (currentSharingOption) {
            case "friends":
                radioGroup.check(R.id.radio_friends);
                break;
            case "share with":
                radioGroup.check(R.id.radio_share_with);
                break;
            case "public":
                radioGroup.check(R.id.radio_public);
                break;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }
}
