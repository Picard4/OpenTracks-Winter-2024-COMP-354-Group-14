package de.dennisguse.opentracks.data.models;

import java.util.List;

public record AltitudeGainLoss(float gain_m, float loss_m) {
    private static final double altitudeChangeThreshold = 10.0;

    private static boolean isSkiing;
    private static boolean isChairlift;

    private static boolean isWaiting;

    public static boolean isSkiing(){
        return isSkiing;
    }
    public static boolean isChairlift(){
        return isChairlift;
    }
    private boolean isWaiting() {
        return  isWaiting;
    }
    public boolean shouldStartNewSegment(List<TrackPoint> trackPoints, int currentIndex){
        TrackPoint currentPoint = trackPoints.get(currentIndex);
        TrackPoint previousPoint = trackPoints.get(currentIndex - 1);

        Altitude currentAltitude = currentPoint.getAltitude();
        Altitude previousAltitude = previousPoint.getAltitude();

        if (currentAltitude != null && previousAltitude != null){
            double altitudeChange = currentAltitude.toM() - previousAltitude.toM();
            if (altitudeChange > altitudeChangeThreshold){
                isChairlift = true;
                isSkiing = false;
                return true;
            }
            else if (altitudeChange < altitudeChangeThreshold){
                isChairlift = false;
                isSkiing = true;
                return true;

            }
            else if (altitudeChange < 0.5){
                isChairlift = false;
                isSkiing = false;
                isWaiting = true;
                return true;
            }
            else{
                return false;
            }
        }
        isChairlift = false;
        isSkiing = false;
        return false;
    }




}
