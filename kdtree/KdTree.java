/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class KdTree {

    private Node root;
    private int size;

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the lb/bottom subtree
        private Node rt;        // the right/top subtree
    }

    private class Alternator {
        private boolean isEvenDepth;
        private double xmin, ymin, xmax, ymax; // alt is also for Rect lmfao

        public Alternator() {
            isEvenDepth = true;
            xmin = 0;
            ymin = 0;
            xmax = 1;
            ymax = 1;
        }

        public void switchParity() {
            isEvenDepth = !isEvenDepth;
        }

        public void setXmin(double val) {
            xmin = val;
        }

        public void setYmin(double val) {
            ymin = val;
        }

        public void setXmax(double val) {
            xmax = val;
        }

        public void setYmax(double val) {
            ymax = val;
        }
    }

    public KdTree() {
        size = 0;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Alternator alt = new Alternator();
        root = insertHelper(root, p, alt);
        size++;
    }

    private Node insertHelper(Node h, Point2D p, Alternator alt) {
        if (h == null) {
            h = new Node();
            h.p = p;
            h.rect = new RectHV(alt.xmin, alt.ymin, alt.xmax, alt.ymax);
            return h;
        }
        if (alt.isEvenDepth) {
            alt.switchParity();
            if (p.x() == h.p.x() && p.y() == h.p.y()) {
                h.p = p;
                size--;
            }
            else if (p.x() < h.p.x()) {
                alt.setXmax(h.p.x());
                h.lb = insertHelper(h.lb, p, alt);
            }
            else if (p.x() >= h.p.x()) {
                alt.setXmin(h.p.x());
                h.rt = insertHelper(h.rt, p, alt);
            }
            return h;
        }
        else {
            alt.switchParity();
            if (p.x() == h.p.x() && p.y() == h.p.y()) {
                h.p = p;
                size--;
            }
            else if (p.y() < h.p.y()) {
                alt.setYmax(h.p.y());
                h.lb = insertHelper(h.lb, p, alt);
            }
            else if (p.y() >= h.p.y()) {
                alt.setYmin(h.p.y());
                h.rt = insertHelper(h.rt, p, alt);
            }
            return h;
        }
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Alternator alt = new Alternator();
        return get(root, p, alt) != null;
    }

    private Point2D get(Node h, Point2D p, Alternator alt) {
        if (h == null) return null;
        if (alt.isEvenDepth) {
            alt.switchParity();
            if (p.x() == h.p.x() && p.y() == h.p.y()) return h.p;
            else if (p.x() < h.p.x()) return get(h.lb, p, alt);
            else if (p.x() >= h.p.x()) return get(h.rt, p, alt);
            else return null; // ???
        }
        else {
            alt.switchParity();
            if (p.x() == h.p.x() && p.y() == h.p.y()) return h.p;
            else if (p.y() < h.p.y()) return get(h.lb, p, alt);
            else if (p.y() >= h.p.y()) return get(h.rt, p, alt);
            else return null; // ???
        }
    }

    public void draw() {
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.rectangle(0.5, 0.5, 0.5, 0.5);
        Alternator alt = new Alternator();
        drawTraversePreOrder(root, alt);
        StdDraw.show();
    }

    private void traverseInOrder(Node h) {   // don't need this method
        if (h.lb != null) traverseInOrder(h.lb);
        StdOut.println("found point: " + h.p + " with rect: " + h.rect);
        if (h.rt != null) traverseInOrder(h.rt);
    }

    private void drawTraversePreOrder(Node h, Alternator alt) {
        StdDraw.setPenRadius(0.02);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(h.p.x(), h.p.y());
        if (alt.isEvenDepth) {
            StdDraw.setPenRadius(0.005);
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(h.p.x(), h.rect.ymin(), h.p.x(), h.rect.ymax());
        }
        else {
            StdDraw.setPenRadius(0.005);
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(h.rect.xmin(), h.p.y(), h.rect.xmax(), h.p.y());
        }

        if (h.lb != null) {
            alt.switchParity();
            drawTraversePreOrder(h.lb, alt);
        }
        else alt.switchParity();

        if (h.rt != null) {
            drawTraversePreOrder(h.rt, alt);
            alt.switchParity();
        }
        else alt.switchParity();
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        else return new IterableRange(rect);
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
            rangeSearch(root, r);
        }

        private void rangeSearch(Node h, RectHV r) {
            if (h.lb != null && r.intersects(h.lb.rect)) rangeSearch(h.lb, r);
            if (r.contains(h.p)) q.enqueue(h.p);
            if (h.rt != null && r.intersects(h.rt.rect)) rangeSearch(h.rt, r);
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
            Point2D nearest = root.p;   // initialize temp nearest as root point
            return nearestSearch(root, p, nearest);
        }
    }

    private Point2D nearestSearch(Node h, Point2D p, Point2D nearest) {
        // if (h.lb != null && h.rt != null) {
        //
        // }
        if (h.lb != null && nearest.distanceSquaredTo(p) > h.lb.rect.distanceSquaredTo(p)) {
            // StdOut.println("going left");
            nearest = nearestSearch(h.lb, p, nearest);
        }

        if (h.rt != null && nearest.distanceSquaredTo(p) > h.rt.rect.distanceSquaredTo(p)) {
            // StdOut.println("going right");
            nearest = nearestSearch(h.rt, p, nearest);
        }
        if (h.p.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
            // StdOut.println("found new nearest: " + h.p);
            nearest = h.p;
        }
        return nearest;
    }

    public static void main(String[] args) {
        // KdTree tree = new KdTree();
        // Point2D a = new Point2D(0.7, 0.2);
        // Point2D b = new Point2D(0.5, 0.4);
        // Point2D c = new Point2D(0.2, 0.3);
        // Point2D d = new Point2D(0.4, 0.7);
        // Point2D e = new Point2D(0.9, 0.6);
        // Point2D query = new Point2D(1, 1);
        // Point2D x = new Point2D(1, 0);
        // tree.insert(a);
        // tree.insert(b);
        // tree.insert(c);
        // tree.insert(d);
        // tree.insert(e);
        // tree.insert(x);
        // RectHV rect = new RectHV(0, 0, 1, 1);
        // rect.draw();
        // query.draw();
        // tree.draw();
        // tree.traverseInOrder(tree.root);
        // StdOut.println("contains x? " + tree.contains(x));
        // StdOut.println("contains query? " + tree.contains(query));
        // StdOut.println("contains a? " + tree.contains(a));
        // StdOut.println("contains b? " + tree.contains(b));
        // StdOut.println("contains c? " + tree.contains(c));
        // StdOut.println("contains d? " + tree.contains(d));
        // StdOut.println("contains e? " + tree.contains(e));
        // StdOut.println();
        // for (Point2D p : tree.range(rect)) {
        //     StdOut.println("point in range: " + p);
        // }
        // StdOut.println("nearest to " + query + " is " + tree.nearest(query));

        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            StdOut.println(kdtree.contains(new Point2D(x, y)));
        }
        Point2D query = new Point2D(0.81, 0.3);
        kdtree.draw();
        StdOut.println("nearest to " + query + " is " + kdtree.nearest(query));

    }
}
