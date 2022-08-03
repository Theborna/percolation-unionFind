import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE = 1.96;
    private final double[] p;
    private final int trials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        validate(n, trials);
        this.trials = trials;
        p = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation mapTrial = new Percolation(n);
            while (!mapTrial.percolates()) {
                int randomRow = StdRandom.uniform(n) + 1, randomCol = StdRandom.uniform(n) + 1;
                mapTrial.open(randomRow, randomCol);
            }
            p[i] = mapTrial.numberOfOpenSites() / (double) (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(p);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(p);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - CONFIDENCE * stddev() / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + CONFIDENCE * stddev() / Math.sqrt(trials);
    }

    private void validate(int n, int trials) {
        if (n < 1)
            throw new IllegalArgumentException(n + " is not a positive integer");
        if (trials < 1)
            throw new IllegalArgumentException(trials + " is not a positive integer");
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]), trials = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, trials);
        System.out.println("mean                    = " + stats.mean());
        System.out.println("stddev                  = " + stats.stddev());
        System.out.println("95% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }
}