package jehlenfeldt.assessments.rectangleoperations.resources;

import jehlenfeldt.assessments.rectangleoperations.resources.adjacency.Adjacency;
import jehlenfeldt.assessments.rectangleoperations.resources.adjacency.AdjacencyAxis;
import jehlenfeldt.assessments.rectangleoperations.resources.adjacency.AdjacencyType;
import jehlenfeldt.assessments.rectangleoperations.resources.exceptions.InvalidDimensionsException;
import jehlenfeldt.assessments.rectangleoperations.resources.intersection.IntersectionType;
import jehlenfeldt.assessments.rectangleoperations.resources.intersection.Intersections;
import jehlenfeldt.assessments.rectangleoperations.resources.shared.Node;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class RectangleTest {

    @ParameterizedTest
    @MethodSource("constructNewRectangleTestParameters")
    void constructNewRectangle(Node upperLeft, Node lowerRight, Node expectedLowerLeft, Node expectedUpperRight) throws InvalidDimensionsException {

        Rectangle rectangle = new Rectangle(upperLeft, lowerRight);

        assertThat(rectangle.getUpperLeftCorner()).usingRecursiveComparison().isEqualTo(upperLeft);
        assertThat(rectangle.getLowerRightCorner()).usingRecursiveComparison().isEqualTo(lowerRight);
        assertThat(rectangle.getLowerLeftCorner()).usingRecursiveComparison().isEqualTo(expectedLowerLeft);
        assertThat(rectangle.getUpperRightCorner()).usingRecursiveComparison().isEqualTo(expectedUpperRight);
    }

    @Test
    void getListOfIntegerNodes() throws InvalidDimensionsException {
        Rectangle rectangle = new Rectangle(new Node(4, 13), new Node(11, 10));
        List<Node> expectedNodes = Arrays.asList(
                new Node(4, 13), new Node(5, 13), new Node(6, 13), new Node(7, 13), new Node(8, 13), new Node(9, 13), new Node(10, 13), new Node(11, 13),
                new Node(4, 12), new Node(11, 12),
                new Node(4, 11), new Node(11, 11),
                new Node(4, 10), new Node(5, 10), new Node(6, 10), new Node(7, 10), new Node(8, 10), new Node(9, 10), new Node(10, 10), new Node(11, 10));

        List<Node> returnedNodes = rectangle.getListOfIntegerNodes();
        assertThat(returnedNodes).containsExactlyInAnyOrderElementsOf(expectedNodes);
    }

    @ParameterizedTest(name = "{index}: {2}")
    @MethodSource("sortArrayTestParameters")
    void sortArray(int[] inputArray, int[] sortedArray, String testName) {
        int[] outputArray = Rectangle.sortArray(inputArray);

        assertThat(outputArray).containsExactly(sortedArray);
    }

    //~~~~~~~~ Intersection Identification ~~~~~~~~//
    @Test
    void identifyIntersections_rectanglesHaveSameNodes() throws InvalidDimensionsException {
        Node rectangleUpperLeftNode = new Node(5, 3);
        Node rectangleLowerRightNode = new Node(10, 0);

        Rectangle rectangleOne = new Rectangle(rectangleUpperLeftNode, rectangleLowerRightNode);
        Rectangle rectangleTwo = new Rectangle(rectangleUpperLeftNode, rectangleLowerRightNode);
        List<Node> expectedNodes = Arrays.stream(new Node[] {
                        new Node(5, 3), new Node(6, 3), new Node(7, 3), new Node(8, 3), new Node(9, 3), new Node(10, 3),
                        new Node(5, 2), new Node(5, 1), new Node(10, 2), new Node(10, 1),
                        new Node(5, 0), new Node(6, 0), new Node(7, 0), new Node(8, 0), new Node(9, 0), new Node(10, 0)})
                .collect(Collectors.toList());

        Intersections intersections = rectangleOne.identifyIntersections(rectangleTwo);
        assertThat(intersections).isNotNull();
        assertThat(intersections.getIntersectionType()).isEqualTo(IntersectionType.ALL_POINTS_INTERSECT);
        assertThat(intersections.getIntersectionNodes()).containsAll(expectedNodes);
    }

    @ParameterizedTest(name = "{index}: {2}")
    @MethodSource("noIntersectionsTestParameters")
    void identifyIntersections_rectanglesHaveNoIntersections(Rectangle rectangleOne, Rectangle rectangleTwo, String testName) {

        Intersections intersections = rectangleOne.identifyIntersections(rectangleTwo);
        assertThat(intersections.getIntersectionType()).isEqualTo(IntersectionType.NON_INTERSECTING);
        assertThat(intersections.getIntersectionNodes()).isEmpty();
    }

    @ParameterizedTest(name = "{index}: {3}")
    @MethodSource("intersectionsExistTestParameters")
    void identifyIntersections_intersectionsExist(Rectangle rectangleOne, Rectangle rectangleTwo, List<Node> expectedIntersections, String testName) {

        Intersections intersections = rectangleOne.identifyIntersections(rectangleTwo);

        assertThat(intersections).isNotNull();
        assertThat(intersections.getIntersectionType()).isEqualTo(IntersectionType.NODE);
        assertThat(intersections.getIntersectionNodes()).isNotEmpty();
        assertThat(intersections.getIntersectionNodes()).containsAll(expectedIntersections);
    }


    //~~~~~~~~ Contained-By Identification ~~~~~~~~//
    @ParameterizedTest(name = "{index}: {3}")
    @MethodSource("isFullyContainedByTestParameters")
    void isFullyContainedBy(Rectangle rectangleOne, Rectangle rectangleTwo, boolean expectedResult, String testName) {
        boolean fullyContained = rectangleOne.isFullyContainedBy(rectangleTwo);

        assertThat(fullyContained).isEqualTo(expectedResult);
    }


    //~~~~~~~~ Adjacency Identification ~~~~~~~~//
    @ParameterizedTest(name = "{index}: {3}")
    @MethodSource("findAnyAdjacencyWithTestParameters")
    void findAnyAdjacencyWith(Rectangle rectangleOne, Rectangle rectangleTwo, List<Adjacency> expectedAdjacencies, String testName) {
        List<Adjacency> adjacencies = rectangleOne.findAnyAdjacencyWith(rectangleTwo);

        assertThat(adjacencies).containsAll(expectedAdjacencies);
    }

    //~~~~~~~~ Test setup methods ~~~~~~~~//
    private static Stream<Arguments> constructNewRectangleTestParameters() {
        return Stream.of(
                Arguments.of(new Node(0, 10), new Node(20, 0), new Node(0, 0), new Node(20, 10)),
                Arguments.of(new Node(1, 40), new Node(50, 30), new Node(1, 30), new Node(50, 40)),
                Arguments.of(new Node(-10, 40), new Node(20, -60), new Node(-10, -60), new Node(20, 40)));
    }

    private static Stream<Arguments> noIntersectionsTestParameters() throws InvalidDimensionsException {
        //Rectangles two through five located in quadrants around rectangle one. No shared axis.
        Rectangle rectangleOne = new Rectangle(new Node(6, 15), new Node(11, 10));
        Rectangle rectangleTwo = new Rectangle(new Node(1, 21), new Node(5, 18));
        Rectangle rectangleThree = new Rectangle(new Node(13, 23), new Node(18, 18));
        Rectangle rectangleFour = new Rectangle(new Node(12, 6), new Node(17, 2));
        Rectangle rectangleFive = new Rectangle(new Node(1, 9), new Node(5, 1));

        //Rectangle one fully contained in rectangle two
        Rectangle testTwoRectangleOne = new Rectangle(new Node(5, 9), new Node(10, 2));
        Rectangle testTwoRectangleTwo = new Rectangle(new Node(3, 11), new Node(15, 1));

        //Rectangle two fully contained in rectangle one
        Rectangle testThreeRectangleOne = new Rectangle(new Node(5, 9), new Node(10, 2));
        Rectangle testThreeRectangleTwo = new Rectangle(new Node(6, 7), new Node(9, 5));

        //Neither contains the other. Non-intersecting x-axis overlap
        Rectangle testFourRectangleOne = new Rectangle(new Node(1, 21), new Node(5, 18));
        Rectangle testFourRectangleTwo = new Rectangle(new Node(3, 17), new Node(7, 14));

        //Neither contains the other. Non-intersecting x-axis overlap
        Rectangle testFiveRectangleOne = new Rectangle(new Node(1, 21), new Node(5, 18));
        Rectangle testFiveRectangleTwo = new Rectangle(new Node(3, 17), new Node(7, 14));

        //Neither contains the other. Non-intersecting x-axis overlap
        Rectangle testSixRectangleOne = new Rectangle(new Node(9,10), new Node(13, 7));
        Rectangle testSixRectangleTwo = new Rectangle(new Node(15, 15), new Node(18, 9));

        return Stream.of(
                Arguments.of(rectangleOne, rectangleTwo, "No Intersections - Separated on all axis: Located to upper left"),
                Arguments.of(rectangleOne, rectangleThree, "No Intersections - Separated on all axis: Located to upper right"),
                Arguments.of(rectangleOne, rectangleFour, "No Intersections - Separated on all axis: Located to lower right"),
                Arguments.of(rectangleOne, rectangleFive, "No Intersections - Separated on all axis: Located to lower left"),
                Arguments.of(testTwoRectangleOne, testTwoRectangleTwo, "No Intersections - First Contained In Second"),
                Arguments.of(testThreeRectangleOne, testThreeRectangleTwo, "No Intersections - Second Contained In First"),
                Arguments.of(testFourRectangleOne, testFourRectangleTwo, "No Intersections - Neither contains the other"),
                Arguments.of(testFiveRectangleOne, testFiveRectangleTwo, "No Intersections - Non-intersecting x-axis overlap"),
                Arguments.of(testSixRectangleOne, testSixRectangleTwo, "No Intersections - Non-intersecting y-axis overlap")
        );
    }

    private static Stream<Arguments> intersectionsExistTestParameters() throws InvalidDimensionsException {
        Rectangle rectangleOne = new Rectangle(new Node(6, 15), new Node(11, 10));

        Rectangle testOneRectangleTwo = new Rectangle(new Node(9, 18), new Node(14, 13));
        List<Node> testOneExpectedIntersections = new ArrayList<>();
        testOneExpectedIntersections.add(new Node(9, 15));
        testOneExpectedIntersections.add(new Node(11, 13));

        Rectangle testTwoRectangleTwo = new Rectangle(new Node(8, 11), new Node(10, 7));
        List<Node> testTwoExpectedIntersections = new ArrayList<>();
        testTwoExpectedIntersections.add(new Node(8, 10));
        testTwoExpectedIntersections.add(new Node(10, 10));

        Rectangle testThreeRectangleTwo = new Rectangle(new Node(4, 14), new Node(7, 12));
        List<Node> testThreeExpectedIntersections = new ArrayList<>();
        testThreeExpectedIntersections.add(new Node(6, 14));
        testThreeExpectedIntersections.add(new Node(6, 12));

        Rectangle testFourRectangleOne = new Rectangle(new Node(11, 5), new Node(18, 3));
        Rectangle testFourRectangleTwo = new Rectangle(new Node(13, 10 ), new Node(16, 1));
        List<Node> testFourExpectedIntersections = new ArrayList<>();
        testFourExpectedIntersections.add(new Node(13, 5));
        testFourExpectedIntersections.add(new Node(16, 5));
        testFourExpectedIntersections.add(new Node(13, 3));
        testFourExpectedIntersections.add(new Node(16, 3));

        Rectangle testFiveRectangleOne = new Rectangle(new Node(3, 22), new Node(6, 18));
        Rectangle testFiveRectangleTwo = new Rectangle(new Node(2, 21), new Node(7, 19));
        List<Node> testFiveExpectedIntersections = new ArrayList<>();
        testFiveExpectedIntersections.add(new Node(3, 21));
        testFiveExpectedIntersections.add(new Node(6, 21));
        testFiveExpectedIntersections.add(new Node(3, 19));
        testFiveExpectedIntersections.add(new Node(6, 19));

        return Stream.of(
                Arguments.of(rectangleOne, testOneRectangleTwo, testOneExpectedIntersections, "Two intersections expected. X and Y axis: (9, 15) & (11, 13)"),
                Arguments.of(rectangleOne, testTwoRectangleTwo, testTwoExpectedIntersections, "Two intersections expected. Y axis only: (8, 10) & (10, 10)"),
                Arguments.of(rectangleOne, testThreeRectangleTwo, testThreeExpectedIntersections, "Two intersections expected. X axis only: (6, 14) & (6, 12)"),
                Arguments.of(testFourRectangleOne, testFourRectangleTwo, testFourExpectedIntersections, "Four intersections expected: (13, 5) & (16, 5) & (13, 3) & (16, 3)"),
                Arguments.of(testFiveRectangleOne, testFiveRectangleTwo, testFiveExpectedIntersections, "Four intersections expected: (3, 21) & (6, 21) & (3, 19) & (6, 19)"));
    }

    private static Stream<Arguments> isFullyContainedByTestParameters() throws InvalidDimensionsException {

        //Rectangle one fully contained in rectangle two
        Rectangle testOneRectangleOne = new Rectangle(new Node(5, 9), new Node(10, 2));
        Rectangle testOneRectangleTwo = new Rectangle(new Node(3, 11), new Node(15, 1));
        boolean testOneExpectedResult = true;

        //Rectangle two fully contained in rectangle one
        Rectangle testTwoRectangleOne = new Rectangle(new Node(5, 9), new Node(10, 2));
        Rectangle testTwoRectangleTwo = new Rectangle(new Node(6, 7), new Node(9, 5));
        boolean testTwoExpectedResult = false;

        //Intersecting rectangles
        Rectangle testThreeRectangleOne = new Rectangle(new Node(9,18), new Node(14, 13));
        Rectangle testThreeRectangleTwo = new Rectangle(new Node(6, 15), new Node(11, 10));
        boolean testThreeExpectedResult = false;

        //Non-intersecting and not contained within either.
        Rectangle testFourRectangleOne = new Rectangle(new Node(1, 21), new Node(5, 18));
        Rectangle testFourRectangleTwo = new Rectangle(new Node(6, 15), new Node(11, 10));
        boolean testFourExpectedResult = false;

        return Stream.of(
                Arguments.of(testOneRectangleOne, testOneRectangleTwo, testOneExpectedResult, "Primary rectangle fully contained in secondary."),
                Arguments.of(testTwoRectangleOne, testTwoRectangleTwo, testTwoExpectedResult, "Secondary rectangle fully contained in primary."),
                Arguments.of(testThreeRectangleOne, testThreeRectangleTwo, testThreeExpectedResult, "Rectangles intersect"),
                Arguments.of(testFourRectangleOne, testFourRectangleTwo, testFourExpectedResult, "Non-intersecting and non-contained")
        );
    }

    private static Stream<Arguments> findAnyAdjacencyWithTestParameters() throws InvalidDimensionsException {
        List<Arguments> testArguments = new ArrayList<>();
        testArguments.addAll(createMultiAxisTestArguments());
        testArguments.addAll(createXAxisTestArguments());
        testArguments.addAll(createYAxisTestArguments());

        return testArguments.stream();
    }

    private static List<Arguments> createMultiAxisTestArguments() throws InvalidDimensionsException {
        Rectangle rectangleOne = new Rectangle(new Node(4, 13), new Node(11, 6));

        String testOneName = "Multi-Axis: Rectangles are equal. Proper adjacency on all sides.";
        List<Adjacency> expectedTestOneAdjacencies = Arrays.asList(
                new Adjacency(AdjacencyType.PROPER, AdjacencyAxis.X, new Node(4, 6),new Node(4, 13)),
                new Adjacency(AdjacencyType.PROPER, AdjacencyAxis.X, new Node(11, 6),new Node(11, 13)),
                new Adjacency(AdjacencyType.PROPER, AdjacencyAxis.Y, new Node(4, 13),new Node(11, 13)),
                new Adjacency(AdjacencyType.PROPER, AdjacencyAxis.Y, new Node(4, 6),new Node(11, 6)));
        Arguments testOneArguments = Arguments.of(rectangleOne, rectangleOne, expectedTestOneAdjacencies, testOneName);

        String testTwoName = "Multi-Axis: Secondary object smaller. Three sides adjacent. Proper adjacency on one side. Partial adjacency on two.";
        Rectangle testTwoRectangleTwo = new Rectangle(new Node(4, 13), new Node(8, 6));
        List<Adjacency> expectedTestTwoAdjacencies = Arrays.asList(
                new Adjacency(AdjacencyType.PROPER, AdjacencyAxis.X, new Node(4, 6),new Node(4, 13)),
                new Adjacency(AdjacencyType.PARTIAL, AdjacencyAxis.Y, new Node(4, 13),new Node(8, 13)),
                new Adjacency(AdjacencyType.PARTIAL, AdjacencyAxis.Y, new Node(4, 6),new Node(8, 6)));
        Arguments testTwoArguments = Arguments.of(rectangleOne, testTwoRectangleTwo, expectedTestTwoAdjacencies, testTwoName);

        String testThreeName = "Multi-Axis: Secondary object smaller. Two sides adjacent. Partial adjacency on both.";
        Rectangle testThreeRectangleTwo = new Rectangle(new Node(4, 10), new Node(8, 6));
        List<Adjacency> expectedTestThreeAdjacencies = Arrays.asList(
                new Adjacency(AdjacencyType.PARTIAL, AdjacencyAxis.X, new Node(4, 6),new Node(4, 10)),
                new Adjacency(AdjacencyType.PARTIAL, AdjacencyAxis.Y, new Node(4, 6),new Node(8, 6)));
        Arguments testThreeArguments = Arguments.of(rectangleOne, testThreeRectangleTwo, expectedTestThreeAdjacencies, testThreeName);

        String testFourName = "Multi-Axis: Secondary object larger. Three sides adjacent. Proper adjacency on one side. Partial adjacency on two.";
        Rectangle testFourRectangleTwo = new Rectangle(new Node(2, 13), new Node(11, 6));
        List<Adjacency> expectedtestFourAdjacencies = Arrays.asList(
                new Adjacency(AdjacencyType.PROPER, AdjacencyAxis.X, new Node(11, 6),new Node(11, 13)),
                new Adjacency(AdjacencyType.PARTIAL, AdjacencyAxis.Y, new Node(4, 13),new Node(11, 13)),
                new Adjacency(AdjacencyType.PARTIAL, AdjacencyAxis.Y, new Node(4, 6),new Node(11, 6)));
        Arguments testFourArguments = Arguments.of(rectangleOne, testFourRectangleTwo, expectedtestFourAdjacencies, testFourName);

        String testFiveName = "Multi-Axis: Secondary object larger. Two sides adjacent. Partial adjacency on both.";
        Rectangle testFiveRectangleTwo = new Rectangle(new Node(2, 13), new Node(11, 3));
        List<Adjacency> expectedtestFiveAdjacencies = Arrays.asList(
                new Adjacency(AdjacencyType.PARTIAL, AdjacencyAxis.X, new Node(11, 6),new Node(11, 13)),
                new Adjacency(AdjacencyType.PARTIAL, AdjacencyAxis.Y, new Node(4, 13),new Node(11, 13)));
        Arguments testFiveArguments = Arguments.of(rectangleOne, testFiveRectangleTwo, expectedtestFiveAdjacencies, testFiveName);

        String testSixName = "Multi-Axis: Secondary object fully contained. No adjacencies";
        Rectangle testSixRectangleTwo = new Rectangle(new Node(5, 12), new Node(10, 7));
        Arguments testSixArguments = Arguments.of(rectangleOne, testSixRectangleTwo, new ArrayList<Adjacency>(), testSixName);

        String testSevenName = "Multi-Axis: Primary object fully contained. No adjacencies";
        Rectangle testSevenRectangleTwo = new Rectangle(new Node(2, 15), new Node(13, 4));
        Arguments testSevenArguments = Arguments.of(rectangleOne, testSevenRectangleTwo, new ArrayList<Adjacency>(), testSevenName);

        return Arrays.asList(testOneArguments, testTwoArguments, testThreeArguments, testFourArguments, testFiveArguments, testSixArguments, testSevenArguments);
    }

    private static List<Arguments> createXAxisTestArguments() throws InvalidDimensionsException {
        Rectangle rectangleOne = new Rectangle(new Node(7, 16), new Node(13, 12));

        String testOneName = "X-Axis: Non-overlapping partial adjacency on left from (7, 14) to (7, 16)";
        Rectangle testOneRectangleTwo = new Rectangle(new Node(1, 18), new Node(7, 14));
        Adjacency testOneExpectedAdjacency = new Adjacency(AdjacencyType.PARTIAL, AdjacencyAxis.X, new Node(7, 14), new Node(7, 16));
        Arguments testOneArguments = Arguments.of(rectangleOne, testOneRectangleTwo, Collections.singletonList(testOneExpectedAdjacency), testOneName);

        String testTwoName = "X-Axis: Non-overlapping partial adjacency on right from (13, 12) to (13, 14)";
        Rectangle testTwoRectangleTwo = new Rectangle(new Node(13, 14), new Node(17, 10));
        Adjacency testTwoExpectedAdjacency = new Adjacency(AdjacencyType.PARTIAL, AdjacencyAxis.X, new Node(13, 12), new Node(13, 14));
        Arguments testTwoArguments = Arguments.of(rectangleOne, testTwoRectangleTwo, Collections.singletonList(testTwoExpectedAdjacency), testTwoName);

        String testThreeName = "X-Axis: Non-overlapping proper adjacency on right from (13, 12) to (13, 16)";
        Rectangle testThreeRectangleTwo = new Rectangle(new Node(13, 16), new Node(17, 12));
        Adjacency testThreeExpectedAdjacency = new Adjacency(AdjacencyType.PROPER, AdjacencyAxis.X, new Node(13, 12), new Node(13,16));
        Arguments testThreeArguments = Arguments.of(rectangleOne, testThreeRectangleTwo, Collections.singletonList(testThreeExpectedAdjacency), testThreeName);

        String testFourName = "X-Axis: Non-overlapping sub-line adjacency on second rectangle x-axis from (13, 13) to (13, 15).";
        Rectangle testFourRectangleTwo = new Rectangle(new Node(13, 15), new Node(17, 13));
        Adjacency testFourExpectedAdjacency = new Adjacency(AdjacencyType.SUB_LINE, AdjacencyAxis.X, new Node(13, 13), new Node(13,15));
        Arguments testFourArguments = Arguments.of(rectangleOne, testFourRectangleTwo, Collections.singletonList(testFourExpectedAdjacency), testFourName);

        String testFiveName = "X-Axis: Non-overlapping sub-line adjacency on first rectangle x-axis from (13, 12) to (13, 16)";
        Rectangle testFiveRectangleTwo = new Rectangle(new Node(13, 20), new Node(17, 5));
        Adjacency testFiveExpectedAdjacency = new Adjacency(AdjacencyType.SUB_LINE, AdjacencyAxis.X, new Node(13, 12), new Node(13,16));
        Arguments testFiveArguments = Arguments.of(rectangleOne, testFiveRectangleTwo, Collections.singletonList(testFiveExpectedAdjacency), testFiveName);

        String testSixName = "X-Axis: Overlapping partial adjacency on both x-axis' from (7, 12) to (7, 15) and (13, 9) to (13, 15)";
        Rectangle testSixRectangleTwo = new Rectangle(new Node(7, 15), new Node(13, 9));
        List<Adjacency> expectedtestSixAdjacencies = Arrays.asList(
                new Adjacency(AdjacencyType.PARTIAL, AdjacencyAxis.X, new Node(7, 12), new Node(7, 15)),
                new Adjacency(AdjacencyType.PARTIAL, AdjacencyAxis.X, new Node(13, 12), new Node(13, 15)));
        Arguments testSixArguments = Arguments.of(rectangleOne, testSixRectangleTwo, expectedtestSixAdjacencies, testSixName);

        String testSevenName = "X-Axis: Overlapping partial adjacency on right x-axis' from (13, 13) to (13, 16)";
        Rectangle testSevenRectangleTwo = new Rectangle(new Node(10, 19), new Node(13, 13));
        Adjacency testSevenExpectedAdjacency = new Adjacency(AdjacencyType.PARTIAL, AdjacencyAxis.X, new Node(13, 13), new Node(13,16));
        Arguments testSevenArguments = Arguments.of(rectangleOne, testSevenRectangleTwo, Collections.singletonList(testSevenExpectedAdjacency), testSevenName);

        String testEightName = "X-Axis: Overlapping partial adjacency on left x-axis' from (7, 9) to (7, 13)";
        Rectangle testEightRectangleTwo = new Rectangle(new Node(7, 13), new Node(10, 6));
        Adjacency testEightExpectedAdjacency = new Adjacency(AdjacencyType.PARTIAL, AdjacencyAxis.X, new Node(7, 12), new Node(7, 13));
        Arguments testEightArguments = Arguments.of(rectangleOne, testEightRectangleTwo, Collections.singletonList(testEightExpectedAdjacency), testEightName);

        return Arrays.asList(testOneArguments, testTwoArguments, testTwoArguments, testThreeArguments, testFourArguments, testFiveArguments, testSixArguments, testSevenArguments, testEightArguments);
    }

    private static List<Arguments> createYAxisTestArguments() throws InvalidDimensionsException {
        Rectangle rectangleOne = new Rectangle(new Node(4, 20), new Node(9, 15));
        Rectangle rectangleTwo = new Rectangle(new Node(11, 12), new Node(16, 7));

        String testOneName = "Y-Axis: Non-overlapping partial adjacency on top from (5, 20) to (9, 20)";
        Rectangle testOneRectangleTwo = new Rectangle(new Node(5, 23), new Node(9, 20));
        Adjacency testOneExpectedAdjacency = new Adjacency(AdjacencyType.PARTIAL, AdjacencyAxis.Y, new Node(5, 20), new Node(9, 20));
        Arguments testOneArguments = Arguments.of(rectangleOne, testOneRectangleTwo, Collections.singletonList(testOneExpectedAdjacency), testOneName);

        String testTwoName = "Y-Axis: Non-overlapping sub-line adjacency on bottom from (5, 15) to (7, 15)";
        Rectangle testTwoRectangleTwo = new Rectangle(new Node(5, 15), new Node(7, 11));
        Adjacency testTwoExpectedAdjacency = new Adjacency(AdjacencyType.SUB_LINE, AdjacencyAxis.Y, new Node(5, 15), new Node(7, 15));
        Arguments testTwoArguments = Arguments.of(rectangleOne, testTwoRectangleTwo, Collections.singletonList(testTwoExpectedAdjacency), testTwoName);

        String testThreeName = "Y-Axis: Overlapping partial adjacency on top and bottom from (14, 7) to (16, 7) and (14, 12) and (16, 12)";
        Rectangle testThreeRectangleTwo = new Rectangle(new Node(14, 12), new Node(17, 7));
        List<Adjacency> testThreeExpectedAdjacencies = Arrays.asList(
                new Adjacency(AdjacencyType.PARTIAL, AdjacencyAxis.Y, new Node(14, 12), new Node(16, 12)),
                new Adjacency(AdjacencyType.PARTIAL, AdjacencyAxis.Y, new Node(14, 7), new Node(16, 7)));
        Arguments testThreeArguments = Arguments.of(rectangleTwo, testThreeRectangleTwo, testThreeExpectedAdjacencies, testThreeName);

        String testFourName = "Y-Axis: Overlapping sub-line adjacency on bottom from (12, 7) to (13, 7)";
        Rectangle testFourRectangleTwo = new Rectangle(new Node(12, 10), new Node(13, 7));
        Adjacency testFourExpectedAdjacency = new Adjacency(AdjacencyType.SUB_LINE, AdjacencyAxis.Y, new Node(12, 7), new Node(13, 7));
        Arguments testFourArguments = Arguments.of(rectangleTwo, testFourRectangleTwo, Collections.singletonList(testFourExpectedAdjacency), testFourName);

        String testFiveName = "Y-Axis: Overlapping sub-line adjacency on left from (12, 12) to (12, 13)";
        Rectangle testFiveRectangleTwo = new Rectangle(new Node(12, 12), new Node(13, 10));
        Adjacency testFiveExpectedAdjacency = new Adjacency(AdjacencyType.SUB_LINE, AdjacencyAxis.Y, new Node(12, 12), new Node(13, 12));
        Arguments testFiveArguments = Arguments.of(rectangleTwo, testFiveRectangleTwo, Collections.singletonList(testFiveExpectedAdjacency), testFiveName);

        return  Arrays.asList(testOneArguments, testTwoArguments, testThreeArguments, testFourArguments, testFiveArguments);
    }

    private static Stream<Arguments> sortArrayTestParameters() {
        return Stream.of(
                Arguments.of(new int[]{1, 2, 3, 4, 5}, new int[]{1, 2, 3, 4, 5}, "Pre-Sorted array"),
                Arguments.of(new int[]{5, 1, 3, 2, 4}, new int[]{1, 2, 3, 4, 5}, "Unsorted array"),
                Arguments.of(new int[]{5, 1, 3, 5, 4}, new int[]{1, 3, 4, 5, 5}, "Unsorted array with duplicate values")
        );
    }

}