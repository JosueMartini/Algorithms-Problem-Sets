import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class UASortAndMerge {

	public static void main(String[] args) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(args[0]));
		String line;
		String[] A = null;

		while ((line = br.readLine()) != null) {

			A = line.split(" ");

		}

		br.close();

		MergeSort(A, 0, A.length - 1, Integer.parseInt(args[2]));

		BufferedWriter br2 = new BufferedWriter(new FileWriter(args[1]));

		for (int i = 0; i < A.length; i++) {
			br2.write(A[i] + " ");
		}

		br2.close();

	}

	public static void InsertionSort(String[] A, int n, int p, int r) {
		int inv = 0;
		String hold1 = A[p];
		String hold2 = A[r];
		for (int i = 1; i < n; i++) {
			String key = A[i];
			int j = i - 1;

			inv = 0;
			for (int z = i - 1; z >= 0; z--) {
				if (A[z].compareTo(key) > 0 || z > i) {
					inv++;
				}
			}

			while (j >= 0 && A[j].compareTo(key) > 0) {
				A[j + 1] = A[j];
				j = j - 1;
			}
			A[j + 1] = key;

		}

		System.out.printf("Ins Sort: [p=%d, r=%d, Inv=%d] Array=[%s %s] Finish=%s\n", p, r, inv, hold1, hold2,
				Arrays.toString(A));

	}

	public static void MergeSort(String[] A, int p, int r, int h) {

		if (p < r) {
			if (p + h > r) {
				InsertionSort(A, p + h, p, r);
			} else {
				int q = (p + r) / 2;
				MergeSort(A, p, q, h);
				MergeSort(A, q + 1, r, h);
				Merge(A, p, q, r);
			}

		}

	}

	public static void Merge(String[] A, int p, int q, int r) {
		int left = q - p + 1;
		int right = r - q;

		String[] L = new String[left];
		String[] R = new String[right];

		for (int i = 0; i < left; i++) {
			L[i] = A[p + i];
		}

		for (int j = 0; j < right; j++) {
			R[j] = A[q + j + 1];
		}

		int i = 0;
		int j = 0;
		int k = p;

		while (i < left && j < right) {
			if (L[i].compareTo(R[j]) <= 0) {
				A[k] = L[i];
				i = i + 1;
			} else {
				A[k] = R[j];
				j = j + 1;
			}
			k = k + 1;
		}

		while (i < left) {
			A[k] = L[i];
			i = i + 1;
			k = k + 1;
		}

		while (j < right) {
			A[k] = R[j];
			j = j + 1;
			k = k + 1;
		}

	}

}
