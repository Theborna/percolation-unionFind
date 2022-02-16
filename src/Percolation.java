import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF union;
    private boolean[][] openGrid; // if open true else false
    private final int n;
    private final int primaryNode, secondaryNode;
    private int openSights;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 1)
            throw new IllegalArgumentException(n + " is not a positive integer");
        this.n = n;
        primaryNode = n * n;
        secondaryNode = n * n + 1;
        union = new WeightedQuickUnionUF(n * n + 2); // the last two are used to simplify percolation calculation
        openGrid = new boolean[n][n];
        openSights = 0;
        for (int i = 0; i < n; i++) {
            union.union(primaryNode, i);
            union.union(secondaryNode, n * n - 1 - i);
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (isOpen(row, col))
            return;
        openGrid[row - 1][col - 1] = true;
        openSights++;
        int place = place(row, col);
        if (col != n)
            if (isOpen(row, col + 1))
                union.union(place, place + 1);
        if (col != 1)
            if (isOpen(row, col - 1))
                union.union(place, place - 1);
        if (row != n)
            if (isOpen(row + 1, col))
                union.union(place, place + n);
        if (row != 1)
            if (isOpen(row - 1, col))
                union.union(place, place - n);
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row > n || row < 1)
            throw new IllegalArgumentException("row " + row + " is not in range 1 to " + n + " .");
        if (col > n || col < 1)
            throw new IllegalArgumentException("col " + col + " is not in range 1 to " + n + " .");
        return union.find(primaryNode) == union.find(place(row, col));
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row > n || row < 1)
            throw new IllegalArgumentException("row " + row + " is not in range 1 to " + n + " .");
        if (col > n || col < 1)
            throw new IllegalArgumentException("col " + col + " is not in range 1 to " + n + " .");
        return openGrid[row - 1][col - 1];

    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSights;
    }

    // does the system percolate?
    public boolean percolates() {
        return union.find(primaryNode) == union.find(secondaryNode);
    }

    private void draw() {
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++)
                System.out.print(isOpen(i, j) + " ");
            System.out.println();
        }
        System.out.println();
    }

    private int place(int row, int col) {
        return (col - 1) + n * (row - 1);
    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        Percolation map = new Percolation(n);
        map.open(3, 3);
        map.open(3, 4);
        map.draw();
        System.out.println(map.percolates());
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++)
                map.open(i, j);
        }
        map.draw();
        System.out.println(map.percolates());
        System.out.println(map.numberOfOpenSites());
    }
}
