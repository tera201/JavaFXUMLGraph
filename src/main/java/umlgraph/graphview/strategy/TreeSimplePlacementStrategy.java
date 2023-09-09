package umlgraph.graphview.strategy;

import umlgraph.graph.DigraphEdgeList;
import umlgraph.graph.Graph;
import umlgraph.graph.Vertex;
import umlgraph.graphview.vertices.GraphVertex;
import umlgraph.graph.Edge;
import umlgraph.graphview.vertices.GraphVertexPaneNode;

import java.util.*;

public class TreeSimplePlacementStrategy implements  PlacementStrategy{

    private double x = 100;
    private double y = 50;
    private double xSpacing = 40;
    private double ySpacing = 40;
    private Set<GraphVertex> visited = new HashSet<>();

    @Override
    public <V, E> void place(double width, double height, Graph<V, E> graph, Map<Vertex<V>, GraphVertexPaneNode<V>> vertexNodes) {
        // Начнем с какой-то "корневой" вершины
        GraphVertex<V> root = vertexNodes.values().stream().findFirst().get();
        placeTree(root, graph, 0, 0, vertexNodes);
    }

    private <V, E> void placeTree(GraphVertex<V> graphVertex, Graph<V, E> graph, double xOffset, double yOffset, Map<Vertex<V>, GraphVertexPaneNode<V>> vertexNodes) {
        if (visited.contains(graphVertex)) {
            return;
        }

        visited.add(graphVertex);

        graphVertex.setPosition(x + xOffset, y + yOffset);
        y += ySpacing;

        Collection<? extends GraphVertex> neighborVertices = neighbors(graphVertex, graph, vertexNodes);
        double localXOffset = 0;

        for (GraphVertex neighbor : neighborVertices) {
            placeTree(neighbor, graph, x + localXOffset, y, vertexNodes);
            localXOffset += xSpacing;
        }
    }

    private <V, E> Collection<GraphVertex> neighbors(GraphVertex<V> graphVertex, Graph<V, E> graph, Map<Vertex<V>, GraphVertexPaneNode<V>> vertexNodes) {
        System.out.println("neighbors: " + graphVertex.getUnderlyingVertex());
        Collection<Edge<E, V>> incidentEdges = graph.incidentEdges(graphVertex.getUnderlyingVertex());
        if (graph instanceof DigraphEdgeList) incidentEdges.addAll(((DigraphEdgeList<V, E>) graph).outboundEdges(graphVertex.getUnderlyingVertex()));
        Collection<GraphVertex> neighbors = new ArrayList<>();

        for (Edge<E, V> edge : incidentEdges) {
            System.out.println(graphVertex.getUnderlyingVertex() + " " + edge);
            Vertex<V> oppositeVertex = graph.opposite(graphVertex.getUnderlyingVertex(), edge);
            GraphVertexPaneNode<V> oppositeGraphVertex = vertexNodes.get(oppositeVertex);
            if (oppositeGraphVertex != null) {
                neighbors.add(oppositeGraphVertex);
            }
        }

        return neighbors;
    }

    // Реализуйте этот метод, чтобы найти "корневую" вершину в вашем графе
    private <V> GraphVertex<V> getRoot(Collection<? extends GraphVertex<V>> vertices) {
        return vertices.stream().findFirst().get();
    }
}