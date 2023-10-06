import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class UASort {

	public static String sortingType;
	public static long startTime;
	public static long endTime;
	public static long elapsedTime;

	public static void main(String[] args) throws Exception {

		File inputDir = new File(args[0]);
		File outputDir = new File(args[1]);
		sortingType = args[3];
		File filesList[] = inputDir.listFiles();
		int numFiles = 0;

		if (args.length != 4) {
			throw new Exception("Missing an important args.");
		}

		startTime = System.currentTimeMillis();
		
		for (File file : filesList) {

			String fileName = file.getName();
			File outputFile = new File(outputDir, fileName);

			if (args[2].equals("numeric")) {
				sortNumFile(file, outputFile);
			} else if (args[2].equals("strings")) {
				sortStrFile(file, outputFile);
			} else {
				throw new Exception("Data Type is not valid.");
			}

			numFiles++;

		}
		
		endTime = System.currentTimeMillis();

		elapsedTime = (endTime - startTime) / 1000;

		// Output File
		System.out.printf("Number of files Sorted: %8s%n", numFiles);
		System.out.printf("Input Directory: %66s%n", inputDir.getAbsolutePath());
		System.out.printf("Output Directory: %66s%n%n", outputDir.getAbsolutePath());
		System.out.printf("Start Time: %32s%n", startTime);
		System.out.printf("End Time: %34s%n", endTime);
		System.out.printf("Elapsed Time: %18s%n", elapsedTime);

	}

	// RUNNING QUICKSORTS AND OUTPUTTING FILES
	public static void sortNumFile(File inputFile, File outputFile) throws Exception {
		
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		String line;
		int i = 0;
		int[] num = new int[arrSize(inputFile)];

		while ((line = br.readLine()) != null) {

			num[i] = Integer.parseInt(line);
			i++;

		}

		br.close();

		

		quickSort(num, 0, num.length - 1);

		

		BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
		if (sortingType.equals("descending")) {
			for (int j = num.length - 1; j >= 0; j--) {
				bw.write(Integer.toString(num[j]));
				bw.newLine();
			}
		} else if (sortingType.equals("ascending")) {
			for (int j : num) {
				bw.write(Integer.toString(j));
				bw.newLine();
			}
		} else {
			bw.close();
			throw new Exception("Sorting type is not valid.");
		}

		bw.close();

	}

	public static void sortStrFile(File inputFile, File outputFile) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		String line;
		int i = 0;
		String[] str = new String[arrSize(inputFile)];

		while ((line = br.readLine()) != null) {

			str[i] = line;
			i++;

		}

		br.close();

		startTime = System.currentTimeMillis();

		quickSort(str, 0, str.length - 1);

		endTime = System.currentTimeMillis();

		elapsedTime = (endTime - startTime) / 1000;

		BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
		if (sortingType.equals("descending")) {
			for (int h = str.length - 1; h >= 0; h--) {
				bw.write(str[h]);
				bw.newLine();
			}
		} else if (sortingType.equals("ascending")) {
			for (String j : str) {
				bw.write(j);
				bw.newLine();
			}
		} else {
			bw.close();
			throw new Exception("Sorting type is not valid.");
		}

		bw.close();

	}

	// CHECKING AMOUNT OF LINES IN THE FILE
	public static int arrSize(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		int i = 0;

		while (br.readLine() != null) {
			i++;
		}

		br.close();
		return i;

	}

	// PARTITIONS
	public static int intPartition(int[] A, int p, int r) {

		int x = selectPivot(A, p, r);
		int pivotValue = A[x];
		A[x] = A[r];
		A[r] = pivotValue;
		int i = p - 1;

		for (int j = p; j < r; j++) {
			if (A[j] <= pivotValue) {
				i = i + 1;
				int temp = A[i];
				A[i] = A[j];
				A[j] = temp;
			}
		}
		int temp = A[i + 1];
		A[i + 1] = A[r];
		A[r] = temp;

		return i + 1;

	}

	public static int stringPartition(String[] A, int p, int r) {

		int pivot = selectStringPivot(A, p, r);
		String x = A[pivot];
		A[pivot] = A[r];
		A[r] = x;
		int i = p - 1;

		for (int j = p; j < r; j++) {
			if (A[j].compareTo(x) < 0) {

				i = i + 1;
				String temp = A[i];
				A[i] = A[j];
				A[j] = temp;
			}
		}

		String temp = A[i + 1];
		A[i + 1] = A[r];
		A[r] = temp;

		return i + 1;

	}

	// QUICK SORTS
	public static void quickSort(int[] A, int p, int r) {
		if (p < r) {
			int q = intPartition(A, p, r);
			quickSort(A, p, q - 1);
			quickSort(A, q + 1, r);
		}
	}

	public static void quickSort(String[] A, int p, int r) {
		if (p < r) {
			int q = stringPartition(A, p, r);
			quickSort(A, p, q - 1);
			quickSort(A, q + 1, r);
		}
	}

	// SELECT PIVOTS
	public static int selectPivot(int[] A, int p, int r) {

		if (r - p + 1 >= 3) {
			int firstIndex = p;
			int middleIndex = p + (r - p) / 2;
			int lastIndex = r;

			int firstValue = A[firstIndex];
			int middleValue = A[middleIndex];
			int lastValue = A[lastIndex];

			if ((firstValue <= middleValue && middleValue <= lastValue)
					|| (lastValue <= middleValue && middleValue <= firstValue)) {
				return middleIndex;
			} else if ((middleValue <= firstValue && firstValue <= lastValue)
					|| (lastValue <= firstValue && firstValue <= middleValue)) {
				return firstIndex;
			} else {
				return lastIndex;
			}
		} else {

			return r;
		}
	}

	public static int selectStringPivot(String[] A, int p, int r) {

		if (r - p + 1 >= 3) {
			int firstIndex = p;
			int middleIndex = p + (r - p) / 2;
			int lastIndex = r;

			String firstValue = A[firstIndex];
			String middleValue = A[middleIndex];
			String lastValue = A[lastIndex];

			if ((firstValue.compareTo(middleValue) <= 0 && middleValue.compareTo(lastValue) <= 0)
					|| (lastValue.compareTo(middleValue) <= 0 && middleValue.compareTo(firstValue) <= 0)) {
				return middleIndex;
			} else if ((middleValue.compareTo(firstValue) <= 0 && firstValue.compareTo(lastValue) <= 0)
					|| (lastValue.compareTo(firstValue) <= 0 && firstValue.compareTo(middleValue) <= 0)) {
				return firstIndex;
			} else {
				return lastIndex;
			}
		} else {
			return r;
		}
	}
}
