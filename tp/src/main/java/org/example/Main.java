package org.example;

import org.graphstream.algorithm.Dijkstra;
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

    private static final int maxWeight = 100;
    public static Graph randomGen(int nodeCount, double averageDegree) {
        Graph graph = new SingleGraph("Random");
        Generator gen = new RandomGenerator((int) Math.round(averageDegree));
        gen.addSink(graph);
        ((BaseGenerator) gen).addEdgeAttribute("weight", 0, maxWeight);

        gen.begin();

        for (int i = 0; i < nodeCount; i++) {
            gen.nextEvents();
        }

        gen.end();
        return graph;
    }


    public static void dijkstra(Graph graph, Node src) {

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

        int max = graph.getEdgeCount() * maxWeight;
        dists.forEach((k,v) -> {
            System.out.printf("GRAPH: %s->%s:\t %s \n", src, k, v > max ? "infinity" : ""+v);
        });

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
        Graph graph = randomGen(8, 2);
        displayGraph(graph);
        System.out.println("---------------------------------");
        System.out.println("Calling Our Custom Dijkstra Algo (naive version)");
        System.out.println("---------------------------------");
        dijkstra(graph, graph.getNode(0));


        System.out.println("---------------------------------");
        System.out.println("Calling GraphStream Dijkstra Algo");
        System.out.println("---------------------------------");

        Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.EDGE, null, "weight");
        dijkstra.setSource(graph.getNode(0));

        dijkstra.init(graph);

        dijkstra.compute();

        /*
        System.out.println(dijkstra.getPath(graph.getNode("8")));
        System.out.println(dijkstra.getPathLength(graph.getNode("8")));
        System.out.println(dijkstra.getTreeLength());
         */


        for (Node node : graph)
            System.out.printf("GRAPH: %s->%s:%10.2f%n", dijkstra.getSource(), node,
                    dijkstra.getPathLength(node));

    }
}
