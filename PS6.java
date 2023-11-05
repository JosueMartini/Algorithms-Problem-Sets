import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PS6 {
	public static int maxValue;
	public static int endRest;

	public static void main(String[] args) throws IOException {
		int[] restaurants = readInputFromFile(args[0]);
		createMemoTable(restaurants);
		int[] path = findOptimalPath(restaurants);

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

				// FINDING MAXVALUE AND RESTAURANT WITH MAXVALUE
				if (maxValue <= table[i][j]) {
					maxValue = table[i][j];
					endRest = j;
				}

			}
		}

		return table;

	}

	public static int[] findOptimalPath(int[] restaurants) {
		int[] path = new int[restaurants.length];
		int currentRestaurant = endRest;
		int currentMax = 0;
		int count = 0;

		for (int i = 0; i < restaurants.length; i++) {
			path[count] = currentRestaurant;
			count++;
			currentMax += restaurants[currentRestaurant];

			//ENDS LOOP IF CURRENTMAX IS SAME AS MAXVALUE
			if (currentMax == maxValue) {
				i = restaurants.length + 1;
			}

			currentRestaurant = (currentRestaurant + 1) % restaurants.length;
		}

		//CREATING CORRECT SIZED ARRAY
		int[] resultPath = new int[count];

		for (int i = 0; i < count; i++) {
			resultPath[i] = path[i];
		}

		return resultPath;
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
