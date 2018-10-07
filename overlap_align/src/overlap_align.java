import java.io.*;
import java.util.*;
public class overlap_align {

	public static void main(String[] args) {
		try{
			File inputFile = new File (args[0]);
			Scanner fileScanner = new Scanner(inputFile);
			String sX = fileScanner.nextLine();
			String sY = fileScanner.nextLine();
			int match = Integer.parseInt(args[1]);
			int mismatch = Integer.parseInt(args[2]);
			int gap = Integer.parseInt(args[3]);
			int space = Integer.parseInt(args[4]);

			//kludgey negative infinity
			int lowest=Math.max(sX.length(), sY.length())*Math.min(Math.min
					(Math.min(match,mismatch), gap),space)-1;

			//For printing final alignments
			ArrayList <Character> sXFinal = new ArrayList<Character>();
			for (int i = 0; i < sX.length(); i ++){
				sXFinal.add(sX.charAt(i));
			}
			ArrayList <Character> sYFinal = new ArrayList<Character>();
			for (int j = 0; j < sY.length(); j ++){
				sYFinal.add(sY.charAt(j));
			}

			//initializion happens with matrix creation
			matrix M = new matrix("M", sX.length(), sY.length(), gap, space, lowest);
			matrix Ix = new matrix("Ix", sX.length(), sY.length(), gap, space, lowest);
			matrix Iy = new matrix("Iy", sX.length(), sY.length(), gap, space, lowest);

			ArrayList<ArrayList<cell>> mCells = M.getCells();
			ArrayList<ArrayList<cell>> IxCells = Ix.getCells();
			ArrayList<ArrayList<cell>> IyCells = Iy.getCells();


			int maxEntry = lowest;
			int startIndex = 0;
			cell startCell = new cell(null, null, null);
			int alignmentScore = 0;

			//fill the matrices
			for (int i = 1; i <= sX.length(); i ++){
				for (int j = 1; j <= sY.length(); j ++){
					String parentType = "";
					Integer mMax = 0;
					Integer IxMax = 0;
					Integer IyMax = 0;

					if (sX.charAt(i-1) == sY.charAt(j-1)){
						alignmentScore = match;
					} else {
						alignmentScore = mismatch;
					}

					//Match Matrix Score
					//last char was a match
					int mMatch = mCells.get(i-1).get(j-1).getScore() + alignmentScore;
					//last char in X was aligned to a gap in Y
					int mXGap = IxCells.get(i-1).get(j-1).getScore() + alignmentScore;
					//last char in Y was aligned to a gap 
					int mYGap = IyCells.get(i-1).get(j-1).getScore() + alignmentScore;

					mMax = Math.max(Math.max(mMatch, mXGap), mYGap);

					if (mMax == mMatch){
						parentType = "M";
					} else if (mMax == mXGap){
						parentType = "Ix";
					} else if (mMax == mYGap){
						parentType = "Iy";
					}

					mCells.get(i).set(j, new cell("M", parentType, mMax));

					//Ix matrix scores
					int xMatch = mCells.get(i-1).get(j).getScore() + gap + space;
					int xGap = IxCells.get(i-1).get(j).getScore() + space;

					IxMax = Math.max(xMatch, xGap);
					if (IxMax == xMatch){
						parentType = "M";
					} else if (IxMax == xGap) {
						parentType = "Ix";
					}

					IxCells.get(i).set(j,  new cell("Ix", parentType, IxMax));

					//Gap in Y matrix scores
					int yMatch = mCells.get(i).get(j-1).getScore() + gap + space;
					int yGap = IyCells.get(i).get(j-1).getScore() + space;

					IyMax = Math.max(yMatch, yGap);

					if (IyMax == yMatch){
						parentType = "M";
					} else if (IyMax == yGap) {
						parentType = "Iy";
					}

					IyCells.get(i).set(j,  new cell("Iy", parentType, IyMax));

					//If the last row is being filled, find the highest value
					//in order to start the traceback
					if (i == sX.length()){
						//find the maximum value in the current cell in all matrices
						int currJmax = Math.max(Math.max(mMax, IxMax), IyMax);
						//start the overall maximum at first j
						if (j == 0){
							maxEntry = currJmax;
						}
						//If the value in this cell is higher than the highest
						//value so far, make it the start cell
						if (currJmax >= maxEntry) {
							maxEntry = currJmax;
							startIndex = j;
							if (maxEntry == mMax){
								startCell = mCells.get(i).get(j);
							} else if (maxEntry == IxMax){
								startCell = IxCells.get(i).get(j);
							} else if (maxEntry == IyMax){
								startCell = IyCells.get(i).get(j);
							}
						}
					}
				}
			}
			//Traceback
			String parentMatrix = startCell.getParentMatrix();
			String currentMatrix = startCell.getCurrentMatrix();


			int currJIndex = startIndex;
			int currIIndex = sX.length();
			int numXGaps = 0;
			int numYGaps = 0;

			while (currJIndex > 0) {
				if (currentMatrix == "M") {
					//Since you're in M, the last char must be a match
					if (parentMatrix == "M") {
						//last two chars matched, stay in M
						startCell = mCells.get(currIIndex- 1).get(currJIndex- 1);
					} else if (parentMatrix == "Ix") {
						//this index was match, but the one before it was a char in x
						//aligned to a gap in y.
						//Go diagonally to Ix
						startCell = IxCells.get(currIIndex- 1).get(currJIndex- 1);
					} else if (parentMatrix == "Iy") {
						//etc for Iy
						startCell = IyCells.get(currIIndex- 1).get(currJIndex- 1);
					}
					currIIndex --; 
					currJIndex --;
				} else if (currentMatrix == "Ix") {
					//this index was a char in x aligned to a gap in y (move up)
					if (parentMatrix == "M") {
						//previous index was a match, move up to M
						startCell = mCells.get(currIIndex-1).get(currJIndex);
					} else if (parentMatrix == "Ix") {
						//previous index was a char in x aligned to a gap in y
						//move up to Ix
						startCell = IxCells.get(currIIndex- 1).get(currJIndex);
					} else if (parentMatrix == "Iy") {
						//etc for Iy
						startCell = IyCells.get(currIIndex- 1).get(currJIndex);
					}
					sYFinal.add(currJIndex, '-');
					currIIndex --;
					numYGaps ++;
				} else if (currentMatrix == "Iy") {
					//this index was a char in y aligned to a gap in x (move over)
					if (parentMatrix == "M") {
						//prev index was a match, move over to M
						startCell = mCells.get(currIIndex).get(currJIndex- 1);
					} else if (parentMatrix == "Ix") {
						//prev index was a char in x aligned to a gap in y
						startCell = IxCells.get(currIIndex).get(currJIndex- 1);
					} else if (parentMatrix == "Iy") {
						startCell = IyCells.get(currIIndex).get(currJIndex- 1);
					}
					sXFinal.add(currIIndex, '-');
					currJIndex --;
					numXGaps ++;
				}
				parentMatrix = startCell.getParentMatrix();
				currentMatrix = startCell.getCurrentMatrix();
			}
			//Add appropriate amount of space to front of second output
			int yOffset = numYGaps-numXGaps;
			for (int j = 0; j < sX.length()-(startIndex)-yOffset; j ++){
				sYFinal.add(j, ' ');
			}

			for (int i = 0; i < sXFinal.size(); i ++){
				System.out.print(sXFinal.get(i));
			}
			System.out.println();
			for (int j = 0; j < sYFinal.size(); j ++){
				System.out.print(sYFinal.get(j));
			}
			System.out.print("\n" + maxEntry);
		} catch (FileNotFoundException e){
			System.out.println("invalid file");
		}
	}
}

