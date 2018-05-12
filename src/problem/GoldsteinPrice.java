package problem;

public class GoldsteinPrice extends BlackBoxProblem {
    public GoldsteinPrice() {
        super(-2, 2);
        dim = 2;
    }

    @Override
    public double evaluate(double[] x, int dim) {
        return (1.0+Math.pow(x[0]+x[1]+1.0,2)*
                (19.0-14.0*x[0]+3.0*x[0]*x[0]-14.0*x[1]
                        +6.0*x[0]*x[1]+3.0*x[1]*x[1]))*
                (30.0+Math.pow(2.0*x[0]-3.0*x[1],2)*
                        (18.0-32.0*x[0]+12.0*x[0]*x[0]+48.0*x[1]
                                -36.0*x[0]*x[1]+27.0*x[1]*x[1]));
    }
}
