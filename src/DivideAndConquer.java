import javax.swing.*;
import java.io.*;
import java.util.Collections;
import java.util.*;
import java.util.Arrays;


public class DivideAndConquer {

	public static void main(String[] args) throws FileNotFoundException, InterruptedException {

		int[][] cities = readArray("att48_xy.txt");
		long start = System.currentTimeMillis();
		Integer[][] tour = divide_tsp(cities, 6);
		long end = System.currentTimeMillis();
		long elapsedTime = end - start;
		System.out.println("Execution time in milliseconds : "+(double)elapsedTime/1000);
		Integer[][] firstCity = new Integer[1][3];
		firstCity[0][0] = tour[0][0];
		firstCity[0][1] = tour[0][1];
		firstCity[0][2] = tour[0][2];
		Integer[][] shortestTour = combine(tour,firstCity);
		System.out.println("Tour");
		for(int i = 0;i<tour.length;i++) {
			System.out.print(tour[i][2] + " ");
		}
		System.out.println();
		int tourLength1 = 0;
		for (int i = 0; i < tour.length-1; i++) {

			tourLength1 += GetDistance(tour[i][0],tour[i+1][0]
					,tour[i][1],tour[i+1][1]);

		}
		System.out.println(tourLength1);
		Integer[][] improvedTour = improveTour(tour,20);

		System.out.println();
		System.out.println("Improved Tour");
		for(int i = 0;i<improvedTour.length;i++) {
			System.out.print(improvedTour[i][2] + " ");
		}
		System.out.println();
		int tourLength2 = 0;
		for (int i = 0; i < improvedTour.length-1; i++) {

			tourLength2 += GetDistance(improvedTour[i][0],improvedTour[i+1][0]
					,improvedTour[i][1],improvedTour[i+1][1]);

		}
		System.out.println(tourLength2);

		System.out.println("TourNew");
		for(int i = 0;i<shortestTour.length;i++) {
			System.out.print(shortestTour[i][2] + " ");
		}
		System.out.println();
		int tourLength3 = 0;
		for (int i = 0; i < shortestTour.length-1; i++) {

			tourLength3 += GetDistance(shortestTour[i][0],shortestTour[i+1][0]
					,shortestTour[i][1],shortestTour[i+1][1]);

		}
		System.out.println(tourLength3);
		Integer[][] improvedTourNew = improveTour(shortestTour,20);

		long startTime = System.currentTimeMillis();
		System.out.println("ImprovedTourNew");
		ArrayList<Integer> improved = new ArrayList<>();
		for(int i = 0;i<improvedTourNew.length;i++) {
			System.out.print(improvedTourNew[i][2] + " ");
			improved.add(improvedTourNew[i][2]);
		}
		System.out.println();
		System.out.println(improved);
		System.out.println();
		int tourLength4 = 0;
		for (int i = 0; i < improvedTourNew.length-1; i++) {

			tourLength4 += GetDistance(improvedTourNew[i][0],improvedTourNew[i+1][0]
					,improvedTourNew[i][1],improvedTourNew[i+1][1]);

		}
		System.out.println(tourLength4);

		long endTime = System.currentTimeMillis();
		 long finalTime = endTime - startTime;
		System.out.println("Execution time in milliseconds : "+(double)finalTime/1000);

		int[][] citiesRemastered = new int[cities.length][2];
		int count = 0;
		while(count != citiesRemastered.length) {
			citiesRemastered[improvedTourNew[count][2]][0] =improvedTourNew[count][0];
			citiesRemastered[improvedTourNew[count][2]][1] =improvedTourNew[count][1];
			count++;
		}

		Chart chart = new Chart("TSP with Divide and Conquer",citiesRemastered,improved);
		chart.setSize(800, 400);
		chart.setLocationRelativeTo(null);
		chart.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		chart.setVisible(true);

	}

