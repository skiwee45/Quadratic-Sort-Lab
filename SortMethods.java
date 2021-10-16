package QuadraticSortProject;

import java.util.*;

public class SortMethods {

    //debuggers
    static int stickySortComparisons = 0;
    static int insertionSortComparisons = 0;

    public static List<Integer> stickySort(List<Integer> arr) {
        LinkedHashMap<Integer, Integer> stuckValues = new LinkedHashMap<>();

        //setup
        int min = Integer.MAX_VALUE;
        for (int e : arr) {
            min = Math.min(e, min);
        }
        int max = min;
        //like bubble sort but the selected value will stick other 1 larger than it
        while (!arr.isEmpty()) {
            if (!stuckValues.containsKey(min))
                stuckValues.put(min, 0);
            min = Integer.MAX_VALUE;

            Iterator<Integer> iterator = arr.iterator();
            while (iterator.hasNext()) {
                stickySortComparisons++;
                int value = iterator.next();
                if (stuckValues.containsKey(value)) {
                    //increment the stuckValues value
                    stuckValues.replace(value, stuckValues.get(value) + 1);
                    iterator.remove();
                } else if (value == max + 1) { //stick on new ones
                    stuckValues.put(value, 1);
                    max = value;
                    iterator.remove();
                } else if (value < min) {
                    min = value;
                }
            }
        }
        return createListFromDictionary(stuckValues);
    }

    private static List<Integer> createListFromDictionary(HashMap<Integer, Integer> map) {
        ArrayList<Integer> output = new ArrayList<>();
        for (int key : map.keySet()) {
            for (int i = 0; i < map.get(key); i++) { //add value however many times
                output.add(key);
            }
        }
        return output;
    }

    static void bubbleSort(int[] arr, int n) {
        int i, j, temp;
        boolean swapped;
        for (i = 0; i < n - 1; i++) {
            swapped = false;
            for (j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    // swap arr[j] and arr[j+1]
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped)
                break;
        }
    }

    public static void insertionSort(int[] stuff) {
        for (int i = 1; i < stuff.length; ++i) {
            int val = stuff[i];
            int j = i;
            while (j > 0 && val < stuff[j - 1]) {
                insertionSortComparisons++;
                stuff[j] = stuff[j - 1];
                j--;
            }
            stuff[j] = val;
        }
    }

    public static void selectionSort(int[] ray) {
        for (int i = 0; i < ray.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < ray.length; j++) {
                if (ray[j] < ray[min])  //flip the symbol - what happens??
                    min = j;    //find location of smallest
            }
            if (min != i) {
                int temp = ray[min];
                ray[min] = ray[i];
                ray[i] = temp;   //put smallest in pos i
            }
        }
    }

    public static int[] generateRandom(int key, int length, int bound) {
        Random rand = new Random(key);
        int[] output = new int[length];
        for (int i = 0; i < length; i++) {
            output[i] = rand.nextInt(bound);
        }
        return output;
    }

    public static List<Integer> generateRandomList(int key, int length, int bound) {
        Random rand = new Random(key);
        List<Integer> output = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            output.add(rand.nextInt(bound));
        }
        return output;
    }

    public static long testInsertionSort(long trials, int[] arr) {
        StopWatch watch = new StopWatch();
        long totalTime = 0;
        for (int i = 0; i < trials; i++) {
            int[] copy = Arrays.copyOf(arr, arr.length);
            watch.start();
            SortMethods.insertionSort(copy);
            totalTime += watch.getElapsedTime();
            watch.stop();
            watch.reset();
        }
        return totalTime / trials;
    }

    public static long testStickySort(long trials, List<Integer> arr) {
        StopWatch watch = new StopWatch();
        long totalTime = 0;
        for (int i = 0; i < trials; i++) {
            List<Integer> copy = new ArrayList<>(arr);
            watch.start();
            SortMethods.stickySort(copy);
            totalTime += watch.getElapsedTime();
            watch.stop();
            watch.reset();
        }
        return totalTime / trials;
    }

    public static void main(String[] args) {
        List<Integer> testerList = generateRandomList(1, 10000, 100);
        System.out.printf("%-20s%s%n", "Original Array: ", testerList);
        int[] testerArray = generateRandom(1, 10000, 100);

        //check pass / fail
        int[] temp = testerArray.clone();
        insertionSort(temp);
        List<Integer> expectedResults = new ArrayList<>(temp.length);
        for (int i : temp) {
            expectedResults.add(i);
        }
        List<Integer> sortedArr = stickySort(new ArrayList<>(testerList));
        System.out.printf("%-20s%s%n", "Sticky Sort: ", sortedArr);
        System.out.printf("%-20s%s%n", "Expected Result: ", expectedResults);
        System.out.println("Test Result: " + (sortedArr.equals(expectedResults) ? "pass" : "fail"));

        //compare sticky sort vs insertion sort comparisons
        System.out.println("\nSticky Sort Total Comparisons: " + stickySortComparisons);
        System.out.println("Insertion Sort Total Comparisons: " + insertionSortComparisons);

        //test sort times
        System.out.println("\nSticky Sort Time: " + testStickySort(3, testerList) + " nanoseconds");
        System.out.println("Insertion Sort Time: " + testInsertionSort(3, testerArray) + " nanoseconds");
    }
}
