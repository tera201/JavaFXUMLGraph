package org.tera201.umlgraph.graphview.strategy;

import javafx.util.Pair;
import org.tera201.umlgraph.graph.DigraphEdgeList;
import org.tera201.umlgraph.graph.Edge;
import org.tera201.umlgraph.graph.Graph;
import org.tera201.umlgraph.graph.Vertex;
import org.tera201.umlgraph.graphview.vertices.GraphVertex;
import org.tera201.umlgraph.graphview.vertices.GraphVertexPaneNode;

import java.util.*;

public class TreeCentralPlacementStrategy implements  PlacementStrategy{

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
        List<Set<GraphVertexPaneNode<V>>> connectedComponents = findConnectedComponents(graph, vertexNodes);

        double xOffset = 0;
        double yOffset = 0;
        double xLastOffset = 1;

        for (Set<GraphVertexPaneNode<V>> component : connectedComponents) {
            Pair<GraphVertexPaneNode<V>, Integer> rootPair = findBalancedRoot(component, graph, vertexNodes);
            GraphVertex root = rootPair.getKey();
            placeTree(root, graph, width / 2 + xOffset, yOffset, vertexNodes);
            xOffset = 200 * Math.max(rootPair.getValue(), 1) * xLastOffset;
            yOffset += 200;
            xLastOffset = Math.max(rootPair.getValue(), 1);
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
        Collection<Edge<E, V>> incidentEdges = graph.incidentEdges(graphVertex.getUnderlyingVertex());
        if (graph instanceof DigraphEdgeList) incidentEdges.addAll(((DigraphEdgeList<V, E>) graph).outboundEdges(graphVertex.getUnderlyingVertex()));
        Collection<GraphVertex> neighbors = new ArrayList<>();

        for (Edge<E, V> edge : incidentEdges) {
            Vertex<V> oppositeVertex = graph.opposite(graphVertex.getUnderlyingVertex(), edge);
            GraphVertexPaneNode<V> oppositeGraphVertex = vertexNodes.get(oppositeVertex);
            if (oppositeGraphVertex != null) {
                neighbors.add(oppositeGraphVertex);
            }
        }

        return neighbors;
    }

    public <V, E> List<Set<GraphVertexPaneNode<V>>> findConnectedComponents(Graph<V, E> graph, Map<Vertex<V>, GraphVertexPaneNode<V>> vertexNodes) {
        List<Set<GraphVertexPaneNode<V>>> connectedComponents = new ArrayList<>();
        Set<GraphVertexPaneNode<V>> visited = new HashSet<>();

        for (GraphVertexPaneNode<V> node : vertexNodes.values()) {
            if (!visited.contains(node)) {
                Set<GraphVertexPaneNode<V>> component = new HashSet<>();
                dfs(graph, node, visited, component, vertexNodes);
                connectedComponents.add(component);
            }
        }

        return connectedComponents;
    }

    private <V, E> void dfs(Graph<V, E> graph, GraphVertexPaneNode<V> current, Set<GraphVertexPaneNode<V>> visited, Set<GraphVertexPaneNode<V>> component, Map<Vertex<V>, GraphVertexPaneNode<V>> vertexNodes) {
        visited.add(current);
        component.add(current);

        Collection<Edge<E, V>> incidentEdges = graph.incidentEdges(current.getUnderlyingVertex());

        for (Edge<E, V> edge : incidentEdges) {
            Vertex<V> oppositeVertex = graph.opposite(current.getUnderlyingVertex(), edge);
            GraphVertexPaneNode<V> oppositeNode = vertexNodes.get(oppositeVertex);

            if (oppositeNode != null && !visited.contains(oppositeNode)) {
                dfs(graph, oppositeNode, visited, component, vertexNodes);
            }
        }
    }

    public <V, E> Pair<GraphVertexPaneNode<V>, Integer> findBalancedRoot(Set<GraphVertexPaneNode<V>> component, Graph<V, E> graph, Map<Vertex<V>, GraphVertexPaneNode<V>> vertexNodes) {
        int minDifference = Integer.MAX_VALUE;
        int maxWidth = Integer.MIN_VALUE;
        GraphVertexPaneNode<V> balancedRoot = null;

        for (GraphVertexPaneNode<V> vertex : component) {
            int totalDifference = 0;
            int totalWidth = 0;

            for (GraphVertex neighbor : neighbors(vertex, graph, vertexNodes)) {
                int subtreeSize1 = calculateSubtreeSize(neighbor, vertex, graph, vertexNodes);
                int subtreeSize2 = component.size() - subtreeSize1 - 1;
                totalWidth += calculateWidthSize(neighbor);
                totalDifference += Math.abs(subtreeSize1 - subtreeSize2);
            }

            if (totalDifference < minDifference) {
                minDifference = totalDifference;
                balancedRoot = vertex;
            }

            if (totalWidth > maxWidth) maxWidth = totalWidth;
        }

        return new Pair<>(balancedRoot, maxWidth);
    }

    private int calculateWidthSize(GraphVertex vertex) {
        if (visitedBalanced.contains(vertex)) {
            return 0;
        }
        visitedBalanced.add(vertex);
        return 1;
    }

    private <V,E> int calculateSubtreeSize(GraphVertex vertex, GraphVertex parent, Graph<V, E> graph, Map<Vertex<V>, GraphVertexPaneNode<V>> vertexNodes) {
        if (visitedBalanced.contains(vertex)) {
            return 0;
        }
        visitedBalanced.add(vertex);

        int size = 1;
        for (GraphVertex neighbor : neighbors(vertex, graph, vertexNodes)) {
            if (!neighbor.equals(parent)) {
                size += calculateSubtreeSize(neighbor, vertex, graph, vertexNodes);
            }
        }
        return size;
    }
}