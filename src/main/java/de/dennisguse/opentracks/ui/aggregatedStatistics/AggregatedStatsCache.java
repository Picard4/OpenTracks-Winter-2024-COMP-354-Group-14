package de.dennisguse.opentracks.ui.aggregatedStatistics;
import android.content.Context;
import android.content.SharedPreferences;
import de.dennisguse.opentracks.stats.TrackStatistics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AggregatedStatsCache {

    private static final String CACHE_KEY = "aggregated_stats";
    private static final String PREF_NAME = "aggregated_stats_cache";
    private static final long CACHE_EXPIRATION = 24 * 60 * 60 * 1000;

    private SharedPreferences sharedPreferences;

    public AggregatedStatsCache(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public TrackStatistics get() {
        long currentTime = System.currentTimeMillis();
        long lastUpdatedTime = sharedPreferences.getLong(CACHE_KEY + "_time", 0);
        if(currentTime - lastUpdatedTime > CACHE_EXPIRATION) {
            String statsJson = sharedPreferences.getString(CACHE_KEY, null);
            if(statsJson !=null)
            {
                return new Gson().fromJson(statsJson, TrackStatistics.class);
            }
        }
        return null;
    }

    public void put(TrackStatistics trackStatistics) {
        Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
        String statsJson = gson.toJson(trackStatistics);
        sharedPreferences.edit()
                .putString(CACHE_KEY, statsJson)
                .putLong(CACHE_KEY + "_time", System.currentTimeMillis())
                .apply();
    }


}
