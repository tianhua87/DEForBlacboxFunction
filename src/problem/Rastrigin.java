package problem;

public class Rastrigin extends BlackBoxProblem {
    public Rastrigin() {
        super(-5.12, 5.12);
        dim = 10;
    }

    @Override
    public double evaluate(double[] X, int dim) {
        int i;
        double sum, t;
        if (dim > 20) {
            dim = 20;
        }
        sum = 0.0;
        for (i = 0; i < dim; i++) {
            t = X[i];
            sum += t*t - 10*Math.cos(2.0 * Math.PI * X[i]);
        }
        return sum + dim*10;
    }
}