//Matrix checking...
/*
for (int i = 0; i < mCells.size(); i ++){
	for (int j = 0; j < mCells.get(i).size(); j ++){
		try{
			if (i == 0 && j ==0){
				System.out.print("  ");
			}
			if (i == 0){
				System.out.print(mCells.get(i).get(j).getScore() + " ");
			} else{
				if (mCells.get(i).get(j).getScore() >= 0){
					System.out.print(" ");
				}
				System.out.print(mCells.get(i).get(j).getScore() + "  ");

			}
		} catch (NullPointerException n){
			System.out.print("_");
		}
	}
	System.out.println();
}
System.out.println("\n");
for (int i = 0; i < IxCells.size(); i ++){
	for (int j = 0; j < IxCells.get(i).size(); j ++){
		try{
			if (i == 0 && j ==0){
				System.out.print("");
			}
			if (i == 0){
				System.out.print(IxCells.get(i).get(j).getScore() + " ");
			} else{
				if (IxCells.get(i).get(j).getScore() < -10){
					System.out.print(IxCells.get(i).get(j).getScore() + " ");
				}else {
					System.out.print(IxCells.get(i).get(j).getScore() + " ");
					if (j > 0){
						System.out.print(" ");
					}
				}

			}
		} catch (NullPointerException n){
			System.out.print("_");
		}
	}
	System.out.println();
}

System.out.println("\n");
for (int i = 0; i < IyCells.size(); i ++){
	for (int j = 0; j < IyCells.get(i).size(); j ++){
		try{
			if (i == 0 && j ==0){
				System.out.print("");
			}
			if (i == 0){
				System.out.print(" " + IyCells.get(i).get(j).getScore() + " ");
			} else{
				if (IyCells.get(i).get(j).getScore() < -10){
					System.out.print(IyCells.get(i).get(j).getScore() + " ");
				}else {
					System.out.print(" " + IyCells.get(i).get(j).getScore() + " ");
					if (j > 0){
						//System.out.print(" ");
					}
				}
			}
		} catch (NullPointerException n){
			System.out.print("_");
		}
	}
	System.out.println();
}*/