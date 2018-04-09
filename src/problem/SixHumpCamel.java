package problem;

public class SixHumpCamel extends BlackBoxProblem {
    public SixHumpCamel() {
        super(-3, 3);
        dim = 2;
    }

    @Override
    public double evaluate(double[] X, int dim) {

        return 4*Math.pow(X[0],2)-
                2.1*Math.pow(X[0],4)+
                1.0/3*Math.pow(X[0],6)+
                X[0]*X[1]-
                4*X[1]*X[1]+
                4*Math.pow(X[1],4);
    }
}
