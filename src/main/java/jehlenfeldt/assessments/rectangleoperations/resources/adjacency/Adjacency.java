package jehlenfeldt.assessments.rectangleoperations.resources.adjacency;

import jehlenfeldt.assessments.rectangleoperations.resources.shared.Node;

public class Adjacency {
    private final AdjacencyType adjacencyType;
    private final AdjacencyAxis adjacencyAxis;
    private final Node startNode;
    private final Node endNode;

    public Adjacency(AdjacencyType adjacencyType, AdjacencyAxis adjacencyAxis, Node startNode, Node endNode) {
        this.adjacencyType = adjacencyType;
        this.adjacencyAxis = adjacencyAxis;
        this.startNode = startNode;
        this.endNode = endNode;
    }

    public AdjacencyType getAdjacencyType() {
        return adjacencyType;
    }

    public AdjacencyAxis getAdjacencyAxis() {
        return adjacencyAxis;
    }

    public Node getStartNode() {
        return startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    @Override
    public String toString() {
        return "Adjacency{" +
                "\n\tadjacencyType=" + adjacencyType +
                "\n\tadjacencyAxis=" + adjacencyAxis +
                "\n\tstartValue=" + startNode +
                "\n\tendValue=" + endNode +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        return obj.getClass().equals(Adjacency.class)
                && this.adjacencyType.equals(((Adjacency) obj).adjacencyType)
                && this.adjacencyAxis.equals(((Adjacency) obj).adjacencyAxis)
                && this.startNode.equals(((Adjacency) obj).startNode)
                && this.endNode.equals(((Adjacency) obj).endNode);
    }
}
