package algorithm;

import random.DERandom;

public class CommonDEInitializer implements DEInitializer {
    @Override
    public void init(double[][] p, double lowLimit, double highLimit, int NP, double F, double Cr, int dim) {
        DERandom deRandom = new DERandom();
        for(int i = 0;i < NP;i++){
            for(int j = 0;j < dim;j++){
                p[i][j] = deRandom.nextDouble(lowLimit,highLimit);
            }
        }
    }
}
