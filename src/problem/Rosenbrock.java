package problem;

public class Rosenbrock extends BlackBoxProblem {
    public Rosenbrock(){
        super(-10,10);
        dim = 2;
    }
    @Override
    public double evaluate(double[] x, int dim) {
        double a = x[1]-x[0]*x[0] ;
        double b = 1.0-x[0] ;
        return 100.0*a*a + b*b ;
    }

}
