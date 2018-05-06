package problem;

public class Trecanni extends BlackBoxProblem {
    public Trecanni(){
        super(-5,5);
        dim = 2;
    }
    @Override
    public double evaluate(double[] x, int dim) {
        return x[0]*x[0]*x[0]*x[0]+4*x[0]*x[0]*x[0]+4*x[0]*x[0]+x[1]*x[1];
    }

}
