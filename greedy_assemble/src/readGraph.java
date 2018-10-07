import java.util.*;

public class readGraph {
	private ArrayList<graphNode<String>> nodes;
	private ArrayList<weightedEdge<String>> edges;
	private int numNodes;
	
	public readGraph(){
		this.nodes = new ArrayList<graphNode<String>>();
		this.edges = new ArrayList<weightedEdge<String>>();
		this.numNodes = 0;
	}
	
	public void addNode (graphNode<String> node){
		nodes.add(node);
		this.numNodes ++;
	}
	
	public void addEdge(weightedEdge<String> edge){
		edge.getOrigin().addOutgoingNode(edge.getDest());
		edge.getDest().addIncomingNode(edge.getOrigin());
		edges.add(edge);
	}
	
	public void removeEdge(weightedEdge<String> edge){
		edge.getOrigin().removeOutgoingNode(edge.getDest());
		edge.getDest().removeIncomingNode(edge.getOrigin());
		this.edges.remove(edge);
	}
	public ArrayList<graphNode<String>> getNodes (){
		return this.nodes;
	}
	
	public ArrayList<weightedEdge<String>> getEdges (){
		return this.edges;
	}
	
	public weightedEdge<String> getEdge(graphNode<String>origin, 
			graphNode<String>dest){
		for (int i = 0; i < edges.size(); i ++){
			weightedEdge<String>curEdge = edges.get(i);
			graphNode<String>curOri = curEdge.getOrigin();
			graphNode<String>curDest = curEdge.getDest();
			if (curOri.equals(origin) && curDest.equals(dest)){
				return curEdge;
			}
		}
		return null;
	}
	
	public String traceGraph(graphNode<String> startNode){
		String output = startNode.getRead();
		return traceGraphHelper(startNode, output);
	}
	
	private String traceGraphHelper(graphNode<String> startNode, String output){
		if (startNode.getSuffixes().size() < 1){
			return output;
		}
		//Since each node should be visited exactly once, each node's first 
		//suffix is the next node in the assembled string (the list of 
		//suffixes should definitely not be greater than 1 in length
		graphNode<String> nextNode = startNode.getSuffixes().get(0);
		weightedEdge<String> nextEdge = this.getEdge(startNode, nextNode);
		output += nextNode.getRead().substring(-1*nextEdge.getWeight());
		//System.out.println(output);
		return traceGraphHelper(nextNode, output);
		
		
	}
	
	public boolean isConnected(){
		//The graph is connected if every node has indegree at least 1
		//In this case they should be exactly one
		for (int i = 0; i < nodes.size(); i ++){
			if (nodes.get(i).indegree() == 0){
				return false;
			} else if (nodes.get(i).indegree() > 1){
				//System.out.println("ERROR ERROR ERROR ERRRRRRORRRRRRR");
				return true;
			}
		}
		return true;
		
	}
	
	public boolean isCyclic(graphNode<String> startNode){
		//start by setting all nodes to undiscovered
		for (int i = 0; i < numNodes; i ++){
			this.nodes.get(i).setDiscovered(false);
			this.nodes.get(i).setOnCycleStack(false);
		}
		return isCyclicHelper(startNode);
	}
	
	private boolean isCyclicHelper (graphNode<String> startNode){
		//set this node to discovered and put it on the "stack" 
		startNode.setDiscovered(true);
		startNode.setOnCycleStack(true);
		
		//DFS: if a node is still on the "stack" when it is found, there
		//must be a cycle
		ArrayList<graphNode<String>> suffs = startNode.getSuffixes();
		for (int i = 0; i < suffs.size(); i ++) {
			graphNode<String> nextNode = suffs.get(i);
			if (!nextNode.getDiscovered()){
				boolean cycle = this.isCyclicHelper(nextNode);
				if (cycle){
					return true;
				}
			} else if (nextNode.getOnCycleStack())
				return true;
		}
		startNode.setOnCycleStack(false);
		return false;
	}
}
