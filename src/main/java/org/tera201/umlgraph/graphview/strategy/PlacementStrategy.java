package org.tera201.umlgraph.graphview.strategy;

import org.tera201.umlgraph.graph.Graph;
import org.tera201.umlgraph.graph.Vertex;
import org.tera201.umlgraph.graphview.vertices.UMLVertexNode;

import java.util.*;

public class PlacementStrategy {

    private double xSpacing = 100;
    private double ySpacing = 100;
    private final Set<UMLVertexNode> visited = new HashSet<>();
    Set<UMLVertexNode> visitedBalanced = new HashSet<>();
    Set<UMLVertexNode> visitedWidth = new HashSet<>();

    public <V, E> void place(double width, double height, Graph<V, E> graph, Map<Vertex<V>, UMLVertexNode<V>> vertexNodes) {
        visited.clear();
        visitedBalanced.clear();
        visitedWidth.clear();
        List<UMLVertexNode<V>> connectedComponents = new ArrayList<>();
        List<UMLVertexNode<V>> aloneComponents = new ArrayList<>();
        Map<UMLVertexNode<V>, Collection<UMLVertexNode<V>>> neighborsMap = new HashMap<>();

        for (Vertex<V> vertex : vertexNodes.keySet()) {
            if (vertex.getParent() == null && vertex.getChildren() != null) {
                connectedComponents.add(vertexNodes.get(vertex));
            } else if (vertex.getParent() == null && vertex.getChildren() == null) {
                aloneComponents.add(vertexNodes.get(vertex));
            }
        }

        double xOffset = 0;
        double yOffset = 0;
        final int[] yOffset2 = {0};
        final int[] xOffset2 = {10};
        connectedComponents.forEach(it -> neighborsMap.put(it, neighbors(it, vertexNodes)));
        neighborsMap.entrySet().stream().filter(it -> it.getValue().isEmpty()).forEach(it -> {
            placeAloneVertex(it.getKey(), xOffset2[0], yOffset2[0]);
            xOffset2[0] = (xOffset2[0] + it.getKey().getWidth() + 10) < width * 1.2 ? (int) (xOffset2[0] + it.getKey().getWidth() + 10) : 10;
            yOffset2[0] += xOffset2[0] == 10? 50 : 0;
        });

        for (UMLVertexNode<V> node : aloneComponents) {
            placeAloneVertex(node, width / 2 + xOffset, yOffset2[0]);
//            yOffset[0] += 50;
        }
        yOffset2[0] += 50;

        neighborsMap.entrySet().stream().filter(it -> !it.getValue().isEmpty()).forEach(it -> {
            placeTree(it.getKey(), width / 2 + xOffset, yOffset2[0], vertexNodes);
            yOffset2[0] += 200 * it.getKey().getUnderlyingVertex().getDepth();
        });
    }

    private <V> void placeAloneVertex(UMLVertexNode<V> root, double x, double y) {
        visited.add(root);
        root.setPosition(x, y);
    }


    private <V> void placeTree(UMLVertexNode<V> root, double x, double y, Map<Vertex<V>, UMLVertexNode<V>> vertexNodes) {
        if (visited.contains(root)) {
            return;
        }
        visited.add(root);
        root.setPosition(x, y);

        Collection<UMLVertexNode<V>> neighbors = neighbors(root, vertexNodes);

        int numChildren = neighbors.size();
        int yVarSpacing = 1;
        double xOffset = -((numChildren - 1) * xSpacing) / 2;

        for (UMLVertexNode<V> neighbor : neighbors) {
            placeTree(neighbor, x + xOffset, y + ySpacing + ySpacing * yVarSpacing * 0.4, vertexNodes);
            xOffset += xSpacing;
            yVarSpacing = (yVarSpacing + 1) % 3;
        }
    }

    private <V> Collection<UMLVertexNode<V>> neighbors(UMLVertexNode<V> graphVertex, Map<Vertex<V>, UMLVertexNode<V>> vertexNodes) {
        List<? extends Vertex<?>> vertexTreeList;
        Collection<UMLVertexNode<V>> neighbors = new ArrayList<>();
        vertexTreeList = ((Vertex<?>) graphVertex.getUnderlyingVertex()).getChildren();
        vertexTreeList.forEach(it -> neighbors.add(vertexNodes.get(it)));

        return neighbors;
    }
}