	public static Integer[][] improveTour (Integer[][] tour, int nIter){
		int k = 0;
		while(k<nIter){
			for(int i = 0; i<tour.length-1 ; i = i+1){
				for(int j=i+2 ; j < tour.length-1; j=j+1){
					Integer[] A = tour[i];
					Integer[] B = tour[i+1];
					Integer[] C = tour[j];
					Integer[] D = tour[j+1];
					double distanceAB = GetDistance(tour[i][0], tour[i+1][0], tour[i][1], tour[i+1][1]);
					double distanceCD = GetDistance(tour[j][0], tour[j+1][0], tour[j][1], tour[j+1][1]);
					double totaldistance1 = distanceAB + distanceCD;
					double distanceAC = GetDistance(tour[i][0], tour[j][0], tour[i][1], tour[j][1]);
					double distanceBD = GetDistance(tour[i+1][0], tour[j+1][0], tour[i+1][1], tour[j+1][1]);
					double totaldistance2 = distanceAC + distanceBD;

					if(totaldistance1 > totaldistance2){

						tour[i+1] = C;
						tour[j] = B;
					}

				}

			}
			k++;
		}
		return tour;

	}

	private static void permute(int[] tour, int l, int r, List<Integer[]> tours) {
		if (l == r) {
			Integer[] completeTour = new Integer[tour.length + 2]; // 0 1 2 3 0
			completeTour[0] = 0;
			for (int i = 0; i < tour.length; i++) {
				completeTour[i + 1] = tour[i];
			}
			completeTour[completeTour.length - 1] = 0;
			tours.add(completeTour);
		} else {
			for (int i = l; i <= r; i++) {
				tour = swap(tour, l, i);
				permute(tour, l + 1, r, tours);
				tour = swap(tour, l, i);
			}
		}
	}

	public static int[] swap(int[] tour, int i, int j) {
		int temp;
		temp = tour[i];
		tour[i] = tour[j];
		tour[j] = temp;
		return tour;
	}

	public static int[][] readArray(String file) throws FileNotFoundException {
		// we'll count how many elements are there?
		int counter = 0; // counter for calculating text's row length

		Scanner sc1 = new Scanner(new File(file)); // scanner for calculating text's row length
		while (sc1.hasNextLine()) { // checks for if there is any line
			counter++;
			sc1.nextLine();// jumps to nextline
		}

		int[][] cities = new int[counter][2]; // creating our cities array with
		// rows as "counter" and columns as 2(X, Y)
		Scanner sc2 = new Scanner(new File(file)); // scanner for getting values from text

		int i = 0;
		while (sc2.hasNext()) {

			String tempX = sc2.next();// first next will be X coordinate
			String tempY = sc2.next();// second next will be Y coordinate

			cities[i][0] = Integer.parseInt(tempX);
			cities[i][1] = Integer.parseInt(tempY);
			i++;
		}

		return cities; // returns our 2d array
	}

	public static Integer[][] divide_tsp(int[][] cities, int n) {

		if (cities.length <= n) {
			return exhaustive_tsp(cities);
		}

		else {
			Cities citiesObj = split_cities(cities);
			return join_tours(divide_tsp(citiesObj.firstHalf, n), divide_tsp(citiesObj.secondHalf, n));
		}
		//0 1 2 3  0 1 2 3
		//5 6 7 8  1 2 3 4

	}

	public static Integer[][] exhaustive_tsp(int[][] citiesHalf) {
		List<Integer[]> tours = new ArrayList<Integer[]>();
		int[] tour = new int[citiesHalf.length - 1]; // 1 2 3
		for (int i = 0; i < tour.length; i++) {
			tour[i] = i + 1;
		}
		permute(tour, 0, tour.length - 1, tours);


		//0 1 5 4 3 2 0
		return shortest_tour(tours, citiesHalf);
	}

	public static Integer[][] join_tours(Integer[][] tour1, Integer[][] tour2) {

		// [0,1,2,3] [
		// 0 1 3 2 0 //0 1 3 2 // 1 3 2 0 // 3 2 0 1 // 2 0 1 3
		Integer[][] segment1 = tour1.clone();
		Integer[][] segment2 = tour2.clone();
		List<Integer[][]> segments1 = new ArrayList<Integer[][]>();
		List<Integer[][]> segments2 = new ArrayList<Integer[][]>();

		leftRotate(segment1, segment1.length, segment1.length, segments1);
		leftRotate(segment2, segment2.length, segment2.length, segments2);


		System.out.println("Segments 1");
		for (int i = 0; i < segments1.size(); i++) {
			for (int j = 0; j < segments1.get(i).length; j++) {
				System.out.print(segments1.get(i)[j][2] + " ");

			}
			System.out.println();
		}
		System.out.println();
		System.out.println("Segments 2");
		for (int i = 0; i < segments2.size(); i++) {
			for (int j = 0; j < segments2.get(i).length; j++) {
				System.out.print(segments2.get(i)[j][2] + " ");

			}
			System.out.println();
		}

		// 1 3 2 0
		// 3 2 0 1
		// 1 3 2 0
		// 4 6 8 7
		//

		return shortest_tour_segments(segments1, segments2);

	}

