package org.tera201.umlgraph.graphview.strategy;

import org.tera201.umlgraph.graph.Graph;
import org.tera201.umlgraph.graph.Vertex;
import org.tera201.umlgraph.graph.VertexTree;
import org.tera201.umlgraph.graph.VertexTreeNode;
import org.tera201.umlgraph.graphview.vertices.GraphVertex;
import org.tera201.umlgraph.graphview.vertices.GraphVertexPaneNode;

import java.util.*;

public class DigraphTreePlacementStrategy implements  PlacementStrategy{

    private double xSpacing = 100;
    private double ySpacing = 100;
    private Set<GraphVertex> visited = new HashSet<>();
    Set<GraphVertex> visitedBalanced = new HashSet<>();
    Set<GraphVertex> visitedWidth = new HashSet<>();

    @Override
    public <V, E> void place(double width, double height, Graph<V, E> graph, Map<Vertex<V>, GraphVertexPaneNode<V>> vertexNodes) {
        visited.clear();
        visitedBalanced.clear();
        visitedWidth.clear();
        List<GraphVertexPaneNode<V>> connectedComponents = new ArrayList<>();

        for (Vertex<V> vertex : vertexNodes.keySet()) {
            if (vertex instanceof VertexTreeNode<V>) {
                if (((VertexTreeNode<V>) vertex).getParent() == null) {
                    connectedComponents.add(vertexNodes.get(vertex));
                }
            }
        }

        double xOffset = 0;
        double yOffset = 0;

        for (GraphVertexPaneNode<V> root : connectedComponents) {
            placeTree(root, graph, width / 2 + xOffset, yOffset, vertexNodes);
            if (root.getUnderlyingVertex() instanceof  VertexTreeNode<?>) yOffset += 200 * ((VertexTreeNode<?>) root.getUnderlyingVertex()).getDepth();
            yOffset += 50;
        }
    }

    private <V, E> void placeTree(GraphVertex root, Graph<V, E> graph, double x, double y, Map<Vertex<V>, GraphVertexPaneNode<V>> vertexNodes) {
        if (visited.contains(root)) {
            return;
        }
        visited.add(root);
        root.setPosition(x, y);

        Collection<GraphVertex> neighbors = neighbors(root, graph, vertexNodes);

        int numChildren = neighbors.size();
        int yVarSpacing = 1;
        double xOffset = -((numChildren - 1) * xSpacing) / 2;

        for (GraphVertex neighbor : neighbors) {
            placeTree(neighbor, graph, x + xOffset, y + ySpacing + ySpacing * yVarSpacing * 0.4, vertexNodes);
            xOffset += xSpacing;
            yVarSpacing = (yVarSpacing + 1) % 3;
        }
    }

    private <V, E> Collection<GraphVertex> neighbors(GraphVertex graphVertex, Graph<V, E> graph, Map<Vertex<V>, GraphVertexPaneNode<V>> vertexNodes) {
        List<? extends VertexTree<?>> vertexTreeList;
        Collection<GraphVertex> neighbors = new ArrayList<>();
        if (graphVertex.getUnderlyingVertex() instanceof VertexTreeNode<?>) {
            vertexTreeList = ((VertexTreeNode<?>) graphVertex.getUnderlyingVertex()).getChilds();
            vertexTreeList.forEach(it -> neighbors.add(vertexNodes.get(it)));
        }

        return neighbors;
    }
}