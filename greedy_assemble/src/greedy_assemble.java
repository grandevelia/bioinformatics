import java.util.*;
import java.io.*;

public class greedy_assemble {

	public static void main(String[] args) {
		PriorityQueue<weightedEdge<String>>edgeQueue = new PriorityQueue<weightedEdge<String>>();
		readGraph myReadGraph = new readGraph();
		try{
			File inputFile = new File (args[0]);
			Scanner fileScanner = new Scanner (inputFile);
			while (fileScanner.hasNextLine()){
				myReadGraph.addNode(new graphNode<String>(fileScanner.nextLine()));
			}
			ArrayList<graphNode<String>> myNodes = myReadGraph.getNodes();
			for (int i = 0; i < myNodes.size(); i ++){
				for (int j = 0; j < myNodes.size(); j ++){
					if (i != j){
						edgeQueue.add(new weightedEdge<String>(myNodes.get(i), 
								myNodes.get(j)));
					}
				}
			}
			boolean connected = false;
			while (!connected && edgeQueue.peek() != null){
				weightedEdge<String> tempEdge = edgeQueue.poll();
				//if the next edge in the queue comes from a node with no 
			    //outgoing edges and goes to a node with no incoming edges
				//add it
				if (tempEdge.getOrigin().outdegree() == 0 
						&& tempEdge.getDest().indegree() == 0){
					
					myReadGraph.addEdge(tempEdge);
					//If adding this edge created a cycle, remove it
					if (myReadGraph.isCyclic(tempEdge.getDest())){
						myReadGraph.removeEdge(tempEdge);
					}
				}
				//Check if the graph is now connected
				connected = myReadGraph.isConnected();
			}
			
			//The starting node has no incoming edges. Find it, and trace the
			//graph to find the output string
			for (int i = 0; i < myReadGraph.getNodes().size(); i ++){
				graphNode<String>startNode = myReadGraph.getNodes().get(i);
				if(startNode.getPrefixes().size() == 0){
					System.out.println(myReadGraph.traceGraph(startNode));
				}
			}
			
		} catch (InputMismatchException e){
			System.out.println("Please only enter a file");
		} catch (ArrayIndexOutOfBoundsException e){
			System.out.println("Please provide a read file");
		} catch (FileNotFoundException f){
			System.out.println("Invalid File");
		}


	}

}
