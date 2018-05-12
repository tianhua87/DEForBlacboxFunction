package problem;

public class Booth extends BlackBoxProblem {
    public Booth() {
        super(-10, 10);
        dim = 2;
    }

    @Override
    public double evaluate(double[] x, int dim) {
        return Math.pow(x[0] + 2*x[1] - 7, 2) +
                Math.pow(2 * x[0] + x[1] - 5, 2);
    }
}
