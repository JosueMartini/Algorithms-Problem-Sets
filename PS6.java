import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PS6 {
	public static int maxValue;

	public static void main(String[] args) throws IOException {
		int[] restaurants = readInputFromFile(args[0]);
		int[][] table = createMemoTable(restaurants);
		maxValue = findMaxValue(table);
		int[] path = findOptimalPath(table);

		System.out.println("Maximum Value: " + maxValue);
		System.out.println("\nPath");
		System.out.println("-----------------");

		int subtotal = 0;

		for (int i = 0; i < path.length; i++) {
			int restaurantId = path[i];
			int value = restaurants[restaurantId];
			subtotal += value;
			System.out.println(restaurantId + ": Value = " + value + ", Subtotal: " + subtotal);
		}

	}

	public static int[][] createMemoTable(int[] restaurants) {
		int length = restaurants.length;
		int[][] table = new int[length][length];

		// BASE CASE i = 1
		for (int i = 0; i < length; i++) {
			table[0][i] = restaurants[i];
		}

		// FILLING TABLE WITH SUBARRAY PROBLEMS
		for (int i = 1; i < length; i++) {
			for (int j = 0; j < length; j++) {
				int above = table[0][j];
				int diagonal = table[i - 1][(j + 1) % length];

				table[i][j] = above + diagonal;

			}
		}

		return table;

	}

	public static int[] findOptimalPath(int[][] table) {

		// FINDING MAXVALUE AGAIN
		int maxValue = 0;
		int endRest = 0;
		for (int i = 0; i < table.length; i++) {
			if (maxValue < table[i][table.length - 1]) {
				maxValue = table[i][table.length - 1];
				endRest = i;
			}
		}

		// FINDING OPTIMAL PATH
		int currRestaurant = endRest;
		int[] path = new int[table.length];

		return path;
	}

	public static int findMaxValue(int[][] table) {
		int maxValue = 0;
		for (int i = 0; i < table.length; i++) {
			if (maxValue < table[i][table.length - 1]) {
				maxValue = table[i][table.length - 1];
			}
		}

		return maxValue;

	}

	public static int[] readInputFromFile(String inputFile) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		String line;
		int count = 0;
		while ((line = br.readLine()) != null) {
			count++;
		}
		br.close();

		int[] restaurants = new int[count];
		BufferedReader br2 = new BufferedReader(new FileReader(inputFile));
		String line2;
		int i = 0;
		while ((line2 = br2.readLine()) != null) {
			String[] split = line2.split(",");

			restaurants[i] = Integer.parseInt(split[1]);
			i++;

		}
		br2.close();
		return restaurants;

	}
}
