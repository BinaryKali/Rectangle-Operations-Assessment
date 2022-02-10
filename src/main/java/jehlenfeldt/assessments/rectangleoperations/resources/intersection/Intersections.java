package jehlenfeldt.assessments.rectangleoperations.resources.intersection;


import jehlenfeldt.assessments.rectangleoperations.resources.shared.Node;

import java.util.List;

public class Intersections {
    private final IntersectionType intersectionType;
    private final List<Node> intersectionNodes;


    public Intersections(IntersectionType intersectionType, List<Node> coordinates) {
        this.intersectionType = intersectionType;
        this.intersectionNodes = coordinates;
    }

    public IntersectionType getIntersectionType() {
        return intersectionType;
    }

    public List<Node> getIntersectionNodes() {
        return intersectionNodes;
    }

    @Override
    public String toString() {
        return "Intersections{" +
                "intersectionType=" + intersectionType.toString() +
                ", intersectionNodes=" + intersectionNodes +
                '}';
    }
}
