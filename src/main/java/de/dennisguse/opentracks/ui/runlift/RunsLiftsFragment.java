package de.dennisguse.opentracks.ui.runlift;

import android.content.SharedPreferences;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.data.ContentProviderUtils;
import de.dennisguse.opentracks.data.models.Track;
import de.dennisguse.opentracks.databinding.RunLiftListViewBinding;
import de.dennisguse.opentracks.settings.PreferencesUtils;
import de.dennisguse.opentracks.settings.UnitSystem;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class RunsLiftsFragment extends Fragment {

    private static final String TAG = RunsLiftsFragment.class.getSimpleName();

    private static final String TRACK_ID_KEY = "trackId";

    private RunLiftStatisticsModel viewModel;

    private Track.Id trackId;
    private UnitSystem unitSystem = UnitSystem.defaultUnitSystem();
    private RunLiftStatisticsAdapter adapter;

    private RunLiftListViewBinding viewBinding;

    protected final SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = (sharedPreferences, key) -> {
        if (PreferencesUtils.isKey(R.string.stats_units_key, key) || PreferencesUtils.isKey(R.string.stats_rate_key, key)) {
            updateRunLifts(PreferencesUtils.getUnitSystem());

        }
    };

    public static RunsLiftsFragment newInstance(@NonNull Track.Id trackId) {
        Objects.nonNull(trackId);
        Bundle bundle = new Bundle();
        bundle.putParcelable(TRACK_ID_KEY, trackId);
        RunsLiftsFragment runsLiftsFragment = new RunsLiftsFragment();
        runsLiftsFragment.setArguments(bundle);
        return runsLiftsFragment;
    }


    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trackId = getArguments().getParcelable(TRACK_ID_KEY);
    }

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewBinding = RunLiftListViewBinding.inflate(inflater, container, false);
        return viewBinding.getRoot();
    }

    @Deprecated //TODO This method must be re-implemented.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new RunLiftStatisticsAdapter(getContext(), unitSystem);
        viewBinding.runLiftList.setLayoutManager(new LinearLayoutManager(getContext()));
        // TODO handle empty view: before we did viewBinding.intervalList.setEmptyView(viewBinding.intervalListEmptyView);
        viewBinding.runLiftList.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        PreferencesUtils.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);

        ContentProviderUtils contentProviderUtils = new ContentProviderUtils(getContext());
        Track track = contentProviderUtils.getTrack(trackId);

        viewModel = new ViewModelProvider(getActivity()).get(RunLiftStatisticsModel.class);
        loadRunLifts();
    }

    @Override
    public void onPause() {
        super.onPause();

        PreferencesUtils.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);

        if (viewModel != null) {
            viewModel.onPause();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewBinding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        adapter = null;
        viewModel = null;
    }

    protected synchronized void loadRunLifts() {
        if (viewModel == null) {
            return;
        }

        LiveData<List<RunLiftStatistics.SkiSubActivity>> liveData = viewModel.getSkiSubActivityStats(trackId, unitSystem);
        liveData.observe(getActivity(), intervalList -> adapter.swapData(intervalList, unitSystem));
    }

    private synchronized void updateRunLifts(UnitSystem unitSystem) {
        boolean update = unitSystem != this.unitSystem;
        this.unitSystem = unitSystem;

        if (update && viewModel != null) {
            viewModel.update(trackId, this.unitSystem);
        }
    }
}
