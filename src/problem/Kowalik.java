package problem;

public class Kowalik extends BlackBoxProblem {

    private static final double b[] = {
            4.0, 2.0, 1.0, 1/2.0,1/4.0,1/6.0,1/8.0,
            1/10.0,1/12.0,1/14.0, 1/16.0 };
    private static final double a[] = {
            0.1957, 0.1947, 0.1735, 0.1600, 0.0844,
            0.0627, 0.0456, 0.0342, 0.0323, 0.0235,
            0.0246 };

    public Kowalik() {
        super(0, 5);
        dim = 4;
    }

    @Override
    public double evaluate(double[] X, int dim) {
        int i;
        double sum;
        double bb, t;
        sum = 0.0;
        for (i = 0; i < 11; i++) {
            bb = b[i] * b[i];
            t = a[i]-(X[0]*(bb+b[i]*X[1])/(bb+b[i]*X[2]+X[3]));
            sum += t * t;
        }
        return sum;

    }
}
