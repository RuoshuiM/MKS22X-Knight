/**
 *
 */


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * A knights tour:
 *
 * Selecting a series of moves for a knight such that each square is visited
 * exactly once. If the knight ends on a square that is reachable by a knight's
 * move from the beginning square, the tour is closed, otherwise it is open.
 *
 * <p>
 * Note:
 * <ul>
 * <li>The board dimensions do not have to be square.</li>
 * <li>The starting square counts as visited.</li>
 * <li>You do not have to come back to where you started. Closed tours take much
 * longer to find (potentially)</li>
 * </ul>
 * </p>
 *
 * <p>
 * Not all board sizes have solutions. A 2 by N board has no closed or open
 * tour. From Wikipedia: Any m �� n board with m �� n, a closed knight's tour
 * is always possible unless one or more of these three conditions are met:
 * <ol>
 * <li>m and n are both odd</li>
 * <li>m = 1, 2, or 4</li>
 * <li>m = 3 and n = 4, 6, or 8.</li>
 * </ol>
 * It follows that if a closed tour is possible, then an open tour is also
 * possible.
 * </p>
 */
public class KnightBoard {

    private int[][] board;
    int[][] moves;
    private boolean movesFilled = false;
    private int rows;
    private int cols;
    private int maxLevel;

    /**
     * A helper class to represent a coordinate on the KnightBoard
     *
     * @author ruosh
     *
     */
    /*
     * private class Cor implements Comparable<Cor> { private final int x; private
     * final int y; private int validMoves;
     *
     * public Cor(int x, int y) { this.x = x; this.y = y; }
     *
     * public Cor(int x, int y, boolean b) { this.x = x; this.y = y; if (b) {
     * this.validMoves = moves[x][y]; } }
     *
     * public int getX() { return this.x; }
     *
     * public int getY() { return this.y; }
     *
     * public int getMoves() { return this.validMoves; }
     *
     * public void setMoves(int moves) { this.validMoves = moves; }
     *
     * @Override public int compareTo(Cor thatCoor) { return
     * Integer.valueOf(this.validMoves).compareTo(Integer.valueOf(thatCoor.
     * validMoves)); } }
     */

    private static final int[] drow = new int[] { 1, -1, 1, -1, 2, -2, 2, -2 };
    private static final int[] dcol = new int[] { 2, 2, -2, -2, 1, 1, -1, -1 };

    class Cors {
        private final int row;
        private final int col;
        private int[] order = new int[] { 0, 1, 2, 3, 4, 5, 6, 7 };

        public Cors(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public int getRow(int i) {
            if (i < 0 || i > 7) {
                throw new IllegalArgumentException("Cor must be from 0 - 7");
            }

            return row + drow[order[i]];
        }

        public int getCol(int i) {
            if (i < 0 || i > 7) {
                throw new IllegalArgumentException("Cor must be from 0 - 7");
            }
            return col + dcol[order[i]];
        }
        
//        /* Not Used. Manual sorting instead.
        public void sortMoveOrder() {
          // System.out.println("Sorting...");
            Integer[] o = new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7 };
            Arrays.sort(o, new movesComparator());
            int[] order = new int[8];
            for (int i = 0; i < o.length; i++) {
              // auto-unboxing of Integer
                order[i] = o[i];
            }
            this.order = order;
            // System.out.println("End Sorting");
        }


        private class movesComparator implements Comparator<Integer> {

            @Override
            public int compare(Integer o1, Integer o2) {
              int row1 = Cors.this.getRow(o1);
              int col1 = Cors.this.getCol(o1);
              int row2 = Cors.this.getRow(o2);
              int col2 = Cors.this.getCol(o2);
              if (KnightBoard.this.isCorInBound(row1, col1) && KnightBoard.this.isCorInBound(row2, col2)) {
                return Integer.valueOf(KnightBoard.this.moves[row1][col1])
                        .compareTo(Integer.valueOf(KnightBoard.this.moves[row2][col2]));
              } else {
                return 0;
              }
            }

        }
//        */

    }

