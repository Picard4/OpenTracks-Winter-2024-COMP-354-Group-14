package de.dennisguse.opentracks.ui.runlift;

import androidx.annotation.Nullable;

import de.dennisguse.opentracks.data.TrackPointIterator;
import de.dennisguse.opentracks.data.models.Distance;
import de.dennisguse.opentracks.data.models.HeartRate;
import de.dennisguse.opentracks.data.models.Speed;
import de.dennisguse.opentracks.data.models.TrackPoint;
import de.dennisguse.opentracks.stats.TrackStatistics;
import de.dennisguse.opentracks.stats.TrackStatisticsUpdater;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RunLiftStatistics {
    private TrackStatisticsUpdater trackStatisticsUpdater = new TrackStatisticsUpdater();
    private final List<SkiSubActivity> skiSubActivityList;

    private final double waitThreshold = 1.6;
    private final double runThreshold = 3.5;
    private int thresholdCount = 3;

    private final Queue<TrackPoint> waitQueue = new LinkedList<>();

    private RunLiftStatistics.SkiSubActivity skiSubActivity, lastSkiSubActivity;

    public RunLiftStatistics() {
        skiSubActivity = new SkiSubActivity();
        lastSkiSubActivity = new SkiSubActivity();

        skiSubActivityList = new ArrayList<>();
        skiSubActivityList.add(lastSkiSubActivity);
    }

    private boolean isWaiting(TrackPoint trackPoint) {
        return trackPoint.getSpeed().speed_mps() <= waitThreshold;
    }

    private boolean isRun(TrackPoint trackPoint) {
        return trackPoint.getAltitudeLoss() - trackPoint.getAltitudeGain() >= 0.0
                && trackPoint.getSpeed().speed_mps() >= runThreshold;
    }

    public TrackPoint.Id addTrackPoints(TrackPointIterator trackPointIterator) {
        boolean newSkiSubActivityAdded = false;
        TrackPoint trackPoint = null;
        TrackPoint lastTrackPoint = null;

        while (trackPointIterator.hasNext()) {
            trackPoint = trackPointIterator.next();

            if (isWaiting(trackPoint)) {
                waitQueue.add(trackPoint);
            } else {
                if (lastTrackPoint != null && lastTrackPoint.getAltitudeLoss() != trackPoint.getAltitudeLoss()) {
                    skiSubActivity = new SkiSubActivity();

                    trackStatisticsUpdater = new TrackStatisticsUpdater();

                    lastSkiSubActivity = new SkiSubActivity(skiSubActivity);
                    skiSubActivityList.add(lastSkiSubActivity);
                }

                if (!waitQueue.isEmpty()) {
                    if (lastTrackPoint == null || !(isRun(lastTrackPoint) == isRun(trackPoint))) {
                        if (lastTrackPoint == null) lastTrackPoint = waitQueue.peek();
                        lastSkiSubActivity.setWaitTime(Duration.between(lastTrackPoint.getTime(), trackPoint.getTime()));
                    }
                }

                while (!waitQueue.isEmpty()) {
                    trackStatisticsUpdater.addTrackPoint(waitQueue.remove());
                    lastSkiSubActivity.add(trackStatisticsUpdater.getTrackStatistics(), trackPoint);
                }
                trackStatisticsUpdater.addTrackPoint(trackPoint);

                lastSkiSubActivity.add(trackStatisticsUpdater.getTrackStatistics(), trackPoint);
                lastTrackPoint = trackPoint;
                newSkiSubActivityAdded = true;
            }
        }
        if (newSkiSubActivityAdded) {
            lastSkiSubActivity.add(trackStatisticsUpdater.getTrackStatistics(), null);
        } else {
            lastSkiSubActivity.set(trackStatisticsUpdater.getTrackStatistics());
        }
        return trackPoint != null ? trackPoint.getId() : null;
    }

    public List<SkiSubActivity> getSkiSubActivityList() { return skiSubActivityList; }

    public SkiSubActivity getLastSkiSubActivity() {
        if (!skiSubActivityList.isEmpty()) {
            return skiSubActivityList.get(skiSubActivityList.size() - 1);
        }
        return null;
    }

    public static class SkiSubActivity {
        private TrackStatistics trackStatistics;
        private final List<TrackPoint> trackPoints = new ArrayList<>();
        private Distance distance = Distance.of(0);
        private Duration time = Duration.ofSeconds(0);
        private Float gain_m;
        private Float loss_m;
        private HeartRate avgHeartRate;
        private Speed maxSpeed = Speed.of(0);

        private Duration waitTime = Duration.ofSeconds(0);
        private Duration totalTime = Duration.ofSeconds(0);

        public SkiSubActivity() {
            trackStatistics = new TrackStatistics();
        }

        public SkiSubActivity(SkiSubActivity s) {
            trackStatistics = new TrackStatistics(s.trackStatistics);
            trackPoints.addAll(s.trackPoints);
            waitTime = s.waitTime;
            distance = s.distance;
            time = s.time;
            gain_m = s.gain_m;
            loss_m = s.loss_m;
            avgHeartRate = s.avgHeartRate;
        }

        public Distance getDistance() {
            return distance;
        }

        public Speed getSpeed() {
            return Speed.of(distance, time.minus(waitTime));
        }

        public Speed getMaxSpeed() { return maxSpeed; }

        public boolean hasGain() {
            return gain_m != null;
        }

        public Float getGain_m() {
            return gain_m;
        }

        public boolean hasLoss() {
            return loss_m != null;
        }

        public Float getLoss_m() {
            return loss_m;
        }

        public boolean hasAverageHeartRate() {
            return avgHeartRate != null;
        }

        public HeartRate getAverageHeartRate() {
            return avgHeartRate;
        }

        public Duration getWaitTime() {
            return waitTime;
        }

        public boolean isLift() {
            return gain_m >= loss_m;
        }

        public Duration getTotalTime() { return totalTime; }

        public double getSlopePercentage() {
            if (distance.distance_m() == 0) return 0;
            return Math.abs(gain_m - loss_m) / distance.distance_m() * 100;
        }

        private void add(TrackStatistics trackStatistics, @Nullable TrackPoint lastTrackPoint) {
            distance = distance.plus(trackStatistics.getTotalDistance());
            time = time.plus(trackStatistics.getTotalTime());
            gain_m = trackStatistics.hasTotalAltitudeGain() ? trackStatistics.getTotalAltitudeGain() : gain_m;
            loss_m = trackStatistics.hasTotalAltitudeLoss() ? trackStatistics.getTotalAltitudeLoss() : loss_m;
            avgHeartRate = trackStatistics.getAverageHeartRate();
            maxSpeed = trackStatistics.getMaxSpeed();
            totalTime = trackStatistics.getTotalTime();

            //set(trackStatistics);
            if (lastTrackPoint == null) {
                return;
            }
            trackPoints.add(lastTrackPoint);
            if (hasGain() && lastTrackPoint.hasAltitudeGain()) {
                gain_m = gain_m - lastTrackPoint.getAltitudeGain();
            }
            if (hasLoss() && lastTrackPoint.hasAltitudeLoss()) {
                loss_m = loss_m - lastTrackPoint.getAltitudeLoss();
            }
        }

        private void set(TrackStatistics trackStatistics) {
            distance = trackStatistics.getTotalDistance();
            time = trackStatistics.getTotalTime();
            gain_m = trackStatistics.hasTotalAltitudeGain() ? trackStatistics.getTotalAltitudeGain() : gain_m;
            loss_m = trackStatistics.hasTotalAltitudeLoss() ? trackStatistics.getTotalAltitudeLoss() : loss_m;
            avgHeartRate = trackStatistics.getAverageHeartRate();
        }

        public List<TrackPoint> getTrackPoints() {
            return trackPoints;
        }

        public boolean isNew() { return trackPoints.isEmpty(); }

        public TrackPoint lastTrackPoint() {
            if (trackPoints.isEmpty()) return null;
            return trackPoints.get(trackPoints.size() - 1);
        }

        public void setWaitTime(Duration time) {
            waitTime = time;
        }
    }
}
