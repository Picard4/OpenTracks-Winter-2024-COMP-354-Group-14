/*
 * This class implements a utility for calculating the moving average of speeds from track points.
 * Portions of this code were generated with the assistance of ChatGPT-4,
 * an AI developed by OpenAI (https://openai.com/).
 * The code has been modified for the specific use case.
 */
package de.dennisguse.opentracks.data.models;

import android.content.Context;
import android.content.SharedPreferences;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TrackPointUtils {

    /**
     * Calculates the moving average of speeds from a list of TrackPoints,
     * considering only points that are at least 5 seconds apart.
     *
     * @param trackPoints   The list of track points to be analyzed.
     * @param numDataPoints The number of data points to include in the moving average calculation.
     * @return A list of calculated moving averages.
     */
    public static List<Double> calculateSpeedMovingAverage(List<TrackPoint> trackPoints, int numDataPoints) {
        List<Double> movingAverages = new ArrayList<>(); // Stores the resulting moving averages
        Queue<TrackPoint> window = new LinkedList<>(); // A sliding window of track points for the moving average
        double sumOfSpeeds = 0.0; // Sum of speeds in the current window for calculating the average
        Instant lastTime = null; // Timestamp of the last track point added to the window

        for (TrackPoint trackPoint : trackPoints) { // Iterate over each track point
            Instant currentTime = trackPoint.getTime(); // Get the timestamp of the current track point

            // Skip the current point if less than 5 seconds have passed since the last added point
            if (lastTime != null && Duration.between(lastTime, currentTime).getSeconds() < 5) {
                continue; // Move to the next track point without adding this one
            }

            lastTime = currentTime; // Update the timestamp for the last added track point

            window.offer(trackPoint); // Add the current track point to the window
            sumOfSpeeds += trackPoint.getSpeed().toKMH(); // Add its speed to the sum

            // If the window exceeds the desired size, remove the oldest track point
            if (window.size() > numDataPoints) {
                TrackPoint removed = window.poll(); // Remove the oldest track point from the window
                sumOfSpeeds -= removed.getSpeed().toKMH(); // Subtract its speed from the sum
            }

            // Calculate and add the moving average to the list if the window is at the desired size
            if (window.size() == numDataPoints) {
                movingAverages.add(sumOfSpeeds / numDataPoints); // Calculate and store the average speed
            }
        }

        return movingAverages; // Return the list of moving averages
    }







    //====================================================================================
    //               CODE BELOW REQUIRES IMPLEMENTATION OF OTHER DEPENDENCIES
    //====================================================================================

//    // Code for implementation of user input 5,10,15. Requires full implementation of others parts in order to function.


//    private Context context;
    // Method to retrieve the number of data points the user wants to include in the moving average
//    private int getSelectedDataPoints(Context context) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
//        return sharedPreferences.getInt("SelectedValue", 5); // Default to 5 if no value is set
//    }



//    //  Code to implement on the page that uses the moving average. Dependency still required. Mock variables in place until full implementation.
//
//    // Method to calculate the moving average of speed for a specific activity
//    public void calculateMovingAverageForActivity(Context context, int activityIndex) {
//        // Retrieve the user-selected number of data points from SharedPreferences
//        int numDataPoints = getSelectedDataPoints(context);
//
//
//        // Method to retrieve the sub-activity list
//        List<RunLiftStatistics.SkiSubActivity> skiSubActivityList = getSubActivitiesList();
//
//        // Retrieve the selected sub-activity using the provided index
//        RunLiftStatistics.SkiSubActivity selectedActivity = skiSubActivityList.get(activityIndex);
//
//        // Retrieve track points from the selected sub-activity
//        List<TrackPoint> trackPoints = selectedActivity.getTrackPoints();
//
//        // Calculate the moving averages for the selected activity using the user-selected number of data points
//        List<Double> movingAverages = TrackPointUtils.calculateSpeedMovingAverage(trackPoints, numDataPoints);
//
//
//    }


}