    /**
     * Initialize the board to the correct size and make them all 0's
     *
     * @throws IllegalArgumentException when either parameter is negative.
     */
    public KnightBoard(int startingRows, int startingCols) {
        if (startingRows <= 0 || startingCols <= 0) {
            throw new IllegalArgumentException("Size of the board must be positive");
        }

        this.board = new int[startingRows][startingCols];
        this.rows = startingRows;
        this.cols = startingCols;
        this.maxLevel = rows * cols + 1;
    }

    private boolean isCorInBound(int row, int col) {
        return !(row >= rows || col >= cols || row < 0 || col < 0);
    }

    private void clearBoard() {
        board = new int[rows][cols];
    }

    private void checkBoard() {
        if (!Arrays.deepEquals(board, new int[rows][cols])) {
            throw new IllegalStateException("Board must be empty");
        }
    }

    private void checkInputs(int row, int col) {
        if (!isCorInBound(row, col)) {
            throw new IllegalArgumentException("Board dimension must be nonnegative and in bounds");
        }
    }

    private boolean addKnight(int row, int col, int level) {
        if (!isCorInBound(row, col)) {
            return false;
        } else if (board[row][col] != 0) {
            return false;
        } else {
            board[row][col] = level;
            return true;
        }
    }

    private boolean removeKnight(int row, int col, int level) {
        if (!isCorInBound(row, col)) {
            return false;
        } else if (board[row][col] != level) {
            return false;
        } else {
            board[row][col] = 0;
            return true;
        }
    }

    private boolean addKnightWithMoves(int row, int col, int level) {
        if (!this.movesFilled) {
            throw new IllegalStateException("Moves board not filled");
        }

        if (!addKnight(row, col, level))
            return false;
        else {
            Cors cors = new Cors(row, col);
            for (int k = 0, i, j; k < 8; k++) {
                i = cors.getRow(k);
                j = cors.getCol(k);
                if (isCorInBound(i, j)) {
                    this.moves[i][j]--;
                }
            }
            return true;
        }
    }

    private boolean removeKnightWithMoves(int row, int col, int level) {
        if (!this.movesFilled) {
            throw new IllegalStateException("Moves board not filled");
        }

        if (!removeKnight(row, col, level))
            return false;
        else {
            Cors cors = new Cors(row, col);
            for (int k = 0, i, j; k < 8; k++) {
                i = cors.getRow(k);
                j = cors.getCol(k);
                if (isCorInBound(i, j)) {
                    this.moves[i][j]++;
                }
            }
            return true;
        }
    }

