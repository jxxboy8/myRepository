package CloneGraph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Hashtable;

public class CloneGraph {
	
	public UndirectedGraphNode cloneGraph(UndirectedGraphNode node) {
        // Note: The Solution object is instantiated only once and is reused by each test case.
        if(node == null)
        	return null;
		Hashtable<UndirectedGraphNode, UndirectedGraphNode> table = new Hashtable<UndirectedGraphNode, UndirectedGraphNode>();
        LinkedList<UndirectedGraphNode> list = new LinkedList<UndirectedGraphNode>();
        UndirectedGraphNode newNode = new UndirectedGraphNode(node.label);
        table.put(node, newNode);
        list.add(node);
        while(!list.isEmpty()){
        	UndirectedGraphNode first = list.removeFirst();
        	for(int i=0; i<first.neighbors.size(); i++){
        		UndirectedGraphNode neighbor = first.neighbors.get(i);
        		if(table.containsKey(neighbor))
        			table.get(first).neighbors.add(table.get(neighbor));
        		else{
        			UndirectedGraphNode newNeighbor = new UndirectedGraphNode(neighbor.label);
        			table.put(neighbor, newNeighbor);
        			list.add(neighbor);
        			table.get(first).neighbors.add(newNeighbor);
        		}
        	}
        }
        
        return newNode;
    }

}
