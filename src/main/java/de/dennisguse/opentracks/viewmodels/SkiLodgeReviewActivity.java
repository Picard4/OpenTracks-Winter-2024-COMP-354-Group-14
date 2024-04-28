package de.dennisguse.opentracks.viewmodels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
public class SkiLodgeReviewActivity {
    private List<SkiLodge> lodges = new ArrayList<>();
    private Scanner scanner;

    public SkiLodgeReviewActivity() {
        scanner = new Scanner(System.in);

        addInitialLodges();
    }

    private void addInitialLodges() {
        for (int i = 1; i <= 5; i++) {
            lodges.add(new SkiLodge("Lodge " + i));
        }
    }

    public void getUserInput() {
        while (true) {
            System.out.println("Choose an option: \n1. Add a Lodge \n2. Add Review Rating \n3. Display Lodges \n4. Recommended Lodges \n5. Exit");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addNewLodge();
                    break;
                case "2":
                    getUserInputForReviews();
                    break;
                case "3":
                    displayLodgesBasedOnRatings();
                    break;
                case "4":
                    recommendRandomLodges();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        }
    }

    private void addNewLodge() {
        System.out.println("Enter new Lodge's name:");
        String lodgeName = scanner.nextLine().trim();

        if (lodges.stream().anyMatch(lodge -> lodge.getName().equalsIgnoreCase(lodgeName))) {
            System.out.println("Lodge already exists.");
        } else {
            lodges.add(new SkiLodge(lodgeName));
            System.out.println("New lodge added: " + lodgeName);
        }
    }

    private void getUserInputForReviews() {
        System.out.println("Enter lodge name for review:");
        String lodgeName = scanner.nextLine().trim();

        SkiLodge lodge = lodges.stream()
                .filter(l -> l.getName().equalsIgnoreCase(lodgeName))
                .findFirst()
                .orElse(null);

        if (lodge == null) {
            System.out.println("Lodge not found.");
            return;
        }

        System.out.println("Enter ratings for " + lodge.getName() + ": (Enter a number between 1 and 5, or 'done' to finish)");
        while(scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();

            if("done".equalsIgnoreCase(input)) break;

            try {
                double score = Double.parseDouble(input);
                if (score >= 1.0 && score <= 5.0) {
                    lodge.addRating(score);
                } else {
                    System.out.println("Rating must be between 1 and 5. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number or 'done' to finish.");
            }
        }
    }

    void displayLodgesBasedOnRatings() {
        Collections.sort(lodges, (lodge1, lodge2) -> Double.compare(lodge2.getAverageRating(), lodge1.getAverageRating()));

        for (SkiLodge lodge : lodges) {
            System.out.println("Lodge: " + lodge.getName() + " | Average Rating: " + String.format("%.2f", lodge.getAverageRating()));
        }
    }

    void recommendRandomLodges() {
        Random rand = new Random();
        for (int i = 0; i < 3; i++) {
            int randomIndex = rand.nextInt(lodges.size());
            SkiLodge lodge = lodges.get(randomIndex);
            System.out.println("Random Lodge Recommendation: " + lodge.getName() + " | Average Rating: " + String.format("%.2f", lodge.getAverageRating()));
        }
    }
}
