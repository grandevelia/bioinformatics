import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.*;

public class CGROptions  {
	private String fileName;

	public CGROptions (String s){
		fileName = s;
	}

	public void runNuc () throws IOException{
		try {
			File myFile = new File (fileName);
			Scanner fileScanner = new Scanner (myFile);
			//Dimensions of image
			int width = 500;
			int height = 500;
			BufferedImage image = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g = (Graphics2D) image.getGraphics();
			g.setBackground(Color.WHITE);
			g.clearRect(0, 0, width, height);
			Color dotColor = Color.BLUE;

			//(A)
			int x1 = 0;
			int y1 = 0;
			//(T/U)
			int x2 = 500;
			int y2 = 0;
			//(G)
			int x3 = 500;
			int y3 = 500;
			//(C)
			int x4 = 0;
			int y4 = 500;

			//first point
			int xR = 0;
			int yR = 0;

			fileScanner.nextLine();
			while (fileScanner.hasNextLine()){
				String line = fileScanner.nextLine();
				String [] tokens = line.split("");
				for (int j = 0; j < tokens.length; j++) {
					String nextBase = tokens[j];
					if (nextBase.equalsIgnoreCase("C")) {
						xR = (x1 + xR) / 2;
						yR = (y1 + yR) / 2;
					}
					if (nextBase.equalsIgnoreCase("G")) {
						xR = (x2 + xR) / 2;
						yR = (y2 + yR) / 2;
					}
					if (nextBase.equalsIgnoreCase("T") || nextBase.equalsIgnoreCase("U")) {
						xR = (x3 + xR) / 2;
						yR = (y3 + yR) / 2;
					}
					if (nextBase.equalsIgnoreCase("A")) {
						xR = (x4 + xR) / 2;
						yR = (y4 + yR) / 2;
					}
					image.setRGB(xR, yR, dotColor.getRGB());
				}
			}
			ImageIO.write(image, "png", new File(fileName + ".png"));
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}
	}
	public void runAminoAcid () throws IOException{
		try {
			File myFile = new File (fileName);
			Scanner fileScanner = new Scanner (myFile);
			//Dimensions of image
			int width = 500;
			int height = 500;
			BufferedImage image = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g = (Graphics2D) image.getGraphics();
			g.setBackground(Color.WHITE);
			g.clearRect(0, 0, width, height);
			Color dotColor = Color.BLUE;

			//Hydrophobic
			int x1 = 0;
			int y1 = 0;
			//Positive
			int x2 = 500;
			int y2 = 0;
			//Negative
			int x3 = 500;
			int y3 = 500;
			//Uncharged Polar 
			int x4 = 0;
			int y4 = 500;
			
			
			String [] nonPolar = new String []{"A", "I", "L", "M", "F", "P", "W", "V"};
			String [] negative = new String [] {"D", "E"};
			String [] positive = new String [] {"R", "H", "K"};
			String [] unchargedPolar = new String [] {"N", "C", "Q", "G", "S", "T", "Y"};
			//first point
			int xR = 0;
			int yR = 0;

			while (fileScanner.hasNextLine()){
				String line = fileScanner.nextLine();
				String [] tokens = line.split("");
				for (int j = 0; j < tokens.length; j++) {
					for (String aminoAcid: nonPolar){
						if (tokens[j].equals(aminoAcid)){
							xR = (x1 + xR) / 2;
							yR = (y1 + yR) / 2;
							System.out.print(aminoAcid);
						}
					}
					for (String aminoAcid: negative){
						if (tokens[j].equals(aminoAcid)){
							xR = (x2 + xR) / 2;
							yR = (y2 + yR) / 2;
							System.out.print(aminoAcid);
						}
					}
					for (String aminoAcid: positive){
						if (tokens[j].equals(aminoAcid)){
							xR = (x3 + xR) / 2;
							yR = (y3 + yR) / 2;
							System.out.print(aminoAcid);
						}
					}
					for (String aminoAcid: unchargedPolar){
						if (tokens[j].equals(aminoAcid)){
							xR = (x4 + xR) / 2;
							yR = (y4 + yR) / 2;
							System.out.print(aminoAcid);
						}
					}
					image.setRGB(xR, yR, dotColor.getRGB());
				}
			}
			ImageIO.write(image, "png", new File("AA_Attractor.png"));
		} catch (FileNotFoundException f){
			f.printStackTrace();
		}
	}
	
	public void runTriangle () throws IOException{
		//Dimensions of image
		int width = 500;
		int height = 500;
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setBackground(Color.WHITE);
		g.clearRect(0, 0, width, height);
		Color dotColor = Color.BLUE;

		//set up triangle
		int x1 = 250;
		int y1 = 0;

		int x2 = 500;
		int y2 = 500;

		int x3 = 0;
		int y3 = 500;

		//first point
		int xR = 0;
		int yR = 0;

		Random r = new Random();
		for (int i = 0; i < 10000; i ++){
			int r1 = r.nextInt(7);
			if (r1 == 0 || r1 == 1){
				xR = (x1+xR)/2;
				yR = (y1+yR)/2;
			}
			if (r1 == 2 || r1 == 3){
				xR = (x2+xR)/2;
				yR = (y2+yR)/2;
			}
			if (r1 == 4 || r1 == 5){
				xR = (x3+xR)/2;
				yR = (y3+yR)/2;
			}
			image.setRGB(xR, yR, dotColor.getRGB());
		}
		ImageIO.write(image, "png", new File("Sierpinski.png"));
	}
	
	public String getFileName (){
		return this.fileName;
	}
}
