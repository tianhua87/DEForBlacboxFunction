package problem;

public class Matyas extends BlackBoxProblem {
    public Matyas(){
        super(-10,10);
        dim = 2;
    }
    @Override
    public double evaluate(double[] x, int dim) {
        return 0.26 * (x[0]*x[0] + x[1]*x[1]) - 0.48 * x[0] * x[1];
    }

}
