package problem;

public class Shekel2 extends BlackBoxProblem {
    public Shekel2() {
        super(-66, 66);
        dim = 2;
    }

    private static final double a[][] = {
            {-32,-16,0,16,32,-32,-16,0,16,32,-32,-16,0, 16,32,-32,-16,0,16,32,-32,-16,0,16,32},
            {-32,-32,-32,-32,-32,-16,-16,-16,-16,-16,0, 0,0,0,0,16,16,16,16,16,32,32,32,32,32}
    };

    @Override
    public double evaluate(double[] X, int dim) {
        int j;
        double s, t0, t1;

        s = 0.0;
        for (j = 0; j < 25; j++) {
            t0 = X[0] - a[0][j];
            t1 = X[1] - a[1][j];
            t0 = (t0 * t0 * t0);
            t0 *= t0 * t0;
            t1 = (t1 * t1 * t1);
            t1 = t1 * t1;
            s += 1.0/ ((double) j + t0 + t1);
        }
        return 1.0 / (1.0/500.0 + s);

    }
}
