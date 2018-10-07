public class weightedEdge<E> implements Comparable<weightedEdge<E>> {
	private int weight;
	private graphNode<E> startNode;
	private graphNode<E> endNode;

	public weightedEdge (graphNode<E> origin, graphNode<E> dest){
		this.weight = computeWeight(origin, dest)*-1;
		this.startNode = origin;
		this.endNode = dest;
	}

	public int getWeight(){
		return this.weight;
	}
	
	public graphNode<E> getOrigin(){
		return this.startNode;
	}
	
	public graphNode<E> getDest(){
		return this.endNode;
	}
	
	private int computeWeight(graphNode<E> origin, graphNode<E> dest){
		String originRead = origin.getRead();
		String destRead = dest.getRead();
		return this.computeWeightHelper (originRead, destRead, 0);
	}

	private  int computeWeightHelper (String s1, String s2, int i){
		if (s1.length() > s2.length()){
			s1 = s1.substring(s1.length()-s2.length());
		}
		if (s2.length() > s1.length()){
			s2 = s2.substring(0,s1.length());
		}
		if (i == s1.length()){
			return 0;
		}
		if (s1.substring(i).equals(s2.substring(0,s2.length()-i))){
			return s1.length()-i;
		}
		return computeWeightHelper (s1, s2, i+1);
	}
	
	public int compareTo(weightedEdge<E> otherEdge) {
		int returner = this.getWeight()-otherEdge.getWeight();
		if (returner != 0){
			return returner;
		}
		//tie-breaking
		String otherDestRead = otherEdge.getDest().getRead();
		
		return this.endNode.getRead().compareTo(otherDestRead);
	}
}

