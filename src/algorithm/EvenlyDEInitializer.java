package algorithm;

import utilities.NumberUtil;

public class EvenlyDEInitializer implements DEInitializer {
    @Override
    public void init(double[][] p, double lowLimit, double highLimit, int NP, double F, double Cr, int dim) {
        double xmin[] = new double[dim+1];
        double xmax[] = new double[dim+1];
        for(int i = 0; i <= dim; i++) {
            xmin[i] = lowLimit;
            xmax[i] = highLimit;
        }
        NumberUtil.latin_hyp(p,NP,dim,xmin,xmax);
    }
}
