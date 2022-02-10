package jehlenfeldt.assessments.rectangleoperations.resources.shared;

public class Node {
    private final int xCoordinate;
    private final int yCoordinate;


    public Node(Node node) {
        this.xCoordinate = node.getXCoordinate();
        this.yCoordinate = node.getYCoordinate();
    }

    public Node(int xCoordinate, int yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public int getXCoordinate() {
        return xCoordinate;
    }

    public int getYCoordinate() {
        return yCoordinate;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null
                && obj.getClass().equals(Node.class)
                && this.xCoordinate == ((Node) obj).getXCoordinate()
                && this.yCoordinate == ((Node) obj).getYCoordinate();
    }

    @Override
    public String toString() {
        return "(" + this.xCoordinate + ", " + this.yCoordinate + ")";
    }
}
