/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {

    private LineSegment[] segments;
    private LineSegment[] noRepeatSegments;
    private int count = 0;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("null array");
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException("null point");
            for (int j = i + 1; j < points.length; j++) {
                if (points[i] == points[j]) throw new IllegalArgumentException("repeated point");
            }
        }

        Point[] originalPoints = new Point[points.length];
        for (int i = 0; i < points.length; i++) originalPoints[i] = points[i];

        segments = new LineSegment[points.length];
        double prev;
        double curr;
        int num;
        int startIndex = 0;
        Point[] temp;
        for (int p = 0; p < points.length; p++) {
            Arrays.sort(points, originalPoints[p].slopeOrder());

            // StdOut.println("\nslope-sorted points for p=" + p);
            // printPoints(points);

            prev = originalPoints[p].slopeTo(points[1]);
            num = 1;
            for (int i = 2; i < points.length; i++) {
                curr = originalPoints[p].slopeTo(points[i]);
                if (prev == curr || curr == Double.NEGATIVE_INFINITY) {
                    if (num == 1) {
                        startIndex = i - 1;
                    }
                    num++;
                    if (i == points.length - 1 && num >= 3) {
                        temp = new Point[num + 1];  // should get rid of temp
                        temp[num] = originalPoints[p];      // too much memory
                        for (int x = startIndex; x < startIndex + num; x++) {
                            temp[x - startIndex] = points[x];
                        }

                        // StdOut.println("\ntemp for p=" + p);
                        // printPoints(temp);

                        Arrays.sort(temp);
                        segments[p] = new LineSegment(temp[0], temp[num]);
                    }
                }
                else {
                    if (num >= 3) {
                        temp = new Point[num + 1];  // should get rid of temp
                        temp[num] = originalPoints[p];
                        for (int x = startIndex; x < startIndex + num; x++) {
                            temp[x - startIndex] = points[x];
                        }

                        // StdOut.println("\ntemp for p=" + p);
                        // printPoints(temp);

                        Arrays.sort(temp);
                        segments[p] = new LineSegment(temp[0], temp[num]);
                    }
                    num = 1;
                    prev = curr;
                }
            }
        }
        // double for loop is to get no repeats
        int end = segments.length;
        /*
        StdOut.println("==============\nsegments:");
        for (LineSegment ls : segments) {
            StdOut.println(ls);
        }
        StdOut.println("==============");
        */
        for (int i = 0; i < end; i++) {
            for (int j = i + 1; j < end; j++) {
                if (segments[i] != null && segments[j] != null) {
                    if (segments[i].toString().equals(segments[j].toString())) {
                        int k = end - 1;
                        while (true) {
                            if (segments[k] != null) {
                                segments[j] = segments[k];
                                break;
                            }
                            k--;
                        }
                        end--;
                        j--;
                    }
                }
                else {
                    if (segments[i] == segments[j]) {
                        segments[j] = segments[end - 1];
                        end--;
                        j--;
                    }
                }
            }
        }
        noRepeatSegments = Arrays.copyOf(segments, end);
        int nullCount = 0;
        for (int i = 0; i < noRepeatSegments.length; i++) {
            if (noRepeatSegments[i] == null) {
                nullCount++;
            }
        }
        count = end - nullCount;
        /*
        StdOut.println("count: " + count);
        StdOut.println("==============\nno repeat segments:");
        for (LineSegment ls : noRepeatSegments) {
            StdOut.println(ls);
        }
        StdOut.println("==============");
        */
    }

    private void printPoints(Point[] points) {
        StdOut.println("==============");
        for (Point p : points) {
            StdOut.println(p);
        }
        StdOut.println("==============");
    }

    // the number of line segments
    public int numberOfSegments() {
        return count;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] finalSegments = new LineSegment[count];
        int nextSlot = 0;
        for (int i = 0; i < noRepeatSegments.length; i++) {
            if (noRepeatSegments[i] != null) {
                finalSegments[nextSlot++] = noRepeatSegments[i];
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
        FastCollinearPoints collinear = new FastCollinearPoints(myPoints);

        StdOut.println("========================");

        for (LineSegment ls : collinear.segments()) {
            StdOut.println(ls);
        }
        StdOut.print(collinear.numberOfSegments());
    }
}
