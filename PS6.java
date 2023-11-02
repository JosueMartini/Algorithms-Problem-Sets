import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PS6 {
    private int[] restaurants;

    public static void main(String[] args) {
        String inputFile = args[0];
        int[] restaurants = readInputFromFile(inputFile);
        PS6 ps6 = new PS6(restaurants);
        ps6.findMaximumValuePath();
    }

    public PS6(int[] restaurants) {
        this.restaurants = restaurants;
    }

    public void findMaximumValuePath() {
        int n = restaurants.length;
        int maxSum = 0;
        int startRestaurant = -1;
        int endRestaurant = -1;

        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                int maxValue = calculateMaxValue(i, j, n);

                if (maxValue > maxSum) {
                    maxSum = maxValue;
                    startRestaurant = i;
                    endRestaurant = j;
                }
            }
        }

        System.out.println("Maximum Value: " + maxSum);
        System.out.println("Path");
        System.out.println("-----------------");

        int current = startRestaurant;
        int subtotal = 0;
        do {
            int restaurantIdx = current;
            int value = restaurants[restaurantIdx];
            System.out.println(restaurantIdx + ": Value = " + value + ", Subtotal: " + (subtotal + value));
            subtotal += value;
            current = (current + 1) % n;
        } while (current != endRestaurant);

        int restaurantIdx = endRestaurant;
        int value = restaurants[restaurantIdx];
        System.out.println(restaurantIdx + ": Value = " + value + ", Subtotal: " + (subtotal + value));
    }

    private int calculateMaxValue(int i, int j, int n) {
        int maxValue = 0;
        int sum = 0;

        for (int k = i; k <= j; k++) {
            int value = restaurants[k];
            sum += value;
            if (sum > maxValue) {
                maxValue = sum;
            }
            if (sum < 0) {
                sum = 0;
            }
        }

        int circularSum = 0;
        for (int k = (j + 1) % n; k != i; k = (k + 1) % n) {
            int value = restaurants[k];
            circularSum += value;
            if (circularSum > maxValue) {
                maxValue = circularSum;
            }
            if (circularSum < 0) {
                circularSum = 0;
            }
        }

        return maxValue;
    }

    private static int[] readInputFromFile(String inputFile) {
        List<Integer> values = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    int value = Integer.parseInt(parts[1]);
                    values.add(value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int[] restaurants = new int[values.size()];
        for (int i = 0; i < values.size(); i++) {
            restaurants[i] = values.get(i);
        }

        return restaurants;
    }
}
