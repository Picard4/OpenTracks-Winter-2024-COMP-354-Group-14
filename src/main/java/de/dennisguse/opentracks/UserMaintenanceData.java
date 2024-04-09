package de.dennisguse.opentracks;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import android.content.Context;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.Toast;

public class UserMaintenanceData {
    private int sharpeningInterval;
    private double baseAngle;
    private double edgeAngle;
    private LocalDate lastSharpeningDate;
    private int waxingInterval;
    private String waxType;
    private LocalDate lastWaxingDate;

    // default constructor
    public UserMaintenanceData() {
        this.sharpeningInterval = 0;
        this.baseAngle = 0.0;
        this.edgeAngle = 0.0;
        this.lastSharpeningDate = LocalDate.now();
        this.waxingInterval = 0;
        this.waxType = "none";
        this.lastWaxingDate = LocalDate.now();
    }

    // Constructor
    public UserMaintenanceData(int sharpeningInterval, double baseAngle, double edgeAngle, LocalDate lastSharpeningDate, int waxingInterval, String waxType, LocalDate lastWaxingDate) {
        this.sharpeningInterval = sharpeningInterval;
        this.baseAngle = baseAngle;
        this.edgeAngle = edgeAngle;
        this.lastSharpeningDate = lastSharpeningDate;
        this.waxingInterval = waxingInterval;
        this.waxType = waxType;
        this.lastWaxingDate = lastWaxingDate;
    }

    // Getters and Setters
    public int getSharpeningInterval() {
        return sharpeningInterval;
    }

    public void setSharpeningInterval(int sharpeningInterval) {
        this.sharpeningInterval = sharpeningInterval;
    }

    public double getBaseAngle() {
        return baseAngle;
    }

    public void setBaseAngle(double baseAngle) {
        this.baseAngle = baseAngle;
    }

    public double getEdgeAngle() {
        return edgeAngle;
    }

    public void setEdgeAngle(double edgeAngle) {
        this.edgeAngle = edgeAngle;
    }

    public LocalDate getLastSharpeningDate() {
        return lastSharpeningDate;
    }

    public void setLastSharpeningDate(LocalDate lastSharpeningDate) {
        this.lastSharpeningDate = lastSharpeningDate;
    }

    public int getWaxingInterval() {
        return waxingInterval;
    }

    public void setWaxingInterval(int waxingInterval) {
        this.waxingInterval = waxingInterval;
    }
    public String getWaxType() {
            return waxType;
        }

    public void setWaxType(String waxType) {
        this.waxType = waxType;
    }

    public LocalDate getLastWaxingDate() {
        return lastWaxingDate;
    }

    public void setLastWaxingDate(LocalDate lastWaxingDate) {
        this.lastWaxingDate = lastWaxingDate;
    }
    public static void showDatePickerDialog(Context context, LocalDate currentDate, final DateUpdateListener listener) {
        Calendar calendar = Calendar.getInstance();
        if (currentDate != null) {
            calendar.set(currentDate.getYear(), currentDate.getMonthValue() - 1, currentDate.getDayOfMonth());
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                LocalDate newDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth);
                listener.onDateSet(newDate);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    public interface DateUpdateListener {
        void onDateSet(LocalDate newDate);
    }
}
