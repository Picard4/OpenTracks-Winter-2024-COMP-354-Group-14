package de.dennisguse.opentracks.data.models;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Chairlift {
    private String name;
    private int number;
    private double averageSpeed;
    private String liftType;
    private int id;
    private static int nextId = 1;

    private static final Map<Integer, Chairlift> validChairlifts = new HashMap<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private double totalDistance;
    private double totalTime;
    private double maxSpeed;
    private double movingTime;
    private double waitingTime;



    private double slopePercentage;

    public double getSlopePercentage() {
        return slopePercentage;
    }

    public void setSlopePercentage(double slopePercentage) {
        this.slopePercentage = slopePercentage;
    }

    private Altitude altitude;

    public Altitude getAltitude() {
        return altitude;
    }
    public void setAltitude(Altitude altitude) {
        this.altitude = altitude;
    }

    private Distance distanceFromPPoint;

    public Distance getDistanceFromPPoint() {
        return distanceFromPPoint;
    }

    public void setDistanceFromPPoint(Distance distanceFromPPoint) {
        this.distanceFromPPoint = distanceFromPPoint;
    }

    // Constructor
    public Chairlift(String name, int number, double averageSpeed, int id, double totalDistance, double totalTime, double maxSpeed, double movingTime, double waitingTime) {
        this.name = name;
        this.number = number;
        this.averageSpeed = averageSpeed;
        this.id = id;
        this.totalDistance = totalDistance;
        this.totalTime = totalTime;
        this.maxSpeed = maxSpeed;
        this.movingTime = movingTime;
        this.waitingTime = waitingTime;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public String getLiftType() {
        return liftType;
    }

    public void setLiftType(String liftType) {
        this.liftType = liftType;
    }

    public static int getNextId() {
        return nextId;
    }

    public static void setNextId(int nextId) {
        Chairlift.nextId = nextId;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public double getMovingTime() {
        return movingTime;
    }

    public void setMovingTime(double movingTime) {
        this.movingTime = movingTime;
    }

    public double getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(double waitingTime) {
        this.waitingTime = waitingTime;
    }

    public void updateMetrics(List<TrackPoint> trackPoints){
        totalDistance = calculateTotalDistance(trackPoints);
        totalTime = calculateTotalTime(trackPoints);
        movingTime = calculateMovingTime(trackPoints);
        waitingTime = calculateWaitingTime(trackPoints);
        averageSpeed = totalDistance / totalTime;
        maxSpeed = calculateMaxSpeed(trackPoints);
    }
    //to determine if the user is riding the chairlift
    public boolean isUserRidingChairlift(List<TrackPoint> trackPoints) {
        if (trackPoints.size() < 2) {
            return false; //not enough data
        }
        double altitudeChangeThreshold = 10.0;
        double speedThreshold = 2.0;
        double waitingSpeedThreshold = 0.5; //m/s
        double waitingTimeThreshold = 40;

        TrackPoint firstPoint = trackPoints.get(0);
        TrackPoint lastPoint = trackPoints.get(trackPoints.size() - 1);

        double altitudeChange = Math.abs(firstPoint.getAltitude().toM() - lastPoint.getAltitude().toM());
        double totalDistance = calculateTotalDistance(trackPoints);
        double totalTime = calculateTotalTime(trackPoints);
        double averageSpeed = totalDistance / totalTime;

        if (altitudeChange > altitudeChangeThreshold && averageSpeed < speedThreshold) {
            double waitingTime = 0.0;
            double movingTime = 0.0;
            boolean isWaiting = false;
            for (int i = 1; i < trackPoints.size(); i++) {
                TrackPoint prevPoint = trackPoints.get(i - 1);
                TrackPoint currPoint = trackPoints.get(i);
                double speed = currPoint.getSpeed().toMPS();
                double duration = Duration.between(prevPoint.getTime(), currPoint.getTime()).getSeconds();

                if (speed < waitingSpeedThreshold) {
                    //user is waiting
                    waitingTime += duration;
                    isWaiting = true;
                }
                else {
                    //user is moving
                    if (isWaiting) {
                        movingTime += waitingTime;
                        waitingTime = 0.0;
                        isWaiting = false;
                    }
                    movingTime += duration;
                }
            }

            //Calculate total time moving + waiting
            double totalTimeMovingWaiting = movingTime + waitingTime;

            //Check if the total time spent moving + waiting is significant
            double totalTimeThreshold = 40; //inminutes
            if (totalTimeMovingWaiting > totalTimeThreshold) {

                Chairlift validChairlift = new Chairlift(name, number, averageSpeed, id, totalDistance, totalTime, maxSpeed, movingTime, waitingTime);
                validChairlifts.put(validChairlift.getId(), validChairlift);
                for (TrackPoint trackPoint : trackPoints){
                    trackPoint.setChairliftSegment(true);
                }
                validChairlift.updateMetrics(trackPoints);
                return true; //Likely on chairlift
            }
        }
        return false; //Not on chairlift
    }


    private void put(int id, Chairlift validChairlift) {

    }

    //Helper method to calculate total distance covered
    private double calculateTotalDistance(List<TrackPoint> trackPoints) {

        double totalDistance = 0.0; //km
        for (int i = 1; i< trackPoints.size(); i++){
            TrackPoint pPoint = trackPoints.get(i - 1);
            TrackPoint cPoint = trackPoints.get(i);
            Distance distance = pPoint.distanceToPrevious(cPoint);
            totalDistance += distance.toKM();
        }
        return totalDistance;
    }

    private double calculateTotalTime(List<TrackPoint> trackPoints) {
        TrackPoint fPoint = trackPoints.get(0);
        TrackPoint lPoint = trackPoints.get(trackPoints.size() - 1);
        Duration duration = Duration.between(fPoint.getTime(), lPoint.getTime());
        double durationMinutes = duration.toMinutes(); //convert into minutes

        return durationMinutes;
    }
    private double calculateMovingTime(List<TrackPoint> trackPoints) {

        double movingTime = 0; //max time for chairlift

        for (int i = 1; i < trackPoints.size(); i++) {
            TrackPoint pPoint = trackPoints.get(i - 1);
            TrackPoint cPoint = trackPoints.get(i);
            if (!pPoint.isChairliftSegment() && cPoint.isChairliftSegment()) {
                Duration duration = Duration.between(pPoint.getTime(), cPoint.getTime());
                movingTime += duration.toMinutes();
            }
        }
        return movingTime;
    }

    private double calculateWaitingTime(List<TrackPoint> trackPoints){
        double waitingTime = 0.0;
        for (int i = 1; i < trackPoints.size(); i++){
            TrackPoint pPoint = trackPoints.get(i - 1);
            TrackPoint cPoint = trackPoints.get(i);
            if (pPoint.isChairliftSegment() && !cPoint.isChairliftSegment()){
                Duration duration = Duration.between(pPoint.getTime(),cPoint.getTime());
                waitingTime += duration.toMinutes();
            }
        }
        return  waitingTime;
    }

    private double calculateMaxSpeed(List<TrackPoint> trackPoints){
        double maxSpeed = 0.0;
        for (TrackPoint trackPoint : trackPoints) {
            Speed speed = trackPoint.getSpeed();
            double speedValueMPS = speed.toMPS(); // Assuming toMPS() returns speed in mps
            maxSpeed = Math.max(maxSpeed, speedValueMPS);
        }
        return maxSpeed;
    }

    private double getSlopePercentage(TrackPoint previousPoint){
        double slopePercentage;
        //to calculate altitude change
        double altitudeChange = this.getAltitude().toM() - previousPoint.getAltitude().toM();
        //to calculate distance traveled (using meters)
        Distance distanceTraveled = Distance.of(this.calculateTotalDistance((List<TrackPoint>) previousPoint));
        //to check if the distance traveled is not zero to avoid division by zero
        if (distanceTraveled.toM() != 0) {
            //to calculate slope percentage based on altitude change and distance traveled
            slopePercentage = (altitudeChange / distanceTraveled.toM()) * 100;
        }
        else {
            slopePercentage = 0; //If distance is zero
        }
        this.setSlopePercentage(slopePercentage);

        return slopePercentage;

    }

    public static List<Chairlift> getValidChairlifts() {
        return new ArrayList<>(validChairlifts.values());
    }

    //Method to get all valid chairlifts
    public static List<Chairlift> getAllValidChairlifts() {
        return new ArrayList<>(validChairlifts.values()); //use .values from hashmap to get the collection.
    }

    //Method to get a chairlift by ID
    public static Chairlift getChairliftById(int id) {
        return validChairlifts.get(id);
    }

    //Method to get the metrics to display
    public static Object[][] getChairliftMetrics() {
        List<Chairlift> chairlifts = getAllValidChairlifts();
        Object[][] metricsData = new Object[chairlifts.size()][/* Number of columns */];

        for (int i = 0; i < chairlifts.size(); i++) {
            Chairlift chairlift = chairlifts.get(i);
            // Populate metricsData with chairlift metrics as needed
        }

        return metricsData;
    }

}
