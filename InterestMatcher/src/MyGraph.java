
import java.util.Arrays;

public class MyGraph {

    private MyHashTable<String, MyHashTable<String, Boolean>> adjacencyList;

    public MyGraph() {
        this.adjacencyList = new MyHashTable<>();
    }
    public MyGraph getGraph() {
        return this;
    }
    public void addVertex(String vertex) {
        adjacencyList.put(vertex, new MyHashTable<>());
    }

    
    public void addEdge(String sourceVertex, String targetVertex) {
        adjacencyList.getOrDefault(sourceVertex, new MyHashTable<>()).put(targetVertex, true);
        adjacencyList.getOrDefault(targetVertex, new MyHashTable<>()).put(sourceVertex, true);
    }

    public MyHashTable<String, Boolean> getNeighbors(String vertex) {
        return adjacencyList.getOrDefault(vertex, new MyHashTable<>());
    }

    @Override
public String toString() {
    StringBuilder result = new StringBuilder();
    Object[] vertices = adjacencyList.getVertices();
    for (Object vertex : vertices) {
        if (vertex != null) {
            String vertexString = (String) vertex; 
            MyHashTable<String, Boolean> neighbors = adjacencyList.get(vertexString);
            Object[] neighborVertices = neighbors.getVertices();
            result.append(vertexString).append(" -> ").append(Arrays.toString(neighborVertices)).append("\n");
        }
    }
    return result.toString();
}
}
