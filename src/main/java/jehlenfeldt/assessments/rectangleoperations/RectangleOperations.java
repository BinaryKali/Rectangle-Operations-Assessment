package jehlenfeldt.assessments.rectangleoperations;

import jehlenfeldt.assessments.rectangleoperations.resources.Rectangle;
import jehlenfeldt.assessments.rectangleoperations.resources.adjacency.Adjacency;
import jehlenfeldt.assessments.rectangleoperations.resources.exceptions.InvalidDimensionsException;
import jehlenfeldt.assessments.rectangleoperations.resources.intersection.Intersections;
import jehlenfeldt.assessments.rectangleoperations.resources.shared.Node;

import java.util.List;
import java.util.Scanner;

public class RectangleOperations {
    private static final Scanner sc = new Scanner(System.in);
    public static final String RECTANGLE_ONE_PARAMETERS = "\nRectangle One Parameters";
    public static final String RECTANGLE_TWO_PARAMETERS = "\nRectangle Two Parameters";

    public static void main(String[] args) throws InvalidDimensionsException {
        String chosenOperation = queryUserForDesiredOperation();

        switch (chosenOperation) {
            case "1":
                identifyIntersections();
                break;
            case "2":
                doesRectangleFullyContainSecond();
                break;
            case "3":
                doesAdjacencyExist();
                break;
            default:
                System.out.println("Please choose a supported option.");
        }

        System.out.println("Press any key to exit");
        sc.nextLine();
    }

    private static String queryUserForDesiredOperation() {
        System.out.print("What operation would you like to perform?" +
                "\n\t1. Identify intersections between rectangles" +
                "\n\t2. Check if first rectangle is fully contained in second." +
                "\n\t3. Check for adjacency between rectangles." +
                "\nPlease enter the number of your selection: ");
        return sc.nextLine();
    }

    private static Rectangle queryUserForRectangleParameters(String queryPrompt) throws InvalidDimensionsException {
        System.out.println(queryPrompt);
        System.out.print("Upper left corner x-coordinate: ");
        int upperLeftX = Integer.parseInt(sc.nextLine());

        System.out.print("Upper left corner y-coordinate: ");
        int upperLeftY = Integer.parseInt(sc.nextLine());

        System.out.print("Lower right corner x-coordinate: ");
        int lowerRightX = Integer.parseInt(sc.nextLine());

        System.out.print("Lower right corner y-coordinate: ");
        int lowerRightY = Integer.parseInt(sc.nextLine());

        return new Rectangle(new Node(upperLeftX, upperLeftY), new Node(lowerRightX, lowerRightY));
    }

    private static void identifyIntersections() throws InvalidDimensionsException {
        Rectangle rectangleOne = queryUserForRectangleParameters(RECTANGLE_ONE_PARAMETERS);
        Rectangle rectangleTwo = queryUserForRectangleParameters(RECTANGLE_TWO_PARAMETERS);

        Intersections intersections = rectangleOne.identifyIntersections(rectangleTwo);
        System.out.println("\nResults: \n" + intersections + "\n\n");
    }

    private static void doesRectangleFullyContainSecond() throws InvalidDimensionsException {
        Rectangle rectangleOne = queryUserForRectangleParameters(RECTANGLE_ONE_PARAMETERS);
        Rectangle rectangleTwo = queryUserForRectangleParameters(RECTANGLE_TWO_PARAMETERS);

        String outputText = rectangleOne.isFullyContainedBy(rectangleTwo)
                ? "First rectangle is fully contained within the second"
                : "First rectangle is not fully contained within the second";
        System.out.println("\n" + outputText + "\n\n");
    }

    private static void doesAdjacencyExist() throws InvalidDimensionsException {
        Rectangle rectangleOne = queryUserForRectangleParameters(RECTANGLE_ONE_PARAMETERS);
        Rectangle rectangleTwo = queryUserForRectangleParameters(RECTANGLE_TWO_PARAMETERS);

        List<Adjacency> adjacencies = rectangleOne.findAnyAdjacencyWith(rectangleTwo);

        System.out.println("\nResults: \n");
        for (Adjacency adjacency: adjacencies) {
            System.out.println(adjacency);
        }

        System.out.println();
    }
}
