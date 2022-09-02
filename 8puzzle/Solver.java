/* *****************************************************************************
 *  Name: Felix Zhu
 *  Date: 9/30/20
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Solver {

    private MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
    private MinPQ<SearchNode> pqSwap = new MinPQ<SearchNode>();
    private int totalMoves;
    private boolean solvable;
    private SearchNode finalNode;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        pq.insert(new SearchNode(initial, null));
        pqSwap.insert(new SearchNode(initial.twin(), null));
        SearchNode min;
        SearchNode minSwap;
        int totalMovesOriginal = -1;
        int totalMovesSwap = -1;
        while (true) {
            /*
            for (SearchNode sn : pq) {
                StdOut.println(sn);
            }
            */
            min = pq.delMin();
            minSwap = pqSwap.delMin();
            if (min.board.isGoal()) {
                totalMovesOriginal = min.moves;
                finalNode = min;
                // StdOut.println("Goal reached for original in " + totalMovesOriginal + " moves");
                break;
            }
            if (minSwap.board.isGoal()) {
                totalMovesSwap = minSwap.moves;
                finalNode = null;
                // StdOut.println("Goal reached for swap in " + totalMovesSwap + " moves");
                break;
            }
            for (Board b : min.board.neighbors()) {
                if (min.prev == null) {
                    pq.insert(new SearchNode(b, min));
                }
                else if (!min.prev.board.equals(b)) {
                    pq.insert(new SearchNode(b, min));
                }
            }
            for (Board b : minSwap.board.neighbors()) {
                if (minSwap.prev == null) {
                    pqSwap.insert(new SearchNode(b, minSwap));
                }
                else if (!minSwap.prev.board.equals(b)) {
                    pqSwap.insert(new SearchNode(b, minSwap));
                }
            }
        }
        if (totalMovesOriginal == -1) {
            solvable = false;
            totalMoves = totalMovesSwap;
        }
        else {
            solvable = true;
            totalMoves = totalMovesOriginal;
        }
    }

    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int moves, manhattan, priority;
        private SearchNode prev;

        public SearchNode(Board b, SearchNode p) {
            board = b;
            if (p == null) moves = 0;
            else moves = p.moves + 1;
            prev = p;
            manhattan = board.manhattan();
            priority = moves + manhattan;
        }

        public int compareTo(SearchNode that) {
            if (this.priority > that.priority) return 1;
            else if (this.priority < that.priority) return -1;
            else return 0;
        }

        public String toString() {
            String str = "priority  = " + priority + "\nmoves     = " + moves + "\nmanhattan = "
                    + manhattan;
            return str + "\n" + board.toString();
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) return -1;
        else return totalMoves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        else return new IterableSolution(finalNode, totalMoves);
    }

    private class IterableSolution implements Iterable<Board> {
        private SearchNode sn;
        private int tm;

        public IterableSolution(SearchNode node, int totalMoves) {
            sn = node;
            tm = totalMoves;
        }

        public Iterator<Board> iterator() {
            return new SolutionIterator(sn, tm);
        }
    }

    private class SolutionIterator implements Iterator<Board> {

        private SearchNode currNode;
        private Board[] solutionSteps;
        private int counter = 0;

        public SolutionIterator(SearchNode finalNode, int totalMoves) {
            currNode = finalNode;
            solutionSteps = new Board[totalMoves + 1];
            for (int i = totalMoves; i > -1; i--) {
                solutionSteps[i] = currNode.board;
                if (currNode.prev != null) currNode = currNode.prev;
            }
        }

        public boolean hasNext() {
            return counter < solutionSteps.length;
        }

        public Board next() {
            if (!hasNext()) throw new NoSuchElementException();
            return solutionSteps[counter++];
        }
    }

    // test client
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // int[][] y = new int[][] { { 1, 2, 3, 4 }, { 5, 6, 7, 8 }, { 9, 10, 11, 12 }, { 13, 15, 14, 0 } };
        // Board b = new Board(x);
        // Board c = new Board(y);
        Solver solver = new Solver(initial);
        StdOut.println("solvable? " + solver.isSolvable());
        /*for (Board b : solver.solution()) {
            StdOut.println(b);
        }*/
    }

}
