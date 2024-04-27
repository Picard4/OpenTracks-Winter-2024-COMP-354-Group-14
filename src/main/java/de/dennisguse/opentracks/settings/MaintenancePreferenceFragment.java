package de.dennisguse.opentracks.settings;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import de.dennisguse.opentracks.R; // Make sure to import your R class.
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Calendar;

public class MaintenancePreferenceFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.activity_maintenance_infor, rootKey);
    }
    public void onDisplayPreferenceDialog(Preference preference) {
        // Check if the preference is one of the date preferences
        if ("last_sharpening_date".equals(preference.getKey()) || "last_waxing_date".equals(preference.getKey())) {
            showDatePickerDialog(preference);
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }

    private void showDatePickerDialog(Preference preference) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                preference.setSummary(date);

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