	private static int GetDistance(int x1, int x2, int y1, int y2) {
		return (int) (Math.sqrt((Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2))));
	}

	public static Integer[][] shortest_tour(List<Integer[]> tours, int[][] citiesHalf) {
		List<Integer> tourLengths = new ArrayList<Integer>();
		for (int i = 0; i < tours.size(); i++) {
			int tourLength = 0;
			for (int j = 0; j < tours.get(i).length - 1; j++) {
				tourLength += GetDistance(citiesHalf[tours.get(i)[j]][0], citiesHalf[tours.get(i)[j + 1]][0],
						citiesHalf[tours.get(i)[j]][1], citiesHalf[tours.get(i)[j + 1]][1]);
			}// 0 1 2 3 4
			tourLengths.add(tourLength);
		}

		int shortestTourIndex = tourLengths.indexOf(Collections.min(tourLengths));

		System.out.println("Shortest Tour");
		for (int j = 0; j < tours.get(shortestTourIndex).length; j++) {
			System.out.print(tours.get(shortestTourIndex)[j] + " ");
		}
		System.out.println();
		Integer[][] shortestTour = new Integer[citiesHalf.length][3];
		for(int i = 0;i < shortestTour.length;i++) {
			shortestTour[i][0] = citiesHalf[tours.get(shortestTourIndex)[i]][0];
			shortestTour[i][1] = citiesHalf[tours.get(shortestTourIndex)[i]][1];
			shortestTour[i][2] = tours.get(shortestTourIndex)[i];
			System.out.println(shortestTour[i][0] + " " + shortestTour[i][1] + " " + shortestTour[i][2]);
		}


		return shortestTour;
	}

	public static Integer[][] shortest_tour_segments(List<Integer[][]> segments1, List<Integer[][]> segments2) {

		//segments1
		//0 1 2 3 4
		//0 2 3 1 4
		//0 3 4 1 2

		//segments2
		//0 1 2 3 4
		//0 2 3 1 4
		//0 3 4 1 2
		//4 3 2 1 0
		//4 1 3 2 0
		//2 1 4 3 0
		int segmentsTwoLength = segments2.size();
		for (int i = 0; i < segmentsTwoLength; i++) {
			Integer[][] segment = segments2.get(i).clone();
			List<Integer[]> segmentList = Arrays.asList(segment);
			Collections.reverse(segmentList);
			Integer[][] reverseSegment = segmentList.toArray(new Integer[0][0]);
			segments2.add(reverseSegment);
		}
		List<Integer> tour1Lengths = new ArrayList<Integer>();
		List<Integer> tour2Lengths = new ArrayList<Integer>();

		for (int i = 0; i < segments1.size(); i++) {
			int tourLength = 0;
			for(int j = 0; j < segments1.get(i).length-1;j++) {
				tourLength += GetDistance(segments1.get(i)[j][0],segments1.get(i)[j+1][0]
						,segments1.get(i)[j][1],segments1.get(i)[j+1][1]);
			}
			tour1Lengths.add(tourLength);
		}
		for (int i = 0; i < segments2.size(); i++) {
			int tourLength = 0;
			for (int j = 0; j < segments2.get(i).length - 1; j++) {
				tourLength += GetDistance(segments2.get(i)[j][0],segments2.get(i)[j+1][0]
						,segments2.get(i)[j][1],segments2.get(i)[j+1][1]);
			}
			tour2Lengths.add(tourLength);
		}

		Integer[][] tourCombinations = new Integer[tour1Lengths.size()*tour2Lengths.size()][3]; //(i,j,length)
		int index = 0;
		for (int i = 0; i < tour1Lengths.size(); i++) {
			for(int j = 0; j < tour2Lengths.size();j++) {
				Integer[] tourCombination = {i,j,tour1Lengths.get(i)+tour2Lengths.get(j)};
				tourCombinations[index] = tourCombination;
				index++;
			}
		}

		for (int i = 0; i < tourCombinations.length; i++) {
			for (int j = i + 1; j < tourCombinations.length; j++) {
				if (tourCombinations[i][2] > tourCombinations[j][2]) {
					Integer[] tempNum = tourCombinations[i];
					tourCombinations[i] = tourCombinations[j];
					tourCombinations[j] = tempNum;
				}
			}
		}

		/*System.out.println("Tour Combinations");
		for (int i = 0; i < tourCombinations.length; i++) {
			System.out.println(tourCombinations[i][2]);
		}*/

		System.out.println("Tour1");
		for(int i = 0; i<segments1.get(tourCombinations[0][0]).length;i++) {
			System.out.print(segments1.get(tourCombinations[0][0])[i][2] + " ");
		}
		System.out.println();

		System.out.println("Tour2Before");
		for(int i = 0; i<segments2.get(tourCombinations[0][1]).length;i++) {
			System.out.print(segments2.get(tourCombinations[0][1])[i][2] + " ");
		}
		System.out.println();

		for(int i = 0; i<segments2.get(tourCombinations[0][1]).length;i++) {
			segments2.get(tourCombinations[0][1])[i][2] += segments1.size();
		}
		System.out.println("Tour2After");
		for(int i = 0; i<segments2.get(tourCombinations[0][1]).length;i++) {
			System.out.print(segments2.get(tourCombinations[0][1])[i][2] + " ");
		}

		System.out.println();


		Integer[][] combinedTour = combine(segments1.get(tourCombinations[0][0]),segments2.get(tourCombinations[0][1]));

		System.out.println("combinedTour");
		for(int i = 0; i<combinedTour.length;i++) {
			System.out.print(combinedTour[i][2] + " ");
		}
		System.out.println();
		System.out.println(tourCombinations[0][2]);

		return combinedTour;
	}

	public static Integer[][] combine(Integer[][] a, Integer[][] b){
		int length = a.length + b.length;
		Integer[][] result = new Integer[length][3];
		System.arraycopy(a, 0, result, 0, a.length);
		System.arraycopy(b, 0, result, a.length, b.length);
		return result;
	}

	public static Cities split_cities(int[][] cities) {

		int xSort[][] = cities.clone();
		int ySort[][] = cities.clone();

		for (int i = 0; i < xSort.length; i++) {
			for (int j = i + 1; j < xSort.length; j++) {
				if (xSort[i][0] > xSort[j][0]) {
					int tempNum[] = xSort[i];
					xSort[i] = xSort[j];
					xSort[j] = tempNum;
				}
			}
		}
		for (int i = 0; i < ySort.length; i++) {
			for (int j = i + 1; j < ySort.length; j++) {
				if (ySort[i][1] > ySort[j][1]) {
					int tempNum[] = ySort[i];
					ySort[i] = ySort[j];
					ySort[j] = tempNum;
				}
			}
		}
		int widLeng = xSort[xSort.length - 1][0] - xSort[0][0];
		int tallLeng = ySort[ySort.length - 1][1] - ySort[0][1];
		Cities citiesObj = new Cities();

		if (widLeng >= tallLeng) {
			// xSort
			int[][] firstHalf = Arrays.copyOfRange(xSort, 0, xSort.length / 2);
			int[][] secondHalf = Arrays.copyOfRange(xSort, xSort.length / 2, xSort.length);
			citiesObj.firstHalf = firstHalf;
			citiesObj.secondHalf = secondHalf;

			// 1 2 3 4 5 6
		} else {
			//ySort
			int[][] firstHalf = Arrays.copyOfRange(ySort, 0, ySort.length / 2);
			int[][] secondHalf = Arrays.copyOfRange(ySort, ySort.length / 2, ySort.length);
			citiesObj.firstHalf = firstHalf;
			citiesObj.secondHalf = secondHalf;

		}
		System.out.println();
		return citiesObj;
	}

	public static void leftRotate(Integer arr[][], int d, int n, List<Integer[][]> segments) {
		for (int i = 0; i < d; i++) {
			leftRotatebyOne(arr, n);
			Integer[][] segment = arr.clone();
			segments.add(segment);
		}

	}
	// 48 0 - 24 24 - 47

	public static void leftRotatebyOne(Integer arr[][], int n) {
		int i;
		Integer[] temp;
		temp = arr[0];
		for (i = 0; i < n - 1; i++)
			arr[i] = arr[i + 1];
		arr[i] = temp;
	}

	/* utility function to print an array */
	public static void printArray(Integer arr[], int n) {
		for (int i = 0; i < n; i++)
			System.out.print(arr[i] + " ");
		//System.out.println();
	}
}
