import java.util.*;
import java.io.*;

public class predict_exons {

	private static final String nucs = "acgt";
	private static double [] intronEmissions = new double [4];
	private static double [] exonEmissions = new double [4];
	private static double [] intronTransitions = new double [2];
	private static double [] exonTransitions = new double [3];
	private static double [][] matrix;
	private static int [][] pointers;
	private static double min = Double.NEGATIVE_INFINITY;

	public static void main(String[] args) {

		try{
			File trainingFile = new File (args[0]);
			File testFile = new File (args[1]);
			Scanner trainScanner = new Scanner (trainingFile);
			Scanner testScanner = new Scanner(testFile);

			double [] transitionInfo = {0, 0, 0, 0};
			//0: number Intron - Intron transititions 
			//1: number Intron - Exon transititions 
			//2: number Exon - Exon transititions 
			//3: number Exon - Intron transititions

			double [] iChars = new double [4]; //[0,1,2,3] -> [a,c,g,t]
			double [] eChars = new double [4];
			double totalIntronChars = 0;
			double totalExonChars = 0;
			double totalIntronTransitions = 0;
			double totalExonTransitions = 0;

			while (trainScanner.hasNextLine()){
				double [][] trainingData = train(trainScanner.nextLine());
				//transitions
				for (int i = 0; i < trainingData[0].length; i ++){
					transitionInfo[i] = transitionInfo[i] + trainingData[0][i];
				}
				//intron characters
				for (int i = 0; i < trainingData[1].length; i ++){
					iChars[i] = iChars[i] + trainingData[1][i];
				}
				//exon characters
				for (int i = 0; i < trainingData[2].length; i ++){
					eChars[i] = eChars[i] + trainingData[2][i];
				}
				//add extra place for exon-end transitions
				totalExonTransitions += 1;
			}
			for (int i = 0; i < iChars.length; i ++){
				totalIntronChars += iChars[i];
				totalExonChars += eChars[i];
			}
			for (int j = 0; j < 2; j ++){
				totalIntronTransitions += transitionInfo[j];
				totalExonTransitions += transitionInfo[j+2];
			}
			//estimate emission and transition probabilities
			for (int i = 0; i < iChars.length; i ++){
				intronEmissions[i] = Math.log10((iChars[i]+1)/(totalIntronChars+4));
				exonEmissions[i] = Math.log10((eChars[i]+1)/(totalExonChars+4));
			}
			for (int j = 0; j < 2; j ++){
				intronTransitions[j] = Math.log10((transitionInfo[j]+1)/(totalIntronTransitions+2));
				exonTransitions[j] = Math.log10((transitionInfo[j+2]+1)/(totalExonTransitions+3));
			}
			//Viterbi Algorithm
			while (testScanner.hasNextLine()){
				String sequence = testScanner.nextLine();

				//create matrix for this sequence
				matrix = new double [2][sequence.length()];
				pointers = new int [2][sequence.length()];
				//initialize to -infinity
				for (int i = 0; i < matrix.length; i ++){
					for (int j = 0; j < sequence.length(); j ++){
						matrix[i][j] = min;
					}
				}

				//Last character must be emitted by an exon (start state = 1)
				viterbi(sequence, 1, sequence.length()-1);

				//traceback
				double [] temp = new double [sequence.length()-1];
				//start with an exon
				int start = pointers[1][sequence.length()-1];
				//condense path into 1d array
				for (int i = temp.length -1; i > -1; i --){
					temp[i] = pointers[start][i+1];
					start = pointers [start][i+1];
				}
				for (int i = 0; i < temp.length; i ++){
					if (temp[i] == 0){
						System.out.print(Character.toLowerCase(sequence.charAt(i)));
					} else {
						System.out.print(Character.toUpperCase(sequence.charAt(i)));
					}
				}
				System.out.println(Character.toUpperCase(sequence.charAt(sequence.length()-1)));
			}
		} catch (FileNotFoundException f){
			System.out.println("Invalid File");
		}
	}

	private static double viterbi(String seq, int currState, int index){
		//Remember previous calculated entries
		if (matrix[currState][index] > min){
			return matrix[currState][index];
		}
		double eprob = -1;
		int probIndex = nucs.indexOf((seq.charAt(index)));

		//base case
		if (index < 1){
			if (currState == 0){
				//emit from intron state
				matrix[currState][index] = intronEmissions[probIndex];
			} else {
				//emit from exon state
				matrix[currState][index] = exonEmissions[probIndex];
			}
			return matrix[currState][index];
		}

		if (currState == 0){
			//probability of emitting this character from intron state
			eprob = intronEmissions[probIndex];
			//came from self (intron)
			double self = intronTransitions[0]+viterbi(seq, 0, index-1);
			//came from other (exon)
			double other = exonTransitions[1]+viterbi(seq, 1, index-1);
			//pick the highest to figure out where you came from
			double choice = Math.max(self, other);
			//set the pointer for this entry to the appropriate state
			if (choice == self) pointers[currState][index] = currState;
			else pointers [currState][index] = 1;
			//set this entry
			matrix[currState][index] = eprob + choice;
		} else {
			//emit from exon state
			eprob = exonEmissions[probIndex];
			//came from self (exon)
			double self = exonTransitions[0]+viterbi(seq, 1, index-1);
			//came from other (intron)
			double other = intronTransitions[1]+viterbi(seq, 0, index-1);
			double choice = Math.max(self, other);
			//set the pointer for this entry to the appropriate state
			if (choice == self) pointers[currState][index] = currState;
			else pointers [currState][index] = 0;
			//set this entry
			matrix[currState][index] = eprob + choice;
		}
		return matrix[currState][index];
	}

