import java.awt.*;
import javax.swing.*;
import java.util.*;

@SuppressWarnings("serial")
public class SierpinskyTriangleGen extends JPanel{

	public void paintComponent (Graphics g){
		//takes object from graphics class that sets the color etc
		super.paintComponent(g); //initialize stuff
		this.setBackground(Color.WHITE); 
		
		g.setColor(Color.BLUE);
		//set up triangle
		int x1 = 250;
		int y1 = 0;
		
		int x2 = 500;
		int y2 = 500;
		
		int x3 = 0;
		int y3 = 500;
		
		
		int pointSize = 2;
		
		g.fillOval(x1, y1, pointSize, pointSize);
		g.fillOval(x2, y2, pointSize, pointSize);
		g.fillOval(x3, y3, pointSize, pointSize);
		
		//first point
		int xR = 0;
		int yR = 0;
		
		g.fillOval(xR, yR, pointSize, pointSize);
		
		Random r = new Random();
		g.setColor(Color.GREEN);
		for (int i = 0; i < 60000; i ++){
			int r1 = r.nextInt(7);
			if (r1 == 0 || r1 == 1){
				xR = (x1+xR)/2;
				yR = (y1+yR)/2;
				g.fillOval(xR, yR, pointSize, pointSize);
			}
			if (r1 == 2 || r1 == 3){
				xR = (x2+xR)/2;
				yR = (y2+yR)/2;
				g.fillOval(xR, yR, pointSize, pointSize);
			}
			if (r1 == 4 || r1 == 5){
				xR = (x3+xR)/2;
				yR = (y3+yR)/2;
				g.fillOval(xR, yR, pointSize, pointSize);
			}
		}
	}
}