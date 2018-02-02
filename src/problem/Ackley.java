package problem;

public class Ackley implements BlackBoxProblem {
    @Override
    public double evaluate(double[] x, int dim) {
        int i;
        double t, s1, s2;
        s1 = s2 = 0.0;
        for (i = 0; i < dim; i++) {
            t = x[i];
            s1 += t * t;
            s2 += Math.cos(2.0*Math.PI * x[i]);
        }
        return -20.0 * Math.exp(-0.2 * Math.sqrt(s1/dim)) - Math.exp(s2 / dim) +
                20.0 + Math.E ;
    }

}
