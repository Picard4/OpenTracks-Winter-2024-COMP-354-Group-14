package de.dennisguse.opentracks.ui.runlift;

import android.app.Application;

import android.content.ContentResolver;
import android.database.ContentObserver;

import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;

import androidx.lifecycle.AndroidViewModel;

import androidx.annotation.NonNull;

import androidx.lifecycle.MutableLiveData;

import de.dennisguse.opentracks.data.ContentProviderUtils;
import de.dennisguse.opentracks.data.TrackPointIterator;
import de.dennisguse.opentracks.data.models.Track;
import de.dennisguse.opentracks.data.models.TrackPoint;
import de.dennisguse.opentracks.data.tables.TrackPointsColumns;
import de.dennisguse.opentracks.settings.UnitSystem;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RunLiftStatisticsModel extends AndroidViewModel {

    private static final String TAG = RunLiftStatisticsModel.class.getSimpleName();

    private MutableLiveData<List<RunLiftStatistics.SkiSubActivity>> skiSubActivityLiveData;
    private RunLiftStatistics runLiftStatistics;
    private final ContentResolver contentResolver;
    private ContentObserver trackPointsTableObserver;
    private TrackPoint.Id lastTrackPointId;

    private final Executor executor = Executors.newSingleThreadExecutor();
    private HandlerThread handlerThread;
    private Handler handler;

    public RunLiftStatisticsModel(@NonNull Application application) {
        super(application);
        contentResolver = getApplication().getContentResolver();
        handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (trackPointsTableObserver != null) {
            contentResolver.unregisterContentObserver(trackPointsTableObserver);
            trackPointsTableObserver = null;
        }
        if (handlerThread != null) {
            handlerThread.getLooper().quit();
            handlerThread = null;
        }
        handler = null;
    }

    public MutableLiveData<List<RunLiftStatistics.SkiSubActivity>> getSkiSubActivityStats(Track.Id trackId, UnitSystem unitSystem) {
        if (skiSubActivityLiveData == null) {

            skiSubActivityLiveData = new MutableLiveData<>();
            runLiftStatistics = new RunLiftStatistics();

            loadRunLiftStatistics(trackId);
        }

        trackPointsTableObserver = new ContentObserver(handler) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                loadRunLiftStatistics(trackId);
            }
        };
        contentResolver.registerContentObserver(TrackPointsColumns.CONTENT_URI_BY_TRACKID, false, trackPointsTableObserver);

        return skiSubActivityLiveData;
    }

    private void loadRunLiftStatistics(Track.Id trackId) {
        executor.execute(() -> {
            ContentProviderUtils contentProviderUtils = new ContentProviderUtils(getApplication());
            try (TrackPointIterator trackPointIterator = contentProviderUtils.getTrackPointLocationIterator(trackId, lastTrackPointId)) {
                lastTrackPointId = runLiftStatistics.addTrackPoints(trackPointIterator);
                skiSubActivityLiveData.postValue(runLiftStatistics.getSkiSubActivityList());
            }
        });
    }

    public void onPause() {
        if (trackPointsTableObserver != null) {
            contentResolver.unregisterContentObserver(trackPointsTableObserver);
        }
    }

    public void update(Track.Id trackId, UnitSystem unitSystem) {
        lastTrackPointId = null;
        runLiftStatistics = new RunLiftStatistics();
        loadRunLiftStatistics(trackId);
    }

}