package problem;

public class Bohachevsky1 extends BlackBoxProblem {
    public Bohachevsky1() {
        super(50, -50);
        dim = 2;
    }

    @Override
    public double evaluate(double[] X, int dim) {
        return X[0] * X[0] + 2.0 * X[1]*X[1]
                - 0.3 * Math.cos(3.0 * Math.PI * X[0])
                - 0.4 * Math.cos(4.0 * Math.PI * X[1])
                + 0.7;
    }
}
