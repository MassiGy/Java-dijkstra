package org.example;

import org.graphstream.algorithm.Toolkit;
import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.BreadthFirstIterator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDGS;
import org.graphstream.stream.file.FileSourceEdge;
import org.graphstream.stream.file.FileSourceFactory;
import org.graphstream.ui.view.Viewer;
import org.graphstream.algorithm.Toolkit;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class Main {


    public static Graph randomGen(int nodeCount, double averageDegree) {
        Graph graph = new SingleGraph("Random");
        Generator gen = new RandomGenerator((int) Math.round(averageDegree));
        gen.addSink(graph);
        gen.begin();

        for (int i = 0; i < nodeCount; i++) {
            gen.nextEvents();
        }

        gen.end();
        return graph;
    }

    public static Graph randomPreferencielGraph(int nodeCount, double averageDegree) {
        Graph graph = new SingleGraph("Preferential");
        Generator gen = new BarabasiAlbertGenerator((int) Math.round(averageDegree));
        gen.addSink(graph);
        gen.begin();

        for (int i = 0; i < nodeCount; i++) {
            gen.nextEvents();
        }

        gen.end();
        return graph;
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



    public static void main(String args[]) {





    }
}
