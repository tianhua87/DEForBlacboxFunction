package problem;

public class Exp2 extends BlackBoxProblem {
    public Exp2() {
        super(0, 20);
        dim = 2;
    }

    @Override
    public double evaluate(double[] x, int dim) {
        int i;
        double sum = 0.0;
        double t;
        for (i = 0; i < 10; i ++) {
            t = Math.exp(-i * x[0] / 10.0)
                    - 5 * Math.exp(- i * x[1] * 10) - Math.exp(-i / 10.0)
                    + 5 * Math.exp(-i);
            sum += t * t;
        }
        return sum;
    }
}
