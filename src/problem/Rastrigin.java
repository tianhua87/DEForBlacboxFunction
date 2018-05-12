package problem;

public class Rastrigin extends BlackBoxProblem {
    public Rastrigin() {
        super(-5.12, 5.12);
        dim = 2;
    }

    @Override
    public double evaluate(double[] x, int dim) {
        double t;
        t = x[0];
        return 2.0 + t*t - Math.cos(18 * t) - Math.cos(18 * x[1]);
    }
}
