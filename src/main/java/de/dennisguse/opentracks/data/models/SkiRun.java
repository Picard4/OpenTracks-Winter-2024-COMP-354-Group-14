	package de.dennisguse.opentracks.data.models;
	
	import java.time.Duration;
	import java.util.List;
	import java.util.ArrayList;
	
	public class SkiRun {
			// Run related stats
		    private String name;
		    private double userWeight; // in kilograms
		    private double average_speed;
		    private double maximum_speed;
		    private double totalRunElevation;
		    private double slopePercentage;
		    private String skiingCondition; // Ex: "powder", "groomed", "icy"
		    
		    // Segments / Trackpoints of Run
		    private List<TrackPoint> trackPoints;
	
		    // Constructor
		    public SkiRun(String name, List<TrackPoint> trackPoints, double userWeight) {
		        this.name = name;
		        this.trackPoints = trackPoints;
		        this.userWeight = userWeight;
		        calculateSlopePercentage();
		    }
	
		    
		    private void calculateSlopePercentage() {
		        double totalElevationGain = getTotalRunElevation(); 
		        double totalDistance = getTotalDistance() * 1000; // Convert to meters 
		        
		        if (totalDistance > 0) {
		            slopePercentage = (totalElevationGain / totalDistance) * 100;
		        } else {
		            slopePercentage = 0;
		        }
		    }
		    
		    
		    //  < -- Getters and Setters -- >
		    
		    public Double getUserWeight(){
		    	return userWeight;
		    }
		    
		    public void setUserWeight(Double userWeight) {
		    	this.userWeight = userWeight;
		    }
		    
		    public String getSkiingCondition() {
		    	return skiingCondition;
		    }
		    
		    public void setSkiingCondition(String skiingCondition) {
		    	this.skiingCondition = skiingCondition;
		    }
		    
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
		    
		    public void setAverageSpeed(double speed) {
		    	this.average_speed = speed;
		    }
		    
		    public double getMaxSpeed() {
		    	return average_speed;
		    }
		    
		    public void setMaxSpeed(double speed) {
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
		    
		    private double calculateMET() {
		        double baseMET;
	
		        // Adjust base MET based on average speed 
		        if (average_speed < 10) {
		            baseMET = 4.3; // Light effort
		        } else if (average_speed >= 10 && average_speed < 20) {
		            baseMET = 5.8; // Moderate effort
		        } else if (average_speed >= 20 && average_speed < 30) {
		            baseMET = 8.0; // Hard effort
		        } else {
		            baseMET = 12.0; // Racing
		        }
	
		        // Adjust MET based on slope percentage 
		        if (slopePercentage > 20) {
		            baseMET *= 1.2; // Increase by 20% for steep slopes
		        } else if (slopePercentage < 5) {
		            baseMET *= 0.9; // Decrease by 10% for easy slopes
		        }
	
		        // Further adjust MET based on skiing conditions
		        switch (skiingCondition) {
		            case "powder":
		                baseMET *= 1.3; // Increase by 30% for powder conditions
		                break;
		            case "icy":
		                baseMET *= 1.1; // Slightly increase MET (10%) for icy conditions
		                break;
		            // Assume groomed trails are the baseline.
		        }
	
		        return baseMET;
		    }
		    
		    public double calculateCaloriesBurned() {
		        double met = calculateMET();
		        double durationInHours = getDuration().toMillis() / 3600000.0;
		        return met * userWeight * durationInHours;
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
		        double prevElevation = this.trackPoints.get(0).getAltitude().toM();
		        for (TrackPoint tp:
		             this.trackPoints) {
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
		    	ArrayList<Speed> speedList = new ArrayList<>();
		    	for (TrackPoint tp: this.trackPoints)
		    	{
		    		Speed speed = tp.getSpeed();
		    		speedList.add(speed);
		    		
		    		maxSpeed = Speed.max(maxSpeed, speed);
		    		
		    		totalSpeed += speed.toMPS(); // convert speed to m/s
		    	}
		    	
		    	double averageSpeed = totalSpeed / speedList.size(); // get average speed for ski run
		    	
		    	// Set ski run maximum and average speed
		    	this.average_speed = averageSpeed;
		    	this.maximum_speed = maxSpeed.toMPS();
		    }
        
		    //Temporary commenting thi method
	//	    // Method to calculate slope percentage between two provided track points (negative altitude change as descending)
	//	    public double calculateSlopePercentage(TrackPoint p1, TrackPoint p2)
	//	    {
	//	    	return p1.getSlopePercentage();
	//	    }
	
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
