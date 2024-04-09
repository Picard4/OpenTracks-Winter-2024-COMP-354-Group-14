package de.dennisguse.opentracks.services;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import de.dennisguse.opentracks.data.ContentProviderUtils;
import de.dennisguse.opentracks.data.TrackPointIterator;
import de.dennisguse.opentracks.data.models.*;

/**
 * The TrackDifferentiate class is responsible for differentiating ski lift and
 * ski run segments from a track.
 * Stores trackpoints in arraylists, which are stored in another arraylist.
 * Outside arraylist contains "runs": whether it is a chairlift, a ski run or a
 * waiting segment.
 * Inside arraylist contains the trackpoints (or segments) of said "runs".
 * It will group trackpoints in 3 categories: idle, lift and run.
 * It will iterate through the trackpoints chronologically continusly add
 * trackpoints to an arraylist unless certain conditions are met.
 * Once those conditions are met, it will start a new group of trackpoints.
 */
public class TrackDifferentiate {

	// Internal references to chairLift object and SkiRun models, initalized using differentiated TrackPoints
	private SkiRun run;
	private Chairlift lift;
	
    private final ContentProviderUtils contentProviderUtils;
    private final Track.Id trackId;
    private int liftCount, runCount;
    private TrackPoint.Type prevType;
    private boolean down;
    private ArrayList<TrackPoint> liftPoints;
    private ArrayList<TrackPoint> runPoints;
    private ArrayList<ArrayList<TrackPoint>> runs = new ArrayList<ArrayList<TrackPoint>>();
    private ArrayList<ArrayList<TrackPoint>> lifts = new ArrayList<ArrayList<TrackPoint>>();

    public TrackDifferentiate(Track.Id tid, Context c) {
        trackId = tid;
        contentProviderUtils = new ContentProviderUtils(c);
        runPoints = new ArrayList<TrackPoint>();
        runCount = 0;
        prevType = TrackPoint.Type.IDLE;
        
        // Initalize fields by differentiate
        differentiate();
        run = new SkiRun("run", runPoints);
        //lift = new Chairlift("lift", 1, , trackId, liftPoints); // OBS : most likely this generalizes all chairLifts into a single chairLift object.
    }

    public void differentiate() {
        // iterate through all trackpoints and store them in arraylists
        try (TrackPointIterator tpi = contentProviderUtils.getTrackPointLocationIterator(trackId, null)) {
            TrackPoint trackpoint;
            TrackPoint lastTrackpoint;
            trackpoint = tpi.next();
            lastTrackpoint = trackpoint;
            while (tpi.hasNext()) {
                trackpoint = tpi.next();
                
                // Determine if point is a run point or lift point
                // BOTH : if trackpoint goes from idle to non idle (or vice versa)
                // RUN : if your altitude gain changes from positive to negative.
                // LIFT : if your altitude gain changes from negative or 0 to positive.
                if (trackpoint.getType() != prevType || (lastTrackpoint.hasAltitudeLoss() && trackpoint.hasAltitudeGain()
                		|| (lastTrackpoint.getAltitude().toM() == 0 && trackpoint.hasAltitudeGain()))) {
                    liftCount++;
                    liftPoints = new ArrayList<TrackPoint>();
                    liftPoints.add(trackpoint);
                    lifts.set(liftCount, liftPoints);
                }
                
 
                else if (trackpoint.getType() != prevType || (lastTrackpoint.hasAltitudeGain() && trackpoint.hasAltitudeLoss())) {
                    runCount++;
                    runPoints = new ArrayList<TrackPoint>();
                    runPoints.add(trackpoint);
                    runs.set(runCount, runPoints);
                    
                }
                // If types are equal check if they are part of the same segment of ride / run
                else if (trackpoint.getType() == prevType || (lastTrackpoint.hasAltitudeLoss() && trackpoint.hasAltitudeGain()
                		|| (lastTrackpoint.getAltitude().toM() == 0 && trackpoint.hasAltitudeGain())))
                {
                	// Trackpoint part of same ride segment
                	liftPoints.add(trackpoint);
                    lifts.set(liftCount, liftPoints);
                }
                
                else // if none of the conditions above are met, it must be part of the same run segment
                {
                    runPoints.add(trackpoint);
                    runs.set(runCount, runPoints);
                }
                // set type for next iteration
                prevType = trackpoint.getType();
            }
        }
    }

    public List<TrackPoint> getLiftPoints() {
        return liftPoints;
    }

    public List<ArrayList<TrackPoint>> getRuns() {
        return runs;
    }
    
    // Getters for models of chairLift and SkiRun, allows to get references and call service functions, such as getting wait time, average speed, etc.
    // Check model implementations to see what service functions are available.
    public Chairlift getLift()
    {
    	return lift;
    }
    
    public SkiRun getRun()
    {
    	return run;
    }
}
