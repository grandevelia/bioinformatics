
public class cell {
	
	private String parentMatrix;
	private String currentMatrix;
	private Integer score;
	
	public cell (String currentMatrix, String parentMatrix, Integer score) {
		this.currentMatrix = currentMatrix;
		this.score = score;
		this.parentMatrix = parentMatrix;
	}
	
	public String getParentMatrix (){
		return this.parentMatrix;
	}
	
	public String getCurrentMatrix(){
		return this.currentMatrix;
	}

	public int getScore(){
		return this.score;
	}

}
