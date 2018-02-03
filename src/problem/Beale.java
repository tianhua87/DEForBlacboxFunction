package problem;

public class Beale extends BlackBoxProblem {
    public Beale() {
        super(-4.5, 4.5);
        dim = 2;
    }

    @Override
    public double evaluate(double[] X, int dim) {
        return Math.pow(1.5 - X[0] + X[0]*X[1], 2) +
                Math.pow(2.25 - X[0] + X[0] * X[1]*X[1], 2) +
                Math.pow(2.625 - X[0] + X[0] * Math.pow(X[1], 3), 2);
    }
}
