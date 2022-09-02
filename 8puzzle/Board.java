/* *****************************************************************************
 *  Name: Felix Zhu
 *  Date: 9/29/20
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Board {

    private int n;
    private int[][] tiles;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;   // length is only for one dimension
        this.tiles = new int[n][n];
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                this.tiles[r][c] = tiles[r][c];
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int[][] goal = new int[n][n];
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                goal[r][c] = r * n + (c + 1);
            }
        }
        goal[n - 1][n - 1] = 0;
        int misplaced = 0;
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                if (tiles[r][c] != goal[r][c] && tiles[r][c] != 0) misplaced++;
            }
        }
        return misplaced;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int[][] goal = new int[n][n];
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                goal[r][c] = r * n + (c + 1);
            }
        }
        goal[n - 1][n - 1] = 0;
        int sum = 0;
        int row;  // row and col are for goal position
        int col;  // of any given tile; in 3x3, tile 5 -> row=1, col=1
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                if (tiles[r][c] != goal[r][c] && tiles[r][c] != 0) {
                    row = (tiles[r][c] - 1) / n;
                    col = (tiles[r][c] - 1) % n;
                    sum += Math.abs(row - r) + Math.abs(col - c);
                    // StdOut.println("inspecting tile " + tiles[r][c] + ", sum is now " + sum);
                }
            }
        }
        return sum;
    }

    // is this board the goal board?
    public boolean isGoal() {
        int[][] goal = new int[n][n];
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                goal[r][c] = r * n + (c + 1);
            }
        }
        goal[n - 1][n - 1] = 0;
        Board goalBoard = new Board(goal);
        return this.equals(goalBoard);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (!(y instanceof Board)) return false;

        Board b = (Board) y;
        if (this.n != b.n) return false;
        boolean isEqual = true;
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                if (this.tiles[r][c] != b.tiles[r][c]) isEqual = false;
            }
        }
        return isEqual;
    }

    private void swapTiles(int r1, int c1, int r2, int c2) {
        int temp = tiles[r1][c1];
        tiles[r1][c1] = tiles[r2][c2];
        tiles[r2][c2] = temp;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return new NeighborsIterable(this);
    }

    private class NeighborsIterable implements Iterable<Board> {

        private Board b;

        public NeighborsIterable(Board board) {
            b = board;
        }

        public Iterator<Board> iterator() {
            return new NeighborsIterator(b);
        }
    }

    private class NeighborsIterator implements Iterator<Board> {

        private int zeroRow, zeroCol;
        private Board board;
        private int counter;
        private int side;  // start is 1 2 3 or 4, increment clockwise

        //    1
        // 4  X  2
        //    3

        public NeighborsIterator(Board b) {
            board = b;
            for (int r = 0; r < b.n; r++) {
                for (int c = 0; c < b.n; c++) {
                    if (board.tiles[r][c] == 0) {
                        zeroRow = r;
                        zeroCol = c;
                        if ((r == 0 && c == 0) || (r == b.n - 1 && c == 0) ||
                                (r == b.n - 1 && c == b.n - 1) || (r == 0 && c == b.n - 1)) {
                            counter = 2;
                            if (r == 0 && c == 0) side = 2;
                            if (r == b.n - 1 && c == 0) side = 1;
                            if (r == b.n - 1 && c == b.n - 1) side = 4;
                            if (r == 0 && c == b.n - 1) side = 3;
                        }
                        else if (r == 0 || r == n - 1 || c == 0 || c == n - 1) {
                            counter = 3;
                            if (r == 0) side = 2;
                            if (r == b.n - 1) side = 4;
                            if (c == 0) side = 1;
                            if (c == b.n - 1) side = 3;
                        }
                        else {
                            counter = 4;
                            side = 1;
                        }
                    }
                }
            }
        }

        public boolean hasNext() {
            return counter > 0;
        }

        public Board next() {
            if (!hasNext()) throw new NoSuchElementException();
            Board copyBoard = new Board(new int[board.n][board.n]);
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < n; c++) {
                    copyBoard.tiles[r][c] = board.tiles[r][c];
                }
            }
            if (side == 1) copyBoard.swapTiles(zeroRow, zeroCol, zeroRow - 1, zeroCol);
            if (side == 2) copyBoard.swapTiles(zeroRow, zeroCol, zeroRow, zeroCol + 1);
            if (side == 3) copyBoard.swapTiles(zeroRow, zeroCol, zeroRow + 1, zeroCol);
            if (side == 4) copyBoard.swapTiles(zeroRow, zeroCol, zeroRow, zeroCol - 1);
            side++;
            if (side == 5) side = 1;
            counter--;
            return copyBoard;
        }
    }

    // a board that is obtained by exchanging any pair of tiles
    // is it supposed to be random???
    public Board twin() {
        int R1 = -1;
        int C1 = -1;
        int R2 = -1;
        int C2 = -1;
        Board twinBoard = new Board(new int[n][n]);
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                twinBoard.tiles[r][c] = tiles[r][c];
                if (R1 == -1 && tiles[r][c] != 0) {
                    R1 = r;
                    C1 = c;
                }
                else if (R2 == -1 && tiles[r][c] != 0) {
                    R2 = r;
                    C2 = c;
                }
            }
        }
        twinBoard.swapTiles(R1, C1, R2, C2);
        return twinBoard;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // int[][] x = new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
        int[][] y = new int[][] { { 3, 0, 7 }, { 2, 4, 8 }, { 6, 1, 5 } };
        // Board b = new Board(x);
        Board c = new Board(y);
        StdOut.println(c);
        StdOut.println("twin: " + c.twin());
        StdOut.println("twin: " + c.twin());
        StdOut.println("twin: " + c.twin());
        StdOut.println("twin: " + c.twin());
        StdOut.println("twin: " + c.twin());

        // for (Board b : c.neighbors()) {
        // StdOut.println(b);
        // }
    }
}
