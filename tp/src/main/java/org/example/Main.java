package org.example;

import org.graphstream.algorithm.generator.BaseGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSourceEdge;
import org.graphstream.ui.view.Viewer;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;


public class Main {


    public static Graph randomGen(int nodeCount, double averageDegree) {
        Graph graph = new SingleGraph("Random");
        Generator gen = new RandomGenerator((int) Math.round(averageDegree));
        gen.addSink(graph);
        ((BaseGenerator) gen).addEdgeAttribute("weight", 0, 100);

        gen.begin();

        for (int i = 0; i < nodeCount; i++) {
            gen.nextEvents();
        }

        gen.end();
        return graph;
    }


    public static void Dijkstra(Graph graph, Node src) {

        HashMap<Node, Double> dists = new HashMap<>();
        HashMap<Node, Node> prevs = new HashMap<>();

        graph.nodes().forEach(n -> {
            dists.put(n, Double.MAX_VALUE);
            prevs.put(n, null);
        });
        dists.put(src, 0.0);

        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(
                Comparator.comparingDouble(n -> dists.get(n))
        );
        priorityQueue.add(src);


        while (!priorityQueue.isEmpty()) {
            Node currentNode = priorityQueue.poll();
            List<Node> neighborNodes = currentNode.neighborNodes().toList();

            for (Node v : neighborNodes) {
                double d;

                d = dists.get(currentNode) + (double) currentNode.getEdgeBetween(v).getAttribute("weight");
                if (d < dists.get(v)) {
                    dists.put(v, d);
                    prevs.put(v, currentNode);

                    // update v's priority with the new distance
                    priorityQueue.remove(v);
                    priorityQueue.add(v);
                }
            }


        }

        System.out.println(dists);
        System.out.println(prevs);
    }


    public static Graph graphFromFileSource(String absFilePath) {
        Graph graph = new DefaultGraph("g");
        FileSourceEdge fs = new FileSourceEdge();
        fs.addSink(graph);


        try {
            fs.readAll(absFilePath);
        } catch (IOException e) {
        } finally {
            fs.removeSink(graph);
        }

        return graph;
    }

    public static void displayGraph(Graph graph) {
        System.setProperty("org.graphstream.ui", "swing");
        graph.edges().forEach(e -> e.setAttribute("ui.label", (""+ e.getAttribute("weight")).substring(0, 6)));
        graph.nodes().forEach(n -> n.setAttribute("ui.label", "NodeId@"+n.getId()));
        Viewer viewer = graph.display();
        //viewer.disableAutoLayout();
    }


    public static void main(String[] args) {
        Graph graph = randomGen(4, 2);
        displayGraph(graph);

      

    }
}
