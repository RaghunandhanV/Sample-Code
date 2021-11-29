import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.lang.Math;

class ArrayDetails{
	int[] arr;
	int sum;
	ArrayDetails(int[] array, int sumArray){
		sum = sumArray;
		arr = array;
	}
}

class GetHighestScoreSubstring {
    
	
	static ArrayDetails getMaxScoreSubstring(Integer charArray[])
    {
        int maximumSum = Integer.MIN_VALUE, currentSum = 0, subStringStartIndex = 0, subStringEndIndex = 0, tempIndex = 0;
 
        for (int i = 0; i < charArray.length; i++)
        {
        	currentSum = currentSum + charArray[i];
 
            if (maximumSum < currentSum)
            {
            	maximumSum = currentSum;
            	subStringStartIndex = tempIndex;
            	subStringEndIndex = i;
            }
 
            if (currentSum < 0)
            {
            	currentSum = 0;
            	tempIndex = i + 1;
            }
        }
       int[] returnValue = {subStringStartIndex, subStringEndIndex};
       return new ArrayDetails(returnValue, maximumSum);
    }
	
    static Character[] calcMaxScoreSubstr(char X[], char Y[], int charWeights[])
    {
     
    	HashMap<Character, Integer> characterWeights = new HashMap<Character, Integer>();
    	for (int i = 65; i<=91; i++) {
	    	if(i != 91) {
	    		characterWeights.put((char)i,charWeights[i-65]);	
	    	}else {
	    		characterWeights.put((char)229,charWeights[i-65]);
	    	}	
		}
    	
    	int m = X.length;
    	int n = Y.length;
    	
        int subStringScore[][] = new int[m][n];
       
        int highestScore = 0;
        Character [] highestScoreSubStr = null;
        for (int slice = 1; slice <= X.length+Y.length-1; slice++) {
        	
        	if (slice <= Y.length) {
        		int i = 0;
        		ArrayList<Integer> weightList1 = new ArrayList<Integer>();
        		ArrayList<Character> subStringList1 = new ArrayList<Character>();
        		for (int j = Y.length-slice; j < n && i < m; j++ ) {
        			if (X[i] == Y[j])
                    	subStringScore[i][j] = characterWeights.get(X[i]);
                    else 
                    	subStringScore[i][j] = characterWeights.get((char)229);
        			weightList1.add(subStringScore[i][j]);
        			subStringList1.add(X[i]); 
        			i = i+1;
        		}
        		Integer[] weightArray1 = new Integer[weightList1.size()];
        		weightArray1 = weightList1.toArray(weightArray1);
        		Character[] subStringArray1 = new Character[subStringList1.size()];
        		subStringArray1 = subStringList1.toArray(subStringArray1);
        		ArrayDetails currentArr = getMaxScoreSubstring(weightArray1);
        		Character[] currentSubStr = Arrays.copyOfRange(subStringArray1,currentArr.arr[0],currentArr.arr[1]+1);
        		if (currentArr.sum > highestScore) {
        			highestScore = currentArr.sum;
        			highestScoreSubStr = currentSubStr;
        		}
        	} else {
        		int j = 0;
        		ArrayList<Integer> weightList2 = new ArrayList<Integer>();
        		ArrayList<Character> subStringList2 = new ArrayList<Character>();
        		for (int i = slice-n; i<m && j<n; i++) {
        			if (X[i] == Y[j])
                    	subStringScore[i][j] = characterWeights.get(X[i]);
                    else 
                    	subStringScore[i][j] = characterWeights.get((char)229);
        			weightList2.add(subStringScore[i][j]);
        			subStringList2.add(X[i]); 
        			j = j+1;
        		}
        		Integer[] weightArray2 = new Integer[weightList2.size()];
        		weightArray2 = weightList2.toArray(weightArray2);
        		Character[] subStringArray2 = new Character[subStringList2.size()];
        		subStringArray2 = subStringList2.toArray(subStringArray2);
        		ArrayDetails currentArr = getMaxScoreSubstring(weightArray2);
        		Character[] currentSubStr = Arrays.copyOfRange(subStringArray2,currentArr.arr[0],currentArr.arr[1]+1);
        		if (currentArr.sum > highestScore) {
        			highestScore = currentArr.sum;
        			highestScoreSubStr = currentSubStr;
        		}
        	}
        }
        
        
       return highestScoreSubStr;
    }
 

    public static void main(String[] args)
    {
 
        long progStart = System.nanoTime();
    	String X = "ASLASDLKASJDLLKJL";
        String Y = "GASTDYASDBKJ";
 
		
    	// int charFrequency[] =  {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,-10};
    	int charFrequency[] =  {8,2,4,3,11,2,2,3,7,1,1,5,3,7,7,3,1,7,6,7,4,1,1,1,2,1,-10};
     
       Character[] finalSubStrArray =  calcMaxScoreSubstr(X.toUpperCase().toCharArray(),Y.toUpperCase().toCharArray(),charFrequency);
       System.out.println("The common substring with the best score among " + X.toUpperCase() + " and " + Y.toUpperCase() + " is " + Arrays.toString(finalSubStrArray));
  
       long progEnd = System.nanoTime();
       System.out.println("Time Elapsed: " +(progEnd - progStart)/Math.pow(10,9) + " seconds");
       
       
    }
}