import java.text.DecimalFormat;
import java.util.*;
import java.io.*;

public class exon_accuracy {
	public static void main(String[]args){
		try{
			File realFile = new File (args[0]);
			File outFile = new File (args[1]);
			Scanner realScanner = new Scanner(realFile);
			Scanner outScanner = new Scanner(outFile);
			
			//Assume files have exactly matching lines: Store data at same index
			ArrayList <String> realLines = new ArrayList <String>();
			ArrayList <String> outLines = new ArrayList <String>();
			
			double numPredExons = 0;
			double totalPos = 0;
			double numTrueExons = 0;
			double numCorrectExons = 0;
			double numCorrectPos = 0;
			
			//Put the data into lists so it's easier to work with
			char realChar = ' ';
			char predChar = ' ';
			boolean realUp = false;
			boolean predUp = false;
			while (realScanner.hasNextLine()){
				realLines.add(realScanner.nextLine());
			}
			while (outScanner.hasNextLine()){
				outLines.add(outScanner.nextLine());
			}
			//get information
			for (int i = 0; i < realLines.size(); i ++){
				String realCurr = realLines.get(i);
				String predCurr = outLines.get(i);
				for (int j = 0; j < realCurr.length(); j ++){
					realChar = realCurr.charAt(j);
					predChar = predCurr.charAt(j);
					if (Character.isUpperCase(realChar)){
						numTrueExons ++;
						realUp = true;
					} else {
						realUp = false;
					}
					if (Character.isUpperCase(predChar)){
						numPredExons ++;
						predUp = true;
					} else {
						predUp = false;
					}
					if (realUp == predUp){
						numCorrectPos += 2;
						if (realUp){
							numCorrectExons ++;
						}
					}
					totalPos += 2;
				}
			}
			 DecimalFormat df = new DecimalFormat("0.000");
			double accuracy = numCorrectPos/totalPos*1000.0;
			double recall = numCorrectExons/numTrueExons;
			double precision = numCorrectExons/numPredExons;
			System.out.println(df.format(Math.round(accuracy)/1000.0));
			System.out.println(df.format(recall));
			System.out.println(df.format(precision));
		} catch (FileNotFoundException e){
			System.out.println("Invalid File");
		}
	}
}
