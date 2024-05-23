import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import javax.swing.*;
import java.awt.*;
import org.jgrapht.ext.JGraphXAdapter;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.swing.mxGraphComponent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class JGraphTVisualization {

    public static void visualizeGraph(Graph<String, DefaultEdge> graph) {
        JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<>(graph);

        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        JFrame frame = new JFrame();
        frame.getContentPane().add(new mxGraphComponent(graphAdapter));
        frame.setTitle("Graph Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Graph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

        String filePath = "C:\\Users\\Hp\\OneDrive\\Masaüstü\\keepcoding in java\\ProjeDeneme\\RelatedFollowers.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(": ");
                String username = parts[1].trim();

                graph.addVertex(username);

                while ((line = reader.readLine()) != null && line.startsWith("Related Followers Username: ")) {
                    String follower = line.substring("Related Followers Username: ".length()).trim();
                    graph.addVertex(follower);
                    graph.addEdge(username, follower);
                }
            }

            visualizeGraph(graph);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
