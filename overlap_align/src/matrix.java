import java.util.*;
public class matrix {
	private ArrayList<ArrayList<cell>> cells;
	private String type;
	private int lenSx;
	private int lenSy;
	private int gap;
	private int space;
	private int lowest; 
	public matrix(String type, int lenSx, int lenSy, int gap, int space, 
			int lowest){
		this.type = type;
		this.lenSx = lenSx;
		this.lenSy = lenSy;
		this.gap = gap;
		this.space = space;
		this.lowest=lowest;
		cells = new ArrayList<ArrayList<cell>>();
		
		
		for (int i = 0; i <= lenSx; i ++){
			cells.add(new ArrayList<cell>());
			for (int j = 0; j <= lenSy; j++){
				
				if (type.equals("M")){
					//set top left corner to zero
					if (i == 0 && j == 0){
						cells.get(i).add(new cell(type, type, 0));
					} 
					//set the rest of first row and column to negative infinity
					else if (i == 0 ^ j == 0){
						cells.get(i).add(new cell(type, type, this.lowest));
					} else {
						cells.get(i).add(null);
					}
				} else if (type.equals("Ix")){
					//initialize top row to negative infinity
					//since this requires i to be zero AND j to be greater 
					//than zero, the else will be entered the very first time
					if (i == 0 && j > 0){
						cells.get(i).add(new cell(type, type, this.lowest));
					} 
					//initialize first column to 0
					else if (j == 0){
						cells.get(i).add(new cell(type, type, 0));
					} else {
						cells.get(i).add(null);
					}
				} else if (type.equals("Iy")){
					//set first row to correct value
					if (i == 0){
						cells.get(i).add(new cell(type, type, gap+space*j));
					}
					//set first column to negative infinity
					else if (i > 0 && j == 0){
						cells.get(i).add(new cell(type, type, this.lowest));
					} else {
						cells.get(i).add(null);
					}
				}
			}
		}
	}
	public ArrayList<ArrayList<cell>> getCells(){
		return this.cells;
	}
}
