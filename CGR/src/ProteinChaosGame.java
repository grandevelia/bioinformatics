import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ProteinChaosGame extends JPanel{

	private String fileName;

	public ProteinChaosGame(String s){
		this.fileName = s;
	}

	public void paintComponent (Graphics g){
		try {
			File myFile = new File (fileName);
			Scanner fileScanner = new Scanner (myFile);

			super.paintComponent(g); 
			this.setBackground(Color.WHITE); 

			g.setColor(Color.BLUE);

			//set up boundary points

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

			int pointSize = 2;

			g.fillOval(x1, y1, pointSize, pointSize);
			g.fillOval(x2, y2, pointSize, pointSize);
			g.fillOval(x3, y3, pointSize, pointSize);
			g.fillOval(x4, y4, pointSize, pointSize);

			//first point
			int xR = 0;
			int yR = 0;

			g.fillOval(xR, yR, pointSize, pointSize);

			g.setColor(Color.BLACK);
			fileScanner.nextLine();
			while (fileScanner.hasNextLine()){
			//for (int i = 0; i < 50000; i ++){
				String line = fileScanner.nextLine();
				String [] tokens = line.split("");
				for (int j = 0; j < tokens.length; j++) {
					String nextBase = tokens[j];
					if (nextBase.equalsIgnoreCase("C")) {
						xR = (x1 + xR) / 2;
						yR = (y1 + yR) / 2;
						g.fillOval(xR, yR, pointSize, pointSize);
					}
					if (nextBase.equalsIgnoreCase("G")) {
						xR = (x2 + xR) / 2;
						yR = (y2 + yR) / 2;
						g.fillOval(xR, yR, pointSize, pointSize);
					}
					if (nextBase.equalsIgnoreCase("T") || nextBase.equals("U")) {
						xR = (x3 + xR) / 2;
						yR = (y3 + yR) / 2;
						g.fillOval(xR, yR, pointSize, pointSize);
					}
					if (nextBase.equalsIgnoreCase("A")) {
						xR = (x4 + xR) / 2;
						yR = (y4 + yR) / 2;
						g.fillOval(xR, yR, pointSize, pointSize);
					}
				}
			}
		} catch (FileNotFoundException f){
			f.printStackTrace();
		}

	}
}