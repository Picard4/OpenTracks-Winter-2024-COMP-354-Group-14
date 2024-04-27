package de.dennisguse.opentracks;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import java.util.Calendar;

import de.dennisguse.opentracks.databinding.TrackViewGearBinding;

/**
 * Allow the user to select their gear for the resort they went to at what date.
 *
 * @author Sean Gregory, Zachary Therrien
 * */
public class TrackViewGearActivity extends AbstractActivity
{
    private TrackViewGearBinding viewBinding;
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private void InitDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month++;
            String date = makeDateString(day, month, year);
            dateButton.setText(date);
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);


        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        month++;
        return makeDateString(day, month, year);
    }

    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month)
    {
        switch (month)
        {
            case 1: return "JAN";
            case 2: return "FEB";
            case 3: return "MAR";
            case 4: return "APR";
            case 5: return "MAY";
            case 6: return "JUN";
            case 7: return "JUL";
            case 8: return "AUG";
            case 9: return "SEP";
            case 10: return "OCT";
            case 11: return "NOV";
            case 12: return "DEC";
        }
        return "ERR";
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle("Ski Gear Tracker");

        // Credit: https://www.youtube.com/watch?v=qCoidM98zNk
        InitDatePicker();
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());

        // Credit: https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list
        Spinner skiDropdown = findViewById(R.id.skiDropDown);
        skiDropdown.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Cross-Country Skis", "Downhill Skis", "Miscellaneous Skis", "Child Skis"}));

        Spinner bootDropdown = findViewById(R.id.bootDropDown);
        bootDropdown.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Snow Boots", "Spiky Boots", "Leather Boots", "Iron Boots"}));

        Spinner poleDropdown = findViewById(R.id.poleDropDown);
        poleDropdown.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Cool Poles", "Grounded Poles", "/pol/"}));

        Spinner resortDropdown = findViewById(R.id.resortDropDown);
        resortDropdown.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Printre Noi Resort", "Big White Ski Resort", "Jackson Hole Resort", "Lake Louise Ski Resort", "Romanian Resort"}));

        setSupportActionBar(viewBinding.bottomAppBarLayout.bottomAppBar);
    }


    @Override
    protected View getRootView() {
        viewBinding = TrackViewGearBinding.inflate(getLayoutInflater());
        return viewBinding.getRoot();
    }
}
