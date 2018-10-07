import java.util.ArrayList;
class graphNode<E> {
	private String k_minus_one_mer;
	private ArrayList<graphNode<E>> outNodes;
	private ArrayList<graphNode<E>> inNodes;
	private int indegree, outdegree;
	private boolean discovered;
	private boolean onCycleStack;
	
	public graphNode() {
		this.k_minus_one_mer = null;
		this.outNodes = new ArrayList<graphNode<E>>();
		this.inNodes = new ArrayList<graphNode<E>>();
		this.indegree = this.outdegree = 0;
		this.discovered = false;
		this.onCycleStack = false;
	}
	
	public graphNode(String read) {
		this.k_minus_one_mer = read;
		this.outNodes = new ArrayList<graphNode<E>>();
		this.inNodes = new ArrayList<graphNode<E>>();
		this.indegree = this.outdegree = 0;
	}
	
	public String getRead(){
		return this.k_minus_one_mer;
	}
	
	public ArrayList<graphNode<E>> getSuffixes(){
		return this.outNodes;
	}
	
	public ArrayList<graphNode<E>> getPrefixes(){
		return this.inNodes;
	}
	
	public void setK_minus_one_mer(String k_minus_one_mer){
		this.k_minus_one_mer = k_minus_one_mer;
	}
	
	public void addOutgoingNode (graphNode<E> outNode){
		this.outNodes.add(outNode);
		this.outdegree ++;
	}

	public void addIncomingNode (graphNode<E> inNode){
		this.inNodes.add(inNode);
		this.indegree ++;
	}
	
	public int indegree (){
		return this.indegree;
	}
	
	public int outdegree (){
		return this.outdegree;
	}
	
	public boolean getDiscovered(){
		return this.discovered;
	}
	
	public void setDiscovered(boolean d){
		this.discovered = d;
	}
	
	public boolean getOnCycleStack(){
		return this.onCycleStack;
	}
	
	public void setOnCycleStack(boolean d){
		this.onCycleStack = d;
	}

	public void removeOutgoingNode(graphNode<E> dest) {
		this.outNodes.remove(dest);
		this.outdegree --;
	}

	public void removeIncomingNode(graphNode<E> origin) {
		this.inNodes.remove(origin);
		this.indegree --;
	}
	
	public boolean equals (graphNode<E> compNode){
		if (this.k_minus_one_mer == compNode.getRead()){
			return true;
		}
		return false;
	}
}
