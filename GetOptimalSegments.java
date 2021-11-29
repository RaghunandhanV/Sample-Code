import java.util.Scanner;
import java.util.Random;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GetOptimalSegments{

	static class dataPoint implements Comparable<dataPoint>{
		int x_coord;
		int y_coord;
		dataPoint(int x, int y){
			x_coord = x;
			y_coord = y;
		}
		//This is to override the default toString() method of Java to enable human-readable printing of objects.
		@Override
  		public String toString() {
    		return "(" + x_coord + "," + y_coord + ")";
  		}

		@Override
		public int compareTo(dataPoint d){
			return this.x_coord - d.x_coord;
		}  
	}

	static class optimalSegment{
		double errorScore;
		int segmentStartIndex;
		optimalSegment(double score, int index){
			errorScore = score;
			segmentStartIndex = index;
		}
		@Override
  		public String toString() {
    		return "(" + errorScore + "," + segmentStartIndex + ")";
  		}
	}
	
	
	public static dataPoint[] generateRandomDataPoints(int datapointCount, int datapointLimit){
		dataPoint[] dataSet = new dataPoint[datapointCount];
		for(int i = 0;i<datapointCount;i++){
			Random RandomObj = new Random();
			dataPoint point = new dataPoint(RandomObj.nextInt(datapointLimit), RandomObj.nextInt(datapointLimit)) ;
			dataSet[i] = point;
		}
		return dataSet;
	}

	public static double calculateSumOfSquaresError(int startIndex, int endIndex, dataPoint[] data){
		double sum_x_coord = 0;
		double sum_y_coord = 0;
		double sum_xy_coord = 0;
		double sum_x_coord_sq = 0;
		for (int i = startIndex; i <= endIndex; i++){
			sum_x_coord = sum_x_coord + data[i].x_coord;
			sum_y_coord = sum_y_coord + data[i].y_coord;
			sum_xy_coord = sum_xy_coord + (data[i].x_coord * data[i].y_coord);
			sum_x_coord_sq = sum_x_coord_sq + Math.pow(data[i].x_coord, 2);
		}
		int n = (endIndex - startIndex + 1);
		double linearFitSlope = (n*sum_xy_coord) - (sum_x_coord*sum_y_coord)/((n*sum_x_coord_sq) - Math.pow(sum_x_coord,2));
		double linearFitIntercept = (sum_y_coord - linearFitSlope*sum_x_coord)/n;
		double sumOfSquaresError = 0;
		for (int i = startIndex; i <= endIndex; i++){
			sumOfSquaresError = sumOfSquaresError + Math.pow((data[i].y_coord - (linearFitSlope*data[i].x_coord) - linearFitIntercept),2);
		}
		if (Double.isNaN(sumOfSquaresError)){
			return 0;
		} else {
			return sumOfSquaresError;
		}
	}

	public static optimalSegment getMinErrorSegment(Double a[],optimalSegment[] o, int segmentPenalty) {
		optimalSegment optCurrent = new optimalSegment(0, 0);
		if(o.length == 0){
			optCurrent.errorScore = a[0]+segmentPenalty;
			optCurrent.segmentStartIndex = 0;
		}else{
			double minVal = a[0]+segmentPenalty;
			int minIndex = 0;
			for (int i=1; i<a.length; i++){
				if((a[i]+ segmentPenalty + o[i-1].errorScore) < minVal){
					minVal = a[i]+ segmentPenalty + o[i-1].errorScore;
					minIndex = i;
				}
			}
			optCurrent.errorScore = minVal;
			optCurrent.segmentStartIndex = minIndex;
		}
		return optCurrent;
	}
	
	public static optimalSegment[] prepareSSEDataStruc(dataPoint[] data){
		ArrayList<ArrayList> SSEMap = new ArrayList<ArrayList>(data.length);
		ArrayList<ArrayList> segmentErrors = new ArrayList<ArrayList>(data.length);
		optimalSegment[] OPT;
		OPT = new optimalSegment[data.length];
		for (int i = 0; i < data.length; i++) {
			ArrayList mapRow1 = new ArrayList<Double>();
			ArrayList mapRow2 = new ArrayList<Double>();
			SSEMap.add(mapRow1);
			segmentErrors.add(mapRow2);
		}
		for(int j = 0; j < data.length; j++){
			for (int i = 0; i <= j; i++) {
				SSEMap.get(j).add(i, calculateSumOfSquaresError(i, j, data));
			}
			Double[][] tempSSE = SSEMap.stream().map(list -> list.toArray(new Double[0])).toArray(Double[][]::new);
			OPT[j] = getMinErrorSegment(tempSSE[j],OPT,10);
		}
		return OPT;
	}

	public static void printSegments(optimalSegment[] opt, dataPoint[] data){
		int limit = data.length-1;
		int i = opt[limit].segmentStartIndex;	
		int count = 1;
		while(i >=0){
			ArrayList<dataPoint> segment = new ArrayList<dataPoint>();
			for (int j = i; j<= limit; j++){
				segment.add(data[j]);
			}
			System.out.println("Points in Segment " + count + " are: " + Arrays.toString(segment.toArray()));
			limit = i-1;
			if(i>0){
				i = opt[i-1].segmentStartIndex;
				count = count+1;		
			} else{
				i = -1;
			}
		}
		System.out.println("Thus, the data can be divided into " + count + " segments.");
	}

	public static void main(String[] args) {
		System.out.println("Please Enter the count, the maximum value of the data points and the segment penalty");
		Scanner userInputCount = new Scanner(System.in);  // Create a Scanner object
		Scanner userInputLimit = new Scanner(System.in);
		long progStart = System.nanoTime();
		dataPoint[] finalData = generateRandomDataPoints(userInputCount.nextInt(), userInputLimit.nextInt());
		Arrays.sort(finalData);
		System.out.println("Final Data is: " + Arrays.toString(finalData));
		System.out.println("OPT Data is: " + Arrays.toString(prepareSSEDataStruc(finalData)));
		printSegments(prepareSSEDataStruc(finalData), finalData);
  
       long progEnd = System.nanoTime();
       System.out.println("Time Elapsed: " +(progEnd - progStart)/Math.pow(10,9) + " seconds");
       
	}
}