	private static double [][] train(String data){
		//determine number of state switches and chars in each state

		double [] transitionInfo = {0, 0, 0, 0};
		//0: number Intron - Intron transititions 
		//1: number Intron - Exon transititions 
		//2: number Exon - Exon transititions 
		//3: number Exon - Intron transititions

		double [] iChars = new double [4]; //[0,1,2,3] -> [a,c,g,t]
		double [] eChars = new double [4];

		int iState = 0; //introns are lower case
		int eState = 1; //exons are upper case

		char currChar = data.charAt(0);
		int currState = stateNum(currChar); //0 -> intron, 1 -> exon

		//do first character because indexing in for loop
		if (currState == iState){
			for (int j = 0; j < iChars.length; j ++){
				if (Character.toLowerCase(currChar) == nucs.charAt(j)){
					iChars[j] = iChars[j] + 1;
				}
			}
		} else if (currState == eState){
			for (int j = 0; j < eChars.length; j ++){
				if (Character.toLowerCase(currChar) == nucs.charAt(j)){
					eChars[j] = eChars[j] + 1;
				}
			}
		}

		char prevChar = currChar;
		int prevState = 0;

		for (int i = 1; i < data.length(); i ++){
			prevChar = data.charAt(i-1);
			prevState = stateNum(prevChar);
			currChar = data.charAt(i);
			currState = stateNum(currChar);
			//detect transitions
			if (currState==prevState){
				if (currState == iState){
					transitionInfo[0] = transitionInfo[0] + 1; //II
				} else {
					transitionInfo[2] = transitionInfo[2] + 1; //EE
				}
			} else {
				if (currState == iState){
					transitionInfo[3] = transitionInfo[3] + 1; //EI
				} else {
					transitionInfo[1] = transitionInfo[1] + 1; //IE
				}
			}
			//set number of each type of character detected in each state
			if (currState == iState){
				for (int j = 0; j < iChars.length; j ++){
					if (Character.toLowerCase(currChar) == nucs.charAt(j)){
						iChars[j] = iChars[j] + 1;
					}
				}
			} else if (currState == eState){
				for (int j = 0; j < eChars.length; j ++){
					if (Character.toLowerCase(currChar) == nucs.charAt(j)){
						eChars[j] = eChars[j] + 1;
					}
				}
			}
		}
		double [][] trainingData = new double[3][];
		trainingData[0] = transitionInfo;
		trainingData[1] = iChars;
		trainingData[2] = eChars;
		return trainingData;
	}

	private static int stateNum(char c){
		if (Character.isUpperCase(c)){
			return 1;
		}
		return 0;
	}
}
//in case printing is needed in the future....

//prints the values in the matrix
/*for (int i = 0; i < matrix.length; i ++){
for (int j = 0; j < sequence.length(); j ++){

	double d = matrix[i][j]*10000;
	if (i == 0 && d < -1*10000 && j < 5) System.out.print(" ");
	System.out.print(Math.round(d)/10000.0 + "|");
}
System.out.println();
}
//print the emission and transition values
 System.out.print("i emissions: ");
 for (int i = 0; i < exonEmissions.length; i ++){
	 double d = intronEmissions[i]*10000;
	 System.out.print(Math.round(d)/10000.0+ " ");
 }
 System.out.println();
 System.out.print("e emissions: ");
 for (int i = 0; i < exonEmissions.length; i ++){
	 double d = exonEmissions[i]*10000;
	 System.out.print(Math.round(d)/10000.0+ " ");
 }
 System.out.println();
 System.out.print("transitions: ");
 for (int i = 0; i < 2; i ++){
	 double d = intronTransitions[i]*10000;
	 System.out.print(Math.round(d)/10000.0 + " ");
 }
 for (int i = 0; i < 2; i ++){
	 double d = exonTransitions[i]*10000;
	 System.out.print(Math.round(d)/10000.0 + " ");
 }
 System.out.println();
 
 //print the pointers matrix
  	for (int i = 0; i < pointers.length; i ++){
		for (int j = 0; j < pointers[i].length; j ++){
			System.out.print("    ");
				System.out.print((int)pointers[i][j]);
				System.out.print("   ");
			}
		System.out.println();
	}
  */