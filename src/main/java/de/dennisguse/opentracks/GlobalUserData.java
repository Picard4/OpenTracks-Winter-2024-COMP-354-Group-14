package de.dennisguse.opentracks;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.ContextThemeWrapper;

import java.time.Instant;
import java.time.LocalDate;

public class GlobalUserData {
    private static final String ID = "GlobalUserDataPrefs";
    private static GlobalUserData instance;
    private final SharedPreferences sharedPreferences;
    private int sharpeningInterval;
    private static final String SHARPENING_INTERVAL = "sharpeningInterval";
    private float baseAngle;
    private static final String BASE_ANGLE = "baseAngle";
    private float edgeAngle;
    private static final String EDGE_ANGLE = "edgeAngle";
    private String lastSharpeningDate;
    private static final String LAST_SHARPENING_DATE = "lastSharpeningDate";
    private int waxingInterval;
    private static final String WAXING_INTERVAL = "waxingInterval";
    private String waxType;
    private static final String WAX_TYPE = "waxType";
    private String lastWaxingDate;
    private static final String LAST_WAXING_DATE = "lastWaxingDate";

    private String unit;
    private static final String UNIT = "unit";

    private GlobalUserData(Context context) {
        Instant currentTime = Instant.now(); // current time default for sharpening/waxing dates

        sharedPreferences = context.getSharedPreferences(ID, Context.MODE_PRIVATE);
        sharpeningInterval = sharedPreferences.getInt(SHARPENING_INTERVAL, 1);
        baseAngle = sharedPreferences.getFloat(BASE_ANGLE, 0.0f);
        edgeAngle = sharedPreferences.getFloat(EDGE_ANGLE, 0.0f);
        lastSharpeningDate = sharedPreferences.getString(LAST_SHARPENING_DATE, currentTime.toString());
        waxingInterval = sharedPreferences.getInt(WAXING_INTERVAL, 1);
        waxType = sharedPreferences.getString(WAX_TYPE, "");
        lastWaxingDate = sharedPreferences.getString(LAST_WAXING_DATE, currentTime.toString());
        unit = sharedPreferences.getString(UNIT, "km");

    }

    public static synchronized GlobalUserData getInstance(Context context) {
        if (instance == null) {
            instance = new GlobalUserData(context);
        }
        return instance;
    }


    public void update_state(Context context, String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private void update_state(Context context, String key, float value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    private void update_state(Context context, String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void setSharpeningInterval(Context context, int newSharpeningInterval) {
        sharpeningInterval = newSharpeningInterval;
        update_state(context, SHARPENING_INTERVAL, newSharpeningInterval);
    }

    public void setBaseAngle(Context context, float newBaseAngle) {
        baseAngle = newBaseAngle;
        update_state(context, BASE_ANGLE, newBaseAngle);
    }

    public void setEdgeAngle(Context context, float newEdgeAngle) {
        edgeAngle = newEdgeAngle;
        update_state(context, EDGE_ANGLE, newEdgeAngle);
    }

    public void setLastSharpeningDate(Context context, Instant newSharpeningDate) {
        lastSharpeningDate = newSharpeningDate.toString();
        update_state(context, LAST_SHARPENING_DATE, newSharpeningDate.toString());
    }

    public void setWaxingInterval(Context context, int newWaxingInterval) {
        waxingInterval = newWaxingInterval;
        update_state(context, WAXING_INTERVAL, newWaxingInterval);
    }

    public void setWaxType(Context context, String newWaxType) {
        waxType = newWaxType;
        update_state(context, WAX_TYPE, newWaxType);
    }

    public void setLastWaxingDate(Context context, Instant newLastWaxingDate) {
        lastWaxingDate = newLastWaxingDate.toString();
        update_state(context, LAST_WAXING_DATE, newLastWaxingDate.toString());
    }

    public void setUnit(Context context, String newUnit) {
        unit = newUnit;
        update_state(context, UNIT, newUnit);
    }

    public int getSharpeningInterval() { return sharpeningInterval; }
    public float getBaseAngle() { return baseAngle; }
    public float getEdgeAngle() { return edgeAngle; }

    public Instant getLastSharpeningDate() { return Instant.parse(lastSharpeningDate); }
    public int getWaxingInterval() { return waxingInterval; }

    public String getWaxType() { return waxType; }

    public Instant getLastWaxingDate() { return Instant.parse(lastWaxingDate); }

    public String getUnit() { return unit; }
}
