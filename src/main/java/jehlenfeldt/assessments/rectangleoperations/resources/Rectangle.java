package jehlenfeldt.assessments.rectangleoperations.resources;

import jehlenfeldt.assessments.rectangleoperations.resources.adjacency.Adjacency;
import jehlenfeldt.assessments.rectangleoperations.resources.adjacency.AdjacencyAxis;
import jehlenfeldt.assessments.rectangleoperations.resources.adjacency.AdjacencyType;
import jehlenfeldt.assessments.rectangleoperations.resources.exceptions.InvalidDimensionsException;
import jehlenfeldt.assessments.rectangleoperations.resources.intersection.IntersectionType;
import jehlenfeldt.assessments.rectangleoperations.resources.intersection.Intersections;
import jehlenfeldt.assessments.rectangleoperations.resources.shared.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Rectangle {

    private final Node upperLeftCorner;
    private final Node lowerLeftCorner;
    private final Node upperRightCorner;
    private final Node lowerRightCorner;

    /**
     * Generates a rectangle object based on the input of two opposing corners of the rectangle.
     * @param upperLeftCorner The {@link Node} specifying the x and y coordinates of the upper left corner of the rectangle.
     * @param lowerRightCorner The {@link Node} specifying the x and y coordinates of the lower right corner of the rectangle.
     */
    public Rectangle(Node upperLeftCorner, Node lowerRightCorner) throws InvalidDimensionsException {
        validateInput(upperLeftCorner, lowerRightCorner);
        this.upperLeftCorner = upperLeftCorner;
        this.lowerRightCorner = lowerRightCorner;
        this.lowerLeftCorner = calculateLowerLeftCorner();
        this.upperRightCorner = calculateUpperRightCorner();

    }

    private void validateInput(Node upperLeftCorner, Node lowerRightCorner) throws InvalidDimensionsException {
        if (upperLeftCorner.getXCoordinate() >= lowerRightCorner.getXCoordinate()
                || lowerRightCorner.getYCoordinate() >= upperLeftCorner.getYCoordinate()) {
            throw new InvalidDimensionsException();
        }

        if (upperLeftCorner.getXCoordinate() < 0 || upperLeftCorner.getYCoordinate() < 0
        || lowerRightCorner.getXCoordinate() < 0 || lowerRightCorner.getYCoordinate() < 0) {
            throw new InvalidDimensionsException();
        }
    }

    public Node getUpperLeftCorner() {
        return new Node(upperLeftCorner);
    }

    public Node getLowerLeftCorner() {
        return new Node(lowerLeftCorner);
    }

    public Node getUpperRightCorner() {
        return new Node(upperRightCorner);
    }

    public Node getLowerRightCorner() {
        return new Node(lowerRightCorner);
    }

    /**
     * @return Returns the rightmost (largest) x coordinate.
     */
    public int getRightXLine() {
        return this.upperRightCorner.getXCoordinate();
    }

    /**
     * @return Returns the leftmost (smallest) x coordinate.
     */
    public int getLeftXLine() {
        return this.upperLeftCorner.getXCoordinate();
    }

    /**
     * @return Returns the uppermost (largest) y coordinate.
     */
    public int getUpperYLine() {
        return this.upperLeftCorner.getYCoordinate();
    }

    /**
     * @return Returns the lowest (smallest) y coordinate.
     */
    public int getLowerYLine() {
        return this.lowerLeftCorner.getYCoordinate();
    }

    /**
     * Returns a list of all integer nodes defined by the borders of this rectangle.
     *
     * @return Returns a list of all integer {@link Node}s defined by this rectangle.
     */
    public List<Node> getListOfIntegerNodes() {
        List<Node> nodesList = new ArrayList<>();

        //Creating top and bottom nodes
        for (int x = this.getLeftXLine(); x <= this.getRightXLine(); x++) {
            nodesList.add(new Node(x, this.getUpperYLine()));
            nodesList.add(new Node(x, this.getLowerYLine()));
        }

        //Creating left and right nodes
        for (int y = this.getLowerYLine() + 1; y < this.getUpperYLine(); y++) {
            nodesList.add(new Node(this.getLeftXLine(), y));
            nodesList.add(new Node(this.getRightXLine(), y));
        }

        return nodesList;
    }


    /**
     * Identifies any intersections between the lines that make up the provided rectangle and this one.
     *
     * @param secondRectangle: The rectangle to compare against.
     * @return Returns an {@link Intersections} object containing information about any intersections that may have been found.
     */
    public Intersections identifyIntersections(Rectangle secondRectangle) {
        if (this.equals(secondRectangle)) {
            return new Intersections(IntersectionType.ALL_POINTS_INTERSECT, this.getListOfIntegerNodes());
        }

        if (noIntersectionsExist(secondRectangle)) {
            return new Intersections(IntersectionType.NON_INTERSECTING, new ArrayList<>());
        }

        return calculateIntegerOnlyIntersections(secondRectangle);
    }

    /**
     * Determines if all dimensions of this rectangle are contained within the provided rectangle.
     *
     * @param secondRectangle: The rectangle to compare against.
     * @return Returned boolean will be true if this rectangle is fully contained by the provided rectangle.
     */
    public boolean isFullyContainedBy(Rectangle secondRectangle) {
        if (this.equals(secondRectangle)) {
            return false;
        }

        return secondRectangle.getLeftXLine() < this.getLeftXLine()
                && secondRectangle.getRightXLine() > this.getRightXLine()
                && secondRectangle.getLowerYLine() < this.getLowerYLine()
                && secondRectangle.getUpperYLine() > this.getUpperYLine();
    }

    /**
     * Returns a list of adjacencies between this rectangle and the provided rectangle.
     * Adjacencies are defined as the sharing of a common border.
     *
     * @param secondRectangle: The rectangle to compare against.
     * @return Returns a list ({@link List<Adjacency>}) of adjacent borders and their properties.
     */
    public List<Adjacency> findAnyAdjacencyWith(Rectangle secondRectangle) {
        if (noIntersectionsExist(secondRectangle) || this.isFullyContainedBy(secondRectangle) || secondRectangle.isFullyContainedBy(this)) {
            return new ArrayList<>();
        }

        //If both rectangles are the equivalent then proper adjacency exists on all sides.
        if (this.equals(secondRectangle)) {
            return Arrays.asList(new Adjacency(AdjacencyType.PROPER, AdjacencyAxis.X, this.lowerLeftCorner, this.upperLeftCorner),
                    new Adjacency(AdjacencyType.PROPER, AdjacencyAxis.X, this.lowerRightCorner, this.upperRightCorner),
                    new Adjacency(AdjacencyType.PROPER, AdjacencyAxis.Y, this.lowerLeftCorner, this.lowerRightCorner),
                    new Adjacency(AdjacencyType.PROPER, AdjacencyAxis.Y, this.upperLeftCorner, this.upperRightCorner));
        }

        List<Adjacency> adjacencies = checkForXAdjacencies(secondRectangle);
        adjacencies.addAll(checkForYAdjacencies(secondRectangle));
        return adjacencies;
    }

    @Override
    public boolean equals(Object secondRectangle) {
        return  secondRectangle != null
                && secondRectangle.getClass().equals(Rectangle.class)
                && this.upperLeftCorner.equals(((Rectangle) secondRectangle).upperLeftCorner)
                && this.lowerRightCorner.equals(((Rectangle) secondRectangle).lowerRightCorner);
    }


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //~~~~~~~~~~ Private Methods ~~~~~~~~~~//
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    private Node calculateLowerLeftCorner() {
        return new Node(upperLeftCorner.getXCoordinate(), lowerRightCorner.getYCoordinate());
    }

    private Node calculateUpperRightCorner() {
        return new Node(lowerRightCorner.getXCoordinate(), upperLeftCorner.getYCoordinate());
    }

    //~~~~~~~~~~ Intersection Check Methods ~~~~~~~~~~//
    /**
     * This method calculates if there are any intersecting lines between this rectangle and the provided one.
     *
     * @param secondRectangle: The rectangle to compare against.
     * @return Returns an {@link Intersections} object that contains details about any intersections that were found.
     */
    private Intersections calculateIntegerOnlyIntersections(Rectangle secondRectangle) {
        List<Node> secondRectangleNodes = secondRectangle.getListOfIntegerNodes();

        //Filtering the list of nodes in this rectangle to remove any that are not also present in the provided rectangle.
        List<Node> intersectingNodes = this.getListOfIntegerNodes().stream()
                .filter(secondRectangleNodes::contains)
                .collect(Collectors.toList());

        IntersectionType intersectionType = intersectingNodes.isEmpty() ? IntersectionType.NON_INTERSECTING : IntersectionType.NODE;

        return new Intersections(intersectionType, intersectingNodes);
    }

    //~~~~~~~~~~ Adjacency Check Methods ~~~~~~~~~~//
    /**
     * Compares each rectangle's x-lines to determine if any adjacencies exist on the x-axis and if so then create new {@link Adjacency} objects for each one.

     * @param secondRectangle: The rectangle to compare against.
     * @return Returns a list of {@link Adjacency} objects for each y-axis adjacency detected.
     */
    private List<Adjacency> checkForXAdjacencies(Rectangle secondRectangle) {
        List<Adjacency> adjacencies = new ArrayList<>();

        //If no x-axis adjacencies are possible based on the location of the x-lines then don't perform any further calculations and return an empty list.
        if (xLinesDoNotAllowAdjacencies(secondRectangle)) {
            return adjacencies;
        }

        //If any x-line in this object shares its value with an x-line from the provided object then an adjacency exists.
        if (this.getLeftXLine() == secondRectangle.getLeftXLine()) {
            if (this.getRightXLine() == secondRectangle.getRightXLine()) {
                adjacencies.add(determineXLineAdjacencyParameters(secondRectangle, this.upperLeftCorner.getXCoordinate()));
                adjacencies.add(determineXLineAdjacencyParameters(secondRectangle, this.upperRightCorner.getXCoordinate()));

            } else {
                adjacencies.add(determineXLineAdjacencyParameters(secondRectangle, this.upperLeftCorner.getXCoordinate()));

            }
        } else if (this.getRightXLine() == secondRectangle.getRightXLine()) {
            adjacencies.add(determineXLineAdjacencyParameters(secondRectangle, this.upperRightCorner.getXCoordinate()));

        } else {
            if (this.getLeftXLine() == secondRectangle.getRightXLine()) {
                adjacencies.add(determineXLineAdjacencyParameters(secondRectangle, this.upperLeftCorner.getXCoordinate()));
            } else if(this.getRightXLine() == secondRectangle.getLeftXLine()) {
                adjacencies.add(determineXLineAdjacencyParameters(secondRectangle, this.upperRightCorner.getXCoordinate()));
            }
        }

        return adjacencies;
    }

    /**
     * Compares each rectangle's y-lines to determine if any adjacencies exist on the y-axis and if so then create new {@link Adjacency} objects for each one.

     * @param secondRectangle: The rectangle to compare against.
     * @return Returns a list of {@link Adjacency} objects for each y-axis adjacency detected.
     */
    private List<Adjacency> checkForYAdjacencies(Rectangle secondRectangle) {
        List<Adjacency> adjacencies = new ArrayList<>();

        //If no y-axis adjacencies are possible based on the location of the y-lines then don't perform any further calculations and return an empty list.
        if (yLinesDoNotAllowAdjacencies(secondRectangle)) {
            return adjacencies;
        }

        //If any y-line in this object shares its value with an y-line from the provided object then an adjacency exists.
        if (this.getUpperYLine() == secondRectangle.getUpperYLine()) {
            if (this.getLowerYLine() == secondRectangle.getLowerYLine()) {
                adjacencies.add(determineYLineAdjacencyParameters(secondRectangle, this.upperLeftCorner.getYCoordinate()));
                adjacencies.add(determineYLineAdjacencyParameters(secondRectangle, this.lowerLeftCorner.getYCoordinate()));

            } else {
                adjacencies.add(determineYLineAdjacencyParameters(secondRectangle, this.upperLeftCorner.getYCoordinate()));
            }

        } else if (this.getLowerYLine() == secondRectangle.getLowerYLine()) {
            adjacencies.add(determineYLineAdjacencyParameters(secondRectangle, this.lowerLeftCorner.getYCoordinate()));

        } else {
            if (this.getUpperYLine() == secondRectangle.getLowerYLine()) {
                adjacencies.add(determineYLineAdjacencyParameters(secondRectangle, this.upperLeftCorner.getYCoordinate()));

            } else if (this.getLowerYLine() == secondRectangle.getUpperYLine()) {
                adjacencies.add(determineYLineAdjacencyParameters(secondRectangle, this.lowerLeftCorner.getYCoordinate()));
            }
        }

        return adjacencies;
    }

    /**
     * Determines if adjacencies on the x-axis are possible between this rectangle and the provided one.
     *
     * @param secondRectangle: The rectangle to compare against.
     * @return Returns true if no adjacencies are possible.
     */
    private boolean xLinesDoNotAllowAdjacencies(Rectangle secondRectangle) {
        return this.getUpperYLine() <= secondRectangle.getLowerYLine() || this.getLowerYLine() >= secondRectangle.getUpperYLine();
    }

    /**
     * Determines if adjacencies on the y-axis are possible between this rectangle and the provided one.
     *
     * @param secondRectangle: The rectangle to compare against.
     * @return Returns true if no adjacencies are possible.
     */
    private boolean yLinesDoNotAllowAdjacencies(Rectangle secondRectangle) {
        return this.getRightXLine() <= secondRectangle.getLeftXLine() || this.getLeftXLine() >= secondRectangle.getRightXLine();
    }

    /**
     * This method determines all parameters required to create a new {@link Adjacency} on the x-axis
     * @param secondRectangle: The rectangle to compare against.
     * @param xCoordinate: The x-axis coordinate of the line being analyzed.
     * @return Returns a fully built {@link Adjacency} based on the parameters provided.
     */
    private Adjacency determineXLineAdjacencyParameters(Rectangle secondRectangle, int xCoordinate) {
        AdjacencyType adjacencyType = determineAdjacencyType(this.getUpperYLine(), this.getLowerYLine(), secondRectangle.getUpperYLine(), secondRectangle.getLowerYLine());

        int[] sharedXAxisPoints = sortArray(new int[] {this.getUpperYLine(), secondRectangle.getUpperYLine(), this.getLowerYLine(), secondRectangle.getLowerYLine()});
        return new Adjacency(adjacencyType, AdjacencyAxis.X, new Node(xCoordinate, sharedXAxisPoints[1]), new Node(xCoordinate, sharedXAxisPoints[2]));
    }

    /**
     * This method determines all parameters required to create a new {@link Adjacency} on the y-axis
     * @param secondRectangle: The rectangle to compare against.
     * @param yCoordinate: The y-axis coordinate of the line being analyzed.
     * @return Returns a fully built {@link Adjacency} based on the parameters provided.
     */
    private Adjacency determineYLineAdjacencyParameters(Rectangle secondRectangle, int yCoordinate) {
        AdjacencyType adjacencyType = determineAdjacencyType(this.getRightXLine(), this.getLeftXLine(), secondRectangle.getRightXLine(), secondRectangle.getLeftXLine());

        int[] sharedXAxisPoints = sortArray(new int[] {this.getRightXLine(), secondRectangle.getRightXLine(), this.getLeftXLine(), secondRectangle.getLeftXLine()});
        return new Adjacency(adjacencyType, AdjacencyAxis.Y, new Node(sharedXAxisPoints[1], yCoordinate), new Node(sharedXAxisPoints[2], yCoordinate));
    }


    /**
     * This method determines the correct {@link AdjacencyType} based on the provided lines.
     *  PROPER: A line shares the same set of points as the other.
     *  SUB_LINE: The points of one line are a subset of the points that make up a second line.
     *  PARTIAL: The two lines share some points but also both contain points that the other does not.
     *
     *  All provided lines must either be on the x-axis or the y-axis. They cannot be mixed.
     *
     * @param primaryObjectGreaterLine: The line from the primary object with the largest coordinate value.
     * @param primaryObjectLesserLine: The line from the primary object with the smallest coordinate value.
     * @param secondaryObjectGreaterLine: The line from the secondary object with the largest coordinate value.
     * @param secondaryObjectLesserLine: The line from the secondary object with the smallest coordinate value.
     * @return The {@link AdjacencyType} calculated by the method.
     */
    private static AdjacencyType determineAdjacencyType(int primaryObjectGreaterLine, int primaryObjectLesserLine, int secondaryObjectGreaterLine, int secondaryObjectLesserLine) {
        AdjacencyType adjacencyType;
        if (primaryObjectGreaterLine == secondaryObjectGreaterLine && primaryObjectLesserLine == secondaryObjectLesserLine) {
            adjacencyType = AdjacencyType.PROPER;
        } else if (primaryObjectGreaterLine > secondaryObjectGreaterLine && primaryObjectLesserLine < secondaryObjectLesserLine
                || primaryObjectGreaterLine < secondaryObjectGreaterLine && primaryObjectLesserLine > secondaryObjectLesserLine) {
            adjacencyType = AdjacencyType.SUB_LINE;
        } else {
            adjacencyType = AdjacencyType.PARTIAL;
        }
        return adjacencyType;
    }

    /**
     * Sorting the provided array of integers from smallest to largest.
     * @param array: The int array to be sorted.
     * @return An array of integers sorted in ascending order
     */
    protected static int[] sortArray(int[] array) {
        for (int currentIndex = 1; currentIndex < array.length; currentIndex++) {
            int keyValue = array[currentIndex];
            int previousIndex = currentIndex-1;

            while (previousIndex >= 0 && array[previousIndex] > keyValue) {
                array[previousIndex + 1] = array[previousIndex];
                previousIndex--;
            }

            array[previousIndex + 1] = keyValue;
        }

        return array;
    }

    /**
     * Checking if intersections are possible between the two rectangles on either the x or y-axis.
     * @param secondRectangle: The rectangle to compare against.
     * @return Returns true if no intersections exist
     */
    private boolean noIntersectionsExist(Rectangle secondRectangle) {
        return xCoordinatesDoNotAllowIntersections(secondRectangle) || yCoordinatesDoNotAllowIntersections(secondRectangle);
    }

    /**
     * If the uppermost y coordinate of either rectangle is less than the lowest y coordinate of
     * the other rectangle then no intersections are possible
     *
     * @param secondRectangle: The rectangle to compare against.
     * @return Returns true if no intersections are possible.
     */
    private boolean yCoordinatesDoNotAllowIntersections(Rectangle secondRectangle) {
        return this.getUpperYLine() < secondRectangle.getLowerYLine()
                || this.getLowerYLine() > secondRectangle.getUpperYLine();
    }

    /**
     * If the rightmost x coordinate of either rectangle is less than the leftmost coordinate of
     * the other rectangle then no intersections are possible
     *
     * @param secondRectangle: The rectangle to compare against.
     * @return Returns true if no intersections are possible.
     */
    private boolean xCoordinatesDoNotAllowIntersections(Rectangle secondRectangle) {
        return this.getRightXLine() < secondRectangle.getLeftXLine()
                || this.getLeftXLine() > secondRectangle.getRightXLine();
    }
}
