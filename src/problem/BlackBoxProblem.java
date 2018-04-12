package problem;

import algorithm.DE;

public abstract class BlackBoxProblem {

    public double lowLimit = Double.MIN_VALUE;
    public double highLimit = Double.MAX_VALUE;
    public int dim = 10;
    public String problemName ;

    public BlackBoxProblem(double lowLimit, double highLimit) {
        this.lowLimit = lowLimit;
        this.highLimit = highLimit;
    }

    public abstract double evaluate(double[] X, int dim);
}