    // TODO: Optimize
    private void fillMoves() {
        if (this.moves == null) {
            this.moves = new int[rows][cols];
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Cors cors = new Cors(i, j);
                int moves = 0;
                for (int k = 0; k < 8; k++) {
                    int row = cors.getRow(k);
                    int col = cors.getCol(k);
                    if (isCorInBound(row, col)) {
                        moves++;
                    }
                }
                this.moves[i][j] = moves;
            }
        }
        this.movesFilled = true;
    }

    /**
     * Modifies the board by labeling the moves from 1 (at startingRow,startingCol)
     * up to the area of the board in proper knight move steps.
     *
     * @throws IllegalStateException    when the board contains non-zero values.
     * @throws IllegalArgumentException when either parameter is negative or out of
     *                                  bounds.
     * @param startingRow
     * @param startingCol
     * @returns true when the board is solvable from the specified starting position
     */
    public boolean solve(int startingRow, int startingCol) {
        checkBoard();
        checkInputs(startingRow, startingCol);
        return nextMove(startingRow, startingCol, 1);
    }

    private boolean nextMove(int row, int col, int level) {
//        if (level == maxLevel) {
//            return true;
//        } else {
//            if (!addKnight(row, col, level)) {
//                return false;
//            }
//
//            int nextLevel = level + 1;
//            int nextRow, nextCol;
//
//            Cors cors = new Cors(row, col);
//            for (int i = 0; i < 8; i++) {
//
////                System.out.println(this);
//                nextRow = cors.getRow(i);
//                nextCol = cors.getCol(i);
////                System.out.println("cor: " + nextRow + "," + nextCol + "at level" + level);
//                if (nextMove(nextRow, nextCol, nextLevel)) {
//                    return true;
//                } else {
//                    removeKnight(nextRow, nextCol, nextLevel);
//                }
//            }
//            return false;
//        }
        if (!addKnight(row, col, level)) {
            return false;
        }

        int nextLevel = level + 1;

        if (nextLevel < maxLevel) {
            Cors cors = new Cors(row, col);
            int nextRow, nextCol;
            for (int i = 0; i < 8; i++) {
                nextRow = cors.getRow(i);
                nextCol = cors.getCol(i);
//               System.out.println("cor: " + nextRow + "," + nextCol + "at level" + level);
                // System.out.println(solutions);
//               System.out.println(this);
                if (nextLevel < maxLevel) {
                    if (nextMove(nextRow, nextCol, nextLevel)) {
                        return true; // ignore false value;
                    }
                }
            }
        } else {
            return true;
        }

        // finished with all possibilities, so this level is not possible at this
        // position
        removeKnight(row, col, level);
        return false;
    }

    public boolean fastSolve(int row, int col) {
        checkBoard();
        checkInputs(row, col);
        fillMoves();
         return fastNextMove1(row, col, 1);
    }


    public boolean fastNextMove(int row, int col, int level) {

        if (!addKnightWithMoves(row, col, level)) {
            return false;
        }

        int nextLevel = level + 1;

        if (nextLevel < maxLevel) {
            Cors cors = new Cors(row, col);
            cors.sortMoveOrder();
            int nextRow, nextCol;
            for (int i = 0; i < 8; i++) {
                nextRow = cors.getRow(i);
                nextCol = cors.getCol(i);
              // System.out.println("cor: " + nextRow + "," + nextCol + "at level" + level);
                // System.out.println(solutions);
//               System.out.println(this);
                if (nextLevel < maxLevel) {
                    if (fastNextMove(nextRow, nextCol, nextLevel)) {
                        return true; // ignore false value;
                    }
                }
            }
        } else {
            return true;
        }

        // finished with all possibilities, so this level is not possible at this
        // position
        removeKnightWithMoves(row, col, level);
        return false;
    }

    public boolean fastNextMove1(int row, int col, int level) {

        if (!addKnightWithMoves(row, col, level)) {
            return false;
        }

        int nextLevel = level + 1;

        if (nextLevel < maxLevel) {
            Cors cors = new Cors(row, col);
            List<int[]> order = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                int corRow = cors.getRow(i);
                int corCol = cors.getCol(i);
                if (isCorInBound(corRow, corCol)) {
                    int [] element = new int[] {i, moves[corRow][corCol]};
                    
                    if (order.size() == 0) {
                        order.add(element);
                    } else {
                        // insertion sort
                        for (int j = 0, s = order.size(); j < s; j++) {
                            int nextMoveCount = order.get(j)[1];
                            int curMoveCount = element[1];
                            //Stable vs. non-stable sort comparison
                            //1894509645
                            //6704765
                            if (curMoveCount < nextMoveCount || j == s - 1) {
                                order.add(j, element);
                                break;
                            }
                        }
                    }
                } else {
                    // cor is not in bound, ignore it
                    continue;
                }
            }
//            cors.sortMoveOrder();
            int nextRow, nextCol;
//            for (int i = 0; i < 8; i++) {
//                nextRow = cors.getRow(i);
//                nextCol = cors.getCol(i);
//              // System.out.println("cor: " + nextRow + "," + nextCol + "at level" + level);
//                // System.out.println(solutions);
////               System.out.println(this);
//                if (nextLevel < maxLevel) {
//                    if (fastNextMove(nextRow, nextCol, nextLevel)) {
//                        return true; // ignore false value;
//                    }
//                }
//            }
            
            for (int[] pos : order) {
                int i = pos[0];
                nextRow = cors.getRow(i);
                nextCol = cors.getCol(i);
              // System.out.println("cor: " + nextRow + "," + nextCol + "at level" + level);
                // System.out.println(solutions);
//               System.out.println(this);
                if (nextLevel < maxLevel) {
                    if (fastNextMove(nextRow, nextCol, nextLevel)) {
                        return true; // ignore false value;
                    }
                }
            }
        } else {
            return true;
        }

        // finished with all possibilities, so this level is not possible at this
        // position
        removeKnightWithMoves(row, col, level);
        return false;
    }
    
    /**
     * Count the number of solutions.
     *
     * @throws IllegalStateException    when the board contains non-zero values.
     * @throws IllegalArgumentException when either parameter is negative or out of
     *                                  bounds.
     * @param startingRow
     * @param startingCol
     * @returns the number of solutions from the starting position specified
     */
    public int countSolutions(int startingRow, int startingCol) {
        checkBoard();
        checkInputs(startingRow, startingCol);
        return subSolutions(startingRow, startingCol, 1);
    }

    // TODO: Optimize
    public int countAllSolutions() {
        int solutions = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                clearBoard();
                solutions += countSolutions(i, j);
            }
        }
        return solutions;
    }

    private int subSolutions(int row, int col, int level) {
        if (!addKnight(row, col, level)) {
            return 0;
        }

        int nextLevel = level + 1;
        int solutions = 0;

        if (nextLevel < maxLevel) {
            Cors cors = new Cors(row, col);
            int nextRow, nextCol;
            for (int i = 0; i < 8; i++) {
                nextRow = cors.getRow(i);
                nextCol = cors.getCol(i);
//               System.out.println("cor: " + nextRow + "," + nextCol + "at level" + level);
                // System.out.println(solutions);
//               System.out.println(this);
                if (nextLevel < maxLevel) {
                    solutions += subSolutions(nextRow, nextCol, nextLevel);
                }
            }
        } else {
            solutions += 1;
        }

        removeKnight(row, col, level);
        return solutions;
    }

    /**
     * (THESE ARE NOT VALID SOLUTIONS, They JUST TO DEMONSTRATE FORMAT)
     * <p>
     * Single spaces between numbers, but leading spaces on single digit numbers:
     *
     * <pre>
     1  2  5
     3  4  6
     7  8  9
     * </pre>
     *
     * Which is equivalant to: " 1 2 5\n 3 4 6\n 7 8 9\n"
     * </p>
     * <p>
     * When there are two digit numbers (rows*cols >= 10) Put a leading space in
     * front of single digit numbers: (spaces replaced with _ to show the
     * difference)
     *
     * <pre>
    _1 _2 15 _6
    _3 _4 _7 11
    _8 _9 10 12
    13 14 _5 16
     * </pre>
     *
     * So it looks like this:
     *
     * <pre>
     1  2 15  6
     3  4  7 11
     8  9 10 12
    13 14  5 16
     * </pre>
     * </p>
     * Blank boards display 0's as underscores.
     *
     */
    @Override
    public String toString() {
        return toString(board);
    }

    private String toString(int[][] board) {
        StringBuilder s = new StringBuilder();
        String format;

        if (rows * cols > 10) {
            // need string padding
            int length = (int) Math.floor(Math.log10(rows * cols)) + 1;
            format = "%" + length + "d";
        } else {
            format = "%d";
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                s.append(String.format(format, board[i][j])).append(" ");
            }
            s.delete(s.length(), s.length()).append("\n");
        }

        return s.toString();
    }

    public static void main(String... args) {
        int rows, cols;
        if (args.length != 0) {
            rows = Integer.parseInt(args[0]);
            cols = Integer.parseInt(args[1]);
        } else {
            rows = 25;
            cols = 25;
        }

        long time;

        KnightBoard kb = new KnightBoard(rows, cols);
        System.out.println(kb);
      time = System.nanoTime();
       // System.out.println(kb.solve(0, 0));
        // System.out.println(kb.fastSolve(0, 0));
        System.out.println(System.nanoTime() - time);

        kb.clearBoard();

        time = System.nanoTime();
       // System.out.println(kb.solve(0, 0));
        System.out.println(kb.fastSolve(0, 0));
        System.out.println(System.nanoTime() - time);


//        System.out.println(kb.countAllSolutions());
        System.out.println(kb);
//        kb.fillMoves();
//        System.out.println(kb.toString(kb.moves));
    }
}
