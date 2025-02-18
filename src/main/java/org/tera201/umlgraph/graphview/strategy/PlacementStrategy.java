package org.tera201.umlgraph.graphview.strategy;

import org.tera201.umlgraph.graph.Digraph;
import org.tera201.umlgraph.graph.Vertex;
import org.tera201.umlgraph.graphview.vertices.GraphVertex;
import org.tera201.umlgraph.graphview.vertices.GraphVertexPaneNode;

import java.util.*;

public class PlacementStrategy {

    private double xSpacing = 100;
    private double ySpacing = 100;
    private Set<GraphVertex> visited = new HashSet<>();
    Set<GraphVertex> visitedBalanced = new HashSet<>();
    Set<GraphVertex> visitedWidth = new HashSet<>();

    public <V, E> void place(double width, double height, Digraph<V, E> graph, Map<Vertex<V>, GraphVertexPaneNode<V>> vertexNodes) {
        visited.clear();
        visitedBalanced.clear();
        visitedWidth.clear();
        List<GraphVertexPaneNode<V>> connectedComponents = new ArrayList<>();
        List<GraphVertexPaneNode<V>> aloneComponents = new ArrayList<>();
        Map<GraphVertexPaneNode<V>, Collection<GraphVertex<V>>> neighborsMap = new HashMap<>();

        for (Vertex<V> vertex : vertexNodes.keySet()) {
            if (vertex.getParent() == null && vertex.getChildren() != null) {
                connectedComponents.add(vertexNodes.get(vertex));
            } else if (vertex.getParent() == null && vertex.getChildren() == null) {
                aloneComponents.add(vertexNodes.get(vertex));
                System.out.println(vertexNodes.get(vertex).getAttachedLabel().getText());
            }
        }

        double xOffset = 0;
        double yOffset = 0;
        final int[] yOffset2 = {0};
        final int[] xOffset2 = {10};
        connectedComponents.forEach(it -> neighborsMap.put(it, neighbors(it, graph, vertexNodes)));
        neighborsMap.entrySet().stream().filter(it -> it.getValue().isEmpty()).forEach(it -> {
            placeAloneVertex(it.getKey(), graph, xOffset2[0], yOffset2[0], vertexNodes);
            xOffset2[0] = (xOffset2[0] + it.getKey().getWidth() + 10) < width * 1.2 ? (int) (xOffset2[0] + it.getKey().getWidth() + 10) : 10;
            yOffset2[0] += xOffset2[0] == 10? 50 : 0;
        });

        for (GraphVertexPaneNode<V> node : aloneComponents) {
            placeAloneVertex(node, graph, width / 2 + xOffset, yOffset2[0], vertexNodes);
//            yOffset[0] += 50;
        }
        yOffset2[0] += 50;

        neighborsMap.entrySet().stream().filter(it -> !it.getValue().isEmpty()).forEach(it -> {
            placeTree(it.getKey(), graph, width / 2 + xOffset, yOffset2[0], vertexNodes);
            yOffset2[0] += 200 * it.getKey().getUnderlyingVertex().getDepth();
        });
    }

    private <V, E> void placeAloneVertex(GraphVertex<V> root, Digraph<V, E> graph, double x, double y, Map<Vertex<V>, GraphVertexPaneNode<V>> vertexNodes) {
        visited.add(root);
        root.setPosition(x, y);
    }


    private <V, E> void placeTree(GraphVertex<V> root, Digraph<V, E> graph, double x, double y, Map<Vertex<V>, GraphVertexPaneNode<V>> vertexNodes) {
        if (visited.contains(root)) {
            return;
        }
        visited.add(root);
        root.setPosition(x, y);

        Collection<GraphVertex<V>> neighbors = neighbors(root, graph, vertexNodes);

        int numChildren = neighbors.size();
        int yVarSpacing = 1;
        double xOffset = -((numChildren - 1) * xSpacing) / 2;

        for (GraphVertex<V> neighbor : neighbors) {
            placeTree(neighbor, graph, x + xOffset, y + ySpacing + ySpacing * yVarSpacing * 0.4, vertexNodes);
            xOffset += xSpacing;
            yVarSpacing = (yVarSpacing + 1) % 3;
        }
    }

    private <V, E> Collection<GraphVertex<V>> neighbors(GraphVertex<V> graphVertex, Digraph<V, E> graph, Map<Vertex<V>, GraphVertexPaneNode<V>> vertexNodes) {
        List<? extends Vertex<?>> vertexTreeList;
        Collection<GraphVertex<V>> neighbors = new ArrayList<>();
        vertexTreeList = ((Vertex<?>) graphVertex.getUnderlyingVertex()).getChildren();
        vertexTreeList.forEach(it -> neighbors.add(vertexNodes.get(it)));

        return neighbors;
    }
}