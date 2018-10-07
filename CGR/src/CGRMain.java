import java.awt.*;
import java.io.IOException;

import javax.swing.*;
public class CGRMain {

	public static void main(String[] args) throws IOException {
		System.out.println("" + args.length + " arguments");
		//no arguments to do the S. Triangle
		if (args.length == 0){
			JFrame f = new JFrame("Chaos Game");
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			SierpinskyTriangleGen p = new SierpinskyTriangleGen();
			p.setBackground(Color.WHITE);
			f.add(p);
			f.setSize(500,500);
			f.setVisible(true);
		}
		//This is pretty much useless, I think I wrote it as a test
		else if (args.length == 1){
			JFrame f = new JFrame(args[0]);
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			ProteinChaosGame p = new ProteinChaosGame(args[0]);
			p.setBackground(Color.WHITE);
			f.add(p);
			f.setSize(500,500);
			f.setVisible(true);
		}
		//first argument = filename, second = nuc or AA
		else if (args.length == 2){
			CGROptions test = new CGROptions (args[0]);
			if (test.getFileName().equals("1")){
				test.runTriangle();
			} else if (args[1].equals("nuc")){
				test.runNuc();
			} else if (args[1].equals("AA")){
				test.runAminoAcid();
			}
			else System.out.println("Usage is: Filename protein or: Filename AA");
		}
	}

}
