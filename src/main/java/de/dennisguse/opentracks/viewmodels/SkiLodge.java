package de.dennisguse.opentracks.viewmodels;

import java.util.ArrayList;
import java.util.List;

public class SkiLodge {
    String name;
    double averageRating;
    List<Double> ratings;

    public SkiLodge(String name) {
        this.name = name;
        this.ratings = new ArrayList<>();
        this.averageRating = 0.0;
    }

    public void addRating(double rating) {
        ratings.add(rating);
        averageRating = ratings.stream().mapToDouble(val -> val).average().orElse(0.0);
    }

    public String getName() {
        return name;
    }

    public double getAverageRating() {
        return averageRating;
    }
}