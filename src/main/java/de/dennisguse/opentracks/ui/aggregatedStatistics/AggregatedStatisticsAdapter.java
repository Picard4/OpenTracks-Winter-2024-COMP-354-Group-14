package de.dennisguse.opentracks.ui.aggregatedStatistics;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import de.dennisguse.opentracks.GlobalUserData;
import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.UserMaintenanceData;
import de.dennisguse.opentracks.data.models.ActivityType;
import de.dennisguse.opentracks.data.models.Distance;
import de.dennisguse.opentracks.data.models.DistanceFormatter;
import de.dennisguse.opentracks.data.models.SpeedFormatter;
import de.dennisguse.opentracks.data.models.Track;
import de.dennisguse.opentracks.databinding.AggregatedStatsListItemBinding;
import de.dennisguse.opentracks.settings.PreferencesUtils;
import de.dennisguse.opentracks.settings.UnitSystem;
import de.dennisguse.opentracks.stats.TrackStatistics;
import de.dennisguse.opentracks.util.StringUtils;

import androidx.core.app.NotificationManagerCompat;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class AggregatedStatisticsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private AggregatedStatistics aggregatedStatistics;
    private final Context context;

    // My code
    int uselessCounter;

    public AggregatedStatisticsAdapter(Context context, AggregatedStatistics aggregatedStatistics) {
        this.context = context;
        this.aggregatedStatistics = aggregatedStatistics;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(AggregatedStatsListItemBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;

        AggregatedStatistics.AggregatedStatistic aggregatedStatistic = aggregatedStatistics.getItem(position);

        String type = aggregatedStatistic.getActivityTypeLocalized();
        if (ActivityType.findByLocalizedString(context, type).isShowSpeedPreferred()) {
            viewHolder.setSpeed(aggregatedStatistic);
        } else {
            viewHolder.setPace(aggregatedStatistic);
        }


        // *************************************************************************************
        // Group 2 code
        GlobalUserData globalUserData = GlobalUserData.getInstance(context);

        Instant lastSharpeningDate = globalUserData.getLastSharpeningDate();
        Instant lastWaxingDate = globalUserData.getLastWaxingDate();

        // get all recorded skiing tracks
        ArrayList<TrackStatistics> listOfTracks = aggregatedStatistics.get("skiing").getListOfTracks();

        Distance distanceSinceLastSharpening = new Distance(0);
        Distance distanceSinceLastWaxing = new Distance(0);

        // for each track, if time of recording >= time of last waxing/sharpening, increment distance
        // since last waxing/sharpening
        for (TrackStatistics track : listOfTracks) {
            if (track.getStartTime().equals(lastSharpeningDate) ||
                track.getStartTime().isAfter(lastSharpeningDate)) {
                distanceSinceLastSharpening = distanceSinceLastSharpening.plus(track.getTotalDistance());
            }

            if (track.getStartTime().equals(lastWaxingDate) ||
                track.getStartTime().isAfter(lastWaxingDate)) {
                distanceSinceLastWaxing = distanceSinceLastWaxing.plus(track.getTotalDistance());
            }
        }

        double kmSinceLastSharpening = distanceSinceLastSharpening.toKM();
        double kmSinceLastWaxing = distanceSinceLastWaxing.toKM();

        TextView kmSinceLastSharpeningValue = holder.itemView.findViewById(R.id.km_since_last_sharpening_value);
        kmSinceLastSharpeningValue.setText(Double.toString(kmSinceLastSharpening));

        TextView kmSinceLastWaxingValue = holder.itemView.findViewById(R.id.km_since_last_waxing_value);
        kmSinceLastWaxingValue.setText(Double.toString(kmSinceLastWaxing));

        double percentageOfSharpeningInterval = kmSinceLastSharpening / globalUserData.getSharpeningInterval();
        double percentageOfWaxingInterval = kmSinceLastWaxing / globalUserData.getWaxingInterval();

        TextView sharpeningPercentageValue = holder.itemView.findViewById(R.id.sharpening_percentage_value);
        sharpeningPercentageValue.setText(Double.toString(percentageOfSharpeningInterval));

        TextView waxingPercentageValue = holder.itemView.findViewById(R.id.waxing_percentage_value);
        waxingPercentageValue.setText(Double.toString(percentageOfWaxingInterval));

        TextView unit = holder.itemView.findViewById(R.id.aggregated_stats_waxing_unit);
        unit.setText(globalUserData.getUnit());

        TextView unit2 = holder.itemView.findViewById(R.id.aggregated_stats_sharpening_unit);
        unit2.setText(globalUserData.getUnit());

        //The Thresholds for sharpening and waxing interval
        //setting sharpening Threshold to 0 for testing
        double sharpeningThreshold = 0;
        double waxingThreshold = 100;

        // Check if you've reached the sharpening threshold
        if (percentageOfSharpeningInterval >= sharpeningThreshold) {
            showSharpeningNotification(percentageOfSharpeningInterval);
        }

        // Check if you've reached the waxing threshold
        if (percentageOfWaxingInterval >= waxingThreshold) {
            showWaxingNotification(percentageOfWaxingInterval);
        }

        // *******************************************************************************************
    }

    // *************************************************************************************
    // Group 2 code

    //New method for display the notification when the sharpening interval is reached
    private void showSharpeningNotification(double percentageOfSharpeningInterval) {
        // Unique ID for our notification
        int notificationId = 1;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Sharpening channel"; 
            String description = "Notifications for ski sharpening";
            int importance = NotificationManager.IMPORTANCE_HIGH; 
            NotificationChannel channel = new NotificationChannel("channel_sharpening", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_sharpening")
                .setSmallIcon(R.drawable.ic_activity_skiing_24dp)
                .setContentTitle("Sharpening Alert")
                .setContentText(percentageOfSharpeningInterval + "% reached of the sharpening interval")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setAutoCancel(true);

        //codes from ChatGPT
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        try {
            notificationManager.notify(notificationId, builder.build());
        } catch (SecurityException e) {
            // Handle the security exception
            Log.e("Notification Error", "SecurityException: Could not display notification.", e);
        }
    }

    //New method for display the notification when the waxing interval is reached
    private void showWaxingNotification(double percentageOfWaxingInterval) {
        // Unique ID for our notification
        int notificationId = 2;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Waxing channel"; 
            String description = "Notifications for ski waxing"; 
            int importance = NotificationManager.IMPORTANCE_HIGH; 
            NotificationChannel channel = new NotificationChannel("channel_waxing", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_waxing")
                .setSmallIcon(R.drawable.ic_activity_skiing_24dp)
                .setContentTitle("Waxing Alert")
                .setContentText(percentageOfWaxingInterval + "% reached of the waxing interval")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setAutoCancel(true);

        //codes from ChatGPT
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        try {
            notificationManager.notify(notificationId, builder.build());
        } catch (SecurityException e) {
            // Handle the security exception
            Log.e("Notification Error", "SecurityException: Could not display notification.", e);
        }
    }

    // *************************************************************************************

    @Override
    public int getItemCount() {
        if (aggregatedStatistics == null) {
            return 0;
        }
        return aggregatedStatistics.getCount();
    }

    public void swapData(AggregatedStatistics aggregatedStatistics) {
        this.aggregatedStatistics = aggregatedStatistics;
        this.notifyDataSetChanged();
    }

    public List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        for (int i = 0; i < aggregatedStatistics.getCount(); i++) {
            categories.add(aggregatedStatistics.getItem(i).getActivityTypeLocalized());
        }
        return categories;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private final AggregatedStatsListItemBinding viewBinding;
        private UnitSystem unitSystem = UnitSystem.defaultUnitSystem();
        private boolean reportSpeed;

        public ViewHolder(AggregatedStatsListItemBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
        }

        public void setSpeed(AggregatedStatistics.AggregatedStatistic aggregatedStatistic) {
            setCommonValues(aggregatedStatistic);

            SpeedFormatter formatter = SpeedFormatter.Builder().setUnit(unitSystem).setReportSpeedOrPace(reportSpeed).build(context);
            {
                Pair<String, String> parts = formatter.getSpeedParts(aggregatedStatistic.getTrackStatistics().getAverageMovingSpeed());
                viewBinding.aggregatedStatsAvgRate.setText(parts.first);
                viewBinding.aggregatedStatsAvgRateUnit.setText(parts.second);
                viewBinding.aggregatedStatsAvgRateLabel.setText(context.getString(R.string.stats_average_moving_speed));
            }

            {
                Pair<String, String> parts = formatter.getSpeedParts(aggregatedStatistic.getTrackStatistics().getMaxSpeed());
                viewBinding.aggregatedStatsMaxRate.setText(parts.first);
                viewBinding.aggregatedStatsMaxRateUnit.setText(parts.second);
                viewBinding.aggregatedStatsMaxRateLabel.setText(context.getString(R.string.stats_max_speed));
            }
        }

        public void setPace(AggregatedStatistics.AggregatedStatistic aggregatedStatistic) {
            setCommonValues(aggregatedStatistic);

            SpeedFormatter formatter = SpeedFormatter.Builder().setUnit(unitSystem).setReportSpeedOrPace(reportSpeed).build(context);
            {
                Pair<String, String> parts = formatter.getSpeedParts(aggregatedStatistic.getTrackStatistics().getAverageMovingSpeed());
                viewBinding.aggregatedStatsAvgRate.setText(parts.first);
                viewBinding.aggregatedStatsAvgRateUnit.setText(parts.second);
                viewBinding.aggregatedStatsAvgRateLabel.setText(context.getString(R.string.stats_average_moving_pace));
            }

            {
                Pair<String, String> parts = formatter.getSpeedParts(aggregatedStatistic.getTrackStatistics().getMaxSpeed());
                viewBinding.aggregatedStatsMaxRate.setText(parts.first);
                viewBinding.aggregatedStatsMaxRateUnit.setText(parts.second);
                viewBinding.aggregatedStatsMaxRateLabel.setText(R.string.stats_fastest_pace);
            }
        }

        //TODO Check preference handling.
        private void setCommonValues(AggregatedStatistics.AggregatedStatistic aggregatedStatistic) {
            String activityType = aggregatedStatistic.getActivityTypeLocalized();

            reportSpeed = PreferencesUtils.isReportSpeed(activityType);
            unitSystem = PreferencesUtils.getUnitSystem();

            viewBinding.activityIcon.setImageResource(getIcon(aggregatedStatistic));
            viewBinding.aggregatedStatsTypeLabel.setText(activityType);
            viewBinding.aggregatedStatsNumTracks.setText(StringUtils.valueInParentheses(String.valueOf(aggregatedStatistic.getCountTracks())));

            Pair<String, String> parts = DistanceFormatter.Builder()
                    .setUnit(unitSystem)
                    .build(context).getDistanceParts(aggregatedStatistic.getTrackStatistics().getTotalDistance());
            viewBinding.aggregatedStatsDistance.setText(parts.first);
            viewBinding.aggregatedStatsDistanceUnit.setText(parts.second);

            viewBinding.aggregatedStatsTime.setText(StringUtils.formatElapsedTime(aggregatedStatistic.getTrackStatistics().getMovingTime()));
        }

        private int getIcon(AggregatedStatistics.AggregatedStatistic aggregatedStatistic) {
            String localizedActivityType = aggregatedStatistic.getActivityTypeLocalized();
            return ActivityType.findByLocalizedString(context, localizedActivityType)
                    .getIconDrawableId();
        }
    }
}
