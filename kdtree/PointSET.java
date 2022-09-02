/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class PointSET {

    private SET<Point2D> set;

    public PointSET() {
        set = new SET<Point2D>();
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public int size() {
        return set.size();
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        set.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return set.contains(p);
    }

    public void draw() {
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Point2D p : set) {
            StdDraw.point(p.x(), p.y());
        }
        StdDraw.show();
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        return new IterableRange(rect);
    }

    private class IterableRange implements Iterable<Point2D> {
        private final RectHV r;

        public IterableRange(RectHV rect) {
            r = rect;
        }

        public Iterator<Point2D> iterator() {
            return new RangeIterator(r);
        }
    }

    private class RangeIterator implements Iterator<Point2D> {
        // private int count;
        private Queue<Point2D> q = new Queue<Point2D>();

        public RangeIterator(RectHV r) {
            for (Point2D p : set) {
                if (r.contains(p)) {
                    // count++;
                    q.enqueue(p);
                }
            }
        }

        public boolean hasNext() {
            return !q.isEmpty();
        }

        public Point2D next() {
            if (!hasNext()) throw new NoSuchElementException();
            return q.dequeue();
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        else {
            Point2D closest = null;
            for (Point2D that : set) {
                if (closest == null) closest = that;
                else if (p.distanceSquaredTo(that) < p.distanceSquaredTo(closest)) {
                    closest = that;
                }
            }
            return closest;
        }
    }

    private static void insertRandomPoints(PointSET ps, int num) {
        double x, y;
        for (int i = 0; i < num; i++) {
            x = StdRandom.uniform();
            y = StdRandom.uniform();
            ps.insert(new Point2D(x, y));
        }
    }

    public static void main(String[] args) {
        PointSET ps = new PointSET();
        /*
        ps.insert(new Point2D(0.3, 0.3));
        ps.insert(new Point2D(0.4, 0.4));
        ps.insert(new Point2D(0.5, 0.5));
        ps.insert(new Point2D(0.6, 0.6));
        ps.insert(new Point2D(0.7, 0.7));
         */
        insertRandomPoints(ps, 20);
        Point2D x = new Point2D(0.5, 0.5);
        RectHV r = new RectHV(0.3, 0.3, 0.8, 0.8);
        for (Point2D p : ps.range(r)) {
            StdOut.println(p);
        }
        StdOut.println("nearest to " + x + "\nis         " + ps.nearest(x));

        r.draw();
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(0.01);
        x.draw();
        ps.draw();
    }
}
