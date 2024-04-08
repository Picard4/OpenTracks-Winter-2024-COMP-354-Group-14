package de.dennisguse.opentracks.data.models;

import java.time.Duration;
import java.util.List;

public class SkiRun {
		// Run related stats
	    private String name;
	    private double average_speed;
	    private double maximum_speed;
	    private double totalRunElevation;
	    
	    // Segments / Trackpoints of Run
	    private List<TrackPoint> trackPoints;

	    // Constructor
	    public SkiRun(String name, List<TrackPoint> trackPoints) {
	        this.name = name;
	        this.trackPoints = trackPoints;
	    }

	    //  < -- Getters and Setters -- >
	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public List<TrackPoint> getTrackPoints() {
	        return trackPoints;
	    }

	    public void setTrackPoints(List<TrackPoint> trackPoints) {
	        this.trackPoints = trackPoints;
	    }
	    
	    public double getTotalRunElevation() {
	        return totalRunElevation;
	    }

	    public void setTotalRunElevation(double totalRunElevation) {
	        this.totalRunElevation = totalRunElevation;
	    }
	    
	    public double getAverageSpeed() {
	    	return average_speed;
	    }
	    
	    public double setAverageSpeed(double speed) {
	    	this.average_speed = speed;
	    }
	    
	    public double getMaxSpeed() {
	    	return average_speed;
	    }
	    
	    public double setMaxSpeed(double speed) {
	    	this.maximum_speed = speed;
	    }
	    

	    // Determine the start and end points of the ski run based on trackpoints
	    public TrackPoint getStartPoint() {
	        if (trackPoints.isEmpty()) {
	            return null;
	        }
	        return trackPoints.get(0);
	    }

	    public TrackPoint getEndPoint() {
	        if (trackPoints.isEmpty()) {
	            return null;
	        }
	        return trackPoints.get(trackPoints.size() - 1);
	    }

	    // Calculate the duration of the ski run
	    public Duration getDuration() {
	        if (trackPoints.isEmpty()) {
	            return Duration.ZERO;
	        }
	        TrackPoint startPoint = getStartPoint();
	        TrackPoint endPoint = getEndPoint();
	        return Duration.between(startPoint.getTime(), endPoint.getTime());
	    }

	    // Method to calculate the total distance covered during the ski run
	    public double getTotalDistance() {
	        double totalDistance = 0.0;
	        for (int i = 1; i < trackPoints.size(); i++) {
	            TrackPoint pPoint = trackPoints.get(i - 1);
	            TrackPoint cPoint = trackPoints.get(i);
	            Distance distance = pPoint.distanceToPrevious(cPoint);
	            totalDistance += distance.toKM();
	        }
	        return totalDistance;
	    }
	    
	    
	    // Method to calculate run specific elevation calculation - Written by Volarr
	    public void calculateRunSpecificElevationGain()
	    {
	        double prevElevation = this.trackPointsList.get(0).getAltitude().toM();
	        for (TrackPoint tp:
	             this.trackPointsList) {
	            Altitude altitude = tp.getAltitude();
	            double tpElevationGain = prevElevation - altitude.toM();
	            this.totalRunElevation += tpElevationGain;
	            prevElevation = altitude.toM();
	        }
	    }
	    
	    // Method to calculate the speed information (average/maximum) of a run based on track points
	    public void calculateSpeedStatistics()
	    {
	    	Speed maxSpeed = Speed.zero(); // Inital max speed = 0
	    	double totalSpeed = 0;
	    	ArrayList<Speed> speedList = new ArrayList<>()
	    	for (TrackPoint tp: this.trackPointsList)
	    	{
	    		Speed speed = tp.getSpeed();
	    		speedList.add(speed);
	    		
	    		maxSpeed = Speed.max(maxSpeed, speed);
	    		
	    		totalSpeed += speed.toMPS(); // convert speed to m/s
	    	}
	    	
	    	double averageSpeed = totalSpeed / speedList.size(); // get average speed for ski run
	    	
	    	// Set ski run maximum and average speed
	    	this.average_speed = averageSpeed;
	    	this.maximum_speed = maxSpeed;
	    }
	    
	    // Method to calculate slope percentage between two provided track points (negative altitude change as descending)
	    public double calculateSlopePercentage(TrackPoint p1, TrackPoint p2)
	    {
	    	return p1.getSlopePercentage();
	    }

	    // Determine if the user was skiing
	    public boolean isUserSkiing() {
	    	if (trackPoints.size() < 2) {
	            return false; // Not enough data
	        }

	        // Thresholds to determine skiing activity 
	        double altitudeChangeThreshold = 10.0; // Meters
	        double speedThreshold = 5.0; // Meters per second
	        long timeThresholdInSeconds = 60; // Seconds

	        // Get the first and last track points
	        TrackPoint startPoint = getStartPoint();
	        TrackPoint endPoint = getEndPoint();

	        // Check if altitude change is significant
	        double altitudeChange = Math.abs(startPoint.getAltitude().toM() - endPoint.getAltitude().toM());
	        if (altitudeChange < altitudeChangeThreshold) {
	            return false; // Altitude change not significant, likely not skiing
	        }

	        // Calculate total distance
	        double totalDistance = getTotalDistance();

	        // Calculate total time (in seconds)
	        long totalTimeInSeconds = getDuration().getSeconds();

	        // Calculate average speed
	        double averageSpeed = totalDistance / totalTimeInSeconds;

	        // Check if average speed is above the speed threshhold 
	        if (averageSpeed < speedThreshold) {
	            return false; // Average speed too slow, probably not skiing
	        }

	        // Check if total time is above time threshold
	        if (totalTimeInSeconds < timeThresholdInSeconds) {
	            return false; // Duration too short, probably not skiing
	        }

	        return true; // User is likely skiing
	    }
	}
