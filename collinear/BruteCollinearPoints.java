/* *****************************************************************************
 *  Name: Felix Zhu
 *  Date: 9/19/20
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {

    private LineSegment[] segments;
    private int count = 0;

    // find all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("null array");
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException("null point");
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].toString().equals(points[j].toString()))
                    throw new IllegalArgumentException("repeated point");
            }
        }

        segments = new LineSegment[points.length]; // no idea how large the array should be ???

        // printPoints(points);
        Arrays.sort(points);
        // printPoints(points);

        for (int p = 0; p < points.length; p++) {
            for (int q = p + 1; q < points.length; q++) {
                for (int r = q + 1; r < points.length; r++) {
                    for (int s = r + 1; s < points.length; s++) {
                        if (points[p].slopeTo(points[q]) == points[p].slopeTo(points[r])
                                && points[p].slopeTo(points[q]) == points[p].slopeTo(points[s])) {
                            count++;
                            segments[p] = new LineSegment(points[p], points[s]);
                            // StdOut.println(p + " " + q + " " + r + " " + s + " " + segments[p]);
                        }
                    }
                }
            }
        }
    }

    private void printPoints(Point[] points) {
        StdOut.println("==============");
        for (Point p : points) {
            StdOut.println(p);
        }
        StdOut.println("==============");
    }

    // return number of line segments
    public int numberOfSegments() {
        return count;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] finalSegments = new LineSegment[count];
        int nextSlot = 0;
        for (int i = 0; i < segments.length; i++) {
            if (segments[i] != null) {
                finalSegments[nextSlot++] = segments[i];
            }
        }
        return finalSegments;
    }

    public static void main(String[] args) {
        Point p1 = new Point(10000, 0);
        Point p2 = new Point(0, 10000);
        Point p3 = new Point(3000, 7000);
        Point p4 = new Point(7000, 3000);
        Point p5 = new Point(20000, 21000);
        Point p6 = new Point(3000, 4000);
        Point p7 = new Point(14000, 15000);
        Point p8 = new Point(6000, 7000);
        Point[] myPoints = { p1, p2, p3, p4, p5, p6, p7, p8 };
        BruteCollinearPoints collinear = new BruteCollinearPoints(myPoints);
        StdOut.println("\n# of segments: " + collinear.numberOfSegments());

        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
        }
    }
}
