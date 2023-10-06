import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/*  Student Name:	Josue Martinez
 *  Username:		ua###		<--- this needs to be correct
 *  Date:			09/08/2023
 *  Class:          CS 3103 - Algorithms
 *  Filename:       UAHeap.java
 *  Description:    Implementation of a priority queue using a heap (by J. Martinez)
 */

public class UAHeap {

	static final int INITIAL_SIZE = 1; // just to declare the array with the default starting point

	public int size;

	private int[] a; // This will be used to store the values of the heap

	// Default, no-argument constructor
	public UAHeap() {
		this(INITIAL_SIZE);
	}

	// Construct a UAHeap with the size provided
	public UAHeap(int size) {
		a = new int[size];
	}

	// this method simply returns the array a, so do not modify this one
	public int[] getArray() {
		return this.a;
	}

	// The main method should be already configured to run with the appropriate
	// settings. Do not alter this method.
	public static void main(String[] args) throws IOException {
		if (args.length < 3) {
			System.out.println("Invalid syntax:   java  UAHeap  inputfile  outputfile  inputsize");
			System.exit(100);
		}

		int inputSize = 0;

		try {
			inputSize = Integer.parseInt(args[2]);
		} catch (Exception ex) {
			System.out.println("Invalid input size");
			System.exit(100);
		}

		UAHeap h = new UAHeap(inputSize);
		System.out.println("Step 1: Loading the array");
		h.loadData(args[0]);

		if (h.getArray().length < 20) {
			System.out.println("Start:  " + Arrays.toString(h.getArray()));
			h.buildMinHeap();
			System.out.println("Heap:   " + Arrays.toString(h.getArray()));
			h.getSortedHeap();
			System.out.println("Sorted:   " + Arrays.toString(h.getArray()));
			System.out.println("\n\n");

			h.saveDataToFile(args[1]);
		} else {
			int[] a1 = h.getArray();
			System.out.print("Start (first 20 only): ");
			for (int i = 0; i < 20; i++) {
				System.out.print(a1[i] + " ");
			}
			System.out.println();

			h.buildMinHeap();
			int[] a2 = h.getArray();
			System.out.print("Heap (first 20 only): ");
			for (int i = 0; i < 20; i++) {
				System.out.print(a2[i] + " ");
			}
			System.out.println();

			System.out.println("Step 2: Running Heapsort");

			System.out.println("\n\n");
			h.getSortedHeap();
			h.saveDataToFile(args[1]);

			int[] a3 = h.getArray();
			System.out.print("Sorted (first 20 only): ");
			for (int i = 0; i < 20; i++) {
				System.out.print(a3[i] + " ");
			}
			System.out.println();
			System.out.print("Sorted (last 20 only): ");
			for (int i = a3.length - 20; i < a3.length; i++) {
				System.out.print(a3[i] + " ");
			}
			System.out.println();

			System.out.println("Complete. Saved output to " + args[1]);
		}

		System.out.println();

	}

	// this method should return the height of your heap (you need to calculate it)
	public int getHeight() {

		if (size == 0) {
			return -1;
		}

		int height = 0;
		int nodes = 1;

		for (int i = 1; i < size; i += nodes) {
			height++;
			nodes *= 3;
		}

		return height;

	}

	// Retrieves the min value from the heap
	public int getMinValue() {

		if (a.length == 0) {
			return -1;
		}

		return a[0];

	}

	// Removes and returns the min value from the heap while preserving the heap
	// properties
	public int removeMinValue() {

		if (size == 0) {
			return -1;
		}

		int min = getMinValue();
		a[0] = a[size - 1];
		size--;

		heapifyDown(0);
		return min;
	}

	// Builds the heap structure by starting from ((int) n/3)
	public void buildMinHeap() {

		for (int i = size / 3; i >= 0; i--) {
			heapifyDown(i);
		}

	}

	// Insert a value into the heap
	public void insertValue(int value) {

		a[size] = value;
		size++;
		heapifyUp(size, value);

	}

	// Returns the number of elements within the heap
	public int size() {
		return size;
	}

	// Reorganizes an element at the given index moving downward (if needed)
	public void heapifyDown(int index) {

		int smallest = index;
		if (left(index) < size && a[left(index)] < a[smallest]) {
			smallest = left(index);
		}
		if (middle(index) < size && a[middle(index)] < a[smallest]) {
			smallest = middle(index);
		}
		if (right(index) < size && a[right(index)] < a[smallest]) {
			smallest = right(index);
		}

		if (smallest != index) {
			int holder = a[index];
			a[index] = a[smallest];
			a[smallest] = holder;
			heapifyDown(smallest);
		}

	}

	// Decreases the value at the specified index and moves it upward (if needed).
	public void heapifyUp(int index, int value) {

		while (index > 0 && value < a[parent(index)]) {

			a[index] = a[parent(index)];
			index = parent(index);

		}

		a[index] = value;

	}

	// Loads data into the heap from a file (line-delimited)
	public void loadData(String filename) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line;
		int i = 0;
		while ((line = br.readLine()) != null) {

			int value = Integer.parseInt(line);
			a[i] = value;
			i++;

		}

		size = i;
		br.close();

	}

	// Writes the contents of the heap into the specified file (line-delimited)
	public void saveDataToFile(String filename) throws IOException{
		
		BufferedWriter br2 = new BufferedWriter(new FileWriter(filename));

		for (int i = 0; i < a.length; i++) {
			br2.write(a[i] + " ");
		}

		br2.close();

	}

	// Returns the values from the heap in ascending order (and saves it to the
	// array a)
	public int[] getSortedHeap() {

		int[] sortedArray = new int[size];
		int count = 0;
		for (int i = size - 1; i >= 0; i--) {
			sortedArray[count] = removeMinValue();
			count++;
		}

		a = sortedArray;
		return a;
	}

	/***********************************************************************
	 * Location for additional methods needed to complete this problem set.
	 ***********************************************************************/

	public int parent(int i) {
		return Math.round((float) (i - 1) / 3);
	}

	public int left(int i) {
		return ((i + 1) * 3) - 2;
	}

	public int right(int i) {
		return (i + 1) * 3;
	}

	public int middle(int i) {
		return ((i + 1) * 3) - 1;
	}
}