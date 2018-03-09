package problem;

public class Branin extends BlackBoxProblem {
    public Branin() {
        super(-5, 15);
        dim = 2;
    }

    @Override
    public double evaluate(double[] X, int dim) {
        double s, x0;
        x0 = X[0];
        s = X[1] -
                ( 5.1/(4.0*Math.PI*Math.PI) * x0 - 5.0/Math.PI) * x0 - 6.0;
        return s*s + 10*(1.0 - 1.0/(8.0*Math.PI)) * Math.cos(x0) + 10.0;
    }
}
