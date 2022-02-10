package jehlenfeldt.assessments.rectangleoperations.resources.exceptions;

public class InvalidDimensionsException extends RuntimeException {
    public InvalidDimensionsException() {
        super("Invalid dimensions have been provided for your rectangle. Please ensure that all values are non-negative and that the x value of" +
                "your upper left node is less than the x value of the lower right and that the y value of your upper left node is greater" +
                " than the y value of your lower right.");
    }
}
