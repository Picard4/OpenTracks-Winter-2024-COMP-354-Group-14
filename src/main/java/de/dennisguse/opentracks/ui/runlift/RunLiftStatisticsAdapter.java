package de.dennisguse.opentracks.ui.runlift;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.data.models.DistanceFormatter;
import de.dennisguse.opentracks.data.models.SpeedFormatter;
import de.dennisguse.opentracks.databinding.RunLiftListItemBinding;
import de.dennisguse.opentracks.settings.UnitSystem;
import de.dennisguse.opentracks.util.StringUtils;

public class RunLiftStatisticsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RunLiftStatistics.SkiSubActivity> runLiftList;
    private final Context context;
    private UnitSystem unitSystem = UnitSystem.defaultUnitSystem();

    public RunLiftStatisticsAdapter(Context context, UnitSystem unitSystem) {
        this.context = context;
        this.unitSystem = unitSystem;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RunLiftStatisticsAdapter.ViewHolder(RunLiftListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        RunLiftStatisticsAdapter.ViewHolder viewHolder = (RunLiftStatisticsAdapter.ViewHolder) holder;
        RunLiftStatistics.SkiSubActivity skiSubActivity = runLiftList.get(position);

        String name = (skiSubActivity.isLift() ? context.getString(R.string.lift) : context.getString(R.string.run)) + position;
        viewHolder.viewBinding.runLiftItemName.setText(name);
        viewHolder.viewBinding.runLiftDistance.setText(DistanceFormatter.Builder()
                .setUnit(unitSystem)
                .build(context).formatDistance(skiSubActivity.getDistance()));

        SpeedFormatter formatter = SpeedFormatter.Builder().setUnit(unitSystem).build(context);
        viewHolder.viewBinding.runLiftAverageSpeed.setText(formatter.formatSpeed(skiSubActivity.getSpeed()));

        viewHolder.viewBinding.runLiftItemGain.setText(StringUtils.formatAltitude(context, skiSubActivity.getGain_m(), unitSystem));
        viewHolder.viewBinding.runLiftItemLoss.setText(StringUtils.formatAltitude(context, skiSubActivity.getLoss_m(), unitSystem));
        viewHolder.viewBinding.runLiftTopSpeed.setText(formatter.formatSpeed(skiSubActivity.getMaxSpeed()));
        viewHolder.viewBinding.runLiftTime.setText(StringUtils.formatElapsedTime(skiSubActivity.getTotalTime()));
    }

    @Override
    public int getItemCount() {
        if (runLiftList == null) {
            return 0;
        }
        return runLiftList.size();
    }

    public List<RunLiftStatistics.SkiSubActivity> swapData(List<RunLiftStatistics.SkiSubActivity> data, UnitSystem unitSystem) {
        this.unitSystem = unitSystem;
        runLiftList = data;

        if (data != null) {
            this.notifyDataSetChanged();
        }

        return data;
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        final RunLiftListItemBinding viewBinding;

        public ViewHolder(@NonNull RunLiftListItemBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
        }
    }
}
